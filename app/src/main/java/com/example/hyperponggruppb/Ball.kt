package com.example.hyperponggruppb

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.*
import android.util.Log

class Ball(var context: Context) {

    var isDestroyed = false
    var ballPosX = 800f
    var ballPosY = 800f
    var paint = Paint()
    var hitboxPaint: Paint = Paint()
    var radius = 25f
    var ballSpeedX = 0f
    var ballSpeedY = 0f
    var hitBoxMargin = 18
    var canvasHeight = 0f
    var canvasWidth = 0f
    //hampus men vafaaaaaaaaaaaaannnnn
    var playerCollision = false
    var brickCollision = false
    var ballHitBox: Rect = Rect(
        (ballPosX-hitBoxMargin).toInt(), //left
        (ballPosY-hitBoxMargin).toInt(), //top
        (ballPosX+hitBoxMargin).toInt(), //right
        (ballPosY+hitBoxMargin).toInt() //bottom
    )


    fun update(player: Player) {


        ballHitBox = Rect(
            (ballPosX-hitBoxMargin).toInt(), //left
            (ballPosY-hitBoxMargin).toInt(), //top
            (ballPosX+hitBoxMargin).toInt(), //right
            (ballPosY+hitBoxMargin).toInt() //bottom
        )

        if(ballPosY >= canvasHeight ) {
            isDestroyed = true
            return
        }



        if (ballPosX + radius >= canvasWidth || ballPosX - radius <= 0f || ballPosY - radius <= 0f || playerCollision || brickCollision) {

            if (ballPosX + radius >= canvasWidth || ballPosX - radius <= 0f) {
                ballSpeedX = -ballSpeedX
            }

            if ( ballPosY - radius <= 0f || playerCollision || brickCollision) {


                ballSpeedY = -ballSpeedY


                if (playerCollision){

                    if (player.playerSize - (player.right - ballPosX) <= 0.1 * player.playerSize) { // 0% --> 10% of the pad
                        ballSpeedY = -5f
                        ballSpeedX = -15f
                    } else if (player.playerSize - (player.right - ballPosX) <= 0.2 * player.playerSize) { // 10% --> 20% of the pad
                        ballSpeedY = -7f
                        ballSpeedX = -13f
                    } else if (player.playerSize - (player.right - ballPosX) <= 0.3 * player.playerSize) { // 20% --> 30% of the pad
                        ballSpeedY = -9f
                        ballSpeedX = -11f
                    } else if (player.playerSize - (player.right - ballPosX) <= 0.4 * player.playerSize) { // 30% --> 40% of the pad
                        ballSpeedY = -11f
                        ballSpeedX = -9f
                    } else if (player.playerSize - (player.right - ballPosX) <= 0.5 * player.playerSize) { // 40% --> 50% of the pad
                        ballSpeedY = -13f
                        ballSpeedX = -7f
                    } else if (player.playerSize - (player.right - ballPosX) <= 0.6 * player.playerSize) { // 50% --> 60% of the pad
                        ballSpeedY = -13f
                        ballSpeedX = +7f
                    } else if (player.playerSize - (player.right - ballPosX) <= 0.7 * player.playerSize) { // 60% --> 70% of the pad
                        ballSpeedY = -11f
                        ballSpeedX = +9f
                    } else if (player.playerSize - (player.right - ballPosX) <= 0.8 * player.playerSize) { // 70% --> 80% of the pad
                        ballSpeedY = -9f
                        ballSpeedX = +11f
                    } else if (player.playerSize - (player.right - ballPosX) <= 0.9 * player.playerSize) { // 80% --> 90% of the pad
                        ballSpeedY = -7f
                        ballSpeedX = +13f
                    } else {       // 90 --> 100% of the pad
                        ballSpeedY = -5f
                        ballSpeedX = +15f
                    }

                }
                    /*
                    if (player.right - posX > (player.offset/2)) { // equals left pad hit**

                        if ((player.offset/2) - (posX - player.left) <= (0.2*player.offset)) { // 0% --> 20% of the left side
                            speedY = -4f
                            speedX = -12f
                        } else if ((player.offset/2) - (posX - player.left) <= (0.4*player.offset)){ // 20% --> 40% of the left side
                            speedY = -5f
                            speedX = -11f
                        } else if ((player.offset/2) - (posX - player.left) <= (0.6*player.offset)){ // 40% --> 60% of the left side
                            speedY = -6f
                            speedX = -10f
                        } else if ((player.offset/2) - (posX - player.left) <= (0.8*player.offset)){ // 60% --> 80% of the left side
                            speedY = -7f
                            speedX = -9f
                        } else {    //  80% --> 100% of the left side
                            speedY = -8f
                            speedX = -8f
                        }

                        }
                    }else { // equals right pad hit**

                    if ((player.offset/2) - (posX - player.left) <= (0.2*player.offset)) { // 0% --> 20% of the right side
                        speedY = -4f
                        speedX = -12f
                    } else if ((player.offset/2) - (posX - player.left) <= (0.4*player.offset)){ // 20% --> 40% of the right side
                        speedY = -5f
                        speedX = -11f
                    } else if ((player.offset/2) - (posX - player.left) <= (0.6*player.offset)){ // 40% --> 60% of the right side
                        speedY = -6f
                        speedX = -10f
                    } else if ((player.offset/2) - (posX - player.left) <= (0.8*player.offset)){ // 60% --> 80% of the right side
                        speedY = -7f
                        speedX = -9f
                    } else {    //  80% --> 100% of the right side
                        speedY = -8f
                        speedX = -8f
                    }
                }


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
                    }

                    else if (posX - player.left > 110 && posX - player.left < 125){
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
                    }else

                    {
                        speedY = -8f
                        speedX = 8f
                    }
                }
                    */



                if (brickCollision){
                    Log.d(TAG, "update: we are here")
                    ballPosY += 20f
                    ballPosX += 10f
                }
            }

        }


        brickCollision = false
        playerCollision = false
        ballPosY += ballSpeedY
        ballPosX += ballSpeedX


    }

    fun draw(canvas: Canvas?) {
        canvas?.drawRect(ballHitBox, hitboxPaint)
        canvas?.drawCircle(ballPosX, ballPosY, radius, paint)


    }
}