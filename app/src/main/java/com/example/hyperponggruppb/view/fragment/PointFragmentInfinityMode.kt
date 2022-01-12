package com.example.hyperponggruppb.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.hyperponggruppb.R
import com.example.hyperponggruppb.controller.PlayerManager


class PointFragmentInfinityMode : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_point_infinity_mode, container, false)
        val pointsView = view?.findViewById<TextView>(R.id.tv_fragment_score_points)
        val highScoreView = view?.findViewById<TextView>(R.id.tv_frag_hs_points)
        val speedMeterView = view?.findViewById<ImageView>(R.id.iv_frag_speed)
        val lives = view?.findViewById<ImageView>(R.id.iv_frag_coolant)

        pointsView?.text = PlayerManager.playerPoints.toString()
        highScoreView?.text = PlayerManager.activeUser?.highScore.toString()


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

        when (PlayerManager.playTime) {

            0 -> {
                speedMeterView?.setImageResource(R.drawable.speed_one)
            }
            1 -> {
                speedMeterView?.setImageResource(R.drawable.speed_two)
            }
            2 -> {
                speedMeterView?.setImageResource(R.drawable.speed_three)
            }
            3 -> {
                speedMeterView?.setImageResource(R.drawable.speed_four)
            }
            4 -> {
                speedMeterView?.setImageResource(R.drawable.speed_five)
            }
            5 -> {
                speedMeterView?.setImageResource(R.drawable.speed_six)
            }
            6 -> {
                speedMeterView?.setImageResource(R.drawable.speed_seven)
            }
            7 -> {
                speedMeterView?.setImageResource(R.drawable.speed_eight)
            }
            8 -> {
                speedMeterView?.setImageResource(R.drawable.speed_nine)
            }
            9 -> {
                speedMeterView?.setImageResource(R.drawable.speed_ten)
            }
        }
        return view
    }
}