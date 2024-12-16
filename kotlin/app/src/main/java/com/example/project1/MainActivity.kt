package com.example.project1

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var featureAdapter: FeatureAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("access_token", null)

        if (accessToken == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            setContentView(R.layout.activity_main)
            setupRecyclerView()

            findViewById<TextView>(R.id.nav_services).setOnClickListener {
                startActivity(Intent(this, ServicesActivity::class.java))
            }

            // Add logout functionality
            findViewById<Button>(R.id.logout_button).setOnClickListener {
                logout(sharedPreferences)
            }
        }
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.feature_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        featureAdapter = FeatureAdapter(getSampleFeatures())
        recyclerView.adapter = featureAdapter
    }

    private fun getSampleFeatures(): List<Feature> {
        return listOf(
            Feature(R.drawable.s11, "Repair"),
            Feature(R.drawable.s12, "Improve"),
            Feature(R.drawable.s13, "Maintain")
        )
    }

    // Function to handle logout
    private fun logout(sharedPreferences: SharedPreferences) {
        val refreshToken = sharedPreferences.getString("refresh_token", null)

        if (refreshToken != null) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = logoutInterface.logout(
                        clientId = "mobile-app",
                        refreshToken = refreshToken
                    )

                    if (response.isSuccessful) {
                        // Clear shared preferences
                        sharedPreferences.edit().remove("access_token").apply()
                        sharedPreferences.edit().remove("refresh_token").apply()

                        withContext(Dispatchers.Main) {
                            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                            finish()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@MainActivity, "Logout failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            // If no refresh token, just log out locally
            sharedPreferences.edit().remove("access_token").apply()
            sharedPreferences.edit().remove("refresh_token").apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    // Retrofit logout interface
    private val logoutInterface = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8180/")  // Your correct base URL
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(LogoutInterface::class.java)
}
