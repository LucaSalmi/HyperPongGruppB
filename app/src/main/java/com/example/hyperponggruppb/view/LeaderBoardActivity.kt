package com.example.hyperponggruppb.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hyperponggruppb.controller.PlayerManager
import com.example.hyperponggruppb.adapter.LeaderboardAdapter
import com.example.hyperponggruppb.controller.SoundEffectManager
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

    override fun onResume() {

        if (PlayerManager.isMusicActive) {
            SoundEffectManager.musicSetup(this, 1)
        }
        super.onResume()
    }

    override fun onBackPressed() {

        if (PlayerManager.isMusicActive){
            SoundEffectManager.stopMusic()
        }
        super.onBackPressed()
    }
}