package com.example.hyperponggruppb.view.fragment

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.example.hyperponggruppb.R
import com.example.hyperponggruppb.controller.PlayerManager
import com.example.hyperponggruppb.view.GameView
import com.example.hyperponggruppb.view.MainActivity

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
            if (checkUnlock(1)){
                startLevel()
            }
        }
        levelTwo?.setOnClickListener {
            if (checkUnlock(2)){
                startLevel()
            }else{
                toaster()
            }
        }
        levelThree?.setOnClickListener {
            if (checkUnlock(3)){
                startLevel()
            }else{
                toaster()
            }
        }
        levelFour?.setOnClickListener {
            if (checkUnlock(4)){
                startLevel()
            }else{
                toaster()
            }
        }
        levelFive?.setOnClickListener {
            if (checkUnlock(5)){
                startLevel()
            }else{
                toaster()
            }
        }
        return view
    }

    private fun checkUnlock(levelId: Int): Boolean{
        return PlayerManager.setLevel(levelId)
    }

    private fun startLevel(){
        val toLevel = Intent(super.getContext(), MainActivity::class.java)
        startActivity(toLevel)
    }

    private fun toaster(){
        Toast.makeText(super.getContext(), "Level not yet unlocked", Toast.LENGTH_SHORT).show()
    }

}