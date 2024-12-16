package com.example.project1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TestimonialAdapter(private val testimonials: List<Testimonial>) : RecyclerView.Adapter<TestimonialAdapter.TestimonialViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestimonialViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.testimonial_item, parent, false)
        return TestimonialViewHolder(view)
    }

    override fun onBindViewHolder(holder: TestimonialViewHolder, position: Int) {
        val testimonial = testimonials[position]
        holder.testimonialImage.setImageResource(testimonial.image)
        holder.testimonialName.text = testimonial.name
        holder.testimonialRating.text = testimonial.rating
        holder.testimonialText.text = testimonial.text
    }

    override fun getItemCount(): Int = testimonials.size

    class TestimonialViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val testimonialImage: ImageView = itemView.findViewById(R.id.testimonialImage)
        val testimonialName: TextView = itemView.findViewById(R.id.testimonialName)
        val testimonialRating: TextView = itemView.findViewById(R.id.testimonialRating)
        val testimonialText: TextView = itemView.findViewById(R.id.testimonialText)
    }
}
