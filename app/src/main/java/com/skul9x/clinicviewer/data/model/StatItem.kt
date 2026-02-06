package com.skul9x.clinicviewer.data.model

/**
 * Data class representing a single statistic item.
 * Used for all types of statistics (by day, week, month, year).
 */
data class StatItem(
    val label: String,  // e.g., "05/02/2026 (Thứ 5)" or "2026-W05" or "01/2026"
    val count: Int      // Number of visits/patients
)
