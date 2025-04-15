package com.example.taller2_aplicacion_photobooth

import android.R.attr.text
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale



@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermissionScreen() {
    val permissionState = rememberPermissionState(permission = android.Manifest.permission.CAMERA)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        androidx.compose.ui.graphics.Color(0xFFFFD194),
                        androidx.compose.ui.graphics.Color(0xFF70E1F5)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            when {
                permissionState.status.isGranted -> {
                    CameraView()
                }
                permissionState.status.shouldShowRationale -> {
                    Text("Necesito el permiso para poder tomar las fotos que necesitass!! >:v ")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        permissionState.launchPermissionRequest()
                    },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = androidx.compose.ui.graphics.Color.White,
                            contentColor = androidx.compose.ui.graphics.Color(0xFF2193b0)
                        )
                    ) {
                        Text(text = "Dame Permiso 🙏🥺")
                    }
                }
                else -> {
                    Text("✨Acepta los permisos para poder empezar ✨")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        permissionState.launchPermissionRequest()
                    },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = androidx.compose.ui.graphics.Color.White,
                            contentColor = androidx.compose.ui.graphics.Color(0xFF2193b0)
                        )
                    ) {
                        Text(text = "Dame Permiso 🙏🥺")
                    }
                }
            }
        }
    }
}
