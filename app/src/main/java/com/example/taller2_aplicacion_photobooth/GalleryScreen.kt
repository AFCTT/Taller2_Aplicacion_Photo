package com.example.taller2_aplicacion_photobooth

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import java.io.File

@Composable
fun GalleryScreen(
    photos: List<File> = emptyList(),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    onPhotoTaken: (File) -> Unit = {},
    lensFacing: Int,
    onLensFacingChange: (Int) -> Unit,
    imageCapture: ImageCapture
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        androidx.compose.ui.graphics.Color(0xFFFFD194),
                        androidx.compose.ui.graphics.Color(0xFF70E1F5)
                    )
                )
            )
            .padding(16.dp)
    ) {
        // Contenido de la galería (fotos o mensaje)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Ocupa el espacio disponible
        ) {
            if (photos.isEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(R.drawable.camara_gift),
                        contentDescription = "No photos",
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = "No tienes fotos, toma una!",
                        fontSize = 18.sp,
                        color = androidx.compose.ui.graphics.Color.White,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(photos) { photo ->
                        Image(
                            painter = rememberAsyncImagePainter(photo),
                            contentDescription = "Foto tomada",
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }

        // Botones debajo de la galería
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
                            onPhotoTaken(photoFile)
                            Toast.makeText(context, "📸 Foto guardada correctamente", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }) {
                Image(
                    painter = rememberAsyncImagePainter(R.drawable.camara),
                    contentDescription = "No photos",
                    modifier = Modifier.size(48.dp)
                )
            }

            FloatingActionButton(onClick = {
                onLensFacingChange(
                    if (lensFacing == androidx.camera.core.CameraSelector.LENS_FACING_BACK)
                        androidx.camera.core.CameraSelector.LENS_FACING_FRONT
                    else
                        androidx.camera.core.CameraSelector.LENS_FACING_BACK
                )
            }) {
                Image(
                    painter = rememberAsyncImagePainter(R.drawable.rotar_camara),
                    contentDescription = "No photos",
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}