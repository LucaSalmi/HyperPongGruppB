package com.example.hyperponggruppb

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.View
import com.example.hyperponggruppb.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SurfaceHolder.Callback, View.OnTouchListener {

    private lateinit var binding: ActivityMainBinding

    val brickRow = mutableListOf<Bricks>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val paint = Paint()
        paint.color = Color.RED

        BrickStructure.makeBricks(brickRow)
        binding.surfaceView.setOnTouchListener(this)


    }

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



    override fun surfaceCreated(p0: SurfaceHolder) {
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        makeCanvas()
        return true
    }
}