package com.example.hyperponggruppb.view

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.hyperponggruppb.adapter.ViewPagerAdapter
import com.example.hyperponggruppb.controller.DialogManager
import com.example.hyperponggruppb.controller.PlayerManager
import com.example.hyperponggruppb.controller.SoundEffectManager
import com.example.hyperponggruppb.databinding.ActivityStoryModeBinding

class OverWorldActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryModeBinding
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var storyModeDialog: DialogManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryModeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storyModeDialog = DialogManager(this)

        if (PlayerManager.isMusicActive){
            SoundEffectManager.musicSetup(this, 1)
        }

        adapter = ViewPagerAdapter(this)
        binding.overworldMapContainer.adapter = adapter
        binding.overworldMapContainer.currentItem = 1
    }

    override fun onResume() {

        adapter.notifyDataSetChanged()

        if(PlayerManager.isGameEnded || PlayerManager.isLevelCompleted){

            storyModeDialog.scoreBoardStoryMode()

            PlayerManager.isGameEnded = false
        }

        super.onResume()
    }

    fun startLevel(){
        val toLevel = Intent(this, GameModeStoryActivity::class.java)
        startActivity(toLevel)
    }


}