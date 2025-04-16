package com.example.taller2_aplicacion_photobooth

import android.content.Context
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.background
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LifecycleStartEffect
import com.google.common.util.concurrent.ListenableFuture
import java.io.File
import java.util.concurrent.Executor

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CameraView(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    onPhotoTaken: (File) -> Unit = {} // Callback para notificar cuando se toma una foto
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val photoPath = remember { mutableStateOf<String?>(null) }
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    var lensFacing by remember { mutableIntStateOf(CameraSelector.LENS_FACING_BACK) }
    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }
    val cameraController = remember {
        LifecycleCameraController(context)
    }

    val preview = remember (lensFacing){ Preview.Builder().build() }
    val cameraSelector = remember(lensFacing) {
        CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()
    }

    CameraPreviewWithFrame(
        cameraProviderFuture = cameraProviderFuture,
        lifecycleOwner = lifecycleOwner,
        cameraSelector = cameraSelector,
        preview = preview,
        imageCapture = imageCapture
    )

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        FloatingActionButton (onClick = {
            val photoFile = File(
                context.filesDir,
                "IMG_${System.currentTimeMillis()}.jpg"
            )
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
                        onPhotoTaken(photoFile) // Notificamos que se tomó una foto
                        Toast.makeText(context, "📸 Foto guardada correctamente", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }) {
            Text("Tomar foto")
        }

        FloatingActionButton (onClick = {
            val executor= ContextCompat.getMainExecutor(context)
            takePicture(cameraController, executor)
            lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK)
                CameraSelector.LENS_FACING_FRONT
            else
                CameraSelector.LENS_FACING_BACK
        }) {
            Text("Cambiar cámara")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CameraPreviewWithFrame(
    cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
    lifecycleOwner: LifecycleOwner,
    cameraSelector: CameraSelector,
    preview: Preview,
    imageCapture: ImageCapture
) {
    val gradient = Brush.linearGradient(
        colors = listOf(
            _root_ide_package_.androidx.compose.ui.graphics
                .Color(0xFFE65C00),
            _root_ide_package_.androidx.compose.ui.graphics
                .Color(0xFF0099FF)),
        start = Offset(0f, 0f),
        end = Offset(1000f, 1000f)
    )

    Box(
        modifier = Modifier
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

                try {
                    val cameraProvider = cameraProviderFuture.get()
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture
                    )
                    preview.setSurfaceProvider(previewView.surfaceProvider)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                previewView
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

fun takePicture(cameraController: CameraController, executor: Executor){
    val file= File.createTempFile("imagenText",".jpg")
    val outputDirectory = ImageCapture.OutputFileOptions.Builder(file).build()
    cameraController.takePicture(outputDirectory, executor,object : ImageCapture.OnImageSavedCallback{
        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            println(outputFileResults.savedUri)
        }

        override fun onError(exception: ImageCaptureException) {
            println()
        }
    })
}