package com.skul9x.clinicviewer.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PrescriptionHeader(
    val id: Long,
    @SerialName("patient_id")
    val patientId: Long,
    @SerialName("prescription_date")
    val prescriptionDate: String? = null,
    val diagnosis: String? = null,
    @SerialName("total_amount")
    val totalAmount: Double? = 0.0,
    val notes: String? = null
)

@Serializable
data class PrescriptionDetail(
    val id: Long,
    @SerialName("prescription_header_id")
    val prescriptionHeaderId: Long,
    @SerialName("medicine_id")
    val medicineId: Long? = null,
    val quantity: Int,
    @SerialName("unit_price")
    val unitPrice: Double? = 0.0,
    
    // Joined fields (optional, need to handle view logic)
    @SerialName("medicines")
    val medicine: Medicine? = null
)
