package com.example.hyperponggruppb

import android.content.Context
import android.graphics.*

class Ball(var context: Context, var ballPosX: Float, var ballPosY: Float) {

    //var ballPosX = -50f
    //var ballPosY = -50f
    var paint = Paint()
    var hitBoxPaint: Paint = Paint()
    var radius = 20f
    var ballSpeedX = 0f
    var ballSpeedY = 0f
    var hitBoxMargin = 18

    var playerCollision = false
    var brickCollision = false
    var ballHitBox: Rect = Rect(
        (ballPosX-hitBoxMargin).toInt(), //left
        (ballPosY-hitBoxMargin).toInt(), //top
        (ballPosX+hitBoxMargin).toInt(), //right
        (ballPosY+hitBoxMargin).toInt() //bottom
    )

    init {
        paint.color = Color.TRANSPARENT
        hitBoxPaint.color = Color.TRANSPARENT
    }

    fun draw(canvas: Canvas?) {
        canvas?.drawRect(ballHitBox, hitBoxPaint)
        canvas?.drawCircle(ballPosX, ballPosY, this.radius, paint)

    }
}