package com.example.hyperponggruppb.view

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.widget.*
import com.example.hyperponggruppb.LeaderBoardActivity
import com.example.hyperponggruppb.controller.PlayerManager
import com.example.hyperponggruppb.R
import com.example.hyperponggruppb.controller.SoundEffectManager
import com.example.hyperponggruppb.controller.GameModeOneActivity
import com.example.hyperponggruppb.databinding.ActivityMainBinding
import com.example.hyperponggruppb.model.AssetManager


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var accountText: String = ""
    private var isStoryMode = true
    var isFirstAccount = false
    private lateinit var sp: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        Handler(Looper.myLooper()!!).postDelayed({

            setTheme(R.style.Theme_HyperPongGruppB)

        }, 2000)

        setContentView(binding.root)
        AssetManager.prepareAssets(this)

        sp = getSharedPreferences("com.example.hyperponggruppb.MyPrefs", MODE_PRIVATE)
        PlayerManager.readSave(sp)

        if (PlayerManager.name == "null") {
            isFirstAccount = true
            nameInput()
        }

        setAccount()
        onClick()
    }

    private fun onClick(){

        binding.ivGameMode.setOnClickListener {
            
            SoundEffectManager.stopMusic()
            
            if (isStoryMode){
                startStoryMode()
            }else{
                startInfinityMode()
            }
        }

        binding.btnModeForward.setOnClickListener {
            isStoryMode = !isStoryMode
            changeButtonText()
        }

        binding.btnModeBack.setOnClickListener {
            isStoryMode = !isStoryMode
            changeButtonText()
        }

        binding.ivLeaderboard.setOnClickListener {
            val toLeaderboard = Intent(this, LeaderBoardActivity::class.java)
            startActivity(toLeaderboard)
        }

        binding.btnChangeAccount.setOnClickListener {
            nameInput()
        }
    }

    private fun changeButtonText(){
        if (isStoryMode){
            binding.tvGameMode.text = getString(R.string.txt_story_mode)
        }else{
            binding.tvGameMode.text = getText(R.string.txt_infinite_mode)
        }
    }

    private fun startStoryMode(){

        val toStoryMode = Intent(this, StoryModeActivity::class.java)
        PlayerManager.isInfiniteMode = false
        startActivity(toStoryMode)
    }

    private fun startInfinityMode(){

        val toGameModeOne = Intent(this, GameModeOneActivity::class.java)
        PlayerManager.isInfiniteMode = true
        PlayerManager.setHighScore()
        startActivity(toGameModeOne)
    }

    private fun setAccount(){

        accountText = if (PlayerManager.name != "null"){
            getString(R.string.active_account_string) + PlayerManager.name
        }else{
            getString(R.string.active_account_string) + "None"
        }
        binding.tvActiveAccount.text = accountText
    }

    /**
     * if no Account is loaded, or the user wants to change the account he is playing on, the dialog prompts for a name and loads, if present, all the necessary data coupled with the user
     */
    private fun nameInput(){

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.enter_name_dialog)
        val nameField = dialog.findViewById<EditText>(R.id.et_enter_name_field)
        val saveBtn = dialog.findViewById<Button>(R.id.save_btn)
        val cancelBtn = dialog.findViewById<Button>(R.id.cancel_btn)

        saveBtn.setOnClickListener {

            if (nameField.text != null && nameField.text.length == 3) {
                PlayerManager.name = nameField.text.toString()
                SoundEffectManager.jukebox(this, 1)
                setAccount()
                PlayerManager.saveHighScore(sp)
                PlayerManager.resetHighScore()
                PlayerManager.setHighScore()
                dialog.dismiss()
            }
        }
            cancelBtn.setOnClickListener {

                if (isFirstAccount){

                    PlayerManager.name = "Guest"
                }
                dialog.dismiss()
            }
        
        dialog.show()
    }

    /**
     * creates the scoreboard to show the player their high score and their position in the leaderboard, it also links directly to the full scoreboard, the main menu and restarts the game.
     */
    private fun scoreBoard() {

        val dialog = Dialog(this)
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
                playerPlacementEnding = getString(R.string.result_placement_one)
            }
            2 -> {
                resultMessage.setText(R.string.result_message_two)
                playerPlacementEnding = getString(R.string.result_placement_two)
            }
            3 -> {
                resultMessage.setText(R.string.result_message_three)
                playerPlacementEnding = getString(R.string.result_placement_three)
            }
            in 4..10 -> {
                resultMessage.setText(R.string.result_message_four)
                playerPlacementEnding = getString(R.string.result_placement_four_plus)
            }
            else -> {
                resultMessage.setText(R.string.result_message_five)
                playerPlacementEnding = getString(R.string.result_placement_four_plus)
            }
        }

        resultPlacement.text = (playerPlacement.toString() + playerPlacementEnding)
        var resultScoreWithSign = playerScore.toString() + getString(R.string.result_p_sign)
        resultScore.text = resultScoreWithSign

        returnBtn.setOnClickListener {

            dialog.dismiss()
        }

        retryBtn.setOnClickListener {

            startInfinityMode()
            dialog.dismiss()
        }

        leaderboardBtn.setOnClickListener {

            val toLeaderboard = Intent(this, LeaderBoardActivity::class.java)
            dialog.dismiss()
            startActivity(toLeaderboard)
        }

        dialog.show()
        dialog.window?.setBackgroundDrawableResource(R.color.trans)
    }

    override fun onResume() {

        if (PlayerManager.isGameEnded) {

            PlayerManager.isGameEnded = false
            scoreBoard()
        }
        
        SoundEffectManager.musicSetup(this, 0)
        super.onResume()
    }

}