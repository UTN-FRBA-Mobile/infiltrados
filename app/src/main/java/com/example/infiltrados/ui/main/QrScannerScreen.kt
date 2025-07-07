@file:OptIn(com.google.accompanist.permissions.ExperimentalPermissionsApi::class)

package com.example.infiltrados.ui.main

import android.Manifest
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.saveable.rememberSaveable
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

@OptIn(ExperimentalGetImage::class)
@Composable
fun QrScannerScreen(
    navController: NavController,
    onScanned: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasPermission by rememberSaveable { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    if (!hasPermission) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Se necesita permiso para acceder a la cámara")
        }
        return
    }

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val previewView = remember {
        PreviewView(context).apply { scaleType = PreviewView.ScaleType.FILL_CENTER }
    }

    AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())

    LaunchedEffect(Unit) {
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val barcodeScanner = BarcodeScanning.getClient()
        val analysisUseCase = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        analysisUseCase.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                barcodeScanner.process(image)
                    .addOnSuccessListener { barcodes ->
                        val qr = barcodes.firstOrNull { it.valueType == Barcode.TYPE_TEXT }
                        val value = qr?.rawValue
                        if (!value.isNullOrBlank()) {
                            Log.d("QR_SCANNER", "✅ QR escaneado: $value")
                            onScanned(value)
                            cameraProvider.unbindAll()
                        }
                    }
                    .addOnFailureListener {
                        Log.e("QR_ERROR", "Error escaneando: ${it.localizedMessage}")
                    }
                    .addOnCompleteListener {
                        imageProxy.close()
                    }
            } else {
                imageProxy.close()
            }
        }

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, analysisUseCase)
    }
}
