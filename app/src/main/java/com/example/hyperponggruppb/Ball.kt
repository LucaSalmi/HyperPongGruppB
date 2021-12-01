package com.example.hyperponggruppb

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint

class Ball(context: Context) {

    var posX = 0f
    var posY = 0f
    var paint = Paint()
    var size = 25f
    var speedX = 5f
    var speedY = 5f
    var canvasHeight = 0f
    var canvasWidth = 0f

    fun update() {


        if (posX+size >= canvasWidth || posX-size <= 0f || posY+size >= canvasWidth || posY-size <= 0f) {

            if (posX+size >= canvasWidth || posX-size <= 0f){
                speedX = -speedX
            }

            if (posY+size >= canvasHeight || posY-size <= 0f){
                speedY = -speedY
            }


        } else {
            speedX = +speedX
            speedY = +speedY
        }
        posY += speedY
        posX += speedX

    }

    fun draw(canvas: Canvas?) {
        canvas?.drawCircle(posX, posY, size, paint)
    }
}