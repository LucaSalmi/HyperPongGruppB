package com.example.hyperponggruppb

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint

class Ball(var context: Context) {

    var posX = 0f // 0f ursprungligen
    var posY = 0f // 0f ursprungligen
    var paint = Paint()
    var size = 25f  // 25f ursprungligen
    var speedX = 20f // 5f ursprungligen
    var speedY = 20f // 5f ursprungligen
    var canvasHeight = 0f
    var canvasWidth = 0f
    var collision = false

    fun update() {

        if (!collision) {

            if (posX + size >= canvasWidth || posX - size <= 0f || posY + size >= canvasWidth || posY - size <= 0f) {

                if (posX + size >= canvasWidth || posX - size <= 0f) {
                    speedX = -speedX
                    SoundEffectManager.playImpactSound(0, context)
                }

                if (posY + size >= canvasHeight || posY - size <= 0f) {
                    speedY = -speedY
                    SoundEffectManager.playImpactSound(0, context)
                }

            } else {

                speedX = +speedX
                speedY = +speedY

            }

        } else {

            speedY = -speedY

        }
        posY += speedY
        posX += speedX
        collision = false

    }

    fun draw(canvas: Canvas?) {
        canvas?.drawCircle(posX, posY, size, paint)
    }

}