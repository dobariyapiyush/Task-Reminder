package com.app.task_reminder.reminder

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock

object TaskReminderHelper {

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleTaskReminder(context: Context, taskId: Int, taskTitle: String, deadlineMillis: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, TaskReminderReceiver::class.java).apply {
            action = "TASK_REMINDER_ACTION"
            putExtra("taskId", taskId)
            putExtra("taskTitle", taskTitle)
        }

        val flags = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            PendingIntent.FLAG_MUTABLE
        } else {
            0
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId,
            intent,
            flags
        )

        alarmManager.setExact(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + (deadlineMillis - System.currentTimeMillis()),
            pendingIntent
        )
    }
}