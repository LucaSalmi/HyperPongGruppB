package com.example.hyperponggruppb.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.example.hyperponggruppb.R
import com.example.hyperponggruppb.controller.PlayerManager

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
        val progressBar = 0
        var progressStatus = 0



        score?.text = PlayerManager.playerPoints.toString()
        //highScoreView?.text = PlayerManager.playerHighScore.toString()


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

    private fun progressBar() {

    }

}