package com.example.project1

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*



interface ApiService {

    @GET("api/workers/by-service")
    fun getWorkersByService(@Query("service") serviceName: String): Call<List<Worker>>
    suspend fun getWorkers(
        @Header("Authorization") authHeader: String
    ): Response<List<Worker>>

    @POST("api/workers")
    fun createWorker(@Body worker: Worker): Call<Worker>

    @PUT("api/workers/{id}")
    fun updateWorker(@Path("id") id: Long, @Body worker: Worker): Call<Worker>

    @DELETE("api/workers/{id}")
    fun deleteWorker(@Path("id") id: Long): Call<Void>

}
