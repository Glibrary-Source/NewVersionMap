package com.example.tastywardoffice.network

import android.location.Geocoder
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RequestType(
    @Expose
    @SerializedName("type")
    val type: String
)

data class WholeData(
        @SerializedName("stores")
        val stores: List<Items>
)

data class RequestType2(
    @Expose
    @SerializedName("type")
    val type: String,
    @Expose
    @SerializedName("storeGEOPoints")
    val storeGEOPoints: List<Double>,
    @Expose
    @SerializedName("MyGEOPoint")
    val MyGEOPoint : List<Double>

)

data class Items(
    @Expose
    @SerializedName("district")
    val district: String,
    @Expose
    @SerializedName("storeCategory")
    val storeCategory: Int,
    @Expose
    @SerializedName("storeCntLikes")
    val storeCntLikes: Int,
    @Expose
    @SerializedName("storeGEOPoints")
    val storeGEOPoints: StoreGEOPoints,
    @Expose
    @SerializedName("storeId")
    val storeId: String,
    @Expose
    @SerializedName("storeMenuPictureUrls ")
    val storeMenuPictureUrls :List<String>,
    @Expose
    @SerializedName("storePriceMax")
    val storePriceMax: Int,
    @Expose
    @SerializedName("storePriceMin")
    val storePriceMin: Int,
    @Expose
    @SerializedName("storeRating")
    val storeRating: Int,
    @Expose
    @SerializedName("storeTitle")
    val storeTitle: String
)

data class StoreGEOPoints(
    @Expose
    @SerializedName("latitude")
    val latitude: Double,
    @Expose
    @SerializedName("longitude")
    val longitude: Double
)

