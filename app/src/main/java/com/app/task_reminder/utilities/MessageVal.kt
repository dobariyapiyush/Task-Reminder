package com.app.task_reminder.utilities

import android.content.Context
import android.util.Log
import android.widget.Toast

const val isLogMsg: Boolean = true

fun Context.toastMsg(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun logMsg(msg: String) {
    if (isLogMsg)
        Log.e("BLOG", msg)
}