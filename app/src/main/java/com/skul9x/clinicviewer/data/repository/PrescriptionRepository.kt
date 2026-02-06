package com.skul9x.clinicviewer.data.repository

import com.skul9x.clinicviewer.data.SupabaseClient
import com.skul9x.clinicviewer.data.model.PrescriptionDetail
import com.skul9x.clinicviewer.data.model.PrescriptionHeader
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PrescriptionRepository {

    suspend fun getPrescriptionsByPatient(patientId: Long): List<PrescriptionHeader> {
        return withContext(Dispatchers.IO) {
            try {
                SupabaseClient.client.from("prescriptions_header")
                    .select {
                        filter {
                            eq("patient_id", patientId)
                        }
                        order("prescription_date", Order.DESCENDING)
                    }.decodeList<PrescriptionHeader>()
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    suspend fun getPrescriptionDetails(headerId: Long): List<PrescriptionDetail> {
        return withContext(Dispatchers.IO) {
            try {
                // Fetch details and join with medicines table to get name
                SupabaseClient.client.from("prescription_details")
                    .select(columns = Columns.list("*, medicines(*)")) {
                        filter {
                            eq("prescription_header_id", headerId)
                        }
                    }.decodeList<PrescriptionDetail>()
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }
}
