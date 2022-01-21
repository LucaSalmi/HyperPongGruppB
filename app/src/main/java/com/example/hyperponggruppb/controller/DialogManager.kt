package com.example.hyperponggruppb.controller

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.Window
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hyperponggruppb.R
import com.example.hyperponggruppb.adapter.UserSelectionAdapter
import com.example.hyperponggruppb.view.LeaderBoardActivity
import com.example.hyperponggruppb.view.MainActivityMainMenu
import com.example.hyperponggruppb.view.OverWorldActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlin.math.log

class DialogManager(val context: Context) {

    private var backupLevelId = -1


    /**
     * lets the player change which account he/she is playing on from a list of all saved users.
     */
    fun changeAccount() {

        val userListDialog = Dialog(context)
        userListDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        userListDialog.setContentView(R.layout.user_select_screen_layout)

        val usersList = userListDialog.findViewById<RecyclerView>(R.id.rw_user_list)
        usersList.layoutManager = LinearLayoutManager(context)
        val userSelectionAdapter =
            UserSelectionAdapter(context, PlayerManager.usersArray, userListDialog)
        usersList.adapter = userSelectionAdapter

        userListDialog.show()
        userListDialog.window?.setBackgroundDrawableResource(R.color.trans)
    }

    /**
     * if no Account is loaded, or the user wants to change the account he is playing on, the dialog prompts for a name and loads, if present, all the necessary data coupled with the user
     */
    fun nameInput(sp: SharedPreferences) {

        val nameInputDialog = Dialog(context)
        nameInputDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        nameInputDialog.setCancelable(false)
        nameInputDialog.setContentView(R.layout.enter_name_dialog)
        val nameField = nameInputDialog.findViewById<EditText>(R.id.et_enter_name_field)
        val saveBtn = nameInputDialog.findViewById<ImageView>(R.id.save_btn)
        val cancelBtn = nameInputDialog.findViewById<ImageView>(R.id.cancel_btn)

        saveBtn.setOnClickListener {

            if (nameField.text != null && nameField.text.length == 3) {

                PlayerManager.name = nameField.text.toString()
                SoundEffectManager.jukebox(context, 1)
                if (PlayerManager.createUser()) {
                    PlayerManager.cleanArrays()
                    PlayerManager.saveUserData(sp)
                    getMainActivity().setAccount()
                    nameInputDialog.dismiss()
                } else {
                    nameField.error = "Name already in use"
                }
            }
        }

        cancelBtn.setOnClickListener {
            if (PlayerManager.isFirstAccount) {
                toaster(1)
            } else {
                nameInputDialog.dismiss()
            }
        }

        nameInputDialog.show()
        nameInputDialog.window?.setBackgroundDrawableResource(R.color.trans)
    }

    /**
     * creates the scoreboard to show the player their high score and their position in the leaderboard, it also links directly to the full scoreboard, the main menu and restarts the game.
     */
    fun scoreBoardInfinityMode() {

        val scoreBoardDialog = Dialog(context)
        scoreBoardDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        scoreBoardDialog.setCancelable(false)
        scoreBoardDialog.setContentView(R.layout.result_view)
        val returnBtn = scoreBoardDialog.findViewById(R.id.tv_result_return) as TextView
        val retryBtn = scoreBoardDialog.findViewById(R.id.tv_result_next) as TextView
        val leaderboardBtn = scoreBoardDialog.findViewById(R.id.iv_result_leaderboard) as ImageView

        val resultScore = scoreBoardDialog.findViewById(R.id.tv_result_score) as TextView
        val resultPlacement = scoreBoardDialog.findViewById(R.id.tv_result_placement) as TextView
        val resultMessage = scoreBoardDialog.findViewById(R.id.tv_result_message) as TextView

        val playerScore = PlayerManager.playerPoints
        val playerPlacement = PlayerManager.setPlacement()
        var playerPlacementEnding = ""

        when (playerPlacement) {
            1 -> {
                resultMessage.setText(R.string.result_message_one)
                playerPlacementEnding = context.getString(R.string.result_placement_one)
            }
            2 -> {
                resultMessage.setText(R.string.result_message_two)
                playerPlacementEnding = context.getString(R.string.result_placement_two)
            }
            3 -> {
                resultMessage.setText(R.string.result_message_three)
                playerPlacementEnding = context.getString(R.string.result_placement_three)
            }
            in 4..10 -> {
                resultMessage.setText(R.string.result_message_four)
                playerPlacementEnding = context.getString(R.string.result_placement_four_plus)
            }
            else -> {
                resultMessage.setText(R.string.result_message_five)
                playerPlacementEnding = context.getString(R.string.result_placement_four_plus)
            }
        }

        resultPlacement.text = (playerPlacement.toString() + playerPlacementEnding)
        var resultScoreWithSign = playerScore.toString() + context.getString(R.string.result_p_sign)
        resultScore.text = resultScoreWithSign

        returnBtn.setOnClickListener {

            scoreBoardDialog.dismiss()
        }

        retryBtn.setOnClickListener {

            getMainActivity().startInfinityMode()
            scoreBoardDialog.dismiss()
        }

        leaderboardBtn.setOnClickListener {

            val toLeaderboard = Intent(getMainActivity(), LeaderBoardActivity::class.java)
            scoreBoardDialog.dismiss()
            startActivity(context, toLeaderboard, null)
        }

        scoreBoardDialog.show()
        scoreBoardDialog.window?.setBackgroundDrawableResource(R.color.trans)
    }

    /**
     * shows the player a board with the results they achieved in the level, if they won they can go directly to the next level, retry the same if they lose, see stars, points and go back to the level selection
     */
    fun scoreBoardStoryMode() {

        val scoreBoardDialog = Dialog(context)
        scoreBoardDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        scoreBoardDialog.setCancelable(false)
        scoreBoardDialog.setContentView(R.layout.story_mode_result)

        val starBar = scoreBoardDialog.findViewById(R.id.pb_starbar) as ProgressBar

        val starOne = scoreBoardDialog.findViewById(R.id.iv_sm_result_star_one) as ImageView
        val starTwo = scoreBoardDialog.findViewById(R.id.iv_sm_result_star_two) as ImageView
        val starThree = scoreBoardDialog.findViewById(R.id.iv_sm_result_star_three) as ImageView

        val returnBtn = scoreBoardDialog.findViewById(R.id.iv_sm_result_level_return) as ImageView

        val retryBtn = scoreBoardDialog.findViewById(R.id.iv_sm_result_level_start) as ImageView

        val resultScore = scoreBoardDialog.findViewById(R.id.tv_sm_score_result) as TextView
        val levelText = scoreBoardDialog.findViewById<TextView>(R.id.tv_level_id)

        val resultGemsLooted =
            scoreBoardDialog.findViewById(R.id.tv_sm_total_gem_looted) as TextView

        val resultTotalScore =
            scoreBoardDialog.findViewById(R.id.tv_sm_total_score) as TextView

        val BonusTime = scoreBoardDialog.findViewById<TextView>(R.id.tv_sm_bonustime_result)

        val currentScore = PlayerManager.playerPoints
        var bonusTimeScore = 0

        resultGemsLooted.text = PlayerManager.levelGems.toString()

        resultScore.text = currentScore.toString()

        bonusTimeScore = when (PlayerManager.lives) {

            3 -> (PlayerManager.levelTime.toInt()/1000) * 5
            2 -> (PlayerManager.levelTime.toInt()/1000) * 4
            1 -> (PlayerManager.levelTime.toInt()/1000) * 3
            else ->  0
        }

        val totalScore = currentScore + bonusTimeScore
        BonusTime.text = bonusTimeScore.toString()
        resultTotalScore.text = totalScore.toString()

        val currentLevelMaxScore = PlayerManager.currentTotalBrickScore * 2
        starBar.max = currentLevelMaxScore

        val refScore = 0
        starBar.progress = 0
        var isOneStar = false
        var isTwoStar = false
        val levelString = "Level " + "${PlayerManager.currentLevel}"

        levelText.text = levelString

        if (totalScore > currentLevelMaxScore) {

            starBar.max = totalScore
        }
        starBar.progress = totalScore

        if (starBar.progress >= (currentLevelMaxScore / 2) && !isOneStar) {
            starOne.setImageResource(R.drawable.star)
            Log.d(TAG, "scoreBoardStoryMode: 1 star reach")
            isOneStar = true

        }
        if (starBar.progress >= ((currentLevelMaxScore / 4) * 3) && isOneStar) {
            starTwo.setImageResource(R.drawable.star)
            Log.d(TAG, "scoreBoardStoryMode: 2 star reach")
            isTwoStar = true

        }
        if (starBar.progress >= currentLevelMaxScore && isTwoStar) {
            starThree.setImageResource(R.drawable.star)
            Log.d(TAG, "scoreBoardStoryMode: 3 star reach")

        }

        returnBtn.setOnClickListener {
/*
            if (PlayerManager.isLevelCompleted && !PlayerManager.isReplaying) {
                PlayerManager.isLevelCompleted = false
                PlayerManager.currentLevel++
            }

 */
            scoreBoardDialog.dismiss()
        }

        retryBtn.setOnClickListener {

            if (PlayerManager.isLevelCompleted && !PlayerManager.isReplaying) {
                PlayerManager.isLevelCompleted = false
                PlayerManager.currentLevel++
            }
            getOverWorldActivity().startLevel()
            scoreBoardDialog.dismiss()

        }

        scoreBoardDialog.show()
        scoreBoardDialog.window?.setBackgroundDrawableResource(R.color.trans)
    }

    /**
     *shows the user the settings menu with the option to activate and deactivate music and/or sound effects and crate a new user profile
     */
    fun settingsDialog(sp: SharedPreferences) {

        val settingsDialog = Dialog(context)
        settingsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        settingsDialog.setContentView(R.layout.setting_screen_layout)
        val musicSwitch = settingsDialog.findViewById<SwitchMaterial>(R.id.switch_music)
        val effectSwitch = settingsDialog.findViewById<SwitchMaterial>(R.id.switch_sound_effects)
        val newUserBtn = settingsDialog.findViewById<Button>(R.id.btn_create_account)

        musicSwitch.isChecked = PlayerManager.isMusicActive
        effectSwitch.isChecked = PlayerManager.isSoundEffectsActive

        musicSwitch.setOnCheckedChangeListener { _, b ->
            PlayerManager.isMusicActive = b
            getMainActivity().checkForMusic()
            setUserPreferences()
        }

        effectSwitch.setOnCheckedChangeListener { _, b ->
            PlayerManager.isSoundEffectsActive = b
            setUserPreferences()
        }

        newUserBtn.setOnClickListener {
            nameInput(sp)
            settingsDialog.dismiss()
        }

        settingsDialog.show()
        settingsDialog.window?.setBackgroundDrawableResource(R.color.trans)

    }

    /**
     * shows  aDialog where the player can see how many stars and points he earned in the selected level, and lets he/she choose a powerup to bring in the level or buy one if they have enough gems
     */
    fun enterLevelScreen(levelId: Int) {

        val enterLevelDialog = Dialog(context)

        enterLevelDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        enterLevelDialog.setCancelable(false)
        enterLevelDialog.setContentView(R.layout.enter_level_screen)
        val returnToWorldBtn = enterLevelDialog.findViewById(R.id.iv_level_return) as ImageView
        val startLevelBtn = enterLevelDialog.findViewById(R.id.iv_level_start) as ImageView
        var screenLevelID = enterLevelDialog.findViewById(R.id.tv_level_id) as TextView

        val starProgressResult =
            enterLevelDialog.findViewById(R.id.iv_star_progress_holder) as ImageView
        val screenLevelScore = enterLevelDialog.findViewById(R.id.tv_level_score) as TextView
        val screenLevelScoreResult =
            enterLevelDialog.findViewById(R.id.tv_level_score_result) as TextView
        val playerGemBalance = enterLevelDialog.findViewById<TextView>(R.id.tv_level_gem_amount)

        val leftArrowCharacter =
            enterLevelDialog.findViewById(R.id.iv_level_left_arrow) as ImageView
        val screenLevelCharacter =
            enterLevelDialog.findViewById(R.id.iv_level_character) as ImageView
        val rightArrowCharacter =
            enterLevelDialog.findViewById(R.id.iv_level_right_arrow) as ImageView

        val screenLevelLoadoutOne =
            enterLevelDialog.findViewById(R.id.iv_level_loadout_1) as ImageView
        val screenLevelLoadoutTwo =
            enterLevelDialog.findViewById(R.id.iv_level_loadout_2) as ImageView
        val screenLevelLoadoutThree =
            enterLevelDialog.findViewById(R.id.iv_level_loadout_3) as ImageView
        val screenLevelLoadoutFour =
            enterLevelDialog.findViewById(R.id.iv_level_loadout_4) as ImageView
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
        val starContainerThree =
            enterLevelDialog.findViewById<ImageView>(R.id.iv_pre_level_star_three)

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
            }
        }

        val levelString = context.getString(R.string.level) + levelId.toString()

        when (levelId) {
            1 -> {
                screenLevelID.text = levelString
                screenLevelScoreResult.text =
                    checkPoints(levelId)
                enterLevelDialog.dismiss()
            }
            2 -> {
                screenLevelID.text = levelString
                screenLevelScoreResult.text =
                    checkPoints(levelId)
                enterLevelDialog.dismiss()
            }
            3 -> {
                screenLevelID.text = levelString
                screenLevelScoreResult.text =
                    checkPoints(levelId)
                enterLevelDialog.dismiss()
            }
            4 -> {
                screenLevelID.text = levelString
                screenLevelScoreResult.text =
                    checkPoints(levelId)
                enterLevelDialog.dismiss()
            }
            5 -> {
                screenLevelID.text = levelString
                screenLevelScoreResult.text =
                    checkPoints(levelId)
                enterLevelDialog.dismiss()
            }
            6 -> {
                screenLevelID.text = levelString
                screenLevelScoreResult.text =
                    checkPoints(levelId)
                enterLevelDialog.dismiss()
            }
            7 -> {
                screenLevelID.text = levelString
                screenLevelScoreResult.text =
                    checkPoints(levelId)
                enterLevelDialog.dismiss()
            }
            8 -> {
                screenLevelID.text = levelString
                screenLevelScoreResult.text =
                    checkPoints(levelId)
                enterLevelDialog.dismiss()
            }
            9 -> {
                screenLevelID.text = levelString
                screenLevelScoreResult.text =
                    checkPoints(levelId)
                enterLevelDialog.dismiss()
            }
            10 -> {
                screenLevelID.text = levelString
                screenLevelScoreResult.text =
                    checkPoints(levelId)
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

                if (!checkIfPowerUpAvailable()) {
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

                if (!checkIfPowerUpAvailable()) {
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

                if (!checkIfPowerUpAvailable()) {
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

            enterLevelDialog.dismiss()

            if (PlayerManager.powerUpActivated >= 0) {
                PlayerManager.powerUpInventory[PlayerManager.powerUpActivated] -= 1
            }

            val overWorldActivity = context as OverWorldActivity
            overWorldActivity.startLevel()

        }

        enterLevelDialog.show()
        enterLevelDialog.window?.setBackgroundDrawableResource(R.color.trans)
    }

    private fun checkPoints(levelId: Int): String {

        return if (PlayerManager.levelScoresArray.size < levelId) {
            "0"
        } else {
            PlayerManager.levelScoresArray[levelId - 1].toString()
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

    /**
     * gives the user the option to buy power-ups before starting a level to use inside the level
     */
    fun shopDialog() {
        val shopDialog = Dialog(context)
        shopDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        shopDialog.setContentView(R.layout.shop_dialog_layout)

        val shopText = shopDialog.findViewById<TextView>(R.id.shop_dialog_text)
        val yesButton = shopDialog.findViewById<ImageButton>(R.id.shop_yes_btn)
        val noButton = shopDialog.findViewById<ImageButton>(R.id.shop_no_btn)

        val shopStringOne = context.getString(R.string.shop_dialog_text_part_1)
        val shopStringTwo = context.getString(R.string.shop_dialog_string_part_2)
        val price = when (PlayerManager.selectedPowerUp) {
            0 -> PlayerManager.multiBallPrice.toString()
            1 -> PlayerManager.gunPrice.toString()
            2 -> PlayerManager.shieldPrice.toString()
            else -> PlayerManager.multiBallPrice.toString()
        }
        var shopStringFinal = shopStringOne + price + shopStringTwo

        shopText.text = shopStringFinal

        yesButton.setOnClickListener {

            if (PlayerManager.buyPowerUp(price.toInt())) {
                PlayerManager.powerUpInventory[PlayerManager.selectedPowerUp] = +1
                enterLevelScreen(backupLevelId)
                shopDialog.dismiss()
            } else {
                toaster(2)
            }
        }

        noButton.setOnClickListener {
            shopDialog.dismiss()
            enterLevelScreen(backupLevelId)
        }

        shopDialog.show()
        shopDialog.window?.setBackgroundDrawableResource(R.color.trans)
    }

    private fun setUserPreferences() {

        PlayerManager.activeUser!!.isMusicActive = PlayerManager.isMusicActive
        PlayerManager.activeUser!!.isSoundEffectsActive = PlayerManager.isSoundEffectsActive
    }

    private fun toaster(id: Int) {
        when (id) {
            0 -> Toast.makeText(context, "Level not yet unlocked", Toast.LENGTH_SHORT)
                .show()
            1 -> Toast.makeText(context, "create an account", Toast.LENGTH_SHORT)
                .show()
            2 -> Toast.makeText(context, "not enough gems", Toast.LENGTH_SHORT)
                .show()
        }

    }

    private fun getMainActivity(): MainActivityMainMenu {
        return context as MainActivityMainMenu
    }

    private fun getOverWorldActivity(): OverWorldActivity {
        return context as OverWorldActivity
    }

}