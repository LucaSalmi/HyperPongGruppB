package com.example.hyperponggruppb

import android.app.Dialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import androidx.fragment.app.commit
import com.example.hyperponggruppb.databinding.ActivityGameMode1Binding

class GameMode1Activity : AppCompatActivity() {

    private lateinit var binding: ActivityGameMode1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameMode1Binding.inflate(layoutInflater)
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
            val returnBtn = dialog.findViewById(R.id.iv_result_return) as ImageView
            val retryBtn = dialog.findViewById(R.id.iv_result_next) as ImageView

            returnBtn.setOnClickListener {
                this.finish()
                dialog.dismiss()
            }
            retryBtn.setOnClickListener {

                supportFragmentManager.commit {
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