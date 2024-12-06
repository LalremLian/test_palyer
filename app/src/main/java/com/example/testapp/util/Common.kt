package com.example.testapp.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.view.View

fun View.backPress() {
    this.setOnClickListener {
        (this.context as Activity).finish()
    }
}
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

fun SharedPreferences.putInt(key: String, value: Int) {
    edit().putInt(key, value).apply()
}

fun SharedPreferences.putBoolean(key: String, value: Boolean) {
    edit().putBoolean(key, value).apply()
}
