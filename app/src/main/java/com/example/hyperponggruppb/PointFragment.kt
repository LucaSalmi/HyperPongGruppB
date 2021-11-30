package com.example.hyperponggruppb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.hyperponggruppb.databinding.FragmentPointBinding


class PointFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_point, container, false)

        val pointsView = view?.findViewById<TextView>(R.id.points_view)
        val hiScoreView = view?.findViewById<TextView>(R.id.hi_score_view)
        pointsView?.text = PointManager.player.points.toString()
        hiScoreView?.text = PointManager.player.highScore.toString()
        return view
    }




}