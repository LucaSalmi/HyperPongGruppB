package com.example.hyperponggruppb

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.*
import android.util.Log
import androidx.core.graphics.plus

class Ball(var context: Context) {

    var isDestroyed = false
    var ballPosX = 540f
    var ballPosY = 1638f
    var paint = Paint()
    var hitboxPaint: Paint = Paint()
    var radius = 20f
    var ballSpeedX = 0f
    var ballSpeedY = 0f
    var hitBoxMargin = 15
    var canvasHeight = 0f
    var canvasWidth = 0f

    var playerCollision = false
    var brickCollision = false
    var ballHitBox: Rect = Rect(
        (ballPosX-hitBoxMargin).toInt(), //left
        (ballPosY-hitBoxMargin).toInt(), //top
        (ballPosX+hitBoxMargin).toInt(), //right
        (ballPosY+hitBoxMargin).toInt() //bottom
    )


    fun update(player: Player) {
        PhysicsEngine.BallPhysics(this, player)

    }

    fun draw(canvas: Canvas?) {
        canvas?.drawRect(ballHitBox, hitboxPaint)
        canvas?.drawCircle(ballPosX, ballPosY, radius, paint)

    }
}