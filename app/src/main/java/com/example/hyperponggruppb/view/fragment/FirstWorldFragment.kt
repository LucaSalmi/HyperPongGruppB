package com.example.hyperponggruppb.view.fragment

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.example.hyperponggruppb.LeaderBoardActivity
import com.example.hyperponggruppb.R
import com.example.hyperponggruppb.controller.PlayerManager
import com.example.hyperponggruppb.view.GameModeStoryActivity
import com.example.hyperponggruppb.view.GameView
import com.example.hyperponggruppb.view.MainActivity
import com.example.hyperponggruppb.view.StoryView

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
                enterLevelScreen(1)

                //startLevel()
            }
        }
        levelTwo?.setOnClickListener {
            if (checkUnlock(2)){
                enterLevelScreen(2)
                //startLevel()
            }else{
                toaster()
            }
        }
        levelThree?.setOnClickListener {
            if (checkUnlock(3)){
                enterLevelScreen(3)
                //startLevel()
            }else{
                toaster()
            }
        }
        levelFour?.setOnClickListener {
            if (checkUnlock(4)){
                enterLevelScreen(4)
                //startLevel()
            }else{
                toaster()
            }
        }
        levelFive?.setOnClickListener {
            if (checkUnlock(5)){
                enterLevelScreen(5)
                //startLevel()
            }else{
                toaster()
            }
        }
        return view
    }
    /**
     * creates the scoreboard to show the player their high score and their position in the leaderboard, it also links directly to the full scoreboard, the main menu and restarts the game.
     */
    private fun enterLevelScreen(levelId: Int) {
        val dialog = activity?.applicationContext.let { super.getContext()
            ?.let { it1 -> Dialog(it1) } }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.enter_level_screen)
        val returnToWordlBtn = dialog?.findViewById(R.id.iv_level_return) as ImageView
        val startLevelBtn = dialog.findViewById(R.id.iv_level_start) as ImageView
        var screenLevelID = dialog.findViewById(R.id.tv_level_id) as TextView

        val starProgressResult = dialog.findViewById(R.id.iv_star_progress) as ImageView
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

        when (levelId) {
            1 -> {
                screenLevelID.setText(R.string.level_one)
                screenLevelScoreResult.setText("99") // ändra denna till knuten variabel till leveln
            }
            2 -> {
                screenLevelID.setText(R.string.level_two)
                screenLevelScoreResult.setText("99") // ändra denna till knuten variabel till leveln
            }
            3 -> {
                screenLevelID.setText(R.string.level_three)
                screenLevelScoreResult.setText("99") // ändra denna till knuten variabel till leveln
            }
            4 -> {
                screenLevelID.setText(R.string.level_four)
                screenLevelScoreResult.setText("99") // ändra denna till knuten variabel till leveln
            }
            5 -> {
                screenLevelID.setText(R.string.level_five)
                screenLevelScoreResult.setText("99") // ändra denna till knuten variabel till leveln
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
            //skapa en array av powerups vi ska kunna ha i storymode och en variavel på antal man kan ha.
            //if (playerMannager.powerupMultiball > 0) {
            //  playerMannager.powerupMultiball -1 - KANSKE BÄTTRE ATT ta minus vid start av level om man ändrar sig?!?!?!?!
            //  AKTIVERA multiball som powerup till Leveln.
            // }
            toasterClicked()
        }
        screenLevelLoadoutTwo.setOnClickListener {
            //skapa en array av powerups vi ska kunna ha i storymode och en variavel på antal man kan ha.
            //if (playerMannager.powerupMultiball > 0) {
            //  playerMannager.powerupMultiball -1 - KANSKE BÄTTRE ATT ta minus vid start av level om man ändrar sig?!?!?!?!
            //  AKTIVERA multiball som powerup till Leveln.
            // }
            toasterClicked()
        }
        screenLevelLoadoutThree.setOnClickListener {
            //skapa en array av powerups vi ska kunna ha i storymode och en variavel på antal man kan ha.
            //if (playerMannager.powerupMultiball > 0) {
            //  playerMannager.powerupMultiball -1 - KANSKE BÄTTRE ATT ta minus vid start av level om man ändrar sig?!?!?!?!
            //  AKTIVERA multiball som powerup till Leveln.
            // }
            toasterClicked()
        }
        screenLevelLoadoutFour.setOnClickListener {
            //skapa en array av powerups vi ska kunna ha i storymode och en variavel på antal man kan ha.
            //if (playerMannager.powerupMultiball > 0) {
            //  playerMannager.powerupMultiball -1 - KANSKE BÄTTRE ATT ta minus vid start av level om man ändrar sig?!?!?!?!
            //  AKTIVERA multiball som powerup till Leveln.
            // }
            toasterClicked()
        }

        returnToWordlBtn.setOnClickListener {

            dialog.dismiss()
        }

        startLevelBtn.setOnClickListener {
            // put this button to start level  of selected level
            startLevel()
        }


        dialog.show()
        dialog.window?.setBackgroundDrawableResource(R.color.trans)
    }

    private fun checkUnlock(levelId: Int): Boolean{
        return PlayerManager.setLevel(levelId)
    }

    private fun startLevel(){
        val toLevel = Intent(super.getContext(), GameModeStoryActivity::class.java)
        startActivity(toLevel)
    }

    private fun toaster(){
        Toast.makeText(super.getContext(), "Level not yet unlocked", Toast.LENGTH_SHORT).show()
    }
    private fun toasterClicked(){
        Toast.makeText(super.getContext(), "pressed a button", Toast.LENGTH_SHORT).show()
    }

}