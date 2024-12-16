package com.example.project1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ServicesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services)

        setupServiceButtons()
    }

    private fun setupServiceButtons() {

        val plumbingButton: Button = findViewById(R.id.btn_see_workers_1)
        plumbingButton.setOnClickListener {
            val intent = Intent(this, PlumbingWorkersActivity::class.java)
            startActivity(intent)
        }


        val electricalButton: Button = findViewById(R.id.btn_see_workers_2)
        electricalButton.setOnClickListener {
            val intent = Intent(this, ElectricalWorkersActivity::class.java)
            startActivity(intent)
        }

        // Bouton pour "Maintenance Workers"
        val maintenanceButton: Button = findViewById(R.id.btn_see_workers_3)
        maintenanceButton.setOnClickListener {
            val intent = Intent(this, MaintenanceWorkersActivity::class.java)
            startActivity(intent)
        }
    }
}
