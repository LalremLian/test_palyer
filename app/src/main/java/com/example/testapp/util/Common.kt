package com.example.testapp.util

import android.app.Activity
import android.content.Context

fun Context.findActivity(): Activity? = this as? Activity

fun Long.toHhMmSs(): String {
    val seconds = (this / 1000).toInt()
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val remainingSeconds = seconds % 60
    return if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
    } else {
        String.format("%02d:%02d", minutes, remainingSeconds)
    }
}
