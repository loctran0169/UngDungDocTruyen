package com.example.truyenqq.module.models

import com.google.gson.annotations.SerializedName

data class Subscribe(
    @SerializedName("success")
    var success : Int,
    @SerializedName("status")
    var status : Int
)