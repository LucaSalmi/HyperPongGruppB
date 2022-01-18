package com.example.hyperponggruppb.view.fragment

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.example.hyperponggruppb.R
import com.example.hyperponggruppb.controller.DialogManager
import com.example.hyperponggruppb.controller.PlayerManager
import com.example.hyperponggruppb.view.OverWorldActivity

class FirstWorldFragment : Fragment() {

    private lateinit var worldOneDialog: DialogManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first_world, container, false)

        worldOneDialog = DialogManager(super.getContext()!!)

        val levelOne = view?.findViewById<ImageView>(R.id.iv_level_one)
        val levelTwo = view?.findViewById<ImageView>(R.id.iv_level_two)
        val levelThree = view?.findViewById<ImageView>(R.id.iv_level_three)
        val levelFour = view?.findViewById<ImageView>(R.id.iv_level_four)
        val levelFive = view?.findViewById<ImageView>(R.id.iv_level_five)

        if (PlayerManager.levelStarsArray.size > 0) {

            when (PlayerManager.levelStarsArray[0]) {
                0 -> levelOne!!.setImageResource(R.drawable.flag_one_one_no_star)
                1 -> levelOne!!.setImageResource(R.drawable.flag_one_one_one_star)
                2 -> levelOne!!.setImageResource(R.drawable.flag_one_one_two_star)
                3 -> levelOne!!.setImageResource(R.drawable.flag_one_one_three_star)
            }
        }
        if (PlayerManager.levelStarsArray.size > 1) {
            when (PlayerManager.levelStarsArray[1]) {

                0 -> levelTwo!!.setImageResource(R.drawable.flag_one_two_no_star)
                1 -> levelTwo!!.setImageResource(R.drawable.flag_one_two_one_star)
                2 -> levelTwo!!.setImageResource(R.drawable.flag_one_two_two_star)
                3 -> levelTwo!!.setImageResource(R.drawable.flag_one_two_three_star)
            }
        }
        if (PlayerManager.levelStarsArray.size > 2) {
            when (PlayerManager.levelStarsArray[2]) {

                0 -> levelThree!!.setImageResource(R.drawable.flag_one_three_no_star)
                1 -> levelThree!!.setImageResource(R.drawable.flag_one_three_one_star)
                2 -> levelThree!!.setImageResource(R.drawable.flag_one_three_two_star)
                3 -> levelThree!!.setImageResource(R.drawable.flag_one_three_three_star)
            }
        }
        if (PlayerManager.levelStarsArray.size > 3) {
            when (PlayerManager.levelStarsArray[3]) {

                0 -> levelFour!!.setImageResource(R.drawable.flag_one_four_no_star)
                1 -> levelFour!!.setImageResource(R.drawable.flag_one_four_one_star)
                2 -> levelFour!!.setImageResource(R.drawable.flag_one_four_two_star)
                3 -> levelFour!!.setImageResource(R.drawable.flag_one_four_three_star)
            }
        }
        if (PlayerManager.levelStarsArray.size > 4) {
            when (PlayerManager.levelStarsArray[4]) {

                0 -> levelFive!!.setImageResource(R.drawable.flag_one_five_no_star)
                1 -> levelFive!!.setImageResource(R.drawable.flag_one_five_one_star)
                2 -> levelFive!!.setImageResource(R.drawable.flag_one_five_two_star)
                3 -> levelFive!!.setImageResource(R.drawable.flag_one_five_three_star)
            }
        }

        levelOne?.setOnClickListener {
            if (checkUnlock(1)) {
                worldOneDialog.enterLevelScreen(1)
            }
        }
        levelTwo?.setOnClickListener {
            if (checkUnlock(2)) {
                worldOneDialog.enterLevelScreen(2)
            } else {
                toaster()
            }
        }
        levelThree?.setOnClickListener {
            if (checkUnlock(3)) {
                worldOneDialog.enterLevelScreen(3)
            } else {
                toaster()
            }
        }
        levelFour?.setOnClickListener {
            if (checkUnlock(4)) {
                worldOneDialog.enterLevelScreen(4)
            } else {
                toaster()
            }
        }
        levelFive?.setOnClickListener {
            if (checkUnlock(5)) {
                worldOneDialog.enterLevelScreen(5)
            } else {
                toaster()
            }
        }
        return view
    }

    private fun checkUnlock(levelId: Int): Boolean {
        return PlayerManager.setLevel(levelId)
    }

    private fun toaster() {

        Toast.makeText(super.getContext(), "Level not yet unlocked", Toast.LENGTH_SHORT)
            .show()

    }


}