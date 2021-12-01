package com.example.hyperponggruppb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import com.example.hyperponggruppb.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val music = MusicManager()

    val brickRow = mutableListOf<Bricks>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(GameView(this))

        PointManager.playerPoints

        music.context(this)


    }

    override fun onResume() {
        music.start()
        super.onResume()
    }

}