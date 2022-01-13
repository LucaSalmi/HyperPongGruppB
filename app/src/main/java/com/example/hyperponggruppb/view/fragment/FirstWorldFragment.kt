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
import com.example.hyperponggruppb.controller.PlayerManager
import com.example.hyperponggruppb.view.GameModeStoryActivity
import com.example.hyperponggruppb.view.OverWorldActivity

class FirstWorldFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_first_world, container, false)

        val levelOne = view?.findViewById<ImageView>(R.id.iv_level_one)
        val levelTwo = view?.findViewById<ImageView>(R.id.iv_level_two)
        val levelThree = view?.findViewById<ImageView>(R.id.iv_level_three)
        val levelFour = view?.findViewById<ImageView>(R.id.iv_level_four)
        val levelFive = view?.findViewById<ImageView>(R.id.iv_level_five)

        levelOne?.setOnClickListener {
            if (checkUnlock(1)){
                enterLevelScreen(1)
            }
        }
        levelTwo?.setOnClickListener {
            if (checkUnlock(2)){
                enterLevelScreen(2)
            }else{
                toaster()
            }
        }
        levelThree?.setOnClickListener {
            if (checkUnlock(3)){
                enterLevelScreen(3)
            }else{
                toaster()
            }
        }
        levelFour?.setOnClickListener {
            if (checkUnlock(4)){
                enterLevelScreen(4)
            }else{
                toaster()
            }
        }
        levelFive?.setOnClickListener {
            if (checkUnlock(5)){
                enterLevelScreen(5)
            }else{
                toaster()
            }
        }
        return view
    }
    /**
     *
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
            1 -> {
                screenLevelID.text = levelString
                screenLevelScoreResult.text = checkPoints(levelId) // ändra denna till knuten variabel till leveln
                dialog.dismiss()
            }
            2 -> {
                screenLevelID.text = levelString
                screenLevelScoreResult.text = checkPoints(levelId) // ändra denna till knuten variabel till leveln
                dialog.dismiss()
            }
            3 -> {
                screenLevelID.text = levelString
                screenLevelScoreResult.text = checkPoints(levelId) // ändra denna till knuten variabel till leveln
                dialog.dismiss()
            }
            4 -> {
                screenLevelID.text = levelString
                screenLevelScoreResult.text = checkPoints(levelId) // ändra denna till knuten variabel till leveln
                dialog.dismiss()
            }
            5 -> {
                screenLevelID.text = levelString
                screenLevelScoreResult.text = checkPoints(levelId) // ändra denna till knuten variabel till leveln
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
            PlayerManager.starCounter = 0 // reset the stars in the !!! - UI - !!!
            dialog.dismiss()

            val overWorldActivity = context as OverWorldActivity
            overWorldActivity.startLevel()

            //startLevel()

        }

        dialog.show()
        dialog.window?.setBackgroundDrawableResource(R.color.trans)
    }

    private fun checkUnlock(levelId: Int): Boolean{
        return PlayerManager.setLevel(levelId)
    }

    private fun checkPoints(levelId: Int): String {

        return if (PlayerManager.activeUser!!.levelScoresArray.size < levelId){
            "0"
        }else{
            PlayerManager.activeUser!!.levelScoresArray[levelId - 1].toString()
        }
    }

    /*
    private fun startLevel(){
        val toLevel = Intent(super.getContext(), GameModeStoryActivity::class.java)
        startActivity(toLevel)
    }
     */

    private fun toaster(){
        Toast.makeText(super.getContext(), "Level not yet unlocked", Toast.LENGTH_SHORT).show()
    }

    private fun toasterClicked(){
        Toast.makeText(super.getContext(), "pressed a button", Toast.LENGTH_SHORT).show()
    }

}