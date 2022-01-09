package com.example.hyperponggruppb.model

import android.content.Context
import android.graphics.*

class Ball() {


    var paint = Paint()
    var ballSpeedX = 0f
    var ballSpeedY = 0f
    var ballsize = 20f
    var ballLeft = 0
    var ballTop = 0
    var ballRight = 0
    var ballBottom = 0
    var ballRect = Rect()
    var playerCollision = false
    var brickCollision = false

    init {
        paint.color = Color.TRANSPARENT
    }

    fun update(){
        ballRect = Rect(ballLeft,ballTop,ballRight,ballBottom)
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