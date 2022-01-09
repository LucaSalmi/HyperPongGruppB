package com.example.hyperponggruppb.model

import android.content.Context
import android.graphics.*

class Ball(context: Context,var ballLeft: Int,var ballTop: Int,var ballRight: Int,var ballBottom: Int) {


    var paint = Paint()
    var ballSpeedX = 0f
    var ballSpeedY = 0f
    var ballsize = 20f
    var ballRect = Rect(ballLeft,ballTop,ballRight,ballBottom)
    var playerCollision = false
    var brickCollision = false

    init {
        paint.color = Color.WHITE
    }

    fun ballGoesDown(): Boolean{
        return ballSpeedY > 0
    }

    fun ballGoesRight(): Boolean{
        return ballSpeedX > 0
    }

    fun draw(canvas: Canvas?) {
        canvas?.drawRect(ballRect,paint)
    }
}