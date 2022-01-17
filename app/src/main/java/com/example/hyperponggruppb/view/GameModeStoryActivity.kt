package com.example.hyperponggruppb.view

import android.app.GameManager
import android.content.ContentValues
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import android.widget.ImageView
import androidx.fragment.app.commit
import com.example.hyperponggruppb.R
import com.example.hyperponggruppb.controller.PlayerManager
import com.example.hyperponggruppb.controller.PsyduckEngine
import com.example.hyperponggruppb.databinding.ActivityGameModeStoryBinding
import com.example.hyperponggruppb.view.fragment.PointFragmentInfinityMode
import com.example.hyperponggruppb.view.fragment.StoryLevelFragment
import com.example.hyperponggruppb.view.fragment.PointFragmentStoryMode

class GameModeStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameModeStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameModeStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFragments()


    }

    private fun setFragments() {

        supportFragmentManager.commit {
            add(R.id.score_fragment_container_story, PointFragmentStoryMode())
            add(R.id.story_view_container, StoryLevelFragment())
        }
    }


    fun updateText() {

        runOnUiThread(Runnable {
            try {

                supportFragmentManager.commit {
                    replace(R.id.score_fragment_container_story, PointFragmentStoryMode())
                }

            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "updateText: caught")
            }

        })
    }

    override fun onBackPressed() {
        PsyduckEngine.gameStart = false
        super.onBackPressed()
    }

    fun checkSelectedPowerup() {
        val activatedPowerup = (findViewById<ImageView>(R.id.iv_current_powerup_activated))

        if (PlayerManager.selectedPowerUp >= 0) {

            if (PlayerManager.selectedPowerUp == 0) { //multiball powerup
                activatedPowerup.setImageResource(R.drawable.multiball_button_selected)
            } else if (PlayerManager.selectedPowerUp == 1) {
                activatedPowerup.setImageResource(R.drawable.gun_button_selected)
            } else if (PlayerManager.selectedPowerUp == 2) {
                activatedPowerup.setImageResource(R.drawable.shield_button_selected)
            }
        }
    }


    fun activatePowerup() {
        runOnUiThread(Runnable {
            try {
                val activatedPowerup = (findViewById<ImageView>(R.id.iv_current_powerup_activated))
                activatedPowerup.setOnClickListener {
                    Log.d(TAG, "activatePowerup: clicked")
                    Log.d(TAG, "activatePowerup: selected =  ${PlayerManager.selectedPowerUp}")
                    if (PlayerManager.selectedPowerUp == 0) { //multiball powerup
                        activatedPowerup.setImageResource(R.drawable.multiball_button)
                    } else if (PlayerManager.selectedPowerUp == 1) {
                        activatedPowerup.setImageResource(R.drawable.gun_button)
                    } else if (PlayerManager.selectedPowerUp == 2) {
                        activatedPowerup.setImageResource(R.drawable.shield_button)
                    }
                    if (PlayerManager.selectedPowerUp >= 0) {
                        PlayerManager.activatePowerup = true
                        PlayerManager.powerUpArray[PlayerManager.selectedPowerUp] - 1
                        PlayerManager.selectedPowerUp = -1
                    }
                }
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "updateText: caught")
            }
        })
    }
}
