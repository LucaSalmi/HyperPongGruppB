package com.example.hyperponggruppb

import android.content.ContentValues
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
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

            setContentView(binding.root)
        }, 2000)

        PointManager.playerPoints

        music.context(this)


        binding.ivNewGame.setOnClickListener {
            setContentView(GameView(this))
            SoundEffectManager.menuPress(0, this)
        }


    }

    override fun onResume() {
        music.start()
        super.onResume()
    }

}