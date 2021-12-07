package com.example.hyperponggruppb

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.example.hyperponggruppb.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val music = MusicManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        Handler(Looper.myLooper()!!).postDelayed({

            setTheme(R.style.Theme_HyperPongGruppB)


        }, 2000)

        setContentView(binding.root)
        PointManager.playerPoints

        music.context(this)


        binding.ivNewGame.setOnClickListener {
            val toGameModeOne = Intent(this, GameMode1Activity::class.java)
            SoundEffectManager.menuPress(0, this)
            startActivity(toGameModeOne)
        }


    }

    override fun onResume() {
        music.start()
        super.onResume()
    }

}