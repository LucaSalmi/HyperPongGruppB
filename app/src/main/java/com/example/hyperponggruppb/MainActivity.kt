package com.example.hyperponggruppb

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.example.hyperponggruppb.databinding.ActivityMainBinding
import android.os.Build
import android.os.Handler
import android.os.Looper


class MainActivity : AppCompatActivity() {

   private lateinit var binding: ActivityMainBinding

    val brickRow = mutableListOf<Bricks>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        Handler(Looper.myLooper()!!).postDelayed({

            setTheme(R.style.Theme_HyperPongGruppB)

            setContentView(binding.root)
        }, 3000)


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
    }*/
}