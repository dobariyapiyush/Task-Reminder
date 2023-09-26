package com.app.task_reminder.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.activity.viewModels
import com.app.task_reminder.R
import com.app.task_reminder.databinding.ActivityTaskBinding
import com.app.task_reminder.reminder.TaskReminderHelper
import com.app.task_reminder.room.Task
import com.app.task_reminder.room.TaskViewModel
import com.app.task_reminder.room.TaskViewModelFactory
import com.app.task_reminder.utilities.dateTimeStringToLong
import com.app.task_reminder.utilities.formatDate
import com.app.task_reminder.utilities.formatTime
import com.app.task_reminder.utilities.gone
import com.app.task_reminder.utilities.longToDateString
import com.app.task_reminder.utilities.longToTimeString
import com.app.task_reminder.utilities.toastMsg
import com.app.task_reminder.utilities.visible
import com.app.task_reminder.base.BaseActivity
import com.app.task_reminder.base.BaseBindingActivity
import java.util.Calendar

class UpdateTaskActivity : BaseBindingActivity<ActivityTaskBinding>() {

    private var valueDate = ""
    private var valueTime = ""
    private lateinit var selectedDate: Calendar
    private lateinit var selectedTime: Calendar
    private lateinit var datePicker: DatePickerDialog
    private lateinit var timePicker: TimePickerDialog

    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(applicationContext)
    }

    override fun setBinding(): ActivityTaskBinding {
        return ActivityTaskBinding.inflate(layoutInflater)
    }

    override fun getActivityContext(): BaseActivity {
        return this@UpdateTaskActivity
    }

    override fun initView() {
        super.initView()
        selectedDate = Calendar.getInstance()
        selectedTime = Calendar.getInstance()

        datePicker = DatePickerDialog(
            this,
            { _, year, month, day ->
                selectedDate.set(year, month, day)
                valueDate = formatDate(selectedDate)
                dateSelected()
            },
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.datePicker.minDate = System.currentTimeMillis() - 1000

        timePicker = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)
                valueTime = formatTime(selectedTime)
                timeSelected()
            },
            selectedTime.get(Calendar.HOUR_OF_DAY),
            selectedTime.get(Calendar.MINUTE),
            true
        )

        with(mBinding) {
            val receivedTask = intent.getSerializableExtra("task") as? Task
            if (receivedTask != null) {
                editTitle.setText(receivedTask.title)
                editDescription.setText(receivedTask.description)
                valueDate = longToDateString(receivedTask.deadlineMillis)
                valueTime = longToTimeString(receivedTask.deadlineMillis)
            }

            txtMainTitle.text = resources.getString(R.string.update_task)
            txtSave.text = resources.getString(R.string.update)

            layBack.setOnClickListener {
                finish()
            }

            pickDate.setOnClickListener {
                datePicker.show()
            }

            pickTime.setOnClickListener {
                if (valueDate.isEmpty())
                    toastMsg(getString(R.string.set_the_date_first))
                else
                    timePicker.show()
            }

            timeClear.setOnClickListener {
                timeClear()
            }

            dateClear.setOnClickListener {
                dateClear()
            }

            txtSave.setOnClickListener {
                val title = editTitle.text.toString()
                val description = editDescription.text.toString()
                if (title.isEmpty())
                    toastMsg(getString(R.string.please_enter_title))
                else if (description.isEmpty())
                    toastMsg(getString(R.string.please_enter_description))
                else if (valueDate.isEmpty())
                    toastMsg(getString(R.string.please_pick_date))
                else if (valueTime.isEmpty())
                    toastMsg(getString(R.string.please_pick_time))
                else {
                    val task = Task(
                        id = receivedTask!!.id,
                        title = title,
                        description = description,
                        deadlineMillis = dateTimeStringToLong("$valueDate $valueTime")
                    )
                    TaskReminderHelper.scheduleTaskReminder(
                        applicationContext,
                        task.id,
                        task.title,
                        task.deadlineMillis
                    )
                    taskViewModel.updateTask(task)
                    toastMsg(getString(R.string.task_updated_successfully))
                    finish()
                }
            }

            dateSelected()
            timeSelected()
        }
    }

    private fun dateSelected() {
        mBinding.txtDate.text = valueDate
        mBinding.imgCalendar.gone
        mBinding.dateClear.visible
        mBinding.pickDate.isClickable = false
    }

    private fun timeSelected() {
        mBinding.txtTime.text = valueTime
        mBinding.imgClock.gone
        mBinding.timeClear.visible
        mBinding.pickTime.isClickable = false
    }

    private fun dateClear() {
        valueDate = ""
        mBinding.txtDate.text = resources.getString(R.string.set_date)
        mBinding.imgCalendar.visible
        mBinding.dateClear.gone
        mBinding.pickDate.isClickable = true
        timeClear()
    }

    private fun timeClear() {
        valueTime = ""
        mBinding.txtTime.text = resources.getString(R.string.set_reminder)
        mBinding.imgClock.visible
        mBinding.timeClear.gone
        mBinding.pickTime.isClickable = true
    }
}
