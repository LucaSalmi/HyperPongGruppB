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
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.hyperponggruppb.R
import com.example.hyperponggruppb.controller.PlayerManager
import com.example.hyperponggruppb.view.GameModeStoryActivity

class SecondWorldFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_second_world, container, false)

        val levelSix = view?.findViewById<ImageView>(R.id.iv_level_six)
        val levelSeven = view?.findViewById<ImageView>(R.id.iv_level_seven)
        val levelEight = view?.findViewById<ImageView>(R.id.iv_level_eight)
        val levelNine = view?.findViewById<ImageView>(R.id.iv_level_nine)
        val levelTen = view?.findViewById<ImageView>(R.id.iv_level_ten)

        if (PlayerManager.levelStarsArray.size > 5){
            when(PlayerManager.levelStarsArray[5]){
                0 -> levelSix!!.setImageResource(R.drawable.flag_two_one_no_star)
                1 -> levelSix!!.setImageResource(R.drawable.flag_two_one_one_star)
                2 -> levelSix!!.setImageResource(R.drawable.flag_two_one_two_star)
                3 -> levelSix!!.setImageResource(R.drawable.flag_two_one_three_star)
            }
        }
        if (PlayerManager.levelStarsArray.size > 6){
            when(PlayerManager.levelStarsArray[6]){

                0 -> levelSeven!!.setImageResource(R.drawable.flag_two_two_no_star)
                1 -> levelSeven!!.setImageResource(R.drawable.flag_two_two_one_star)
                2 -> levelSeven!!.setImageResource(R.drawable.flag_two_two_two_star)
                3 -> levelSeven!!.setImageResource(R.drawable.flag_two_two_three_star)
            }
        }
        if (PlayerManager.levelStarsArray.size > 7){
            when(PlayerManager.levelStarsArray[7]){

                0 -> levelEight!!.setImageResource(R.drawable.flag_two_three_no_star)
                1 -> levelEight!!.setImageResource(R.drawable.flag_two_three_one_star)
                2 -> levelEight!!.setImageResource(R.drawable.flag_two_three_two_star)
                3 -> levelEight!!.setImageResource(R.drawable.flag_two_three_three_star)
            }
        }
        if (PlayerManager.levelStarsArray.size > 8){
            when(PlayerManager.levelStarsArray[8]){

                0 -> levelNine!!.setImageResource(R.drawable.flag_two_four_no_star)
                1 -> levelNine!!.setImageResource(R.drawable.flag_two_four_one_star)
                2 -> levelNine!!.setImageResource(R.drawable.flag_two_four_two_star)
                3 -> levelNine!!.setImageResource(R.drawable.flag_two_four_three_star)
            }
        }
        if (PlayerManager.levelStarsArray.size > 9){
            when(PlayerManager.levelStarsArray[9]){

                0 -> levelTen!!.setImageResource(R.drawable.flag_two_five_no_star)
                1 -> levelTen!!.setImageResource(R.drawable.flag_two_five_one_star)
                2 -> levelTen!!.setImageResource(R.drawable.flag_two_five_two_star)
                3 -> levelTen!!.setImageResource(R.drawable.flag_two_five_three_star)
            }
        }

        levelSix?.setOnClickListener {
            if (checkUnlock(6)) {
                enterLevelScreen(6)
            }else{
                toaster()
            }
        }
        levelSeven?.setOnClickListener {
            if (checkUnlock(7)) {
                enterLevelScreen(7)
            } else {
                toaster()
            }
        }
        levelEight?.setOnClickListener {
            if (checkUnlock(8)) {
                enterLevelScreen(8)
            } else {
                toaster()
            }
        }
        levelNine?.setOnClickListener {
            if (checkUnlock(9)) {
                enterLevelScreen(9)
            } else {
                toaster()
            }
        }
        levelTen?.setOnClickListener {
            if (checkUnlock(10)) {
                enterLevelScreen(10)
            } else {
                toaster()
            }
        }
        return view
    }

    /**
     * creates the scoreboard to show the player their high score and their position in the leaderboard, it also links directly to the full scoreboard, the main menu and restarts the game.
     */
    private fun enterLevelScreen(levelId: Int) {
        val dialog = activity?.applicationContext.let {
            super.getContext()
                ?.let { it1 -> Dialog(it1) }
        }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.enter_level_screen)
        val returnToWordlBtn = dialog?.findViewById(R.id.iv_level_return) as ImageView
        val startLevelBtn = dialog.findViewById(R.id.iv_level_start) as ImageView
        var screenLevelID = dialog.findViewById(R.id.tv_level_id) as TextView

        val starProgressResult = dialog.findViewById(R.id.iv_star_progress_holder) as ImageView
        val screenLevelScore = dialog.findViewById(R.id.tv_level_score) as TextView
        val screenLevelScoreResult = dialog.findViewById(R.id.tv_level_score_result) as TextView

        val leftArrowCharacter = dialog.findViewById(R.id.iv_level_left_arrow) as ImageView
        val screenLevelCharacter = dialog.findViewById(R.id.iv_level_character) as ImageView
        val rightArrowCharacter = dialog.findViewById(R.id.iv_level_right_arrow) as ImageView

        val screenLevelLoadoutOne = dialog.findViewById(R.id.iv_level_loadout_1) as ImageView
        val screenLevelLoadoutTwo = dialog.findViewById(R.id.iv_level_loadout_2) as ImageView
        val screenLevelLoadoutThree = dialog.findViewById(R.id.iv_level_loadout_3) as ImageView
        val screenLevelLoadoutFour = dialog.findViewById(R.id.iv_level_loadout_4) as ImageView

        // HÄR ÄR JAG HUEHUEHUEHUEHUEH
        val levelString = getString(R.string.level) + levelId.toString()

        when (levelId) {
            6 -> {
                screenLevelID.text = levelString
                screenLevelScoreResult.text =
                    checkPoints(levelId) // ändra denna till knuten variabel till leveln
                dialog.dismiss()
            }
            7 -> {
                screenLevelID.text = levelString
                screenLevelScoreResult.text =
                    checkPoints(levelId) // ändra denna till knuten variabel till leveln
                dialog.dismiss()
            }
            8 -> {
                screenLevelID.text = levelString
                screenLevelScoreResult.text =
                    checkPoints(levelId) // ändra denna till knuten variabel till leveln
                dialog.dismiss()
            }
            9 -> {
                screenLevelID.text = levelString
                screenLevelScoreResult.text =
                    checkPoints(levelId) // ändra denna till knuten variabel till leveln
                dialog.dismiss()
            }
            10 -> {
                screenLevelID.text = levelString
                screenLevelScoreResult.text =
                    checkPoints(levelId) // ändra denna till knuten variabel till leveln
                dialog.dismiss()
            }
        }

        leftArrowCharacter.setOnClickListener {
            //skapa en array med olika skins
            //screenLevelCharacter.setImageResource(ARRAYID MINUS)
            toasterClicked()
        }
        rightArrowCharacter.setOnClickListener {
            //skapa en array med olika skins
            //screenLevelCharacter.setImageResource(ARRAYID PLUS)
            toasterClicked()
        }


        screenLevelLoadoutOne.setOnClickListener {
            PlayerManager.selectedPowerUp = 0

            if (PlayerManager.powerUpActivated != PlayerManager.selectedPowerUp) {

                checkIfPowerUpAvailable()


                if (PlayerManager.powerUpActivated >= 0) {

                    if (PlayerManager.powerUpInventory[0] > 0) { //multiball powerup
                        screenLevelLoadoutOne.setImageResource(R.drawable.multiball_button)
                    } else {
                        screenLevelLoadoutOne.setImageResource(R.drawable.locked_multiball_button)
                    }

                    if (PlayerManager.powerUpInventory[1] > 0) { //gun powerUp
                        screenLevelLoadoutTwo.setImageResource(R.drawable.gun_button)
                    } else {
                        screenLevelLoadoutTwo.setImageResource(R.drawable.locked_gun_button)
                    }

                    if (PlayerManager.powerUpInventory[2] > 0) { //shield powerup
                        screenLevelLoadoutThree.setImageResource(R.drawable.shield_button)
                    } else {
                        screenLevelLoadoutThree.setImageResource(R.drawable.locked_shield_button)
                    }
                    screenLevelLoadoutOne.setImageResource(R.drawable.multiball_button_selected)
                }

            } else {
                screenLevelLoadoutOne.setImageResource(R.drawable.multiball_button)
                PlayerManager.powerUpActivated = -1
            }
        }
        screenLevelLoadoutTwo.setOnClickListener {
            PlayerManager.selectedPowerUp = 1
            if (PlayerManager.powerUpActivated != PlayerManager.selectedPowerUp) {

                checkIfPowerUpAvailable()


                if (PlayerManager.powerUpActivated >= 0) {

                    if (PlayerManager.powerUpInventory[0] > 0) { //multiball powerup
                        screenLevelLoadoutOne.setImageResource(R.drawable.multiball_button)
                    } else {
                        screenLevelLoadoutOne.setImageResource(R.drawable.locked_multiball_button)
                    }

                    if (PlayerManager.powerUpInventory[1] > 0) { //gun powerUp
                        screenLevelLoadoutTwo.setImageResource(R.drawable.gun_button)
                    } else {
                        screenLevelLoadoutTwo.setImageResource(R.drawable.locked_gun_button)
                    }

                    if (PlayerManager.powerUpInventory[2] > 0) { //shield powerup
                        screenLevelLoadoutThree.setImageResource(R.drawable.shield_button)
                    } else {
                        screenLevelLoadoutThree.setImageResource(R.drawable.locked_shield_button)
                    }
                    screenLevelLoadoutTwo.setImageResource(R.drawable.gun_button_selected)
                }

            } else {
                screenLevelLoadoutTwo.setImageResource(R.drawable.gun_button)
                PlayerManager.powerUpActivated = -1
            }
        }
        screenLevelLoadoutThree.setOnClickListener {
            PlayerManager.selectedPowerUp = 2

            if (PlayerManager.powerUpActivated != PlayerManager.selectedPowerUp) {
                Log.d(ContentValues.TAG, "enterLevelScreen:  check OUTSIDE 1")

                checkIfPowerUpAvailable()


                if (PlayerManager.powerUpActivated >= 0) {
                    Log.d(ContentValues.TAG, "enterLevelScreen:  check INSIDE 1")

                    if (PlayerManager.powerUpInventory[0] > 0) { //multiball powerup
                        screenLevelLoadoutOne.setImageResource(R.drawable.multiball_button)
                        Log.d(ContentValues.TAG, "enterLevelScreen:  check INSIDE 2-1")
                    } else {
                        screenLevelLoadoutOne.setImageResource(R.drawable.locked_multiball_button)
                        Log.d(ContentValues.TAG, "enterLevelScreen:  check INSIDE 2-2")
                    }

                    if (PlayerManager.powerUpInventory[1] > 0) { //gun powerUp
                        screenLevelLoadoutTwo.setImageResource(R.drawable.gun_button)
                        Log.d(ContentValues.TAG, "enterLevelScreen:  check INSIDE 3-1")
                    } else {
                        screenLevelLoadoutTwo.setImageResource(R.drawable.locked_gun_button)
                        Log.d(ContentValues.TAG, "enterLevelScreen:  check INSIDE 3-2")
                    }

                    if (PlayerManager.powerUpInventory[2] > 0) { //shield powerup
                        screenLevelLoadoutThree.setImageResource(R.drawable.shield_button)
                        Log.d(ContentValues.TAG, "enterLevelScreen:  check INSIDE 4-1")
                    } else {
                        screenLevelLoadoutThree.setImageResource(R.drawable.locked_shield_button)
                        Log.d(ContentValues.TAG, "enterLevelScreen:  check INSIDE 4-2")
                    }
                    screenLevelLoadoutThree.setImageResource(R.drawable.shield_button_selected)
                }

            } else {
                screenLevelLoadoutThree.setImageResource(R.drawable.shield_button)
                PlayerManager.powerUpActivated = -1
                Log.d(ContentValues.TAG, "enterLevelScreen:  check OUTSIDE 2")
            }
        }
        screenLevelLoadoutFour.setOnClickListener {

            PlayerManager.selectedPowerUp = 3
            PlayerManager.checkIfPowerUpAvailable()

            toasterClicked()
        }

        returnToWordlBtn.setOnClickListener {

            dialog.dismiss()
        }

        startLevelBtn.setOnClickListener {
            // put this button to start level  of selected level
            dialog.dismiss()
            startLevel()
        }

        dialog.show()
        dialog.window?.setBackgroundDrawableResource(R.color.trans)
    }

    private fun checkUnlock(levelId: Int): Boolean {
        return PlayerManager.setLevel(levelId)
    }

    private fun checkPoints(levelId: Int): String? {

        return if (PlayerManager.levelScoresArray.size < levelId) {
            "0"
        } else {
            PlayerManager.levelScoresArray[levelId-1].toString()
        }
    }

    private fun startLevel() {
        val toLevel = Intent(super.getContext(), GameModeStoryActivity::class.java)
        startActivity(toLevel)
    }

    fun checkIfPowerUpAvailable() {

        if (PlayerManager.powerUpInventory[PlayerManager.selectedPowerUp] > 0) { //MultiBall powerUp
            PlayerManager.powerUpActivated = PlayerManager.selectedPowerUp

        } else {
            PlayerManager.powerUpActivated = -1

        }
    }

    private fun toaster() {
        Toast.makeText(super.getContext(), "Level not yet unlocked", Toast.LENGTH_SHORT).show()
    }

    private fun toasterClicked() {
        Toast.makeText(super.getContext(), "pressed a button", Toast.LENGTH_SHORT).show()
    }
}