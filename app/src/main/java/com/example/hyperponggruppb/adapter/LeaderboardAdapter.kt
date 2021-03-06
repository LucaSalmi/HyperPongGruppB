package com.example.hyperponggruppb.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hyperponggruppb.R
import com.example.hyperponggruppb.model.PlayerData

class LeaderboardAdapter(private val highScoreArray: MutableList<PlayerData>): RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val playerHiScore = highScoreArray[position]

        holder.playerName.text = playerHiScore.name
        holder.playerHiScore.text = playerHiScore.highScore.toString()
        when (position){

            0 -> holder.trophyImage.setImageResource(R.drawable.gold_trophy)
            1 -> holder.trophyImage.setImageResource(R.drawable.silver_trophy)
            2 -> holder.trophyImage.setImageResource(R.drawable.bronze_trophy)
            else -> holder.trophyImage.setImageResource(R.drawable.gem)
        }

    }

    override fun getItemCount(): Int {
        return highScoreArray.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val trophyImage = itemView.findViewById<ImageView>(R.id.iv_trophy)
        val playerName = itemView.findViewById<TextView>(R.id.tv_player_name)
        val playerHiScore = itemView.findViewById<TextView>(R.id.tv_player_high_score)

    }
}
