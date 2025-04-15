package com.example.taller2_aplicacion_photobooth

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CameraPermissionScreen(
    hasCameraPermission: Boolean,
    requestPermission: () -> Unit,
    onPhotoTaken: (File) -> Unit,
    photos: List<File>
) {
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
        if (hasCameraPermission) {
            Column(modifier = Modifier.fillMaxSize()) {
                CameraView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.3f),
                    onPhotoTaken = onPhotoTaken
                )
                GalleryScreen(
                    photos = photos,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.7f)
                )
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "✨Acepta los permisos para poder empezar ✨",
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { requestPermission() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = androidx.compose.ui.graphics.Color.White,
                        contentColor = androidx.compose.ui.graphics.Color(0xFF2193b0)
                    )
                ) {
                    Text(text = "Dame Permiso 🙏🥺")
                }
            }
        }
    }//
}