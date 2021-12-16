package com.example.hyperponggruppb

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.hyperponggruppb.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        Handler(Looper.myLooper()!!).postDelayed({

            setTheme(R.style.Theme_HyperPongGruppB)


        }, 2000)

        setContentView(binding.root)
        PlayerManager.playerPoints

        SoundEffectManager.bGMusic(this)


        binding.ivNewGame.setOnClickListener {
            val toGameModeOne = Intent(this, GameMode1Activity::class.java)
            SoundEffectManager.jukebox(this, 1)
            startActivity(toGameModeOne)
        }

    }

    override fun onResume() {
        var stack = Thread.getAllStackTraces()
        Log.d(TAG, "onResume: $stack")
        super.onResume()
    }

}