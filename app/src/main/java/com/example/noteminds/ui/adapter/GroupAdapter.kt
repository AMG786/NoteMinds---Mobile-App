package com.example.noteminds.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteminds.R
import com.example.noteminds.ui.model.Group
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference

class GroupAdapter(
    ref: DatabaseReference,
    private val onGroupClick: (Group) -> Unit
) : FirebaseRecyclerAdapter<Group, GroupAdapter.GroupViewHolder>(
    FirebaseRecyclerOptions.Builder<Group>()
        .setQuery(ref, Group::class.java)
        .build()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_group, parent, false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int, model: Group) {
        holder.bind(model)
        holder.itemView.setOnClickListener {
            val group = getItem(position)
            onGroupClick(group)
        }
        Log.d("GroupAdapter", "Binding group: ${model.name}")
        System.out.println("GroupAdapter Binding group: ${model.name}")
    }
    inner class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvGroupName)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvGroupDescription)
        private val tvCategory: TextView = itemView.findViewById(R.id.tvGroupCategory)

        fun bind(group: Group) {
            tvName.text = group.name
            tvDescription.text = group.description
            tvCategory.text = group.category

            itemView.setOnClickListener { onGroupClick(group) }
        }
    }


}