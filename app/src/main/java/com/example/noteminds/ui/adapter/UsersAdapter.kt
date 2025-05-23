package com.example.noteminds.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteminds.R
import com.example.noteminds.ui.model.FirebaseUser
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference

class UsersAdapter(
    ref: DatabaseReference,
    private val onUserClick: (String, String) -> Unit
) : FirebaseRecyclerAdapter<FirebaseUser, UsersAdapter.UserViewHolder>(
    FirebaseRecyclerOptions.Builder<FirebaseUser>()
        .setQuery(ref, FirebaseUser::class.java)
        .build()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int, model: FirebaseUser) {
        holder.bind(model)
        holder.itemView.setOnClickListener {
            val userId = getRef(position).key ?: ""
            onUserClick(userId, model.username ?: "")
        }
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvUsername: TextView = itemView.findViewById(R.id.tvUsername)

        fun bind(user: FirebaseUser) {
            tvUsername.text = user.username
        }
    }
}