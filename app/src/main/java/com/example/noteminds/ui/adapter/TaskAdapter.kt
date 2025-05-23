package com.example.noteminds.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteminds.ui.model.Task
import com.example.noteminds.R

/**
Created by Abdul Mueez, 04/24/2025
 */
class TaskAdapter(
    private var tasks: List<Task>,
    private val onTaskClicked: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }
    fun updateTasks(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int = tasks.size

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskIcon: ImageView = itemView.findViewById(R.id.iv_task_icon)
        private val taskTitle: TextView = itemView.findViewById(R.id.tv_task_title)
        private val taskDescription: TextView = itemView.findViewById(R.id.tv_task_description)
        private val statusIndicator: View = itemView.findViewById(R.id.status_indicator)

        fun bind(task: Task) {
            taskTitle.text = task.title
            taskDescription.text = task.description

            val statusColor = when(task.status) {
                Task.Status.COMPLETED -> R.color.green
                Task.Status.IN_PROGRESS -> R.color.yellow
                Task.Status.PENDING -> R.color.red
            }
            statusIndicator.setBackgroundResource(statusColor)

            if (task.isImportant) {
                taskIcon.setImageResource(R.drawable.ic_star)
            } else {
                taskIcon.setImageResource(R.drawable.ic_task)
            }

            itemView.setOnClickListener {
                onTaskClicked(task)
            }
        }
    }
}