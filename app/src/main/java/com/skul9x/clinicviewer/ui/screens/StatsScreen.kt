package com.skul9x.clinicviewer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skul9x.clinicviewer.data.model.StatItem
import com.skul9x.clinicviewer.data.repository.StatsRepository
import com.skul9x.clinicviewer.ui.theme.*
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.asPaddingValues

enum class StatsFilter(val label: String) {
    DAY("Ngày"),
    WEEK("Tuần"),
    MONTH("Tháng"),
    YEAR("Năm")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen() {
    val repository = remember { StatsRepository() }
    val scope = rememberCoroutineScope()
    
    var selectedFilter by remember { mutableStateOf(StatsFilter.MONTH) }
    var statsData by remember { mutableStateOf<List<StatItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    
    // For day filter - month selector
    var availableMonths by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedMonth by remember { mutableStateOf("") }
    var monthDropdownExpanded by remember { mutableStateOf(false) }
    
    // Load initial data (by Month)
    LaunchedEffect(Unit) {
        availableMonths = repository.getDistinctMonths()
        if (availableMonths.isNotEmpty()) {
            selectedMonth = availableMonths.first()
        }
        statsData = repository.getStatsByMonth()
        isLoading = false
    }
    
    // Reload data when filter changes
    fun loadData() {
        scope.launch {
            isLoading = true
            statsData = when (selectedFilter) {
                StatsFilter.DAY -> {
                    if (selectedMonth.isEmpty() && availableMonths.isNotEmpty()) {
                        selectedMonth = availableMonths.first()
                    }
                    repository.getStatsByDay(selectedMonth)
                }
                StatsFilter.WEEK -> repository.getStatsByWeek()
                StatsFilter.MONTH -> repository.getStatsByMonth()
                StatsFilter.YEAR -> repository.getStatsByYear()
            }
            isLoading = false
        }
    }
    
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandDarkBg)
    ) {
        // --- Header ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = statusBarHeight, bottom = 0.dp)
        ) {
            Text(
                text = "📊 Thống Kê",
                color = BrandTeal,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Báo cáo lượt khám phòng khám",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // --- Filter Chips ---
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatsFilter.entries.forEach { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = {
                            selectedFilter = filter
                            loadData()
                        },
                        label = { Text(filter.label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BrandTeal.copy(alpha = 0.2f),
                            selectedLabelColor = BrandTeal,
                            containerColor = Color.White.copy(alpha = 0.1f),
                            labelColor = Color.White.copy(alpha = 0.7f)
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            selectedBorderColor = BrandTeal,
                            borderColor = Color.Transparent,
                            enabled = true,
                            selected = selectedFilter == filter
                        )
                    )
                }
            }
            
            // --- Month Selector (only for Day filter) ---
            if (selectedFilter == StatsFilter.DAY && availableMonths.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Chọn tháng:",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                    
                    ExposedDropdownMenuBox(
                        expanded = monthDropdownExpanded,
                        onExpandedChange = { monthDropdownExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = formatMonthDisplay(selectedMonth),
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .menuAnchor()
                                .width(140.dp),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = monthDropdownExpanded) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = BrandTeal,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            ),
                            textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
                        )
                        
                        ExposedDropdownMenu(
                            expanded = monthDropdownExpanded,
                            onDismissRequest = { monthDropdownExpanded = false }
                        ) {
                            availableMonths.forEach { month ->
                                DropdownMenuItem(
                                    text = { Text(formatMonthDisplay(month)) },
                                    onClick = {
                                        selectedMonth = month
                                        monthDropdownExpanded = false
                                        loadData()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        
        // --- Content Area ---
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(BrandSurface)
        ) {
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = BrandTeal)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Đang tải thống kê...",
                                color = TextSecondary,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
                statsData.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Chưa có dữ liệu thống kê",
                            color = TextSecondary,
                            fontSize = 14.sp
                        )
                    }
                }
                else -> {
                    StatsTable(
                        data = statsData,
                        headerLeft = when (selectedFilter) {
                            StatsFilter.DAY -> "Ngày"
                            StatsFilter.WEEK -> "Tuần (Năm-Tuần)"
                            StatsFilter.MONTH -> "Tháng"
                            StatsFilter.YEAR -> "Năm"
                        },
                        headerRight = "Số lượt khám"
                    )
                }
            }
        }
    }
}

@Composable
private fun StatsTable(
    data: List<StatItem>,
    headerLeft: String,
    headerRight: String
) {
    val total = data.sumOf { it.count }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        // Header Row
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(BrandTeal)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = headerLeft,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = headerRight,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(100.dp)
                )
            }
        }
        
        // Data Rows
        items(data.indices.toList()) { index ->
            val item = data[index]
            val bgColor = if (index % 2 == 0) CardBackground else HistoryBoxBg
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(bgColor)
                    .border(
                        width = 0.5.dp,
                        color = Color.LightGray.copy(alpha = 0.3f)
                    )
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Text(
                    text = item.label,
                    color = TextPrimary,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = item.count.toString(),
                    color = TextPrimary,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.width(100.dp)
                )
            }
        }
        
        // Total Row
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                    .background(BrandDarkBg)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Text(
                    text = "TỔNG CỘNG",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = total.toString(),
                    color = BrandTeal,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(100.dp)
                )
            }
        }
    }
}

/**
 * Convert "YYYY-MM" to "MM/YYYY" for display
 */
private fun formatMonthDisplay(yearMonth: String): String {
    val parts = yearMonth.split("-")
    return if (parts.size == 2) "${parts[1]}/${parts[0]}" else yearMonth
}
