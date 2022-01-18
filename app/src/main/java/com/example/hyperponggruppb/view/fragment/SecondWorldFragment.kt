package com.example.hyperponggruppb.view.fragment

import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.example.hyperponggruppb.R
import com.example.hyperponggruppb.controller.DialogManager
import com.example.hyperponggruppb.controller.PlayerManager
import com.example.hyperponggruppb.view.GameModeStoryActivity

class SecondWorldFragment : Fragment() {

    var backupLevelId = -1
    private lateinit var worldTwoDialog: DialogManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_second_world, container, false)
        worldTwoDialog = DialogManager(super.getContext()!!)

        val levelSix = view?.findViewById<ImageView>(R.id.iv_level_six)
        val levelSeven = view?.findViewById<ImageView>(R.id.iv_level_seven)
        val levelEight = view?.findViewById<ImageView>(R.id.iv_level_eight)
        val levelNine = view?.findViewById<ImageView>(R.id.iv_level_nine)
        val levelTen = view?.findViewById<ImageView>(R.id.iv_level_ten)

        if (PlayerManager.levelStarsArray.size > 5) {
            when (PlayerManager.levelStarsArray[5]) {
                0 -> levelSix!!.setImageResource(R.drawable.flag_two_one_no_star)
                1 -> levelSix!!.setImageResource(R.drawable.flag_two_one_one_star)
                2 -> levelSix!!.setImageResource(R.drawable.flag_two_one_two_star)
                3 -> levelSix!!.setImageResource(R.drawable.flag_two_one_three_star)
            }
        }
        if (PlayerManager.levelStarsArray.size > 6) {
            when (PlayerManager.levelStarsArray[6]) {

                0 -> levelSeven!!.setImageResource(R.drawable.flag_two_two_no_star)
                1 -> levelSeven!!.setImageResource(R.drawable.flag_two_two_one_star)
                2 -> levelSeven!!.setImageResource(R.drawable.flag_two_two_two_star)
                3 -> levelSeven!!.setImageResource(R.drawable.flag_two_two_three_star)
            }
        }
        if (PlayerManager.levelStarsArray.size > 7) {
            when (PlayerManager.levelStarsArray[7]) {

                0 -> levelEight!!.setImageResource(R.drawable.flag_two_three_no_star)
                1 -> levelEight!!.setImageResource(R.drawable.flag_two_three_one_star)
                2 -> levelEight!!.setImageResource(R.drawable.flag_two_three_two_star)
                3 -> levelEight!!.setImageResource(R.drawable.flag_two_three_three_star)
            }
        }
        if (PlayerManager.levelStarsArray.size > 8) {
            when (PlayerManager.levelStarsArray[8]) {

                0 -> levelNine!!.setImageResource(R.drawable.flag_two_four_no_star)
                1 -> levelNine!!.setImageResource(R.drawable.flag_two_four_one_star)
                2 -> levelNine!!.setImageResource(R.drawable.flag_two_four_two_star)
                3 -> levelNine!!.setImageResource(R.drawable.flag_two_four_three_star)
            }
        }
        if (PlayerManager.levelStarsArray.size > 9) {
            when (PlayerManager.levelStarsArray[9]) {

                0 -> levelTen!!.setImageResource(R.drawable.flag_two_five_no_star)
                1 -> levelTen!!.setImageResource(R.drawable.flag_two_five_one_star)
                2 -> levelTen!!.setImageResource(R.drawable.flag_two_five_two_star)
                3 -> levelTen!!.setImageResource(R.drawable.flag_two_five_three_star)
            }
        }

        levelSix?.setOnClickListener {
            if (checkUnlock(6)) {
                worldTwoDialog.enterLevelScreen(6)
            } else {
                toaster()
            }
        }
        levelSeven?.setOnClickListener {
            if (checkUnlock(7)) {
                worldTwoDialog.enterLevelScreen(7)
            } else {
                toaster()
            }
        }
        levelEight?.setOnClickListener {
            if (checkUnlock(8)) {
                worldTwoDialog.enterLevelScreen(8)
            } else {
                toaster()
            }
        }
        levelNine?.setOnClickListener {
            if (checkUnlock(9)) {
                worldTwoDialog.enterLevelScreen(9)
            } else {
                toaster()
            }
        }
        levelTen?.setOnClickListener {
            if (checkUnlock(10)) {
                worldTwoDialog.enterLevelScreen(10)
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