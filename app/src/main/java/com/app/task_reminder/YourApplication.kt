package com.app.task_reminder

import android.app.Application
import com.app.task_reminder.reminder.NotificationChannelHelper

class YourApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NotificationChannelHelper.createNotificationChannel(this)
    }
}
