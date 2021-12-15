package com.example.hyperponggruppb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView


class PointFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_point, container, false)
        val pointsView = view?.findViewById<TextView>(R.id.tv_fragment_score_points)
        val highScoreView = view?.findViewById<TextView>(R.id.tv_frag_hs_points)
        pointsView?.text = PlayerManager.playerPoints.toString()
        highScoreView?.text = PlayerManager.playerHighScore.toString()

        var lifes = view?.findViewById<ImageView>(R.id.iv_frag_coolant)


        when {
            PlayerManager.lives >= 3 -> {

                lifes?.setImageResource(R.drawable.three_coolant)

            }
            PlayerManager.lives >= 2 -> {

                lifes?.setImageResource(R.drawable.two_coolant)

            }
            PlayerManager.lives >= 1 -> {

                lifes?.setImageResource(R.drawable.one_coolant)
            }
        }
        return view
    }
}