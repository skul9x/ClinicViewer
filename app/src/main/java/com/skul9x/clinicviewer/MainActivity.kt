package com.skul9x.clinicviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.skul9x.clinicviewer.data.model.Patient
import com.skul9x.clinicviewer.ui.screens.HomeScreen
import com.skul9x.clinicviewer.ui.screens.PatientDetailDialog
import com.skul9x.clinicviewer.ui.theme.ClinicViewerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClinicViewerTheme {
                MainAppContent()
            }
        }
    }
}

@Composable
fun MainAppContent() {
    var selectedPatient by remember { mutableStateOf<Patient?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        HomeScreen(
            onPatientClick = { patient ->
                selectedPatient = patient
            }
        )
        
        selectedPatient?.let { patient ->
            PatientDetailDialog(
                patient = patient,
                onDismiss = { selectedPatient = null }
            )
        }
    }
}