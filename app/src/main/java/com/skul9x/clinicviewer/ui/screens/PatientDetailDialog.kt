package com.skul9x.clinicviewer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.skul9x.clinicviewer.data.model.Patient
import com.skul9x.clinicviewer.ui.theme.*
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@Composable
fun PatientDetailDialog(
    patient: Patient,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                // --- Header & Close Button ---
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = patient.name,
                            color = BrandTeal,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 28.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "ID: ${patient.id}",
                            color = TextSecondary,
                            fontSize = 14.sp
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextPrimary)
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // --- Info Rows ---
                InfoRow("Ngày sinh:", formatDate(patient.dob))
                InfoRow("Giới tính:", patient.gender ?: "N/A")
                InfoRow("Cân nặng:", patient.weight ?: "N/A")
                InfoRow("SĐT:", patient.phone ?: "N/A")
                InfoRow("Địa chỉ:", patient.address ?: "N/A", isAddress = true)
                InfoRow("Ngày khám:", formatDate(patient.createdAt))
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // --- Diagnosis Section ---
                Text(
                    text = "🩺 Chẩn đoán",
                    color = BrandTeal,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(HistoryBoxBg, RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = patient.diagnosis?.ifEmpty { "Chưa có chẩn đoán" } ?: "Chưa có chẩn đoán",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // --- Prescription Section ---
                Text(
                    text = "💊 Đơn thuốc",
                    color = BrandTeal,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(HistoryBoxBg, RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = patient.medicalHistory?.ifEmpty { "Chưa có đơn thuốc" } ?: "Chưa có đơn thuốc",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // --- Action Buttons ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            val textToCopy = patient.medicalHistory ?: "Chưa có đơn thuốc"
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Đơn thuốc", textToCopy)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Đã copy đơn thuốc!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandTeal),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Copy Toa")
                    }
                    
                    OutlinedButton(
                        onClick = {
                            val phone = patient.phone ?: ""
                            if (phone.isNotEmpty()) {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("SĐT", phone)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Đã copy SĐT: $phone", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Không có SĐT", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Copy SĐT", color = TextPrimary)
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String, isAddress: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = if (isAddress) Alignment.Top else Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = TextSecondary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.width(100.dp)
        )
        Text(
            text = value,
            color = TextPrimary,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f),
            lineHeight = 20.sp
        )
    }
}
