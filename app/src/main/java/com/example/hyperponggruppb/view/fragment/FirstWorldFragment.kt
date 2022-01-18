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
import com.example.hyperponggruppb.view.OverWorldActivity

class FirstWorldFragment : Fragment() {

    var backupLevelId = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first_world, container, false)

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
                enterLevelScreen(1)
            }
        }
        levelTwo?.setOnClickListener {
            if (checkUnlock(2)) {
                enterLevelScreen(2)
            } else {
                toaster(0)
            }
        }
        levelThree?.setOnClickListener {
            if (checkUnlock(3)) {
                enterLevelScreen(3)
            } else {
                toaster(0)
            }
        }
        levelFour?.setOnClickListener {
            if (checkUnlock(4)) {
                enterLevelScreen(4)
            } else {
                toaster(0)
            }
        }
        levelFive?.setOnClickListener {
            if (checkUnlock(5)) {
                enterLevelScreen(5)
            } else {
                toaster(0)
            }
        }
        return view
    }

    /**
     *
     */
    private fun enterLevelScreen(levelId: Int) {
        val enterLevelDialog = activity?.applicationContext.let {
            super.getContext()
                ?.let { it1 -> Dialog(it1) }
        }

        enterLevelDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        enterLevelDialog?.setCancelable(false)
        enterLevelDialog?.setContentView(R.layout.enter_level_screen)
        val returnToWorldBtn = enterLevelDialog?.findViewById(R.id.iv_level_return) as ImageView
        val startLevelBtn = enterLevelDialog.findViewById(R.id.iv_level_start) as ImageView
        var screenLevelID = enterLevelDialog.findViewById(R.id.tv_level_id) as TextView

        val starProgressResult = enterLevelDialog.findViewById(R.id.iv_star_progress_holder) as ImageView
        val screenLevelScore = enterLevelDialog.findViewById(R.id.tv_level_score) as TextView
        val screenLevelScoreResult = enterLevelDialog.findViewById(R.id.tv_level_score_result) as TextView
        val playerGemBalance = enterLevelDialog.findViewById<TextView>(R.id.tv_level_gem_amount)

        val leftArrowCharacter = enterLevelDialog.findViewById(R.id.iv_level_left_arrow) as ImageView
        val screenLevelCharacter = enterLevelDialog.findViewById(R.id.iv_level_character) as ImageView
        val rightArrowCharacter = enterLevelDialog.findViewById(R.id.iv_level_right_arrow) as ImageView

        val screenLevelLoadoutOne = enterLevelDialog.findViewById(R.id.iv_level_loadout_1) as ImageView
        val screenLevelLoadoutTwo = enterLevelDialog.findViewById(R.id.iv_level_loadout_2) as ImageView
        val screenLevelLoadoutThree = enterLevelDialog.findViewById(R.id.iv_level_loadout_3) as ImageView
        val screenLevelLoadoutFour = enterLevelDialog.findViewById(R.id.iv_level_loadout_4) as ImageView
        PlayerManager.powerUpActivated = -1

        backupLevelId = PlayerManager.currentLevel

        playerGemBalance.text = PlayerManager.gems.toString()


        if (PlayerManager.powerUpInventory[0] < 1) { //multiball powerup
            screenLevelLoadoutOne.setImageResource(R.drawable.locked_multiball_button)
        }

        if (PlayerManager.powerUpInventory[1] < 1) { //gun powerUp
            screenLevelLoadoutTwo.setImageResource(R.drawable.locked_gun_button)
        }

        if (PlayerManager.powerUpInventory[2] < 1) { //shield powerup
            screenLevelLoadoutThree.setImageResource(R.drawable.locked_shield_button)
        }


        val starContainerOne = enterLevelDialog.findViewById<ImageView>(R.id.iv_pre_level_star_one)
        val starContainerTwo = enterLevelDialog.findViewById<ImageView>(R.id.iv_pre_level_star_two)
        val starContainerThree = enterLevelDialog.findViewById<ImageView>(R.id.iv_pre_level_star_three)

        // HÄR ÄR JAG HUEHUEHUEHUEHUEH// JAG MED MUHAHAHAAHAHAHAHAH

        if (PlayerManager.levelStarsArray.size > levelId - 1) {
            when (PlayerManager.levelStarsArray[levelId - 1]) {

                1 -> starContainerOne!!.setImageResource(R.drawable.star)

                2 -> {
                    starContainerOne!!.setImageResource(R.drawable.star)
                    starContainerTwo!!.setImageResource(R.drawable.star)
                }

                3 -> {
                    starContainerOne!!.setImageResource(R.drawable.star)
                    starContainerTwo!!.setImageResource(R.drawable.star)
                    starContainerThree!!.setImageResource(R.drawable.star)
                }
                else -> {
                    starContainerOne!!.setImageResource(R.drawable.star)
                    starContainerTwo!!.setImageResource(R.drawable.star)
                    starContainerThree!!.setImageResource(R.drawable.star)
                }
            }
        }

        val levelString = getString(R.string.level) + levelId.toString()

        when (levelId) {
            1 -> {
                screenLevelID.text = levelString
                screenLevelScoreResult.text =
                    checkPoints(levelId) // ändra denna till knuten variabel till leveln
                enterLevelDialog.dismiss()
            }
            2 -> {
                screenLevelID.text = levelString
                screenLevelScoreResult.text =
                    checkPoints(levelId) // ändra denna till knuten variabel till leveln
                enterLevelDialog.dismiss()
            }
            3 -> {
                screenLevelID.text = levelString
                screenLevelScoreResult.text =
                    checkPoints(levelId) // ändra denna till knuten variabel till leveln
                enterLevelDialog.dismiss()
            }
            4 -> {
                screenLevelID.text = levelString
                screenLevelScoreResult.text =
                    checkPoints(levelId) // ändra denna till knuten variabel till leveln
                enterLevelDialog.dismiss()
            }
            5 -> {
                screenLevelID.text = levelString
                screenLevelScoreResult.text =
                    checkPoints(levelId) // ändra denna till knuten variabel till leveln
                enterLevelDialog.dismiss()
            }
        }

        leftArrowCharacter.setOnClickListener {
            //skapa en array med olika skins
            //screenLevelCharacter.setImageResource(ARRAYID MINUS)
            toaster(1)
        }
        rightArrowCharacter.setOnClickListener {
            //skapa en array med olika skins
            //screenLevelCharacter.setImageResource(ARRAYID PLUS)
            toaster(1)
        }

        //0 = multiball powerup
        //1 = gun powerup
        //2 = shield powerup

        screenLevelLoadoutOne.setOnClickListener {
            PlayerManager.selectedPowerUp = 0

            if (PlayerManager.powerUpActivated != PlayerManager.selectedPowerUp) {

                if (!checkIfPowerUpAvailable()){
                    enterLevelDialog.dismiss()
                }


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

                if (!checkIfPowerUpAvailable()){
                    enterLevelDialog.dismiss()
                }

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

                if (!checkIfPowerUpAvailable()){
                    enterLevelDialog.dismiss()
                }

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
                    screenLevelLoadoutThree.setImageResource(R.drawable.shield_button_selected)
                }

            } else {
                screenLevelLoadoutThree.setImageResource(R.drawable.shield_button)
                PlayerManager.powerUpActivated = -1
            }
        }
        screenLevelLoadoutFour.setOnClickListener {

            PlayerManager.selectedPowerUp = 3
            checkIfPowerUpAvailable()

            toaster(1)
        }

        returnToWorldBtn.setOnClickListener {

            enterLevelDialog.dismiss()
        }

        startLevelBtn.setOnClickListener {
            // put this button to start level  of selected level
            PlayerManager.starCounter = 0 // reset the stars in the !!! - UI - !!!
            enterLevelDialog.dismiss()

            val overWorldActivity = context as OverWorldActivity
            overWorldActivity.startLevel()

            //startLevel()

        }

        enterLevelDialog.show()
        enterLevelDialog.window?.setBackgroundDrawableResource(R.color.trans)
    }

    private fun checkUnlock(levelId: Int): Boolean {
        return PlayerManager.setLevel(levelId)
    }

    private fun checkPoints(levelId: Int): String {

        return if (PlayerManager.levelScoresArray.size < levelId) {
            "0"
        } else {
            PlayerManager.levelScoresArray[levelId - 1].toString()
        }
    }

    private fun toaster(id: Int) {
        when(id){
            0-> Toast.makeText(super.getContext(), "Level not yet unlocked", Toast.LENGTH_SHORT)
                .show()
            1-> Toast.makeText(super.getContext(), "pressed a button", Toast.LENGTH_SHORT)
                .show()
            2-> Toast.makeText(super.getContext(), "not enough gems", Toast.LENGTH_SHORT)
                .show()
        }

    }

    fun checkIfPowerUpAvailable(): Boolean {

        if (PlayerManager.powerUpInventory[PlayerManager.selectedPowerUp] > 0) { //MultiBall powerUp
            PlayerManager.powerUpActivated = PlayerManager.selectedPowerUp
            return true

        } else {
            PlayerManager.powerUpActivated = -1
            shopDialog()
            return false
        }
    }

    fun shopDialog(){
        val shopDialog = Dialog(super.getContext()!!)
        shopDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        shopDialog.setContentView(R.layout.shop_dialog_layout)

        val shopText = shopDialog.findViewById<TextView>(R.id.shop_dialog_text)
        val yesButton = shopDialog.findViewById<Button>(R.id.shop_yes_btn)
        val noButton = shopDialog.findViewById<Button>(R.id.shop_no_btn)

        val shopStringOne = getString(R.string.shop_dialog_text_part_1)
        val shopStringTwo = getString(R.string.shop_dialog_string_part_2)
        val price = when(PlayerManager.selectedPowerUp){
            0-> PlayerManager.multiBallPrice.toString()
            1-> PlayerManager.gunPrice.toString()
            2-> PlayerManager.shieldPrice.toString()
            else -> PlayerManager.multiBallPrice.toString()
        }
        var shopStringFinal = shopStringOne + price + shopStringTwo

        shopText.text = shopStringFinal

        yesButton.setOnClickListener {

            if (PlayerManager.buyPowerUp(20)){
                PlayerManager.powerUpInventory[PlayerManager.selectedPowerUp] =+ 1
                enterLevelScreen(backupLevelId)
                shopDialog.dismiss()
            }else{
                toaster(2)
            }
        }

        noButton.setOnClickListener {
            shopDialog.dismiss()
        }

        shopDialog.show()
        shopDialog.window?.setBackgroundDrawableResource(R.color.trans)
    }
}