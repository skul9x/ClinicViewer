package com.skul9x.clinicviewer.data.repository

import com.skul9x.clinicviewer.data.SupabaseClient
import com.skul9x.clinicviewer.data.model.Patient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PatientRepository {

    suspend fun getPatients(limit: Long = 50, offset: Long = 0): List<Patient> {
        return withContext(Dispatchers.IO) {
            try {
                SupabaseClient.client.from("patients")
                    .select(columns = Columns.ALL) {
                        order("created_at", Order.DESCENDING)
                        range(offset, offset + limit - 1)
                    }.decodeList<Patient>()
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    suspend fun searchPatients(query: String): List<Patient> {
        return withContext(Dispatchers.IO) {
            try {
                SupabaseClient.client.from("patients")
                    .select(columns = Columns.ALL) {
                        filter {
                            or {
                                ilike("name", "%$query%")
                                ilike("phone", "%$query%")
                            }
                        }
                        order("created_at", Order.DESCENDING)
                        limit(20)
                    }.decodeList<Patient>()
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    suspend fun getPatientById(id: Long): Patient? {
        return withContext(Dispatchers.IO) {
            try {
               SupabaseClient.client.from("patients")
                   .select {
                       filter {
                           eq("id", id)
                       }
                   }.decodeSingleOrNull<Patient>()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
