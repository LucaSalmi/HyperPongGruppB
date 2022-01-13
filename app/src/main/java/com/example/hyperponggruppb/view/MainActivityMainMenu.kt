package com.example.hyperponggruppb.view

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import com.example.hyperponggruppb.controller.PlayerManager
import com.example.hyperponggruppb.R
import com.example.hyperponggruppb.controller.DialogManager
import com.example.hyperponggruppb.controller.SoundEffectManager
import com.example.hyperponggruppb.controller.GameModeOneActivity
import com.example.hyperponggruppb.databinding.ActivityMainBinding
import com.example.hyperponggruppb.model.AssetManager


class MainActivityMainMenu : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainDialog: DialogManager
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
        mainDialog = DialogManager(this)

        sp = getSharedPreferences("com.example.hyperponggruppb.MyPrefs", MODE_PRIVATE)
        PlayerManager.readSave(sp)
        Log.d(TAG, "onCreate: ${PlayerManager.usersArray}")

        if (PlayerManager.name == "null") {
            isFirstAccount = true
            mainDialog.nameInput(sp)
        }else{

            setAccount()
            onClick()
        }


    }

    fun checkForMusic(){

        if (!PlayerManager.isMusicActive){
            SoundEffectManager.stopMusic()
        }else{
            SoundEffectManager.musicSetup(this, 0)
        }

    }

    private fun onClick(){

        binding.ivGameMode.setOnClickListener {

            if (PlayerManager.isMusicActive){
                SoundEffectManager.stopMusic()
            }

            PlayerManager.loadUserData()
            
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

            mainDialog.changeAccount()
        }

        binding.ivSettings.setOnClickListener {
            mainDialog.settingsDialog(sp)
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

        val toStoryMode = Intent(this, OverWorldActivity::class.java)
        PlayerManager.isInfiniteMode = false
        startActivity(toStoryMode)
    }

    fun startInfinityMode(){

        val toGameModeOne = Intent(this, GameModeOneActivity::class.java)
        PlayerManager.isInfiniteMode = true
        startActivity(toGameModeOne)
    }

    fun setAccount(){

        accountText = if (PlayerManager.name != "null"){
            getString(R.string.active_account_string) + PlayerManager.name
        }else{
            getString(R.string.active_account_string) + "None"
        }
        binding.tvActiveAccount.text = accountText
        PlayerManager.loadUserData()

    }

    override fun onResume() {

        if (PlayerManager.isGameEnded) {

            PlayerManager.isGameEnded = false
            mainDialog.scoreBoardInfinityMode()
        }

        if (PlayerManager.isMusicActive){
            SoundEffectManager.musicSetup(this, 0)
        }

        super.onResume()
    }

}