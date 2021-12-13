package com.example.hyperponggruppb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible


class PointFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_point, container, false)
        val pointsView = view?.findViewById<TextView>(R.id.points_view)
        val highScoreView = view?.findViewById<TextView>(R.id.hi_score_view)
        pointsView?.text = PlayerManager.playerPoints.toString()
        highScoreView?.text = PlayerManager.playerHighScore.toString()

        val heartOne = view?.findViewById<ImageView>(R.id.heart_one)
        val heartTwo = view?.findViewById<ImageView>(R.id.heart_two)
        val heartThree = view?.findViewById<ImageView>(R.id.heart_three)

        when {
            PlayerManager.lives >= 3 -> {

                //heartOne?.setImageResource(R.drawable.ic_baseline_search_24)

            }
            PlayerManager.lives >= 2 -> {

                //heartOne?.setImageResource(R.drawable.ic_baseline_search_24)
                heartThree?.isVisible = false

            }
            PlayerManager.lives >= 1 -> {

                //heartTwo?.setImageResource(R.drawable.ic_baseline_search_24)
                heartTwo?.isVisible = false
            }
        }
        return view
    }
}