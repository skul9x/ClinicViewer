package com.skul9x.clinicviewer.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Patient(
    val id: Long, // Use Long for BigInt
    val name: String,
    val dob: String? = null,
    val gender: String? = null,
    val address: String? = null,
    val phone: String? = null,
    val weight: String? = null,
    @SerialName("medical_history")
    val medicalHistory: String? = null,
    val diagnosis: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null
)
