package com.example.hyperponggruppb

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.*
import android.util.Log

class Ball(var context: Context) {

    var isDestroyed = false
    var posX = 800f
    var posY = 800f
    var paint = Paint()
    var hitboxPaint: Paint = Paint()
    var radius = 25f
    var speedX = 0f
    var speedY = 0f
    var hitBoxMargin = 18
    var canvasHeight = 0f
    var canvasWidth = 0f
    //hampus men vafaaaaaaaaaaaaannnnn
    var playerCollision = false
    var brickCollision = false
    var ballHitBox: Rect = Rect(
        (posX-hitBoxMargin).toInt(), //left
        (posY-hitBoxMargin).toInt(), //top
        (posX+hitBoxMargin).toInt(), //right
        (posY+hitBoxMargin).toInt() //bottom
    )


    fun update(player: Player) {

        ballHitBox = Rect(
            (posX-hitBoxMargin).toInt(), //left
            (posY-hitBoxMargin).toInt(), //top
            (posX+hitBoxMargin).toInt(), //right
            (posY+hitBoxMargin).toInt() //bottom
        )

        if(posY >= canvasHeight ) {
            isDestroyed = true
            return
        }



        if (posX + radius >= canvasWidth || posX - radius <= 0f || posY - radius <= 0f || playerCollision || brickCollision) {

            if (posX + radius >= canvasWidth || posX - radius <= 0f) {
                speedX = -speedX
            }

            if ( posY - radius <= 0f || playerCollision || brickCollision) {


                speedY = -speedY


                if (playerCollision){

                    if (player.right - posX > 110 && player.right - posX < 125){
                        speedY = -7.5f
                        speedX = -8.5f
                    }else if (player.right - posX > 125 && player.right - posX < 140){
                        speedY = -7f
                        speedX = -9f
                    }else if (player.right - posX > 140 && player.right - posX < 155){
                        speedY = -6.5f
                        speedX = -9.5f
                    }else if (player.right - posX > 155 && player.right - posX < 170){
                        speedY = -6f
                        speedX = -10f
                    }else if (player.right - posX > 170 && player.right - posX < 185){
                        speedY = -5.5f
                        speedX = -10.5f
                    }else if (player.right - posX > 185 && player.right - posX < 201){
                        speedY = -5f
                        speedX = -11f
                    }else if (posX - player.left > 110 && posX - player.left < 125){
                        speedY = -7.5f
                        speedX = +8.5f
                    }else if (posX - player.left > 125 && posX - player.left < 140){
                        speedY = -7f
                        speedX = +9f
                    }else if (posX - player.left > 140 && posX - player.left < 155){
                        speedY = -6.5f
                        speedX = +9.5f
                    }else if (posX - player.left > 155 && posX - player.left < 170){
                        speedY = -6f
                        speedX = +10f
                    }else if (posX - player.left > 170 && posX - player.left < 185){
                        speedY = -5.5f
                        speedX = +10.5f
                    }else if (posX - player.left > 185 && posX - player.left < 201){
                        speedY = -4f
                        speedX = +11f
                    }else{
                        speedY = -8f
                        speedX = 8f
                    }

                }

                if (brickCollision){
                    Log.d(TAG, "update: we are here")
                    posY += 20f
                    posX += 10f
                }
            }

        }


        brickCollision = false
        playerCollision = false
        posY += speedY
        posX += speedX


    }

    fun draw(canvas: Canvas?) {
        canvas?.drawRect(ballHitBox, hitboxPaint)
        canvas?.drawCircle(posX, posY, radius, paint)


    }
}