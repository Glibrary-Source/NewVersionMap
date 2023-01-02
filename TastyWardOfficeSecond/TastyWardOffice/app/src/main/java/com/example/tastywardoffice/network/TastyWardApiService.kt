package com.example.tastywardoffice.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

private const val BASE_URL =
    "https://asia-northeast3-delicious-project-e3bed.cloudfunctions.net/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface TastyWardApiService {
    @POST("test1")
    fun getStoreData(
        @Body data: JoinData
    ): Call<MyDTO>

    @POST("test1")
    fun getWholeData(
        @Body data: RequestType
    ): Call<WholeData>

    @POST("test1")
    fun getDistance(
        @Body data: RequestType2
    ): Call<WholeData>
}

object TastyWardApi{
    val service: TastyWardApiService by lazy {
        retrofit.create(TastyWardApiService::class.java)
    }
}