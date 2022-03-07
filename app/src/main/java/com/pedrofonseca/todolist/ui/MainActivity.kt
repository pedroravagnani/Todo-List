package com.pedrofonseca.todolist.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pedrofonseca.todolist.databinding.ActivityMainBinding
import com.pedrofonseca.todolist.datasource.TaskDataSource

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTasks.adapter = adapter
        updateList()

        insertListener()
    }

    private fun insertListener() {
        binding.fab.setOnClickListener{
            startActivityForResult(Intent(this, AddTaskActivity::class.java), CREATE_NEW_TASK)

        }

        adapter.listenerEdit = {

            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID, it.id)
            startActivityForResult(intent, CREATE_NEW_TASK)
        }

        adapter.listenerDelete = {
            TaskDataSource.deleteTask(it)
            updateList()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_NEW_TASK && resultCode == -1){
            updateList()
        }
    }

    private fun updateList(){
        val list = TaskDataSource.getList()

        binding.includeEmpty.emptyState.visibility = if(list.isEmpty()) View.VISIBLE
        else View.GONE

        adapter.submitList(list)
        adapter.notifyDataSetChanged()
    }

    companion object{
        private const val CREATE_NEW_TASK = 1000
    }
}