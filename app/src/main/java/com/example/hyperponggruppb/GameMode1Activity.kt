package com.example.hyperponggruppb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import com.example.hyperponggruppb.databinding.ActivityGameMode1Binding

class GameMode1Activity : AppCompatActivity() {

    private lateinit var binding: ActivityGameMode1Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameMode1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.commit {
            add(R.id.frame_layout, PointFragment())
        }

        //setContentView(GameView(this))
    }
}