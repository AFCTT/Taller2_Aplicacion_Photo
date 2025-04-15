# 📸 Aplicación Photobooth

¡Bienvenido/a a **Photobooth**! 🎉 Esta es una aplicación para Android desarrollada con **Jetpack Compose** que simula una cabina de fotos. Utiliza **CameraX** para la funcionalidad de la cámara y **Accompanist** para gestionar permisos, permitiendo a los usuarios tomar fotos fácilmente. ¡Captura tus mejores momentos! 🖼️

## ✨ Características

- **Gestión de Permisos de Cámara** 🔒  
  - Al abrir la app, se verifica si tiene permisos para usar la cámara.  
  - Si no los tiene, se muestra un mensaje en pantalla completa solicitando al usuario que habilite los permisos con un botón para aceptar.

- **Vista Previa y Captura de Fotos** 📷  
  - Una vista previa de la cámara ocupa el ancho completo y el 30% de la altura de la pantalla.  
  - Incluye dos botones:  
    - **Tomar Foto** 📸: Captura una foto y la guarda en el almacenamiento interno de la app.  
    - **Cambiar Cámara** 🔄: Alterna entre la cámara frontal y trasera.  
  - Un mensaje Toast confirma cada captura exitosa. ✅

- **Galería de Fotos** 🖼️  
  - Muestra las fotos tomadas en un `LazyVerticalStaggeredGrid`.  
  - Si no hay fotos, se muestra un mensaje: "¡No tienes fotos, toma una!". 🚫

## 🛠️ Tecnologías Utilizadas

- **Jetpack Compose** 🎨: Para construir la interfaz de usuario.  
- **CameraX** 📷: Para la funcionalidad de la cámara.  
- **Accompanist Permissions** 🔐: Para gestionar permisos en tiempo de ejecución.  
- **Kotlin** ☕: Lenguaje de programación utilizado.  
- **Almacenamiento Interno de Android** 💾: Para guardar las fotos.

## 📦 Configuración

### Requisitos Previos
- Android Studio (se recomienda la última versión) 🐬  
- Kotlin 1.9+ ☕  
- Dispositivo o emulador Android con API 21+ 📱  

### Pasos para Ejecutar
1. **Clonar el Repositorio** 🖥️  
   ```bash
   git clone https://github.com/tuusuario/photobooth-app.git
2. **Abrir en Android Studio 🛠️**
- Abre el proyecto en Android Studio.
- Sincroniza el proyecto con Gradle. 🔄

 3. **Agregar Dependencias 📚**
Asegúrate de incluir lo siguiente en tu build.gradle (nivel de app):
```plaintext
dependencies {
    // Jetpack Compose
    implementation "androidx.activity:activity-compose:1.8.0"
    implementation "androidx.compose.ui:ui:1.5.0"
    implementation "androidx.compose.material:material:1.5.0"
    implementation "androidx.compose.runtime:runtime-livedata:1.5.0"

    // CameraX
    implementation "androidx.camera:camera-core:1.3.0"
    implementation "androidx.camera:camera-camera2:1.3.0"
    implementation "androidx.camera:camera-lifecycle:1.3.0"
    implementation "androidx.camera:camera-view:1.3.0"

    // Accompanist Permissions
    implementation "com.google.accompanist:accompanist-permissions:0.34.0"
}
```
4. **Agregar Permisos 🔑**
En tu AndroidManifest.xml, incluye
```plaintext
<uses-permission android:name="android.permission.CAMERA" />
<uses-feature android:name="android.hardware.camera" android:required="true" />
```


5. **Ejecutar la App 🚀**
- Conecta un dispositivo Android o inicia un emulador.
- Haz clic en el botón "Run" en Android Studio.


## 📖 Uso
### 1. Iniciar la App 🚀
- Al abrir la app por primera vez, ver
### 2. Tomar Fotos 📸
- Una vez otorgados los permisos, aparecerá la vista previa de la cámara.
- Usa el botón "Tomar Foto" para capturar una imagen.
- Cambia entre las cámaras frontal y trasera con el botón "Cambiar Cámara". 🔄
- Un mensaje Toast confirmará cada foto tomada. 🔔
### 3. Ver Fotos 🖼️
- Desplázate por el `LazyVerticalStaggeredGrid` debajo de la vista previa para ver tus fotos capturadas.
- Si no hay fotos, aparecerá el mensaje "¡No tienes fotos, toma una!". 🚫
## 🖥️ Capturas de Pantalla
- Solicitud de Permisos 📜
- Cámara y Galería 📸
	
## ⚙️ Aspectos Destacados del Código
- **Gestión de Permisos con Accompanist 🔒**
Utiliza `rememberPermissionState` para manejar los permisos de cámara y muestra una UI personalizada si se deniegan.
- **Integración con CameraX 📷**
Integra `CameraX` con un `PreviewView` para la vista previa de la cámara y gestiona la captura de fotos con `ImageCapture`.
- **Almacenamiento de Fotos 💾**
Guarda las fotos en el almacenamiento interno de la app usando `Context.getFilesDir()`.
- **LazyVerticalStaggeredGrid 🖼️**
Muestra las fotos en una cuadrícula escalonada usando `LazyVerticalStaggeredGrid` de Jetpack Compose.
## 📝 Notas
- Asegúrate de que tu dispositivo tenga una cámara funcional. 📷
- Las fotos se guardan en el almacenamiento interno de la app y no son accesibles fuera de ella. 🔒
- La app soporta tanto la cámara frontal como la trasera. 🔄


### 📜 Instrucciones para GitHub:
1. Ve a tu repositorio en GitHub.
2. Crea o edita el archivo `README.md` (si no existe, puedes crearlo haciendo clic en "Create new file").
3. Copia y pega el código anterior en el editor de GitHub.
4. Asegúrate de que las imágenes de las capturas de pantalla (`screenshots/permission_screen.jpg` y `screenshots/camera_screen.jpg`) estén subidas a tu repositorio en una carpeta llamada `screenshots`. Puedes subir las imágenes que compartiste en tu consulta.
5. Haz clic en "Commit changes" para guardar el archivo.

Este README se verá bien formateado en GitHub con los emojis y la estructura clara. Si necesitas ayuda con algo más, como el código de la app en Kotlin, ¡avísame! 😊
