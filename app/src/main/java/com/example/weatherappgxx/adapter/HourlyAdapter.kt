package com.example.weatherappgxx.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherappgxx.R
import com.example.weatherappgxx.model.Hourly
import com.example.weatherappgxx.utils.Common
import kotlin.math.roundToInt

class HourlyAdapter(private val mList: ArrayList<Hourly>?) :
    RecyclerView.Adapter<HourlyAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view 
        // that is used to hold list item
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.rv_hourly_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList?.get(position)
        val forecast =ItemsViewModel?.weather?.get(0)?.main.toString()

        // sets the text to the textview from our itemHolder class
        holder.tvDate.text = ItemsViewModel?.dt?.let { Common.getTime(it) }
        holder.tvWeather.text = forecast
        holder.tvTemp.text = ItemsViewModel?.temp?.roundToInt().toString() + "°C"
        Common.getWeatherData(weather = forecast,holder.cloudIV,)

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList?.size!!
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvWeather: TextView = itemView.findViewById(R.id.tvWeather)
        val tvTemp: TextView = itemView.findViewById(R.id.tvTemp)
        val cloudIV: ImageView = itemView.findViewById(R.id.cloudIV)

    }
}