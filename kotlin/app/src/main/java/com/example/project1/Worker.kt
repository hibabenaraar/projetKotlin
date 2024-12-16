package com.example.project1

data class Worker(
    val id: Long,
    val firstname: String,
    val lastname: String,
    val occupation: String,
    val service: String, // Plain string for service
    val email: String?,
    val address: String?,
    val city: String?,
    val phone: String?,
    val expyear: String?,
    val post: String?,
    val serviceDescription: String?,
    val dob: String? // Use String for simplicity
)
