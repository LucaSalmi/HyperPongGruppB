package com.example.hyperponggruppb.view.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.example.hyperponggruppb.R
import com.example.hyperponggruppb.controller.PlayerManager
import com.example.hyperponggruppb.controller.SoundEffectManager

class PointFragmentStoryMode : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_point_story_mode, container, false)

         val lives = view?.findViewById<ImageView>(R.id.iv_story_lives)
         val starBar = view?.findViewById<ProgressBar>(R.id.pb_starbar)
         val score = view?.findViewById<TextView>(R.id.tv_story_score)
         val starOne = view?.findViewById<ImageView>(R.id.iv_star_one)
         val starTwo = view?.findViewById<ImageView>(R.id.iv_star_two)
        val starThree = view?.findViewById<ImageView>(R.id.iv_star_three)

        starBar?.max = PlayerManager.currentMaxScore
        starBar?.progress = 0

        val refScore = 0
        val currentScore = PlayerManager.playerPoints
        score?.text = PlayerManager.playerPoints.toString()
        if (currentScore > refScore) {
            starBar?.progress = (currentScore)
        }
        if (starBar?.progress!! >= PlayerManager.currentMaxScore / 3 && PlayerManager.starCounter == 0) {
            Log.d(TAG, "storyModeUI: star reach")
            PlayerManager.starCounter = 1
            Log.d(TAG, "onCreateView: starcounter = ${PlayerManager.starCounter}")

        }
        if (starBar.progress >= ((PlayerManager.currentMaxScore / 3) * 2) && PlayerManager.starCounter == 1) {
            Log.d(TAG, "storyModeUI:: 2 star reach")
            PlayerManager.starCounter = 2

        }
        if (starBar.progress >= PlayerManager.currentMaxScore && PlayerManager.starCounter == 2) {
            Log.d(TAG, "storyModeUI: 3 star reach")
            PlayerManager.starCounter = 3
        }
        if (PlayerManager.starCounter == 1){
            starOne?.setImageResource(R.drawable.star)
        }
        if (PlayerManager.starCounter == 2){
            starOne?.setImageResource(R.drawable.star)
            starTwo?.setImageResource(R.drawable.star)
        }
        if (PlayerManager.starCounter == 3){

            starOne?.setImageResource(R.drawable.star)
            starTwo?.setImageResource(R.drawable.star)
            starThree?.setImageResource(R.drawable.star)
        }

        when {
            PlayerManager.lives >= 3 -> {

                lives?.setImageResource(R.drawable.three_coolant)

            }
            PlayerManager.lives >= 2 -> {

                lives?.setImageResource(R.drawable.two_coolant)

            }
            PlayerManager.lives >= 1 -> {

                lives?.setImageResource(R.drawable.one_coolant)
            }

        }

        return view

    }
}

