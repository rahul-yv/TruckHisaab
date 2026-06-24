package com.truckhisaab.data.remote

import retrofit2.Response
import retrofit2.http.*

interface TruckHisaabApi {

    @POST("auth/send-otp")
    suspend fun sendOtp(@Body body: Map<String, String>): Response<Map<String, Any>>

    @POST("auth/verify-otp")
    suspend fun verifyOtp(@Body body: Map<String, String>): Response<Map<String, Any>>

    @POST("sync/upload")
    suspend fun uploadSyncData(@Body body: Map<String, Any>): Response<Map<String, Any>>

    @GET("sync/download")
    suspend fun downloadSyncData(@Query("since") since: Long): Response<Map<String, Any>>
}
