package com.skul9x.clinicviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.skul9x.clinicviewer.ui.screens.MainScreen
import com.skul9x.clinicviewer.ui.theme.ClinicViewerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClinicViewerTheme {
                MainScreen()
            }
        }
    }
}