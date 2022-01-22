package com.example.hyperponggruppb.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.hyperponggruppb.R
import com.example.hyperponggruppb.adapter.ViewPagerAdapter
import com.example.hyperponggruppb.controller.DialogManager
import com.example.hyperponggruppb.controller.PlayerManager
import com.example.hyperponggruppb.controller.SoundEffectManager
import com.example.hyperponggruppb.databinding.ActivityStoryModeBinding

class OverWorldActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryModeBinding
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var storyModeDialog: DialogManager
    private lateinit var tvGemCounter: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryModeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storyModeDialog = DialogManager(this)

        if (PlayerManager.isMusicActive){
            SoundEffectManager.musicSetup(this, 2)
        }

        adapter = ViewPagerAdapter(this)
        binding.overworldContainer.adapter = adapter
        binding.overworldContainer.currentItem = 1

        gemsCounterText()
    }

    override fun onResume() {

        binding.overworldContainer.adapter = adapter
        binding.overworldContainer.currentItem = 1

        if(PlayerManager.isGameEnded || PlayerManager.isLevelCompleted){

            storyModeDialog.scoreBoardStoryMode()

            PlayerManager.isGameEnded = false
        }

        super.onResume()
    }

    fun gemsCounterText(){
        var x = ": ${PlayerManager.gems}"
        tvGemCounter = findViewById(R.id.tv_gem_counter)
        tvGemCounter.text = x
    }

    fun startLevel(){
        val toLevel = Intent(this, GameModeStoryActivity::class.java)
        startActivity(toLevel)
    }

    override fun onBackPressed() {
        if (PlayerManager.isMusicActive){
            SoundEffectManager.stopMusic()
        }
        super.onBackPressed()
    }

}