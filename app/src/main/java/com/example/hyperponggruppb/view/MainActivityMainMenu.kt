package com.example.hyperponggruppb.view

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.example.hyperponggruppb.controller.PlayerManager
import com.example.hyperponggruppb.R
import com.example.hyperponggruppb.controller.DialogManager
import com.example.hyperponggruppb.controller.SoundEffectManager
import com.example.hyperponggruppb.controller.GameModeOneActivity
import com.example.hyperponggruppb.databinding.ActivityMainBinding
import com.example.hyperponggruppb.model.AssetManager
import kotlinx.coroutines.delay


class MainActivityMainMenu : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainDialog: DialogManager
    private var accountText: String = ""
    private var isStoryMode = true
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
        var menuPlayerProp = findViewById<ImageView>(R.id.iv_menu_player)
        var transUpX = 50f
        var transdownX = -50f



        sp = getSharedPreferences("com.example.hyperponggruppb.MyPrefs", MODE_PRIVATE)
        PlayerManager.readSave(sp)

        if (PlayerManager.name == "null") {

            PlayerManager.isFirstAccount = true
            mainDialog.nameInput(sp)
        }


        setAccount()
        onClick()
        startMenuAnimations()

        val greenPlanet = findViewById<ImageView>(R.id.iv_menu_green_planet)
        greenPlanet.setOnClickListener {
            meteorAnim()
        }
    }
    private fun startMenuAnimations() {
        val menuPlayerProp = findViewById<ImageView>(R.id.iv_menu_player)
        val animPlayer = AnimationUtils.loadAnimation(applicationContext, R.anim.player_hover_y)
        menuPlayerProp.startAnimation(animPlayer)

        val menuPurplePlanet = findViewById<ImageView>(R.id.iv_menu_purple_planet)
        val animPurplePlanet =
            AnimationUtils.loadAnimation(applicationContext, R.anim.purple_planet_hover_y)
        menuPurplePlanet.startAnimation(animPurplePlanet)

        val menuGreenPlanet = findViewById<ImageView>(R.id.iv_menu_green_planet)
        val animGreenPlanet =
            AnimationUtils.loadAnimation(applicationContext, R.anim.green_planet_hover_x)
        menuGreenPlanet.startAnimation(animGreenPlanet)

        val menuSpotlightOne = findViewById<ImageView>(R.id.iv_menu_spotlight_one)
        val menuSpotlightTwo = findViewById<ImageView>(R.id.iv_menu_spotlight_two)
        val menuSpotlightThree = findViewById<ImageView>(R.id.iv_menu_spotlight_three)
        val animSpotlightOne =
            AnimationUtils.loadAnimation(applicationContext, R.anim.spotlight_rotation_version_one)
        val animSpotlightTwo =
            AnimationUtils.loadAnimation(applicationContext, R.anim.spotlight_rotation_version_two)
        val animSpotlightThree = AnimationUtils.loadAnimation(
            applicationContext,
            R.anim.spotlight_rotation_version_three
        )
        menuSpotlightOne.startAnimation(animSpotlightOne)
        menuSpotlightTwo.startAnimation(animSpotlightThree)
        menuSpotlightThree.startAnimation(animSpotlightTwo)
    }
    private fun meteorAnim(){
        val menuCometProp = findViewById<ImageView>(R.id.iv_menu_meteor)
        val animMeteor = AnimationUtils.loadAnimation(applicationContext, R.anim.meteor_animation)
        menuCometProp.startAnimation(animMeteor)

    }

    fun checkForMusic() {

        if (!PlayerManager.isMusicActive) {
            SoundEffectManager.stopMusic()
        } else {
            SoundEffectManager.musicSetup(this, 0)
        }

    }

    private fun onClick() {

        binding.ivGameMode.setOnClickListener {

            if (PlayerManager.isMusicActive) {
                SoundEffectManager.stopMusic()
            }

            loadUser()

            if (isStoryMode) {
                startStoryMode()
            } else {
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

    private fun changeButtonText() {

        if (isStoryMode) {
            binding.tvGameMode.text = getString(R.string.txt_story_mode)
        } else {
            binding.tvGameMode.text = getText(R.string.txt_infinite_mode)
        }
    }

    private fun startStoryMode() {

        val toStoryMode = Intent(this, OverWorldActivity::class.java)
        PlayerManager.isInfiniteMode = false
        startActivity(toStoryMode)
    }

    fun startInfinityMode() {

        val toGameModeOne = Intent(this, GameModeOneActivity::class.java)
        PlayerManager.isInfiniteMode = true
        startActivity(toGameModeOne)
    }

    fun setAccount() {

        accountText = if (PlayerManager.name != "null") {
            getString(R.string.active_account_string) + PlayerManager.name
        } else {
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

        if (PlayerManager.isMusicActive) {
            SoundEffectManager.musicSetup(this, 0)
        }


        super.onResume()
    }

    fun loadUser(){
        PlayerManager.loadUserData()
    }

}