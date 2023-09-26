package com.app.task_reminder.adapter

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.task_reminder.R
import com.app.task_reminder.activity.UpdateTaskActivity
import com.app.task_reminder.reminder.TaskReminderHelper
import com.app.task_reminder.room.Task
import com.app.task_reminder.utilities.longToDate1String
import com.app.task_reminder.utilities.longToDateTimeString
import com.app.task_reminder.utilities.longToTimeString

class TaskAdapter(private val context: Context, private val onDeleteClick: (Task) -> Unit) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    private var tasks: List<Task> = emptyList()

    private val handler = Handler()
    private val updateTimeRunnable = object : Runnable {
        override fun run() {
            notifyDataSetChanged()
            handler.postDelayed(this, 1000)
        }
    }

    fun startUpdatingTime() {
        handler.post(updateTimeRunnable)
    }

    fun stopUpdatingTime() {
        handler.removeCallbacks(updateTimeRunnable)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    fun setTasks(tasks: List<Task>) {
        val currentTimeMillis = System.currentTimeMillis()

        val overdueTasks = tasks.filter { it.deadlineMillis <= currentTimeMillis }
        val notOverdueTasks = tasks.filter { it.deadlineMillis > currentTimeMillis }

        val sortedOverdueTasks = overdueTasks.sortedBy { it.deadlineMillis }
        val sortedNotOverdueTasks = notOverdueTasks.sortedBy { it.deadlineMillis }

        this.tasks = sortedNotOverdueTasks + sortedOverdueTasks

        notifyDataSetChanged()
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.textViewDescription)
        private val deadlineDate: TextView = itemView.findViewById(R.id.textViewDeadlineDate)
        private val deadlineTime: TextView = itemView.findViewById(R.id.textViewDeadlineTime)
        private val textViewRemainingTime: TextView =
            itemView.findViewById(R.id.textViewRemainingTime)
        private val editClick: ImageView = itemView.findViewById(R.id.editClick)
        private val deleteClick: ImageView = itemView.findViewById(R.id.deleteClick)
        private val boxLayout: ConstraintLayout = itemView.findViewById(R.id.boxLayout)

        fun bind(task: Task) {
            titleTextView.text = task.title
            descriptionTextView.text = task.description
            textViewRemainingTime.text = task.remainingTime
            deadlineDate.text = longToDate1String(task.deadlineMillis)
            deadlineTime.text = longToTimeString(task.deadlineMillis)
            if (task.remainingTime.contains("completed"))
                boxLayout.setBackgroundResource(R.drawable.card_red)
            else
                boxLayout.setBackgroundResource(R.drawable.card_green)
            editClick.setOnClickListener {
                val intent = Intent(context, UpdateTaskActivity::class.java).setAction("")
                intent.putExtra("task", task)
                context.startActivity(intent)
            }
            deleteClick.setOnClickListener {
                onDeleteClick(task)
            }
        }
    }
}
