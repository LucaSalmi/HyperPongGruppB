package com.example.hyperponggruppb.controller

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Window
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hyperponggruppb.R
import com.example.hyperponggruppb.adapter.UserSelectionAdapter
import com.example.hyperponggruppb.view.GameModeStoryActivity
import com.example.hyperponggruppb.view.LeaderBoardActivity
import com.example.hyperponggruppb.view.MainActivityMainMenu
import com.example.hyperponggruppb.view.OverWorldActivity
import com.example.hyperponggruppb.view.fragment.FirstWorldFragment
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.coroutines.delay

class DialogManager(val context: Context) {


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
        val saveBtn = nameInputDialog.findViewById<Button>(R.id.save_btn)
        val cancelBtn = nameInputDialog.findViewById<Button>(R.id.cancel_btn)

        saveBtn.setOnClickListener {

            if (nameField.text != null && nameField.text.length == 3) {

                PlayerManager.name = nameField.text.toString()
                SoundEffectManager.jukebox(context, 1)
                if (PlayerManager.createUser()){
                    PlayerManager.saveUserData(sp)
                    getMainActivity().setAccount()
                    nameInputDialog.dismiss()
                }else{
                    nameField.error = "Name already in use"
                }

            }
        }

        cancelBtn.setOnClickListener {
            if (PlayerManager.isFirstAccount){
                cancelBtn.error = "create an account"
            }else{
                nameInputDialog.dismiss()
            }

        }

        nameInputDialog.show()
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

        val resultGemsLooted =
            scoreBoardDialog.findViewById(R.id.tv_sm_total_gem_looted) as TextView

        val resultPowerUpsLooted =
            scoreBoardDialog.findViewById(R.id.tv_sm_total_powerups_looted2) as TextView

        val currentScore = PlayerManager.playerPoints

        resultScore.text = PlayerManager.playerPoints.toString()

        var stars = 0
        starBar.progress = 0
        starBar.max = PlayerManager.currentMaxScore
        Log.d(TAG, "scoreBoardStoryMode: maxScore = ${PlayerManager.currentMaxScore}")
        Log.d(TAG, "scoreBoardStoryMode: progress = $currentScore")
        Log.d(TAG, "scoreBoardStoryMode: maxScore = ${PlayerManager.currentMaxScore / 3}")
        Log.d(TAG, "scoreBoardStoryMode: starbar =${starBar.progress}")


        val refScore = 0
        starBar.max = PlayerManager.currentMaxScore
        starBar.progress = 0
        var isOneStar = false
        var isTwoStar = false

        if (currentScore > refScore) {
            //Handler(Looper.myLooper()!!).postDelayed({ FÖRSÖKER DELAYA STARBARLOAD "animation brush" dock FUNKAR EJ
                while (starBar.progress < currentScore) {
                    starBar.progress + 10
                    Log.d(TAG, "scoreBoardStoryMode: starbar =${starBar.progress}")

                    starBar.progress = (currentScore)

                    if (starBar.progress >= (PlayerManager.currentMaxScore / 3) && !isOneStar) {
                        starOne.setImageResource(R.drawable.star)
                        Log.d(TAG, "scoreBoardStoryMode: 1 star reach")
                        isOneStar = true

                        stars = 1
                    }
                    if (starBar.progress >= ((PlayerManager.currentMaxScore / 3) * 2) && isOneStar) {
                        starTwo.setImageResource(R.drawable.star)
                        Log.d(TAG, "scoreBoardStoryMode: 2 star reach")
                        isTwoStar = true

                        stars = 2
                    }
                    if (starBar.progress >= PlayerManager.currentMaxScore && isTwoStar) {
                        starThree.setImageResource(R.drawable.star)
                        Log.d(TAG, "scoreBoardStoryMode: 3 star reach")
                        stars = 3
                    }
                }
           //}, 100)

        }

        returnBtn.setOnClickListener {

            scoreBoardDialog.dismiss()
        }

        retryBtn.setOnClickListener {
            PlayerManager.starCounter = 0 // reset the stars on the !!! - UI - !!!

            getOverWorldActivity().startLevel()
            scoreBoardDialog.dismiss()
        }

        scoreBoardDialog.show()
        scoreBoardDialog.window?.setBackgroundDrawableResource(R.color.trans)
    }


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

    private fun setUserPreferences(){

        PlayerManager.activeUser!!.isMusicActive = PlayerManager.isMusicActive
        PlayerManager.activeUser!!.isSoundEffectsActive = PlayerManager.isSoundEffectsActive
    }

    private fun getMainActivity(): MainActivityMainMenu {
        return context as MainActivityMainMenu
    }

    private fun getOverWorldActivity(): OverWorldActivity {
        return context as OverWorldActivity
    }

}