package kr.co.toplink.keepcon.dto

import com.google.gson.annotations.SerializedName

data class FCM(
    @SerializedName("to") var token: String,
    @SerializedName("notification") var notification: FCM
)
