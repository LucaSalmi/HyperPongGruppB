package com.example.hyperponggruppb.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.hyperponggruppb.R
import com.example.hyperponggruppb.controller.PlayerManager
import com.example.hyperponggruppb.model.PlayerData

class UserSelectionAdapter (val context: Context, private val users: MutableList<PlayerData>): RecyclerView.Adapter<UserSelectionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_selection_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val user = users[position]

        var nameString = "Name: " + user.name
        var highScoreString = "HighScore: " + user.highScore.toString()
        var levelString = "Level: " + user.currentLevel.toString()

        holder.userName.text = nameString
        holder.userHighScore.text = highScoreString
        holder.userLevel.text = levelString

        holder.linearLayout.setOnClickListener {

            Toast.makeText(context, user.name, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val userName = itemView.findViewById<TextView>(R.id.tv_user_name)
        val userHighScore = itemView.findViewById<TextView>(R.id.tv_user_high_score)
        val userLevel = itemView.findViewById<TextView>(R.id.tv_user_current_level)
        val linearLayout = itemView.findViewById<LinearLayout>(R.id.user_linear_layout)

    }
}