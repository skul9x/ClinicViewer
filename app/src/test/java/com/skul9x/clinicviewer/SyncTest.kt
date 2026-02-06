package com.skul9x.clinicviewer

import com.skul9x.clinicviewer.data.model.Patient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Test
import io.github.jan.supabase.postgrest.query.Columns

// Using the constants found in SupabaseClient.kt
private const val SUPABASE_URL = "https://lklemryxamvaarupufco.supabase.co"
private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImxrbGVtcnl4YW12YWFydXB1ZmNvIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzAzMTUzOTQsImV4cCI6MjA4NTg5MTM5NH0.ZMgxE1qYlDuolPyMhHZ54Ze0yj7NWtanCPR5xViy6lM"

class SyncTest {
    @Test
    fun testFetchPatients() = runBlocking {
        println("--- START ISOLATED SYNC TEST ---")
        try {
            // Create a local client to avoid Android dependencies in Unit Test
            val client = createSupabaseClient(
                supabaseUrl = SUPABASE_URL,
                supabaseKey = SUPABASE_KEY
            ) {
                defaultSerializer = KotlinXSerializer(Json {
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                })
                install(Postgrest)
                // Skipping Auth to avoid JVM environment issues
            }

            println("Client initialized. Fetching patients...")
            
            val response = client.from("patients")
                .select(columns = Columns.ALL).decodeList<Patient>()
            
            println("✅ Success! Fetched ${response.size} patients.")
            if (response.isNotEmpty()) {
                val p = response[0]
                println("Sample Patient: ${p.name}, ID: ${p.id}, Phone: ${p.phone}")
            } else {
                println("⚠️ List is empty. Check if RLS policies allow anon select, or if table is truly empty.")
            }
        } catch (e: Exception) {
            println("❌ Sync Failed!")
            e.printStackTrace()
            throw e
        }
        println("--- END ISOLATED SYNC TEST ---")
    }
}
