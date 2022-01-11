package com.example.hyperponggruppb.controller

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hyperponggruppb.R
import com.example.hyperponggruppb.adapter.UserSelectionAdapter
import com.example.hyperponggruppb.view.LeaderBoardActivity
import com.example.hyperponggruppb.view.MainActivity

class DialogManager(val context: Context) {

    private var myActivity: MainActivity = context as MainActivity


    /**
     * lets the player change wich account he/she is playing on from a list of all saved users.
     */
    fun changeAccount(context: Context){

        val userListDialog = Dialog(context)
        userListDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        userListDialog.setContentView(R.layout.fragment_user_select_screen)

        val usersList = userListDialog.findViewById<RecyclerView>(R.id.rw_user_list)
        usersList.layoutManager = LinearLayoutManager(context)
        val userSelectionAdapter = UserSelectionAdapter(context, PlayerManager.usersArray, userListDialog)
        usersList.adapter = userSelectionAdapter

        userListDialog.show()
        userListDialog.window?.setBackgroundDrawableResource(R.color.trans)
    }

    /**
     * if no Account is loaded, or the user wants to change the account he is playing on, the dialog prompts for a name and loads, if present, all the necessary data coupled with the user
     */
    fun nameInput(context: Context, sp: SharedPreferences){

        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.enter_name_dialog)
        val nameField = dialog.findViewById<EditText>(R.id.et_enter_name_field)
        val saveBtn = dialog.findViewById<Button>(R.id.save_btn)

        saveBtn.setOnClickListener {

            if (nameField.text != null && nameField.text.length == 3) {
                PlayerManager.name = nameField.text.toString()
                SoundEffectManager.jukebox(context, 1)
                PlayerManager.saveUserData(sp)
                myActivity.setAccount()
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    /**
     * creates the scoreboard to show the player their high score and their position in the leaderboard, it also links directly to the full scoreboard, the main menu and restarts the game.
     */
    fun scoreBoard() {

        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.result_view)
        val returnBtn = dialog.findViewById(R.id.tv_result_return) as TextView
        val retryBtn = dialog.findViewById(R.id.tv_result_next) as TextView
        val leaderboardBtn = dialog.findViewById(R.id.iv_result_leaderboard) as ImageView

        val resultScore = dialog.findViewById(R.id.tv_result_score) as TextView
        val resultPlacement = dialog.findViewById(R.id.tv_result_placement) as TextView
        val resultMessage = dialog.findViewById(R.id.tv_result_message) as TextView

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

            dialog.dismiss()
        }

        retryBtn.setOnClickListener {

            myActivity.startInfinityMode()
            dialog.dismiss()
        }

        leaderboardBtn.setOnClickListener {

            val toLeaderboard = Intent(myActivity, LeaderBoardActivity::class.java)
            dialog.dismiss()
            startActivity(context, toLeaderboard, null)
        }

        dialog.show()
        dialog.window?.setBackgroundDrawableResource(R.color.trans)
    }


}