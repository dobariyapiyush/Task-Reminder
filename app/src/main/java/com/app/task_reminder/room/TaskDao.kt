package com.app.task_reminder.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM $TASK_TABLE")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM $TASK_TABLE WHERE id = :id")
    fun getTask(id: Int): Flow<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM $TASK_TABLE WHERE deadlineMillis >= :currentTimeMillis ORDER BY deadlineMillis ASC LIMIT 1")
    suspend fun getNextTask(currentTimeMillis: Long): Task?

}

