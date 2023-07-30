package com.example.weatherappgxx.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.example.weatherappgxx.utils.Constant
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject


object ApiClient {
    val cacheSize = (5 * 1024 * 1024).toLong()
    var mRetrofit: Retrofit? = null
    val myCache = Cache(WeatherApplication.applicationContext().cacheDir, cacheSize)

    var onlineInterceptor = Interceptor { chain ->
        val response = chain.proceed(chain.request())
        val maxAge = 60 // read from cache for 60 seconds even if there is internet connection
        response.newBuilder()
            .header("Cache-Control", "public, max-age=$maxAge")
            .removeHeader("Pragma")
            .build()
    }
    var offlineInterceptor = Interceptor { chain ->
        var request = chain.request()
        if (!isInternetAvailable(WeatherApplication.applicationContext())) {
            val maxStale = 60 * 60 * 24 * 7
            request = request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                .removeHeader("Pragma")
                .build()
        }
        chain.proceed(request)
    }


    var okHttpClient: OkHttpClient =
        OkHttpClient.Builder() 
            .addInterceptor(offlineInterceptor)
            .addNetworkInterceptor(onlineInterceptor)
            .cache(myCache)
            .build()

    fun isInternetAvailable(context: Context): Boolean {
        var isConnected: Boolean = false // Initial Value
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }

    val client: Retrofit?
        get() {
            if (mRetrofit == null) {
                mRetrofit = Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return mRetrofit
        }
}