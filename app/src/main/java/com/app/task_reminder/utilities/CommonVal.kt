package com.app.task_reminder.utilities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.*
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.app.task_reminder.R
import java.util.*

@SuppressLint("RestrictedApi")
fun Context.commonDialog(
    layoutResId: Int,
    cancelable: Boolean,
    positiveClickListener: (dialog: Dialog, dialogView: View) -> Unit,
    negativeClickListener: (View) -> Unit,
    outsideTouchListener: DialogInterface.OnCancelListener? = null
) {
    val dialogView = LayoutInflater.from(this).inflate(layoutResId, null)

    val builder = AlertDialog.Builder(this)
        .setView(dialogView)
        .setCancelable(cancelable)

    val dpi = this.resources.displayMetrics.density
    builder.setView(
        dialogView,
        (19 * dpi).toInt(),
        (5 * dpi).toInt(),
        (14 * dpi).toInt(),
        (5 * dpi).toInt()
    )

    val dialog = builder.create()
    dialog.show()

    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

    val positiveButton = dialogView.findViewById<TextView>(R.id.dialog_button_positive)
    val negativeButton = dialogView.findViewById<TextView>(R.id.dialog_button_negative)

    positiveButton.setOnClickListener {
        positiveClickListener(dialog, dialogView)
    }

    negativeButton.setOnClickListener {
        negativeClickListener(dialogView)
        dialog.dismiss()
    }

    dialog.setCanceledOnTouchOutside(cancelable)
    dialog.setOnCancelListener(outsideTouchListener)
}
