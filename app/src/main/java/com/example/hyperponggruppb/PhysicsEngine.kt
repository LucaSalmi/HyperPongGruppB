package com.example.hyperponggruppb

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.util.Log

object PhysicsEngine {

    var isCollisionDetected = false
    var brickHit = Rect()
    var canvasHeight = 0f
    var canvasWidth = 0f


    fun brickCollision(
        brickRow: MutableList<Rect>,
        brickColors: MutableList<Int>,
        ball: Ball,
        context: Context
    ) {

        var toRemove = BrickStructure.totalSumOfBricks + 1

        for (rect in brickRow) {

            if (ball.ballHitBox.intersect(rect)) {
                toRemove = brickRow.indexOf(rect)
                ball.brickCollision = true
                brickHit = rect
                SoundEffectManager.jukebox(context, 0)
            }
        }

        if (ball.brickCollision && toRemove < BrickStructure.totalSumOfBricks + 1) {
            brickRow.removeAt(toRemove)
            brickColors.removeAt(toRemove)
            PlayerManager.addPoints(10)
        }

    }

    fun playerCollision(ball: Ball, player: Player, context: Context) {

        if (ball.ballPosY < 1500f) {
            isCollisionDetected = false
        }

        if (ball.ballHitBox.intersect(player.playerRect)) {

            if (!isCollisionDetected) {
                ball.playerCollision = true
                isCollisionDetected = true
                SoundEffectManager.jukebox(context, 0)
            }
        }

    }

    fun BallPhysics(ball: Ball, player: Player) {

        ball.ballHitBox = Rect(
            (ball.ballPosX - ball.hitBoxMargin).toInt(), //left
            (ball.ballPosY - ball.hitBoxMargin).toInt(), //top
            (ball.ballPosX + ball.hitBoxMargin).toInt(), //right
            (ball.ballPosY + ball.hitBoxMargin).toInt() //bottom
        )

        if (ball.ballPosY > canvasHeight) {
            ball.isDestroyed = true
            return
        }

        if (ball.ballPosX + ball.radius >= canvasWidth || ball.ballPosX - ball.radius <= 10f || ball.ballPosY - ball.radius <= 25 || ball.playerCollision || ball.brickCollision) {

            if (ball.ballPosX + ball.radius >= canvasWidth || ball.ballPosX - ball.radius <= 10f) {

                if (ball.ballPosX > canvasWidth){

                    ball.ballPosX = canvasWidth + ball.radius
                }
                ball.ballSpeedX *= -1f //-ball.ballSpeedX
            }

            if (ball.ballPosY - ball.radius <= 25f || ball.playerCollision || ball.brickCollision) {

                if (ball.ballPosY - ball.radius <= 25f) {

                    ball.ballSpeedY *= -1f //-ballSpeedY
                }

                if (ball.brickCollision) {

                    if (ball.ballPosY + ball.radius < brickHit.bottom && ball.ballPosY - ball.radius > brickHit.top){
                        Log.d(TAG, "BallPhysics: sides")
                        ball.ballSpeedX *= -1f
                    }else{
                        Log.d(TAG, "BallPhysics: top/bottom")
                        ball.ballSpeedY *= -1f
                    }
                }


                if (ball.playerCollision) {

                    Log.d(TAG, "BallPhysics: ballX ${ball.ballPosX}, ballY ${ball.ballPosY}")
                    Log.d(TAG, "BallPhysics: ballX ${player.playerRect}")


                    if (player.playerSize - (player.right - ball.ballPosX) <= 0.1 * player.playerSize) { // 0% --> 10% of the pad
                        ball.ballSpeedY = -5f
                        ball.ballSpeedX = -15f
                    } else if (player.playerSize - (player.right - ball.ballPosX) <= 0.2 * player.playerSize) { // 10% --> 20% of the pad
                        ball.ballSpeedY = -7f
                        ball.ballSpeedX = -13f
                    } else if (player.playerSize - (player.right - ball.ballPosX) <= 0.3 * player.playerSize) { // 20% --> 30% of the pad
                        ball.ballSpeedY = -9f
                        ball.ballSpeedX = -11f
                    } else if (player.playerSize - (player.right - ball.ballPosX) <= 0.4 * player.playerSize) { // 30% --> 40% of the pad
                        ball.ballSpeedY = -11f
                        ball.ballSpeedX = -9f
                    } else if (player.playerSize - (player.right - ball.ballPosX) <= 0.5 * player.playerSize) { // 40% --> 50% of the pad
                        ball.ballSpeedY = -13f
                        ball.ballSpeedX = -7f
                    } else if (player.playerSize - (player.right - ball.ballPosX) <= 0.6 * player.playerSize) { // 50% --> 60% of the pad
                        ball.ballSpeedY = -13f
                        ball.ballSpeedX = +7f
                    } else if (player.playerSize - (player.right - ball.ballPosX) <= 0.7 * player.playerSize) { // 60% --> 70% of the pad
                        ball.ballSpeedY = -11f
                        ball.ballSpeedX = +9f
                    } else if (player.playerSize - (player.right - ball.ballPosX) <= 0.8 * player.playerSize) { // 70% --> 80% of the pad
                        ball.ballSpeedY = -9f
                        ball.ballSpeedX = +11f
                    } else if (player.playerSize - (player.right - ball.ballPosX) <= 0.9 * player.playerSize) { // 80% --> 90% of the pad
                        ball.ballSpeedY = -7f
                        ball.ballSpeedX = +13f
                    } else {       // 90 --> 100% of the pad
                        ball.ballSpeedY = -5f
                        ball.ballSpeedX = +15f
                    }
                }

            }
        }

        ball.brickCollision = false
        ball.playerCollision = false
        ball.ballPosY += ball.ballSpeedY
        ball.ballPosX += ball.ballSpeedX

    }
}