package com.example.hyperponggruppb.controller

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.util.Log
import com.example.hyperponggruppb.model.AssetManager
import com.example.hyperponggruppb.model.Ball
import com.example.hyperponggruppb.model.GameManager
import com.example.hyperponggruppb.model.RandomNumberGenerator

object PhysicsEngine {

    private var isCollisionDetected = false
    private var brickHit = Rect()
    var testBrick = Rect()
    var canvasHeight = 1977f
    var canvasWidth = 1080f
    var gameStart = false
    var damageTaken = false
    var ballToEliminate = 0
    lateinit var powerUp: PowerUp
    var isLeftOccupied = false
    var isRightOccupied = false
    var isTopOccupied = false
    var isBottomOccupied = false
    var isDone = false


    fun brickCollision(
        brickRow: MutableList<Rect>,
        brickAssets: MutableList<Bitmap>,
        ball: Ball,
        powerUpArray: MutableList<PowerUp>,
        context: Context,
        gameManager: GameManager
    ) {

        var toRemove = BrickStructure.totalSumOfBricks + 1

        for (rect in brickRow) {

            if (ball.ballRect.intersect(rect)) {

                toRemove = brickRow.indexOf(rect)
                ball.brickCollision = true
                brickHit = rect
                SoundEffectManager.jukebox(context, 0)
            }
        }


        if (ball.brickCollision){

            for (obj in brickRow){

                testBrick = Rect(brickHit.left-30, brickHit.top, brickHit.left-10, brickHit.bottom)
                if (testBrick.intersect(obj)){
                    isLeftOccupied = true
                }

                testBrick = Rect(brickHit.right+10, brickHit.top, brickHit.right+30, brickHit.bottom)
                if (testBrick.intersect(obj)){
                    isRightOccupied = true
                }

                testBrick = Rect(brickHit.left, brickHit.top-30, brickHit.right, brickHit.top-10)
                if (testBrick.intersect(obj)){
                    isTopOccupied = true
                }

                testBrick = Rect(brickHit.left, brickHit.bottom+10, brickHit.right, brickHit.bottom+30)
                if (testBrick.intersect(obj)){
                    isBottomOccupied = true
                }
            }

            testBrick = Rect(0,AssetManager.getScreenHeight()-20 ,15,AssetManager.getScreenHeight())

        }


        if (ball.brickCollision && !gameManager.isStoryMode) {

            if (RandomNumberGenerator.rNG(1, 8) == 2) {

                var rngLimit = if (PlayerManager.lives >= 3) {
                    4
                } else {
                    5
                }
                powerUp = PowerUp(
                    RandomNumberGenerator.rNG(0, rngLimit),
                    brickHit.left,
                    brickHit.top,
                    brickHit.right,
                    brickHit.bottom
                )
                powerUpArray.add(powerUp)
            }
        }

        if (ball.brickCollision && toRemove < BrickStructure.totalSumOfBricks + 1) {
            brickRow.removeAt(toRemove)
            brickAssets.removeAt(toRemove)
            PlayerManager.addPoints(10)
        }
    }


    fun playerCollision(ball: Ball, player: Player, context: Context) {

        if (ball.ballTop < 1500) {
            isCollisionDetected = false
        }

        if (ball.ballRect.intersect(player.playerRect)) {

            if (!isCollisionDetected) {
                ball.playerCollision = true
                isCollisionDetected = true
                SoundEffectManager.jukebox(context, 0)
            }
        }
    }

    fun ballPhysics(ballsArray: MutableList<Ball>, player: Player) {

        for (ball in ballsArray) {

            var ballIsBottomOfScreen = ball.ballTop > canvasHeight
            var ballIsOutsideTopOfScreen = ball.ballBottom < 0f
            var ballIsOutsideRightOfScreen = ball.ballRight >= canvasWidth
            var ballIsOutsideLeftOfScreen = ball.ballLeft < 0f

            ball.ballRect = Rect(ball.ballLeft, ball.ballTop, ball.ballRight, ball.ballBottom)

            if (ballIsBottomOfScreen && gameStart && !damageTaken) {

                damageTaken = true
                ballToEliminate = ballsArray.indexOf(ball)

            }

            if (ballIsOutsideRightOfScreen || ballIsOutsideLeftOfScreen || ballIsOutsideTopOfScreen || ball.playerCollision || ball.brickCollision) {

                if (ballIsOutsideRightOfScreen || ballIsOutsideLeftOfScreen) {

                    if (ballIsOutsideRightOfScreen) {

                        ball.ballLeft = (canvasWidth - ball.ballsize).toInt()
                        ball.ballRight = canvasWidth.toInt()

                    }
                    if (ballIsOutsideLeftOfScreen) {

                        ball.ballLeft = 0
                        ball.ballRight = ball.ballsize.toInt()
                    }

                    ball.ballSpeedX *= -1f //-ball.ballSpeedX
                }

                if (ballIsOutsideTopOfScreen || ball.playerCollision || ball.brickCollision) {

                    if (ballIsOutsideTopOfScreen) {

                        ball.ballTop = 0
                        ball.ballBottom = ball.ballsize.toInt()

                        ball.ballSpeedY *= -1f //-ballSpeedY
                    }

                    if (ball.brickCollision) { // kollar om bollen har kolliderat med en brick.

                        var ballLeftIsInsideOfBrick = ball.ballLeft + ball.ballsize/2 > brickHit.left
                        var ballRightIsInsideOfBrick = ball.ballRight - ball.ballsize/2 < brickHit.right

                        var ballIsOutsideOfBrickTop = ball.ballTop + ball.ballsize < brickHit.top
                        var ballIsOutsideOfBrickTBottom = ball.ballBottom - ball.ballsize > brickHit.bottom

                        if (!ball.ballGoesRight()) {
                            ball.ballLeft -= ball.ballSpeedX.toInt()
                            ball.ballRight -= ball.ballSpeedX.toInt()

                        }else {
                            ball.ballLeft += ball.ballSpeedX.toInt()
                            ball.ballRight += ball.ballSpeedX.toInt()

                        }
                        if (!ball.ballGoesDown()) {
                            ball.ballTop -= ball.ballSpeedY.toInt()
                            ball.ballBottom -= ball.ballSpeedY.toInt()

                        } else{
                            ball.ballTop += ball.ballSpeedY.toInt()
                            ball.ballBottom += ball.ballSpeedY.toInt()

                        }

                        Log.d(TAG, "top: $isTopOccupied, bottom: $isBottomOccupied, left: $isLeftOccupied, right: $isRightOccupied")
                        Log.d(TAG, "ballspeed Y = ${ball.ballSpeedY} ")
                        Log.d(TAG, "ballspeed X = ${ball.ballSpeedX} ")

                        if (!ball.ballGoesRight() && !ball.ballGoesDown()) {

                            if (isBottomOccupied) {
                                Log.d(TAG, "ballPhysics: right hit 1")
                                ball.ballSpeedX *= -1
                                ball.ballLeft = brickHit.right
                                ball.ballRight = brickHit.right + ball.ballsize.toInt()

                            } else if (isRightOccupied) {
                                Log.d(TAG, "ballPhysics: bot hit 1")
                                ball.ballSpeedY *= -1
                                ball.ballTop = brickHit.bottom
                                ball.ballBottom = brickHit.bottom + ball.ballsize.toInt()

                            } else if (ballLeftIsInsideOfBrick && ballRightIsInsideOfBrick) {
                                // ball is inside of brick's sides & outside of brick top n Bottom
                                Log.d(TAG, "ballPhysics: bot hit 1 - v2")
                                ball.ballSpeedY *= -1
                                ball.ballTop = brickHit.bottom
                                ball.ballBottom = brickHit.bottom + ball.ballsize.toInt()

                            } else {                                             // ball is outside of brick's sides
                                Log.d(TAG, "ballPhysics: right hit 1 - v2")
                                ball.ballSpeedX *= -1
                                ball.ballLeft = brickHit.right
                                ball.ballRight = brickHit.right + ball.ballsize.toInt()
                            }

                        }else if (ball.ballGoesRight() && !ball.ballGoesDown()) {

                            if (isBottomOccupied) {
                                Log.d(TAG, "ballPhysics: left hit 1")
                                ball.ballSpeedX *= -1
                                ball.ballRight = brickHit.left
                                ball.ballLeft = brickHit.left - ball.ballsize.toInt()

                            } else if (isLeftOccupied) {
                                Log.d(TAG, "ballPhysics: bot hit 2")
                                ball.ballSpeedY *= -1
                                ball.ballTop = brickHit.bottom
                                ball.ballBottom = brickHit.bottom + ball.ballsize.toInt()

                            } else if (ballLeftIsInsideOfBrick && ballRightIsInsideOfBrick) {
                                // ball is inside of brick's sides & outside of brick top n Bottom
                                Log.d(TAG, "ballPhysics: bot hit 2 - v2")
                                ball.ballSpeedY *= -1
                                ball.ballTop = brickHit.bottom
                                ball.ballBottom = brickHit.bottom + ball.ballsize.toInt()

                            } else {                                             // ball is outside of brick's sides
                                Log.d(TAG, "ballPhysics: left hit 1 - v2")
                                ball.ballSpeedX *= -1
                                ball.ballRight = brickHit.left
                                ball.ballLeft = brickHit.left - ball.ballsize.toInt()
                            }

                        }else if (!ball.ballGoesRight() && ball.ballGoesDown()) {

                                if (isTopOccupied) {
                                    Log.d(TAG, "ballPhysics: right hit 2")
                                    ball.ballSpeedX *= -1
                                    ball.ballLeft = brickHit.right
                                    ball.ballRight = brickHit.right + ball.ballsize.toInt()

                                } else if (isRightOccupied) {
                                    Log.d(TAG, "ballPhysics: top hit 1")
                                    ball.ballSpeedY *= -1
                                    ball.ballBottom = brickHit.top
                                    ball.ballTop = brickHit.top - ball.ballsize.toInt()

                                } else if (ballLeftIsInsideOfBrick && ballRightIsInsideOfBrick) {
                                    // ball is inside of brick's sides & outside of brick top n Bottom
                                    Log.d(TAG, "ballPhysics: top hit 1 - v2")
                                    ball.ballSpeedY *= -1
                                    ball.ballBottom = brickHit.top
                                    ball.ballTop = brickHit.top - ball.ballsize.toInt()

                                } else {                                             // ball is outside of brick's sides
                                    Log.d(TAG, "ballPhysics: right hit 2 - v2")
                                    ball.ballSpeedX *= -1
                                    ball.ballLeft = brickHit.right
                                    ball.ballRight = brickHit.right + ball.ballsize.toInt()
                                }

                            }else if (ball.ballGoesRight() && ball.ballGoesDown()) {

                                    if (isTopOccupied) {
                                        Log.d(TAG, "ballPhysics: right hit 2")
                                        ball.ballSpeedX *= -1
                                        ball.ballRight = brickHit.right
                                        ball.ballLeft = brickHit.right - ball.ballsize.toInt()

                                    } else if (isLeftOccupied) {
                                        Log.d(TAG, "ballPhysics: top hit 2")
                                        ball.ballSpeedY *= -1
                                        ball.ballBottom = brickHit.top
                                        ball.ballTop = brickHit.top - ball.ballsize.toInt()

                                    } else if (ballLeftIsInsideOfBrick && ballRightIsInsideOfBrick) {
                                        // ball is inside of brick's sides & outside of brick top n Bottom
                                        Log.d(TAG, "ballPhysics: top hit 2 - v2")
                                        ball.ballSpeedY *= -1
                                        ball.ballBottom = brickHit.top
                                        ball.ballTop = brickHit.top - ball.ballsize.toInt()

                                    } else {                                             // ball is outside of brick's sides
                                        Log.d(TAG, "ballPhysics: right hit 2 - v2")
                                        ball.ballSpeedX *= -1
                                        ball.ballRight = brickHit.right
                                        ball.ballLeft = brickHit.right - ball.ballsize.toInt()
                                    }
                                }

                        isRightOccupied = false
                        isLeftOccupied = false
                        isBottomOccupied = false
                        isTopOccupied = false
                        isDone = false

                        Log.d(TAG, "ballspeed Y efter upd = ${ball.ballSpeedY} ")
                        Log.d(TAG, "ballspeed X efter upd = ${ball.ballSpeedX} ")


                    }

                    if (ball.playerCollision) {

                        when {
                            player.playerWidth - (player.right - (ball.ballRight - ball.ballsize / 2)) <= 0.1 * player.playerWidth -> { // 0% --> 10% of the pad

                                ball.ballSpeedY = -5f //  1  - 3
                                ball.ballSpeedX = -15f // 3  - 9
                            }
                            player.playerWidth - (player.right - (ball.ballRight - ball.ballsize / 2)) <= 0.2 * player.playerWidth -> { // 10% --> 20% of the pad
                                ball.ballSpeedY = -7f // 1 -    4,8f
                                ball.ballSpeedX = -13f// 1,86 - 9f
                            }
                            player.playerWidth - (player.right - (ball.ballRight - ball.ballsize / 2)) <= 0.3 * player.playerWidth -> { // 20% --> 30% of the pad
                                ball.ballSpeedY = -9f // 1 -    7,4f
                                ball.ballSpeedX = -11f // 1,22 - 9f
                            }
                            player.playerWidth - (player.right - (ball.ballRight - ball.ballsize / 2)) <= 0.4 * player.playerWidth -> { // 30% --> 40% of the pad
                                ball.ballSpeedY = -11f //1,22 - 11f
                                ball.ballSpeedX = -9f // 1 - 9f
                            }
                            player.playerWidth - (player.right - (ball.ballRight - ball.ballsize / 2)) <= 0.5 * player.playerWidth -> { // 40% --> 50% of the pad
                                ball.ballSpeedY = -13f //  1,86 - 16,74f
                                ball.ballSpeedX = -7f // 1 - 9f
                            }
                            player.playerWidth - (player.right - (ball.ballRight - ball.ballsize / 2)) <= 0.6 * player.playerWidth -> { // 50% --> 60% of the pad
                                ball.ballSpeedY = -13f
                                ball.ballSpeedX = +7f
                            }
                            player.playerWidth - (player.right - (ball.ballRight - ball.ballsize / 2)) <= 0.7 * player.playerWidth -> { // 60% --> 70% of the pad
                                ball.ballSpeedY = -11f
                                ball.ballSpeedX = +9f
                            }
                            player.playerWidth - (player.right - (ball.ballRight - ball.ballsize / 2)) <= 0.8 * player.playerWidth -> { // 70% --> 80% of the pad
                                ball.ballSpeedY = -9f
                                ball.ballSpeedX = +11f
                            }
                            player.playerWidth - (player.right - (ball.ballRight - ball.ballsize / 2)) <= 0.9 * player.playerWidth -> { // 80% --> 90% of the pad
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
            isDone = false
            ball.ballTop += ball.ballSpeedY.toInt()
            ball.ballBottom += ball.ballSpeedY.toInt()
            ball.ballLeft += ball.ballSpeedX.toInt()
            ball.ballRight += ball.ballSpeedX.toInt()
        }

        if (damageTaken && ballsArray.size > 1) {

            ballsArray.removeAt(ballToEliminate)
            damageTaken = false
        }
    }

    fun brickDeathZone(brickRow: MutableList<Rect>): Boolean {

        for (rect in brickRow) {

            if (rect.bottom > (1200f)) {

                return true
            }
        }
        return false
    }

    fun powerUpPhysics(powerUpArray: MutableList<PowerUp>, player: Player) {

        for (powerUp in powerUpArray) {

            powerUp.update()

            if (powerUp.powerUpRect.intersect(player.playerRect)) {
                powerUp.isCatched = true
            }

            if (powerUp.powerUpRect.bottom >= canvasHeight) {

                powerUp.isToDestroy = true
            }
        }
    }
}