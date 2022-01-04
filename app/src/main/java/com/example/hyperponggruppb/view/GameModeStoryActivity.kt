package com.example.hyperponggruppb.view

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.commit
import com.example.hyperponggruppb.R
import com.example.hyperponggruppb.view.fragment.PointFragment
import com.example.hyperponggruppb.view.fragment.StoryLevelFragment

class GameModeStoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_mode_story)
        setFragments()

    }

    private fun setFragments(){

        supportFragmentManager.commit {
            add(R.id.score_fragment_container, PointFragment())
            add(R.id.gameViewContainer, StoryLevelFragment())
        }
    }


    fun updateText() {

        runOnUiThread(Runnable {
            try {

                supportFragmentManager.commit {
                    replace(R.id.frame_layout, PointFragment())
                }

            }catch (e: Exception){
                Log.e(ContentValues.TAG, "updateText: caught")
            }

        })
    }
}