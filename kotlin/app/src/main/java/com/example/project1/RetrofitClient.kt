package com.example.project1

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"
    private lateinit var appContext: Context

    // Initialize RetrofitClient with a valid context
    fun initialize(context: Context) {
        appContext = context.applicationContext
    }

    private val authInterceptor = Interceptor { chain ->
        val token = getAccessToken()
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        chain.proceed(request)
    }

    val apiService: ApiService by lazy {
        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }

    private fun getAccessToken(): String? {
        val sharedPreferences = appContext.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("access_token", null)
    }
}

