package com.example.hyperponggruppb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import com.example.hyperponggruppb.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val brickRow = mutableListOf<Bricks>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(GameView(this))

        PointManager.playerPoints
        /*
        supportFragmentManager.commit {
            add(R.id.fragment_container, PointFragment())
        }

        binding.pointsBtn.setOnClickListener {
            PointManager.addPoints(10)
            supportFragmentManager.commit {
                replace(R.id.fragment_container, PointFragment())
            }
        }

         */


    }
/*
    fun makeCanvas(){
        val canvas: Canvas? = binding.surfaceView.holder.lockCanvas()
        val background = Paint()
        background.color = Color.GREEN

        canvas?.drawRect(0f, 0f, binding.surfaceView.width.toFloat(), binding.surfaceView.height.toFloat(), background)

        if (canvas != null) {

            for(obj in brickRow){

                canvas.drawRect(obj.brickLeft, obj.brickTop, obj.brickRight, obj.brickBottom, obj.brickPaint)
            }

        }





        binding.surfaceView.holder.unlockCanvasAndPost(canvas)
        binding.surfaceView.setZOrderOnTop(true)
    }

 */
}