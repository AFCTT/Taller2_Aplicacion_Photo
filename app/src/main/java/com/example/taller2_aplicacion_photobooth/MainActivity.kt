package com.example.taller2_aplicacion_photobooth

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.example.taller2_aplicacion_photobooth.ui.theme.Taller2_Aplicacion_PhotoboothTheme
import java.io.File

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Lanzador para permisos
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            setContent {
                Taller2_Aplicacion_PhotoboothTheme {
                    val photos = remember { mutableStateListOf<File>() }

                    // Cargar fotos existentes
                    LaunchedEffect(Unit) {
                        val files = filesDir.listFiles { file -> file.extension == "jpg" }?.toList() ?: emptyList()
                        photos.addAll(files)
                    }

                    CameraPermissionScreen(
                        hasCameraPermission = isGranted,
                        requestPermission = {
                            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)

                        },
                        onPhotoTaken = { file ->
                            photos.add(file)
                        },
                        photos = photos
                    )
                }
            }
        }

        setContent {
            Taller2_Aplicacion_PhotoboothTheme {
                val photos = remember { mutableStateListOf<File>() }

                // Cargar fotos existentes
                LaunchedEffect(Unit) {
                    val files = filesDir.listFiles { file -> file.extension == "jpg" }?.toList() ?: emptyList()
                    photos.addAll(files)
                }

                CameraPermissionScreen(
                    hasCameraPermission = checkSelfPermission(android.Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED,
                    requestPermission = {
                        requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                    },
                    onPhotoTaken = { file ->
                        photos.add(file)
                    },
                    photos = photos
                )
            }
        }
    }
}