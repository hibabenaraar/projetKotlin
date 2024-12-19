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

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var featureAdapter: FeatureAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize RetrofitClient with a context
        RetrofitClient.initialize(applicationContext)

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

    private fun getSampleFeatures() = listOf(
        Feature(R.drawable.s11, "Repair"),
        Feature(R.drawable.s12, "Improve"),
        Feature(R.drawable.s13, "Maintain")
    )

    private fun logout(sharedPreferences: SharedPreferences) {
        sharedPreferences.edit().clear().apply()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
