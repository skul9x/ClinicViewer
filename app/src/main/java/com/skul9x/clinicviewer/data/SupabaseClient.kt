package com.skul9x.clinicviewer.data

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.serialization.json.Json

object SupabaseClient {
    private const val SUPABASE_URL = "https://lklemryxamvaarupufco.supabase.co"
    private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImxrbGVtcnl4YW12YWFydXB1ZmNvIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzAzMTUzOTQsImV4cCI6MjA4NTg5MTM5NH0.ZMgxE1qYlDuolPyMhHZ54Ze0yj7NWtanCPR5xViy6lM"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        defaultSerializer = KotlinXSerializer(Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        })
        
        install(Postgrest)
        install(Realtime)
        install(Auth)
    }
}
