package com.app.task_reminder.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.app.task_reminder.R
import com.app.task_reminder.room.Task
import com.app.task_reminder.room.TaskDatabase
import com.app.task_reminder.utilities.longToDateTimeString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask

class TaskWidgetProvider : AppWidgetProvider() {
    private val updateIntervalMillis: Long = 1000
    private val timer = Timer()
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                scope.launch {
                    val currentTimeMillis = System.currentTimeMillis()
                    val nextTask = getNextTask(context, currentTimeMillis)

                    for (appWidgetId in appWidgetIds) {
                        updateWidget(context, appWidgetManager, appWidgetId, nextTask)
                    }
                }
            }
        }, 0, updateIntervalMillis)
    }

    private suspend fun getNextTask(context: Context, currentTimeMillis: Long): Task? {
        val taskDao = TaskDatabase.getDatabase(context).taskDao()
        return taskDao.getNextTask(currentTimeMillis)
    }

    private fun updateWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        nextTask: Task?
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_layout)

        if (nextTask != null) {
            val title = nextTask.title
            val description = nextTask.description
            val dateTime = longToDateTimeString(nextTask.deadlineMillis)
            views.setTextViewText(R.id.widget_task_title, title)
            views.setTextViewText(R.id.widget_task_description, description)
            views.setTextViewText(R.id.widget_date_time, dateTime)
        } else {
            views.setTextViewText(R.id.widget_task_title, "No upcoming tasks")
            views.setTextViewText(R.id.widget_task_description, "")
            views.setTextViewText(R.id.widget_date_time, "")
        }

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}