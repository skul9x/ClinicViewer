package com.skul9x.clinicviewer.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Medicine(
    val id: Long,
    val name: String,
    @SerialName("packing_spec")
    val packingSpec: String? = null,
    val price: Double? = 0.0
)
