package com.example.hyperponggruppb.controller

import android.content.ContentValues
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import com.example.hyperponggruppb.view.fragment.InfinityFragment
import com.example.hyperponggruppb.view.fragment.PointFragmentInfinityMode
import com.example.hyperponggruppb.R
import com.example.hyperponggruppb.databinding.ActivityGameModeOneBinding
import android.util.Log
import android.widget.TextView
import com.example.hyperponggruppb.model.AssetManager
import java.lang.Exception


class GameModeOneActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameModeOneBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameModeOneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.commit {
            add(R.id.frame_layout, PointFragmentInfinityMode())
        }

        supportFragmentManager.commit {
            add(R.id.overworld_container, InfinityFragment())
        }
    }

    fun updateText() {

        runOnUiThread(Runnable {

            try {
                supportFragmentManager.commit {
                    replace(R.id.frame_layout, PointFragmentInfinityMode())
                }
            }catch (e: Exception){
                Log.e(TAG, "updateText: caught")
            }

        })
    }

    fun updateComboCounter(){

        runOnUiThread(Runnable {
            try {

                val comboMeter = findViewById<TextView>(R.id.tv_combo_meter_infinity)
                val comboText = findViewById<TextView>(R.id.tv_hyper_combo_message_infinity)

                if (PlayerManager.comboPoints > 0){
                    val comboString = PlayerManager.comboPoints.toString() + "X"
                    comboMeter.text = comboString

                    if (PlayerManager.textIsOn){
                        comboText.text = getString(R.string.hyper_combo_msg)
                    }else{
                        comboText.text = ""
                    }

                }else{
                    comboMeter.text = ""
                }

            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "updateCombo: caught")
            }
        })
    }

    override fun onBackPressed() {
        PsyduckEngine.gameStart = false
        AssetManager.resetBackGround()
        super.onBackPressed()
    }

    override fun onPause() {

        SoundEffectManager.stopMusic()
        super.onPause()
    }
}