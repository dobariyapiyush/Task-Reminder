package com.app.task_reminder.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.task_reminder.R
import com.app.task_reminder.adapter.TaskAdapter
import com.app.task_reminder.databinding.ActivityMainBinding
import com.app.task_reminder.room.TaskViewModel
import com.app.task_reminder.room.TaskViewModelFactory
import com.app.task_reminder.utilities.commonDialog
import com.app.task_reminder.utilities.gone
import com.app.task_reminder.utilities.toastMsg
import com.app.task_reminder.utilities.visible
import com.app.task_reminder.base.BaseActivity
import com.app.task_reminder.base.BaseBindingActivity

class MainActivity : BaseBindingActivity<ActivityMainBinding>() {

    private val activity: Activity = this@MainActivity
    private lateinit var taskAdapter: TaskAdapter

    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(applicationContext)
    }

    override fun setBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun getActivityContext(): BaseActivity {
        return this@MainActivity
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { _ -> }

    override fun initView() {
        super.initView()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(permission)
            }
        }

        with(mBinding) {
            recyclerView.layoutManager = LinearLayoutManager(activity)
            taskAdapter = TaskAdapter(activity) { task ->
                commonDialog(layoutResId = R.layout.dialog_delete,
                    cancelable = true,
                    positiveClickListener = { dialog, _ ->
                        dialog.dismiss()
                        taskViewModel.deleteTask(task)
                        toastMsg(getString(R.string.task_deleted_successfully))
                    },
                    negativeClickListener = {
                    })
            }

            recyclerView.adapter = taskAdapter

            taskViewModel.tasks.observe(this@MainActivity) { tasks ->
                if (tasks.isEmpty()) {
                    txtNoTask.visible
                    recyclerView.gone
                } else {
                    txtNoTask.gone
                    recyclerView.visible
                    taskAdapter.setTasks(tasks)
                }
            }

            addTask.setOnClickListener {
                startActivity(
                    Intent(
                        this@MainActivity,
                        InsertTaskActivity::class.java
                    ).setAction("")
                )
            }

            layBack.setOnClickListener {
                finishAffinity()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        taskAdapter.startUpdatingTime()
    }

    override fun onPause() {
        super.onPause()
        taskAdapter.stopUpdatingTime()
    }

}
