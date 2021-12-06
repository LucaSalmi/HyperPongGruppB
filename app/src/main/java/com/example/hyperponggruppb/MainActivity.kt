package com.example.hyperponggruppb

import android.content.ContentValues
import android.content.ContentValues.TAG
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

        supportFragmentManager.commit {
            add(R.id.frame_layout, PointFragment())
        }


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