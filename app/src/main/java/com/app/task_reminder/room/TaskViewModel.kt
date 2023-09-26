package com.app.task_reminder.room

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TaskViewModel(context: Context) : ViewModel() {
    private val taskRepository: TaskRepository

    private val _tasks: MutableLiveData<List<Task>> = MutableLiveData()
    val tasks: LiveData<List<Task>> = _tasks

    private val _task: MutableLiveData<Task> = MutableLiveData()
    val task: LiveData<Task> = _task

    init {
        val taskDao: TaskDao = TaskDatabase.getDatabase(context).taskDao()
        taskRepository = TaskRepository(taskDao)
        getAllTasks()
    }

    private fun getAllTasks() {
        viewModelScope.launch {
            taskRepository.getAllTasks().collect { taskList ->
                _tasks.value = taskList
            }
        }
    }

    fun getTask(id: Int) {
        viewModelScope.launch {
            taskRepository.getTask(id).collect { task ->
                _task.value = task
            }
        }
    }

    fun insertTask(task: Task) {
        viewModelScope.launch {
            taskRepository.insertTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            taskRepository.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class TaskViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            return TaskViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
