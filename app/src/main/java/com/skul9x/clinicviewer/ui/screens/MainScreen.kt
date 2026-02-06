package com.skul9x.clinicviewer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.skul9x.clinicviewer.data.model.Patient
import com.skul9x.clinicviewer.ui.theme.BrandDarkBg
import com.skul9x.clinicviewer.ui.theme.BrandTeal

enum class BottomNavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    Clinic("Phòng khám", Icons.Filled.Home, Icons.Outlined.Home),
    Stats("Thống kê", Icons.Filled.Info, Icons.Outlined.Info)
}

@Composable
fun MainScreen() {
    var selectedTab by remember { mutableStateOf(BottomNavItem.Clinic) }
    var selectedPatient by remember { mutableStateOf<Patient?>(null) }
    
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = BrandDarkBg,
                contentColor = Color.White
            ) {
                BottomNavItem.entries.forEach { item ->
                    NavigationBarItem(
                        selected = selectedTab == item,
                        onClick = { selectedTab = item },
                        icon = {
                            Icon(
                                imageVector = if (selectedTab == item) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        },
                        label = { Text(item.title) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BrandTeal,
                            selectedTextColor = BrandTeal,
                            unselectedIconColor = Color.White.copy(alpha = 0.6f),
                            unselectedTextColor = Color.White.copy(alpha = 0.6f),
                            indicatorColor = BrandTeal.copy(alpha = 0.2f)
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                BottomNavItem.Clinic -> {
                    HomeScreen(
                        onPatientClick = { patient ->
                            selectedPatient = patient
                        }
                    )
                }
                BottomNavItem.Stats -> {
                    StatsScreen()
                }
            }
        }
        
        // Patient Detail Dialog (shown on top of everything)
        selectedPatient?.let { patient ->
            PatientDetailDialog(
                patient = patient,
                onDismiss = { selectedPatient = null }
            )
        }
    }
}
