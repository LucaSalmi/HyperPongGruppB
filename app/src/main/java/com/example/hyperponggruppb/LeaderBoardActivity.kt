package com.example.hyperponggruppb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hyperponggruppb.controller.PlayerManager
import com.example.hyperponggruppb.databinding.ActivityLeaderBoardBinding


private lateinit var binding: ActivityLeaderBoardBinding

class LeaderBoardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaderBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.leaderboard.layoutManager = LinearLayoutManager(this)
        val adapter = LeaderboardAdapter(PlayerManager.usersArray)
        binding.leaderboard.adapter = adapter
    }
}