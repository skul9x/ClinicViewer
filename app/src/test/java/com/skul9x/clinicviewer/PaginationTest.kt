package com.skul9x.clinicviewer

import com.skul9x.clinicviewer.data.model.Patient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Test

// Using the constants found in SupabaseClient.kt
private const val SUPABASE_URL = "https://lklemryxamvaarupufco.supabase.co"
private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImxrbGVtcnl4YW12YWFydXB1ZmNvIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzAzMTUzOTQsImV4cCI6MjA4NTg5MTM5NH0.ZMgxE1qYlDuolPyMhHZ54Ze0yj7NWtanCPR5xViy6lM"

class PaginationTest {
    @Test
    fun testPagination() = runBlocking {
        println("--- START PAGINATION TEST ---")
        try {
             val client = createSupabaseClient(
                supabaseUrl = SUPABASE_URL,
                supabaseKey = SUPABASE_KEY
            ) {
                defaultSerializer = KotlinXSerializer(Json {
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                })
                install(Postgrest)
            }
            
            println("1. Fetching first page (Limit: 5, Offset: 0)")
            val page1 = client.from("patients")
                .select(columns = Columns.ALL) {
                    order("created_at", Order.DESCENDING)
                    range(0, 4) // 0 to 4 is 5 items
                }.decodeList<Patient>()
            
            println("   - Page 1 count: ${page1.size}")
            page1.forEach { println("     - [${it.id}] ${it.name}") }
            
            println("2. Fetching second page (Limit: 5, Offset: 5)")
            val page2 = client.from("patients")
                 .select(columns = Columns.ALL) {
                    order("created_at", Order.DESCENDING)
                    range(5, 9) // 5 to 9 is 5 items
                }.decodeList<Patient>()
                
            println("   - Page 2 count: ${page2.size}")
            page2.forEach { println("     - [${it.id}] ${it.name}") }
            
            // Verification: Ensure IDs are different (no duplicates implies pagination worked)
            val page1Ids = page1.map { it.id }.toSet()
            val page2Ids = page2.map { it.id }.toSet()
            val intersection = page1Ids.intersect(page2Ids)
            
            if (intersection.isEmpty() && page1.isNotEmpty() && page2.isNotEmpty()) {
                println("✅ Pagination SUCCESS! No duplicate patients between pages.")
            } else {
                println("❌ Pagination FAILED! Duplicates found or empty pages.")
                println("   - Duplicates: $intersection")
            }

        } catch (e: Exception) {
            println("❌ Test Failed with Exception!")
            e.printStackTrace()
            throw e
        }
        println("--- END PAGINATION TEST ---")
    }
}
