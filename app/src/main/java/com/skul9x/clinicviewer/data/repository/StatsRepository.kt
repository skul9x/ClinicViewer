package com.skul9x.clinicviewer.data.repository

import com.skul9x.clinicviewer.data.SupabaseClient
import com.skul9x.clinicviewer.data.model.StatItem
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// Response models for RPC functions
@Serializable
data class MonthResponse(val month: String)

@Serializable
data class DayStatResponse(
    @SerialName("visit_date") val visitDate: String,
    @SerialName("day_of_week") val dayOfWeek: Int,
    val count: Long
)

@Serializable
data class WeekStatResponse(
    val week: String,
    val count: Long
)

@Serializable
data class MonthStatResponse(
    val month: String,
    val count: Long
)

@Serializable
data class YearStatResponse(
    val year: String,
    val count: Long
)

class StatsRepository {
    
    private val vietnameseWeekDays = mapOf(
        0 to "Chủ nhật",  // PostgreSQL DOW: Sunday = 0
        1 to "Thứ 2",
        2 to "Thứ 3",
        3 to "Thứ 4",
        4 to "Thứ 5",
        5 to "Thứ 6",
        6 to "Thứ 7"
    )
    
    /**
     * Get distinct months that have data, for the month selector dropdown.
     * Uses RPC function: get_distinct_months()
     */
    suspend fun getDistinctMonths(): List<String> {
        return withContext(Dispatchers.IO) {
            try {
                val result = SupabaseClient.client.postgrest
                    .rpc("get_distinct_months")
                    .decodeList<MonthResponse>()
                
                result.map { it.month }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }
    
    /**
     * Get statistics by day for a specific month.
     * Uses RPC function: get_stats_by_day(year_month)
     */
    suspend fun getStatsByDay(yearMonth: String): List<StatItem> {
        return withContext(Dispatchers.IO) {
            try {
                val result = SupabaseClient.client.postgrest
                    .rpc("get_stats_by_day", mapOf("year_month" to yearMonth))
                    .decodeList<DayStatResponse>()
                
                val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                
                result.map { stat ->
                    val localDate = try {
                        LocalDate.parse(stat.visitDate)
                    } catch (e: Exception) {
                        null
                    }
                    val formattedDate = localDate?.format(dateFormatter) ?: stat.visitDate
                    val weekDay = vietnameseWeekDays[stat.dayOfWeek] ?: ""
                    val label = if (weekDay.isNotEmpty()) "$formattedDate ($weekDay)" else formattedDate
                    StatItem(label, stat.count.toInt())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }
    
    /**
     * Get statistics by week.
     * Uses RPC function: get_stats_by_week(limit_count)
     */
    suspend fun getStatsByWeek(): List<StatItem> {
        return withContext(Dispatchers.IO) {
            try {
                val result = SupabaseClient.client.postgrest
                    .rpc("get_stats_by_week", mapOf("limit_count" to 52))
                    .decodeList<WeekStatResponse>()
                
                result.map { stat ->
                    StatItem(stat.week, stat.count.toInt())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }
    
    /**
     * Get statistics by month.
     * Uses RPC function: get_stats_by_month(limit_count)
     */
    suspend fun getStatsByMonth(): List<StatItem> {
        return withContext(Dispatchers.IO) {
            try {
                val result = SupabaseClient.client.postgrest
                    .rpc("get_stats_by_month", mapOf("limit_count" to 24))
                    .decodeList<MonthStatResponse>()
                
                result.map { stat ->
                    // Convert "YYYY-MM" to "MM/YYYY"
                    val parts = stat.month.split("-")
                    val displayMonth = if (parts.size == 2) "${parts[1]}/${parts[0]}" else stat.month
                    StatItem(displayMonth, stat.count.toInt())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }
    
    /**
     * Get statistics by year.
     * Uses RPC function: get_stats_by_year()
     */
    suspend fun getStatsByYear(): List<StatItem> {
        return withContext(Dispatchers.IO) {
            try {
                val result = SupabaseClient.client.postgrest
                    .rpc("get_stats_by_year")
                    .decodeList<YearStatResponse>()
                
                result.map { stat ->
                    StatItem(stat.year, stat.count.toInt())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }
}

