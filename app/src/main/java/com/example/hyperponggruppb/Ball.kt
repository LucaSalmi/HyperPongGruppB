package com.example.hyperponggruppb

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.util.Log

class Ball(var context: Context) {

    var posX = 0f
    var posY = 0f
    var paint = Paint()
    var hitboxPaint: Paint = Paint()
    var radius = 25f
    var speedX = 0f
    var speedY = 0f
    var canvasHeight = 0f
    var canvasWidth = 0f
    val downLimit = 1790f
    var collision = false
    var center = PointF()
    var ballHitbox: Rect = Rect(
        (posX - radius).toInt(),
        (posY - radius).toInt(),
        (posX + radius).toInt(),
        (posY + radius).toInt()
    )


    fun update() {

        ballHitbox = Rect(
            (posX - 5f - radius).toInt(), //left
            (posY - 5f - radius).toInt(), //top
            (posX + 5f + radius).toInt(), //right
            (posY + 5f + radius).toInt() //bottom
        )



        if (posX + radius >= canvasWidth || posX - radius <= 0f || posY + radius >= canvasWidth || posY - radius <= 0f || collision) {

            if (posX + radius >= canvasWidth || posX - radius <= 0f) {
                speedX = -speedX
                SoundEffectManager.playImpactSound(0, context)
            }

            if (posY + radius >= canvasHeight || posY - radius <= 0f || collision) {
                speedY = -speedY
                SoundEffectManager.playImpactSound(0, context)
            }


        }


        Log.d(TAG, "update: speedX = $speedX, speedY = $speedY")
            collision = false
            posY += speedY
            posX += speedX





    }

    fun draw(canvas: Canvas?) {
        canvas?.drawRect(ballHitbox, paint)
        canvas?.drawCircle(posX, posY, radius, paint)

    }
}