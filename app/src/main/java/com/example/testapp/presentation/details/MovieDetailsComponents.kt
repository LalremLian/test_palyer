package com.example.testapp.presentation.details

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewDialog(
    showDialog: MutableState<Boolean>,
    url: String,
    onDismiss: () -> Unit = {}
) {
    var showDismissButton by remember { mutableStateOf(false) }
    var countdown by remember { mutableStateOf(15) } // countdown starts from 5

    LaunchedEffect(key1 = showDialog.value) {
        if (showDialog.value) {
            while (countdown > 0) {
                delay(1000L) // delay for 1 second
                countdown--
            }
            showDismissButton = true
        }
    }

    if (showDialog.value) {
        Dialog(onDismissRequest = {}) { // Removed the onDismissRequest parameter
            // Use `Box` for the Dialog's container
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.White)) {
                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            this.settings.javaScriptEnabled = true
                            this.webViewClient = WebViewClient() // Set WebViewClient
                            this.loadUrl(url)
                        }
                    },
                    update = { webView -> webView.loadUrl(url) }
                )
                if (showDismissButton) {
                    Button(onClick = {
                        showDialog.value = false
                        onDismiss.invoke()
                    }) {
                        Text("Dismiss")
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                            .background(Color.White, RoundedCornerShape(15.dp))
                            .clip(RoundedCornerShape(15.dp))
                    ) {
                        Text(
                            "$countdown",
                            Modifier.align(Alignment.Center),
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}
