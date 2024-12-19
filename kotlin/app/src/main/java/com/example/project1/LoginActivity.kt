package com.example.project1

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    interface LoginInterface {
        @FormUrlEncoded
        @POST("realms/realm-ebank/protocol/openid-connect/token/")
        suspend fun getAccessToken(
            @Field("client_id") clientId: String,
            @Field("client_secret") clientSecret: String,
            @Field("grant_type") grantType: String,
            @Field("scope") scope: String,
            @Field("username") username: String,
            @Field("password") password: String
        ): retrofit2.Response<AccessToken>
    }

    private val loginInterface: LoginInterface = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8180/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(LoginInterface::class.java)

    data class AccessToken(
        @SerializedName("access_token") val accessToken: String?,
        @SerializedName("expires_in") val expiresIn: Int?,
        @SerializedName("refresh_token") val refreshToken: String?
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameEditText = findViewById(R.id.username)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.login_button)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            } else {
                loginAsAdmin(username, password)
            }
        }
    }

    private fun loginAsAdmin(username: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = loginInterface.getAccessToken(
                    clientId = "mobile-app",
                    clientSecret = "QDCGvYIUczcfQ6LkEdDwCA2BxM0HkpRE",
                    grantType = "password",
                    scope = "openid",
                    username = username,
                    password = password
                )

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val accessToken = response.body()
                        if (accessToken != null && accessToken.accessToken != null && accessToken.refreshToken != null) {
                            saveToken("access_token", accessToken.accessToken)
                            saveToken("refresh_token", accessToken.refreshToken)
                            navigateToMainActivity()
                        } else {
                            Toast.makeText(this@LoginActivity, "Invalid response from server", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Toast.makeText(this@LoginActivity, "Login failed: $errorBody", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveToken(key: String, value: String) {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        sharedPreferences.edit().putString(key, value).apply()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
