package com.example.hyperponggruppb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
        PlayerManager.playerPoints

        music.context(this)


        binding.ivNewGame.setOnClickListener {
            val toGameModeOne = Intent(this, GameMode1Activity::class.java)
            SoundEffectManager.jukebox(this, 1)
            startActivity(toGameModeOne)
        }


    }

    override fun onResume() {
        music.start()
        super.onResume()
    }

}