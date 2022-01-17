package com.example.hyperponggruppb.controller

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import com.example.hyperponggruppb.view.fragment.InfinityFragment
import com.example.hyperponggruppb.view.fragment.PointFragmentInfinityMode
import com.example.hyperponggruppb.R
import com.example.hyperponggruppb.databinding.ActivityGameModeOneBinding
import android.util.Log
import com.example.hyperponggruppb.model.AssetManager
import java.lang.Exception


class GameModeOneActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameModeOneBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameModeOneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.commit {
            add(R.id.frame_layout, PointFragmentInfinityMode())
        }

        supportFragmentManager.commit {
            add(R.id.overworld_map_container, InfinityFragment())
        }
    }

    fun updateText() {

        runOnUiThread(Runnable {

            try {
                supportFragmentManager.commit {
                    replace(R.id.frame_layout, PointFragmentInfinityMode())
                }
            }catch (e: Exception){
                Log.e(TAG, "updateText: caught")
            }

        })
    }

    override fun onBackPressed() {
        PsyduckEngine.gameStart = false
        AssetManager.resetBackGround()
        SoundEffectManager.stopMusic()
        super.onBackPressed()
    }
}