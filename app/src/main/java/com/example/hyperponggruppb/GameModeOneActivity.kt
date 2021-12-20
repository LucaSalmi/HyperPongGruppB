package com.example.hyperponggruppb

import android.app.Dialog
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.commit
import com.example.hyperponggruppb.databinding.ActivityGameModeOneBinding

class GameModeOneActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameModeOneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameModeOneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.commit {
            add(R.id.frame_layout, PointFragment())
        }

        supportFragmentManager.commit {
            add(R.id.gameViewContainer, GameOneFragment())
        }
    }

    fun updateText() {

        runOnUiThread(Runnable {
            supportFragmentManager.commit {
                replace(R.id.frame_layout, PointFragment())
            }
        })
    }

    fun scoreBoard() {

        runOnUiThread(Runnable {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.result_view)
            //val body = dialog.findViewById(R.id.body) as TextView
            //body.text = title
            val returnBtn = dialog.findViewById(R.id.tv_result_return) as TextView
            val retryBtn = dialog.findViewById(R.id.tv_result_next) as TextView

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

                supportFragmentManager.commit {
                    remove(PointFragment())
                    remove(GameOneFragment())
                }
                dialog.dismiss()

                Handler(Looper.myLooper()!!).postDelayed({

                    this.finish()

                }, 500)

            }
            retryBtn.setOnClickListener {

                supportFragmentManager.commit {
                    PlayerManager.resetPoints()
                    remove(GameOneFragment())
                    add(R.id.gameViewContainer, GameOneFragment())
                }

                dialog.dismiss()
            }




            dialog.show()
            dialog.window?.setBackgroundDrawableResource(R.color.trans)
        })
    }

}