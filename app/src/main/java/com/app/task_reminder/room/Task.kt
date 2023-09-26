package com.app.task_reminder.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Locale
import kotlin.math.ceil
import kotlin.math.floor

@Entity(tableName = TASK_TABLE)
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String = "",
    var description: String = "",
    var deadlineMillis: Long = 0L
) : Serializable {

    val remainingTime: String
        get() {
            val currentTimeMillis = System.currentTimeMillis()
            val timeDifferenceMillis = deadlineMillis - currentTimeMillis

            if (timeDifferenceMillis <= 0) {
                return "Task is completed"
            }

            val days = floor(timeDifferenceMillis.toDouble() / (24 * 60 * 60 * 1000)).toInt()
            val hours = floor((timeDifferenceMillis.toDouble() % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000)).toInt()
            val minutes = ceil((timeDifferenceMillis.toDouble() % (60 * 60 * 1000)) / (60 * 1000)).toInt()

            return when {
                days > 0 -> {
                    if (hours > 0) {
                        if (minutes > 0) {
                            String.format(Locale.getDefault(), "%d days %d hours %d minutes", days, hours, minutes)
                        } else {
                            String.format(Locale.getDefault(), "%d days %d hours", days, hours)
                        }
                    } else {
                        if (minutes > 0) {
                            String.format(Locale.getDefault(), "%d days %d minutes", days, minutes)
                        } else {
                            String.format(Locale.getDefault(), "%d days", days)
                        }
                    }
                }
                hours > 0 -> {
                    if (minutes > 0) {
                        String.format(Locale.getDefault(), "%d hours %d minutes", hours, minutes)
                    } else {
                        String.format(Locale.getDefault(), "%d hours", hours)
                    }
                }
                else -> {
                    String.format(Locale.getDefault(), "%d minutes", minutes)
                }
            }
        }
}


