package com.example.taller2_aplicacion_photobooth

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CameraView(
    modifier: Modifier = Modifier,
    onPhotoTaken: (File) -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    var lensFacing by remember { mutableIntStateOf(CameraSelector.LENS_FACING_BACK) }
    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }
    val photoPath = remember { mutableStateOf<String?>(null) }

    // Columna para organizar la vista previa y los botones
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Vista previa de la cámara
        CameraPreviewWithFrame(
            cameraProviderFuture = cameraProviderFuture,
            lifecycleOwner = lifecycleOwner,
            lensFacing = lensFacing,
            imageCapture = imageCapture,
            modifier = Modifier.weight(1f)
        )

        // Botones debajo de la cámara
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            FloatingActionButton(onClick = {
                val photoFile = File(context.filesDir, "IMG_${System.currentTimeMillis()}.jpg")
                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
                imageCapture.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onError(exc: ImageCaptureException) {
                            Toast.makeText(context, "Error: ${exc.message}", Toast.LENGTH_SHORT).show()
                        }

                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            photoPath.value = photoFile.absolutePath
                            onPhotoTaken(photoFile)
                            Toast.makeText(context, "📸 Foto guardada correctamente", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }) {
                Text("Tomar foto")
            }

            FloatingActionButton(onClick = {
                lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK)
                    CameraSelector.LENS_FACING_FRONT
                else
                    CameraSelector.LENS_FACING_BACK
            }) {
                Text("Cambiar cámara")
            }
        }
    }
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
            androidx.compose.ui.graphics.Color(0xFFE65C00),
            androidx.compose.ui.graphics.Color(0xFF0099FF)
        ),
        start = androidx.compose.ui.geometry.Offset(0f, 0f),
        end = androidx.compose.ui.geometry.Offset(1000f, 1000f)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(LocalConfiguration.current.screenHeightDp.dp * 0.3f)
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
                // Actualizar la cámara cuando lensFacing cambie
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