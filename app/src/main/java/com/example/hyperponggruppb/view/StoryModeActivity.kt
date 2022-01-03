package com.example.hyperponggruppb.view

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.commit
import com.example.hyperponggruppb.R
import com.example.hyperponggruppb.databinding.ActivityStoryModeBinding
import com.example.hyperponggruppb.view.fragment.FirstWorldFragment
import com.example.hyperponggruppb.view.fragment.PointFragment
import java.lang.Exception

class StoryModeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryModeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryModeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.commit {
            add(R.id.gameViewContainer, FirstWorldFragment())
        }
    }

    private fun setPointsFragment(){

        supportFragmentManager.commit {
            add(R.id.score_fragment_container,PointFragment())
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