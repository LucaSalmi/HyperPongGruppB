package com.example.hyperponggruppb.model

import android.content.Context
import android.graphics.*

class Ball(context: Context,var ballLeft: Int,var ballTop: Int,var ballRight: Int,var ballBottom: Int) {


    var paint = Paint()
    var hitBoxPaint: Paint = Paint()
    var radius = 20f
    var ballSpeedX = 0f
    var ballSpeedY = 0f
    var ballsize = 18f
    var ballRect = Rect(ballLeft,ballTop,ballRight,ballBottom)

    var playerCollision = false
    var brickCollision = false

    init {
        paint.color = Color.TRANSPARENT
        hitBoxPaint.color = Color.TRANSPARENT
    }

    fun draw(canvas: Canvas?) {
        canvas?.drawRect(ballRect,paint)

    }
}