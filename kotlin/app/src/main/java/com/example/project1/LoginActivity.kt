package com.example.project1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    // Retrofit interface for login
    interface LoginInterface {
        @FormUrlEncoded
        @POST("realms/realm-ebank/protocol/openid-connect/token")
        suspend fun getAccessToken(
            @Field("client_id") clientId: String,
            @Field("client_secret") clientSecret: String,
            @Field("grant_type") grantType: String,
            @Field("scope") scope: String,
            @Field("username") username: String,
            @Field("password") password: String
        ): retrofit2.Response<AccessToken>
    }

    // Retrofit client setup
    private val loginInterface = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8180/") // Correct base URL for your login service
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(LoginInterface::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameEditText = findViewById(R.id.username)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.login_button)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                loginAsAdmin(username, password)
            } else {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    data class AccessToken (
        @SerializedName("access_token")
        val accessToken: String,
        @SerializedName("expires_in")
        val expiresIn: Int,
        @SerializedName("refresh_expires_in")
        val refreshExpiresIn: Int,
        @SerializedName("refresh_token")
        val refreshToken: String,
        @SerializedName("token_type")
        val tokenType: String,
        @SerializedName("id_token")
        val idToken: String,
        @SerializedName("not_before_policy")
        val notBeforePolicy: Int,
        @SerializedName("session_state")
        val sessionState: String,
        @SerializedName("scope")
        val scope: String
    )

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

                if (response.isSuccessful) {
                    response.body()?.accessToken?.let { accessToken ->
                        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
                        sharedPreferences.edit().putString("access_token", accessToken).apply()

                        withContext(Dispatchers.Main) {
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@LoginActivity, "Login failed: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
