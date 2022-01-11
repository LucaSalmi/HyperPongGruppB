package com.example.hyperponggruppb.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hyperponggruppb.adapter.ViewPagerAdapter
import com.example.hyperponggruppb.controller.PlayerManager
import com.example.hyperponggruppb.controller.SoundEffectManager
import com.example.hyperponggruppb.databinding.ActivityStoryModeBinding

class StoryModeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryModeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryModeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (PlayerManager.isMusicActive){
            SoundEffectManager.musicSetup(this, 1)
        }

        val adapter = ViewPagerAdapter(this)
        binding.overworldMapContainer.adapter = adapter
        binding.overworldMapContainer.currentItem = 1
    }

}