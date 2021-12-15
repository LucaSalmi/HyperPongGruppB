package com.example.hyperponggruppb

import android.content.Context
import android.graphics.*

class Ball(var context: Context, val isExtra: Boolean) {

    var ballPosX = 0f
    var ballPosY = 0f
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


    fun update(player: Player) {
        PhysicsEngine.ballPhysics(this, player)

    }

    fun draw(canvas: Canvas?) {
        canvas?.drawRect(ballHitBox, hitBoxPaint)
        canvas?.drawCircle(ballPosX, ballPosY, this.radius, paint)

    }
}