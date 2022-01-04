package com.example.hyperponggruppb.view

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.commit
import com.example.hyperponggruppb.R
import com.example.hyperponggruppb.controller.PhysicsEngine
import com.example.hyperponggruppb.databinding.ActivityGameModeStoryBinding
import com.example.hyperponggruppb.view.fragment.PointFragment
import com.example.hyperponggruppb.view.fragment.StoryLevelFragment

class GameModeStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameModeStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameModeStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFragments()

    }

    private fun setFragments(){

        supportFragmentManager.commit {
            add(R.id.score_fragment_container_story, PointFragment())
            add(R.id.story_view_container, StoryLevelFragment())
        }
    }


    fun updateText() {

        runOnUiThread(Runnable {
            try {

                supportFragmentManager.commit {
                    replace(R.id.score_fragment_container_story, PointFragment())
                }

            }catch (e: Exception){
                Log.e(ContentValues.TAG, "updateText: caught")
            }

        })
    }

    override fun onBackPressed() {
        PhysicsEngine.gameStart = false
        super.onBackPressed()
    }
}