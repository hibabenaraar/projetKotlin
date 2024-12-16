package com.example.project1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WorkerAdapter(
    private val workers: List<Worker>,
    private val onEdit: (Worker) -> Unit,
    private val onDelete: (Worker) -> Unit
) : RecyclerView.Adapter<WorkerAdapter.WorkerViewHolder>() {

    class WorkerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.worker_name)
        //val occupation: TextView = itemView.findViewById(R.id.worker_occupation)
        val experience: TextView = itemView.findViewById(R.id.worker_experience)
        //val service: TextView = itemView.findViewById(R.id.worker_service)
        val email: TextView = itemView.findViewById(R.id.worker_email)
        //val address: TextView = itemView.findViewById(R.id.worker_address)
        val city: TextView = itemView.findViewById(R.id.worker_city)
        val phone: TextView = itemView.findViewById(R.id.worker_phone)
        //val post: TextView = itemView.findViewById(R.id.worker_post)
        //val serviceDescription: TextView = itemView.findViewById(R.id.worker_service_description)
        //val dob: TextView = itemView.findViewById(R.id.worker_dob)
        val edit: TextView = itemView.findViewById(R.id.worker_edit)
        val delete: TextView = itemView.findViewById(R.id.worker_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.worker_item_layout, parent, false)
        return WorkerViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkerViewHolder, position: Int) {
        val worker = workers[position]
        holder.name.text = "${worker.firstname} ${worker.lastname}"
        //holder.occupation.text = "Occupation: ${worker.occupation}"
        holder.experience.text = "Experience: ${worker.expyear} years"
        //holder.service.text = "Service: ${worker.service}"
        holder.email.text = "Email: ${worker.email}"
        //holder.address.text = "Address: ${worker.address}"
        holder.city.text = "City: ${worker.city}"
        holder.phone.text = "Phone: ${worker.phone}"
        //holder.post.text = "Post: ${worker.post}"
        //holder.serviceDescription.text = "Description: ${worker.serviceDescription}"
        //holder.dob.text = "Date of Birth: ${worker.dob}"

        holder.edit.setOnClickListener { onEdit(worker) }
        holder.delete.setOnClickListener { onDelete(worker) }
    }

    override fun getItemCount(): Int = workers.size
}