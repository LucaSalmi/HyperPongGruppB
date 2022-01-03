package com.example.hyperponggruppb.view.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import com.example.hyperponggruppb.R

class FirstWorldFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_first_world, container, false)

        val levelOne = view?.findViewById<ImageButton>(R.id.ib_level_one)
        val levelTwo = view?.findViewById<ImageButton>(R.id.ib_level_two)
        val levelThree = view?.findViewById<ImageButton>(R.id.ib_level_three)
        val levelFour = view?.findViewById<ImageButton>(R.id.ib_level_four)
        val levelFive = view?.findViewById<ImageButton>(R.id.ib_level_five)

        levelOne?.setOnClickListener {
            Log.d(TAG, "onCreateView: 1")
        }
        levelTwo?.setOnClickListener {
            Log.d(TAG, "onCreateView: 2")
        }
        levelThree?.setOnClickListener {
            Log.d(TAG, "onCreateView: 3")
        }
        levelFour?.setOnClickListener {
            Log.d(TAG, "onCreateView: 4")
        }
        levelFive?.setOnClickListener {
            Log.d(TAG, "onCreateView: 5")
        }
        return view
    }

}