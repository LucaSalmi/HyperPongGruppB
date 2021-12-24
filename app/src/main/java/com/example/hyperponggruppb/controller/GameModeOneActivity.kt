package com.example.hyperponggruppb.controller

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.commit
import com.example.hyperponggruppb.LeaderBoardActivity
import com.example.hyperponggruppb.view.fragment.GameOneFragment
import com.example.hyperponggruppb.view.fragment.PointFragment
import com.example.hyperponggruppb.R
import com.example.hyperponggruppb.databinding.ActivityGameModeOneBinding

class GameModeOneActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameModeOneBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameModeOneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.commit {
            add(R.id.frame_layout, PointFragment())
        }

        supportFragmentManager.commit {
            add(R.id.gameViewContainer, GameOneFragment())
        }
    }

    fun updateText() {

        runOnUiThread(Runnable {
            supportFragmentManager.commit {
                replace(R.id.frame_layout, PointFragment())
            }
        })
    }

}