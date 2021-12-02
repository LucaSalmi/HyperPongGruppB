package com.example.hyperponggruppb

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect

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
    var collision = false
    val downLimit = 1790f
    var center = PointF()
    var ballHitbox: Rect = Rect((posX-radius).toInt(), (posY-radius).toInt(), (posX+radius).toInt(), (posY+radius).toInt())




    fun update() {

        ballHitbox = Rect((posX-radius).toInt(), (posY-radius).toInt(), (posX+radius).toInt(), (posY+radius).toInt())


        if (!collision) {

            if (posX + radius >= canvasWidth || posX - radius <= 0f || posY + radius >= canvasWidth || posY - radius <= 0f) {

                if (posX + radius >= canvasWidth || posX - radius <= 0f) {
                    speedX = -speedX
                    SoundEffectManager.playImpactSound(0, context)
                }

                if (posY + radius >= canvasHeight || posY - radius <= 0f) {
                    speedY = -speedY
                    SoundEffectManager.playImpactSound(0, context)
                }

            } else {

                speedX = +speedX
                speedY = +speedY

            }
        }else{

            speedY = -speedY

        }
        posY += speedY
        posX += speedX
        collision = false

    }

    fun draw(canvas: Canvas?) {
        canvas?.drawCircle(posX, posY, radius, paint)
        canvas?.drawRect(ballHitbox, hitboxPaint)
    }

}