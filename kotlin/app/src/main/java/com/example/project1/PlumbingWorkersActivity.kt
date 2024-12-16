package com.example.project1

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlumbingWorkersActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WorkerAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.plumbing_workers_layout)

        recyclerView = findViewById(R.id.plumbing_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val addWorkerButton: FloatingActionButton = findViewById(R.id.add_worker_button)
        addWorkerButton.setOnClickListener {
            showWorkerDialog(isEdit = false)
        }

        fetchWorkers()
    }

    private fun fetchWorkers() {
        RetrofitClient.apiService.getWorkersByService("Plumbing")
            .enqueue(object : Callback<List<Worker>> {
                override fun onResponse(call: Call<List<Worker>>, response: Response<List<Worker>>) {
                    if (response.isSuccessful) {
                        val workers = response.body() ?: emptyList()
                        adapter = WorkerAdapter(workers, ::showEditDialog, ::deleteWorker)
                        recyclerView.adapter = adapter
                    } else {
                        Toast.makeText(this@PlumbingWorkersActivity, "Failed to load workers", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Worker>>, t: Throwable) {
                    Toast.makeText(this@PlumbingWorkersActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun createWorker(worker: Worker) {
        RetrofitClient.apiService.createWorker(worker)
            .enqueue(object : Callback<Worker> {
                override fun onResponse(call: Call<Worker>, response: Response<Worker>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@PlumbingWorkersActivity, "Worker created successfully", Toast.LENGTH_SHORT).show()
                        fetchWorkers() // Refresh the list
                    } else {
                        Toast.makeText(this@PlumbingWorkersActivity, "Failed to create worker", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Worker>, t: Throwable) {
                    Toast.makeText(this@PlumbingWorkersActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun updateWorker(worker: Worker) {
        RetrofitClient.apiService.updateWorker(worker.id, worker)
            .enqueue(object : Callback<Worker> {
                override fun onResponse(call: Call<Worker>, response: Response<Worker>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@PlumbingWorkersActivity, "Worker updated successfully", Toast.LENGTH_SHORT).show()
                        fetchWorkers() // Refresh the list
                    } else {
                        Toast.makeText(this@PlumbingWorkersActivity, "Failed to update worker", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Worker>, t: Throwable) {
                    Toast.makeText(this@PlumbingWorkersActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun deleteWorker(worker: Worker) {
        RetrofitClient.apiService.deleteWorker(worker.id)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@PlumbingWorkersActivity, "Worker deleted successfully", Toast.LENGTH_SHORT).show()
                        fetchWorkers()
                    } else {
                        Toast.makeText(this@PlumbingWorkersActivity, "Failed to delete worker", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@PlumbingWorkersActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun showWorkerDialog(isEdit: Boolean, worker: Worker? = null) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_worker, null)
        val firstnameInput = dialogView.findViewById<EditText>(R.id.worker_firstname_input)
        val lastnameInput = dialogView.findViewById<EditText>(R.id.worker_lastname_input)
        val occupationInput = dialogView.findViewById<EditText>(R.id.worker_occupation_input)
        val experienceInput = dialogView.findViewById<EditText>(R.id.worker_experience_input)
        val serviceInput = dialogView.findViewById<EditText>(R.id.worker_service_input)
        val emailInput = dialogView.findViewById<EditText>(R.id.worker_email_input)
        val addressInput = dialogView.findViewById<EditText>(R.id.worker_address_input)
        val cityInput = dialogView.findViewById<EditText>(R.id.worker_city_input)
        val phoneInput = dialogView.findViewById<EditText>(R.id.worker_phone_input)
        val postInput = dialogView.findViewById<EditText>(R.id.worker_post_input)
        val serviceDescriptionInput = dialogView.findViewById<EditText>(R.id.worker_service_description_input)
        val dobInput = dialogView.findViewById<EditText>(R.id.worker_dob_input)

        if (isEdit && worker != null) {
            firstnameInput.setText(worker.firstname)
            lastnameInput.setText(worker.lastname)
            occupationInput.setText(worker.occupation)
            experienceInput.setText(worker.expyear)
            serviceInput.setText(worker.service)
            emailInput.setText(worker.email)
            addressInput.setText(worker.address)
            cityInput.setText(worker.city)
            phoneInput.setText(worker.phone)
            postInput.setText(worker.post)
            serviceDescriptionInput.setText(worker.serviceDescription)
            dobInput.setText(worker.dob)
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle(if (isEdit) "Edit Worker" else "Add Worker")
            .setView(dialogView)
            .setPositiveButton("Save", null)
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val newWorker = Worker(
                id = worker?.id ?: 0L,
                firstname = firstnameInput.text.toString(),
                lastname = lastnameInput.text.toString(),
                occupation = occupationInput.text.toString(),
                expyear = experienceInput.text.toString(),
                service = serviceInput.text.toString(),
                email = emailInput.text.toString(),
                address = addressInput.text.toString(),
                city = cityInput.text.toString(),
                phone = phoneInput.text.toString(),
                post = postInput.text.toString(),
                serviceDescription = serviceDescriptionInput.text.toString(),
                dob = dobInput.text.toString()
            )

            if (isEdit) {
                updateWorker(newWorker)
            } else {
                createWorker(newWorker)
            }

            dialog.dismiss()
        }
    }

    private fun showEditDialog(worker: Worker) {
        showWorkerDialog(isEdit = true, worker = worker)
    }
}
