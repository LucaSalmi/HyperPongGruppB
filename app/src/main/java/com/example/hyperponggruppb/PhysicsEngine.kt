package com.example.hyperponggruppb

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.util.Log

object PhysicsEngine {

    var isCollisionDetected = false
    var brickHit = Rect()
    var canvasHeight = 1977f
    var canvasWidth = 1080f
    var gameStart = false
    var damageTaken = false
    var isPowerUpLive = false
    var isPowerUpCatch = false
    lateinit var powerUp: PowerUp


    fun brickCollision(
        brickRow: MutableList<Rect>,
        brickAssets: MutableList<Bitmap>,
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

        if (ball.brickCollision && !isPowerUpLive){

            if (RandomNumberGenerator.rNG(1,6) %2 == 0){

                powerUp = PowerUp(RandomNumberGenerator.rNG(0,3), brickHit.left, brickHit.top, brickHit.right, brickHit.bottom)
                isPowerUpLive = true

            }
        }

        if (ball.brickCollision && toRemove < BrickStructure.totalSumOfBricks + 1) {
            brickRow.removeAt(toRemove)
            brickAssets.removeAt(toRemove)
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

    fun ballPhysics(ball: Ball, player: Player) {

        ball.ballHitBox = Rect(
            (ball.ballPosX - ball.hitBoxMargin).toInt(), //left
            (ball.ballPosY - ball.hitBoxMargin).toInt(), //top
            (ball.ballPosX + ball.hitBoxMargin).toInt(), //right
            (ball.ballPosY + ball.hitBoxMargin).toInt() //bottom
        )

        if (ball.ballPosY - ball.radius > canvasHeight && gameStart && !damageTaken) {
            damageTaken = true
            PlayerManager.loseLife()
            Log.d(TAG, "BallPhysics: ${PlayerManager.lives}")
            return

        }

        if (ball.ballPosX + ball.radius >= canvasWidth || ball.ballPosX - ball.radius <= 10f || ball.ballPosY - ball.radius <= 20f || ball.playerCollision || ball.brickCollision) {

            if (ball.ballPosX + ball.radius >= canvasWidth || ball.ballPosX - ball.radius <= 10f) {

                if (ball.ballPosX > canvasWidth) {

                    ball.ballPosX = canvasWidth + ball.radius
                }
                ball.ballSpeedX *= -1f //-ball.ballSpeedX
            }

            if (ball.ballPosY - ball.radius <= 20f || ball.playerCollision || ball.brickCollision) {

                if (ball.ballPosY - ball.radius <= 20f) {

                    ball.ballSpeedY *= -1f //-ballSpeedY
                }

                if (ball.brickCollision) {

                    if (ball.ballPosY + 10f < brickHit.bottom && ball.ballPosY - 10f > brickHit.top) {
                        Log.d(TAG, "BallPhysics: sides")
                        if (ball.ballSpeedX <= 0){
                            ball.ballPosX += 9f
                            if (ball.ballSpeedX < -13){
                                ball.ballPosY += 3f
                            } else if (ball.ballSpeedX < -11){
                                ball.ballPosY += 4.8f
                            } else if (ball.ballSpeedX < -9){
                                ball.ballPosY += 7.4f
                            } else if (ball.ballSpeedX < -7){
                                ball.ballPosY += 11f
                            } else {
                                ball.ballPosY += 16.7f
                            }
                        }
                        else{
                            ball.ballPosX -=9f

                            if (ball.ballSpeedX > 13){
                                ball.ballPosY -= 3f
                            } else if (ball.ballSpeedX > 11){
                                ball.ballPosY -= 4.8f
                            } else if (ball.ballSpeedX > 9){
                                ball.ballPosY -= 7.4f
                            } else if (ball.ballSpeedX > 7){
                                ball.ballPosY -= 11f
                            } else {
                                ball.ballPosY -= 16.7f
                            }
                        }
                        ball.ballSpeedX *= -1f
                    } else {
                        Log.d(TAG, "BallPhysics: top/bottom")
                        if (ball.ballSpeedY <= 0){
                            ball.ballPosY += 9f
                            if (ball.ballSpeedY < -13){
                                ball.ballPosX += 3f
                            } else if (ball.ballSpeedY < -11){
                                ball.ballPosX += 4.8f
                            } else if (ball.ballSpeedY < -9){
                                ball.ballPosX += 7.4f
                            } else if (ball.ballSpeedY < -7){
                                ball.ballPosX += 11f
                            } else {
                                ball.ballPosX += 16.7f
                            }
                        }
                        else{
                            ball.ballPosY -= 9f

                            if (ball.ballSpeedY > 13){
                                ball.ballPosX -= 3f
                            } else if (ball.ballSpeedY > 11){
                                ball.ballPosX -= 4.8f
                            } else if (ball.ballSpeedY > 9){
                                ball.ballPosX -= 7.4f
                            } else if (ball.ballSpeedY > 7){
                                ball.ballPosX -= 11f
                            } else {
                                ball.ballPosX -= 16.7f
                            }
                        }
                        ball.ballSpeedY *= -1f
                    }
                }

                if (ball.playerCollision) {

                    when {
                        player.playerWidth - (player.right - ball.ballPosX) <= 0.1 * player.playerWidth -> { // 0% --> 10% of the pad
                            ball.ballSpeedY = -5f //  1  - 3
                            ball.ballSpeedX = -15f // 3  - 9
                        }
                        player.playerWidth - (player.right - ball.ballPosX) <= 0.2 * player.playerWidth -> { // 10% --> 20% of the pad
                            ball.ballSpeedY = -7f // 1 -    4,8f
                            ball.ballSpeedX = -13f// 1,86 - 9f
                        }
                        player.playerWidth - (player.right - ball.ballPosX) <= 0.3 * player.playerWidth -> { // 20% --> 30% of the pad
                            ball.ballSpeedY = -9f // 1 -    7,4f
                            ball.ballSpeedX = -11f // 1,22 - 9f
                        }
                        player.playerWidth - (player.right - ball.ballPosX) <= 0.4 * player.playerWidth -> { // 30% --> 40% of the pad
                            ball.ballSpeedY = -11f //1,22 - 11f
                            ball.ballSpeedX = -9f // 1 - 9f
                        }
                        player.playerWidth - (player.right - ball.ballPosX) <= 0.5 * player.playerWidth -> { // 40% --> 50% of the pad
                            ball.ballSpeedY = -13f //  1,86 - 16,74f
                            ball.ballSpeedX = -7f // 1 - 9f
                        }
                        player.playerWidth - (player.right - ball.ballPosX) <= 0.6 * player.playerWidth -> { // 50% --> 60% of the pad
                            ball.ballSpeedY = -13f
                            ball.ballSpeedX = +7f
                        }
                        player.playerWidth - (player.right - ball.ballPosX) <= 0.7 * player.playerWidth -> { // 60% --> 70% of the pad
                            ball.ballSpeedY = -11f
                            ball.ballSpeedX = +9f
                        }
                        player.playerWidth - (player.right - ball.ballPosX) <= 0.8 * player.playerWidth -> { // 70% --> 80% of the pad
                            ball.ballSpeedY = -9f
                            ball.ballSpeedX = +11f
                        }
                        player.playerWidth - (player.right - ball.ballPosX) <= 0.9 * player.playerWidth -> { // 80% --> 90% of the pad
                            ball.ballSpeedY = -7f
                            ball.ballSpeedX = +13f
                        }
                        else -> { // 90 --> 100% of the pad
                            ball.ballSpeedY = -5f
                            ball.ballSpeedX = +15f
                        }
                    }
                }

            }
        }

        ball.brickCollision = false
        ball.playerCollision = false
        ball.ballPosY += ball.ballSpeedY
        ball.ballPosX += ball.ballSpeedX

    }

    fun brickDeathZone(brickRow: MutableList<Rect>): Boolean {

        for (rect in brickRow) {

            if (rect.bottom > (canvasHeight / 2)) {

                return true
            }
        }
        return false
    }

    fun powerUpPhysics(player: Player){

        if (isPowerUpLive){

            powerUp.update()

            if (powerUp.powerUpRect.intersect(player.playerRect)){
                isPowerUpCatch = true
            }

            if (powerUp.powerUpRect.bottom >= canvasHeight && !isPowerUpCatch){

                isPowerUpLive = false
            }
        }
    }
}