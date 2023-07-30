package com.example.weatherappgxx.model

import com.google.gson.annotations.SerializedName


data class Minutely (

  @SerializedName("dt"            ) var dt            : Double? = null,
  @SerializedName("precipitation" ) var precipitation : Double? = null

)