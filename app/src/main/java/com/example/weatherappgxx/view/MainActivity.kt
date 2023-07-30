package com.example.weatherappgxx.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherappgxx.R
import com.example.weatherappgxx.adapter.DailyAdapter
import com.example.weatherappgxx.adapter.HourlyAdapter
import com.example.weatherappgxx.databinding.ActivityMainBinding
import com.example.weatherappgxx.model.BaseResponse
import com.example.weatherappgxx.model.WeatherResponse
import com.example.weatherappgxx.viewmodel.WeatherViewModel
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place.*
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.util.*
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {
    private val AUTOCOMPLETE_REQUEST_CODE = 0
    var binding: ActivityMainBinding? = null
    private lateinit var viewModel: WeatherViewModel
    private var mAdapter: HourlyAdapter? = null
    private var dailyAdapter: DailyAdapter? = null
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLocation()

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.api_key))
        }
        binding?.rvHourly?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        binding?.rvDaily?.layoutManager = LinearLayoutManager(this)

        viewModel.weatherResult.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()
                    processInfo(it.data)
                }

                is BaseResponse.Error -> {
                    stopLoading()
                    processError(it.msg)
                }

                else -> {
                    stopLoading()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search -> {
                onSearchCalled()
                true
            }

            R.id.home -> {
                finish()
                true
            }

            else -> false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        return true
    }

    private fun onSearchCalled() {
        // Set the fields to specify which types of place data to return.
        val fields = Arrays.asList(
            Field.ID, Field.NAME, Field.ADDRESS, Field.LAT_LNG
        )
        // Start the autocomplete intent.
        val intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.FULLSCREEN, fields
        ).build(this)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                RESULT_OK -> {
                    val place = Autocomplete.getPlaceFromIntent(intent)
                    val address = place.name
                    binding?.tvCity?.text = address
                    place.latLng?.let {
                        place.latLng?.let { it1 ->
                            viewModel.getWeatherInfo(
                                it.latitude, it1.longitude
                            )
                        }
                    }
                }

                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    val status: Status = Autocomplete.getStatusFromIntent(intent)
                    processError(status.statusMessage)
                }

                RESULT_CANCELED -> {
                    // The user canceled the operation.
                    processError("The user canceled the operation.")
                }
            }
        }
    }

    fun showLoading() {
        binding?.prgbar?.visibility = View.VISIBLE
    }

    fun stopLoading() {
        binding?.prgbar?.visibility = View.GONE
    }

    fun processInfo(data: WeatherResponse?) {
        mAdapter = HourlyAdapter(data?.hourly)
        dailyAdapter = DailyAdapter(data?.daily)
        binding?.rvHourly?.adapter = mAdapter
        binding?.rvDaily?.adapter = dailyAdapter
        binding?.tvTemperature?.text = data?.current?.temp?.roundToInt().toString() + "°C"
        binding?.tvWeather?.text = data?.current?.weather?.get(0)?.main.toString()
        binding?.tvLabelForecast?.text = data?.current?.weather?.get(0)?.description.toString()

    }

    fun processError(msg: String?) {
        showToast("Error:" + msg)
    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this@MainActivity) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val list: MutableList<Address>? =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        binding?.tvCity?.text = list?.get(0)?.locality
                        list?.get(0).let { viewModel.getWeatherInfo(it?.latitude, it?.longitude) }

                    }
                }
            } else {
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ), permissionId
        )
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
            else
                requestPermissions()
        }
    }
}