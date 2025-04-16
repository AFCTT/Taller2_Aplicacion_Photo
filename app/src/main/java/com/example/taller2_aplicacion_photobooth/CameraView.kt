package com.example.taller2_aplicacion_photobooth

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.common.util.concurrent.ListenableFuture

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CameraView(
    modifier: Modifier = Modifier,
    lensFacing: Int, // Pasamos lensFacing como parámetro
    imageCapture: ImageCapture // Pasamos imageCapture como parámetro
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    // Vista previa de la cámara (30%)
    CameraPreviewWithFrame(
        cameraProviderFuture = cameraProviderFuture,
        lifecycleOwner = lifecycleOwner,
        lensFacing = lensFacing,
        imageCapture = imageCapture,
        modifier = Modifier.fillMaxWidth().fillMaxSize(0.3f)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CameraPreviewWithFrame(
    cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
    lifecycleOwner: LifecycleOwner,
    lensFacing: Int,
    imageCapture: ImageCapture,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val gradient = Brush.linearGradient(
        colors = listOf(
            androidx.compose.ui.graphics.Color(0xFFFFD194),
            androidx.compose.ui.graphics.Color(0xFF70E1F5)
        ),
        start = androidx.compose.ui.geometry.Offset(0f, 0f),
        end = androidx.compose.ui.geometry.Offset(1000f, 1000f)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(4.dp, gradient, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
    ) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx).apply {
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                }
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(lensFacing)
                    .build()

                cameraProviderFuture.addListener({
                    try {
                        val cameraProvider = cameraProviderFuture.get()
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageCapture
                        )
                    } catch (e: Exception) {
                        Toast.makeText(ctx, "Error al iniciar la cámara: ${e.message}", Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
                }, ContextCompat.getMainExecutor(ctx))
                previewView
            },
            modifier = Modifier.fillMaxSize(),
            update = { previewView ->
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(lensFacing)
                    .build()

                cameraProviderFuture.addListener({
                    try {
                        val cameraProvider = cameraProviderFuture.get()
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageCapture
                        )
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error al cambiar la cámara: ${e.message}", Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
                }, ContextCompat.getMainExecutor(context))
            }
        )
    }
}