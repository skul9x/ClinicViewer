package com.skul9x.clinicviewer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skul9x.clinicviewer.data.model.Patient
import com.skul9x.clinicviewer.data.repository.PatientRepository
import com.skul9x.clinicviewer.ui.theme.*
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.asPaddingValues

@Composable
fun HomeScreen(
    onPatientClick: (Patient) -> Unit
) {
    val repository = remember { PatientRepository() }
    var patients by remember { mutableStateOf<List<Patient>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    
    // Pagination state
    var isLoading by remember { mutableStateOf(true) }
    var isLoadingMore by remember { mutableStateOf(false) }
    var canLoadMore by remember { mutableStateOf(true) }
    var currentOffset by remember { mutableLongStateOf(0L) }
    val pageSize = 50L
    
    val listState = rememberLazyListState()
    
    // Initial load
    LaunchedEffect(Unit) {
        isLoading = true
        patients = repository.getPatients(limit = pageSize, offset = 0)
        currentOffset = pageSize
        canLoadMore = patients.size >= pageSize.toInt()
        isLoading = false
    }
    
    // Detect scroll to end for loading more
    LaunchedEffect(listState, canLoadMore, searchQuery) {
        snapshotFlow {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = listState.layoutInfo.totalItemsCount
            lastVisibleItem >= totalItems - 3 && totalItems > 0
        }
        .distinctUntilChanged()
        .collect { reachedEnd ->
            if (reachedEnd && canLoadMore && !isLoadingMore && !isLoading && searchQuery.isEmpty()) {
                isLoadingMore = true
                val newPatients = repository.getPatients(limit = pageSize, offset = currentOffset)
                if (newPatients.isNotEmpty()) {
                    patients = patients + newPatients
                    currentOffset += pageSize
                    canLoadMore = newPatients.size >= pageSize.toInt()
                } else {
                    canLoadMore = false
                }
                isLoadingMore = false
            }
        }
    }
    
    // Search handler
    fun performSearch(query: String) {
        searchQuery = query
        scope.launch {
            if (query.isEmpty()) {
                isLoading = true
                patients = repository.getPatients(limit = pageSize, offset = 0)
                currentOffset = pageSize
                canLoadMore = patients.size >= pageSize.toInt()
                isLoading = false
            } else {
                patients = repository.searchPatients(query)
                canLoadMore = false // Disable pagination during search
            }
        }
    }

    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandDarkBg)
    ) {
        // --- Header Section ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = statusBarHeight, bottom = 0.dp)
        ) {
            Text(
                text = "PK Ngọc Trường",
                color = BrandTeal,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Premium Medical Dashboard",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Search Bar
            TextField(
                value = searchQuery,
                onValueChange = { performSearch(it) },
                placeholder = { Text("Tìm kiếm bệnh nhân...", color = Color.White.copy(alpha = 0.5f)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.White.copy(alpha = 0.5f)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(30.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White.copy(alpha = 0.1f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.1f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )
        }
        
        // --- Body / Content Section ---
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(BrandSurface)
        ) {
            when {
                // Initial loading
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = BrandTeal)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Đang tải dữ liệu...",
                                color = TextSecondary,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
                // Empty list
                patients.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (searchQuery.isNotEmpty()) "Không tìm thấy bệnh nhân" else "Chưa có dữ liệu",
                            color = TextSecondary,
                            fontSize = 14.sp
                        )
                    }
                }
                // Patient list
                else -> {
                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(patients) { patient ->
                            PatientCard(patient, onClick = { onPatientClick(patient) })
                        }
                        
                        // Loading more indicator at the bottom
                        if (isLoadingMore) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(24.dp),
                                            color = BrandTeal,
                                            strokeWidth = 2.dp
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = "Đang tải thêm...",
                                            color = TextSecondary,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PatientCard(patient: Patient, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar Placeholder
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(BrandTeal.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = patient.name.take(1).uppercase(),
                    color = BrandTeal,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = patient.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Khám ngày: ${formatDate(patient.createdAt)}",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
            
            // Detail Button (Visual only)
            Surface(
                color = AccentPink,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    text = "Chi tiết",
                    color = AccentRed,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Simple Helper for Date Formatting (Improve later)
fun formatDate(dateStr: String?): String {
    if (dateStr == null) return "N/A"
    return try {
        // Supabase returns ISO format: 2024-01-27T...
        dateStr.substring(0, 10).split("-").reversed().joinToString("-")
    } catch (e: Exception) {
        dateStr
    }
}
