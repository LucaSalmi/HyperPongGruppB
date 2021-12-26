package com.example.hyperponggruppb.view

import android.app.Dialog
import android.content.Context
import android.content.Intent
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


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        Handler(Looper.myLooper()!!).postDelayed({

            setTheme(R.style.Theme_HyperPongGruppB)


        }, 2000)

        setContentView(binding.root)
        SoundEffectManager.bGMusic(this)

        val sp = getSharedPreferences("com.example.hyperponggruppb.MyPrefs", MODE_PRIVATE)
        PlayerManager.readSave(sp)


        binding.ivNewGame.setOnClickListener {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.enter_name_dialog)
            val nameField = dialog.findViewById<EditText>(R.id.et_enter_name_field)
            val startBtn = dialog.findViewById<Button>(R.id.start_btn)
            val cancelBtn = dialog.findViewById<Button>(R.id.cancel_btn)

            startBtn.setOnClickListener {

                if (nameField.text != null && nameField.text.length == 3){
                    PlayerManager.name = nameField.text.toString()
                    val toGameModeOne = Intent(this, GameModeOneActivity::class.java)
                    SoundEffectManager.jukebox(this, 1)
                    dialog.dismiss()
                    startActivity(toGameModeOne)
                }
            }

            cancelBtn.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        binding.ivLeaderboard.setOnClickListener {
            val toLeaderboard = Intent(this, LeaderBoardActivity::class.java)
            startActivity(toLeaderboard)
        }

    }


    fun scoreBoard() {

            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.result_view)
            //val body = dialog.findViewById(R.id.body) as TextView
            //body.text = title
            val returnBtn = dialog.findViewById(R.id.tv_result_return) as TextView
            val retryBtn = dialog.findViewById(R.id.tv_result_next) as TextView
            val leaderboardBtn = dialog.findViewById(R.id.iv_result_leaderboard) as ImageView

            val resultScore = dialog.findViewById(R.id.tv_result_score) as TextView
            val resultPlacement = dialog.findViewById(R.id.tv_result_placement) as TextView
            val resultMessage= dialog.findViewById(R.id.tv_result_message) as TextView

            val playerScore = PlayerManager.playerPoints
            val playerPlacement = PlayerManager.setPlacement()
            var PlayerPlacementEnding = ""

            when (playerPlacement) {
                1 -> {
                    resultMessage.setText(R.string.result_message_one)
                    PlayerPlacementEnding = getString(R.string.result_placement_one)
                }
                2 -> {
                    resultMessage.setText(R.string.result_message_two)
                    PlayerPlacementEnding = getString(R.string.result_placement_two)
                }
                3 -> {
                    resultMessage.setText(R.string.result_message_three)
                    PlayerPlacementEnding = getString(R.string.result_placement_three)
                }
                in 4..10 -> {
                    resultMessage.setText(R.string.result_message_four)
                    PlayerPlacementEnding = getString(R.string.result_placement_four_plus)
                }
                else -> {
                    resultMessage.setText(R.string.result_message_five)
                    PlayerPlacementEnding = getString(R.string.result_placement_four_plus)
                }
            }

            resultPlacement.text = (playerPlacement.toString() + PlayerPlacementEnding)
            var resultScoreWithSign = playerScore.toString() + getString(R.string.result_p_sign)
            resultScore.text = resultScoreWithSign

            returnBtn.setOnClickListener {

                dialog.dismiss()
            }

            retryBtn.setOnClickListener {

                val toGameModeOne = Intent(this, GameModeOneActivity::class.java)
                startActivity(toGameModeOne)
                dialog.dismiss()
            }

            leaderboardBtn.setOnClickListener {

                val toLeaderboard = Intent(this, LeaderBoardActivity::class.java)
                startActivity(toLeaderboard)
            }

            dialog.show()
            dialog.window?.setBackgroundDrawableResource(R.color.trans)
    }

    override fun onResume() {

        if (PlayerManager.isGameEnded){

            PlayerManager.isGameEnded = false
            PlayerManager.resetPoints()
            scoreBoard()
        }
        super.onResume()
    }

}