package com.example.taller2_aplicacion_photobooth

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
        ) { isGranted: Boolean ->
            if (isGranted) {
                setContent {
                    Taller2_Aplicacion_PhotoboothTheme {
                        MainScreen()
                    }
                }
            } else {
                setContent {
                    Taller2_Aplicacion_PhotoboothTheme {
                        Text("Permiso de cámara denegado. Por favor, habilítalo en los ajustes.")
                    }
                }
            }
        }

        // Verificar permisos iniciales y lanzar la UI
        val hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED
        if (hasCameraPermission) {
            setContent {
                Taller2_Aplicacion_PhotoboothTheme {
                    MainScreen()
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    private fun MainScreen() {
        val photos = remember { mutableStateListOf<File>() }

        // Cargar fotos existentes al iniciar
        LaunchedEffect(Unit) {
            val files = filesDir.listFiles { file -> file.extension == "jpg" }?.toList() ?: emptyList()
            photos.addAll(files)
        }

        CameraPermissionScreen(
            hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED,
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