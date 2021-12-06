package com.example.hyperponggruppb

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
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
    //hampus men vafaaaaaaaaaaaaannnnn
    var playerCollision = false
    var brickCollision = false
    var brickSideCollision = false
    var ballHitbox: Rect = Rect(
        (posX-15).toInt(), //left
        (posY-15).toInt(), //top
        (posX+15).toInt(), //right
        (posY+15).toInt() //bottom
    )


    fun update(player: Player) {

        ballHitbox = Rect(
            (posX-15).toInt(), //left
            (posY-15).toInt(), //top
            (posX+15).toInt(), //right
            (posY+15).toInt() //bottom
        )



        if (posX + radius >= canvasWidth || posX - radius <= 0f || posY + radius >= canvasWidth || posY - radius <= 0f || playerCollision || brickCollision) {

            if (posX + radius >= canvasWidth || posX - radius <= 0f) {
                speedX = -speedX
                SoundEffectManager.playImpactSound(0, context)
            }

            if ( posY - radius <= 0f || posY + radius >= canvasHeight  || playerCollision || brickCollision) {


                speedY = -speedY

                if (playerCollision){

                    if (player.right - posX > 130){
                        speedY = -7f
                        speedX = -9f
                    }else if (posX - player.left > 130){
                        speedY = -7f
                        speedX = +9f
                    }else{
                        speedY = -16f
                        speedX = 0f
                    }

                }



                SoundEffectManager.playImpactSound(0, context)
            }


        }

        brickCollision = false
        playerCollision = false
        brickSideCollision = false
        posY += speedY
        posX += speedX

    }

    fun draw(canvas: Canvas?) {
        canvas?.drawRect(ballHitbox, hitboxPaint)
        canvas?.drawCircle(posX, posY, radius, paint)

    }
}