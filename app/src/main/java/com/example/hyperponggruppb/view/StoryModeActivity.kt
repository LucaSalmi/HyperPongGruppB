package com.example.hyperponggruppb.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import com.example.hyperponggruppb.R
import com.example.hyperponggruppb.ViewPagerAdapter
import com.example.hyperponggruppb.controller.SoundEffectManager
import com.example.hyperponggruppb.databinding.ActivityStoryModeBinding
import com.example.hyperponggruppb.view.fragment.FirstWorldFragment

class StoryModeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryModeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryModeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SoundEffectManager.musicSetup(this, 1)

        binding.overworldMapContainer.adapter = ViewPagerAdapter(this)
    }

}