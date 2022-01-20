package com.example.hyperponggruppb.controller

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Rect
import android.util.Log
import com.example.hyperponggruppb.model.*

object PsyduckEngine {

    private var isCollisionDetected = false
    private var brickHit = Rect()
    private lateinit var infoBrick: Bricks
    var testBrick = Rect()
    var canvasHeight = AssetManager.getScreenHeight()
    var canvasWidth = AssetManager.getScreenWidth()
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
        brickRow: MutableList<Bricks>,
        ball: Ball,
        powerUpArray: MutableList<PowerUp>,
        context: Context,
        gameManager: GameManager
    ) {

        var toRemove = BrickStructure.totalSumOfBricks + 1

        for (brick in brickRow) {

            var brickRect =
                Rect(brick.brickLeft, brick.brickTop, brick.brickRight, brick.brickBottom)

            if (ball.ballRect.intersect(brickRect)) {

                toRemove = brickRow.indexOf(brick)
                ball.brickCollision = true
                brickHit = brickRect

                SoundEffectManager.playBrickHitSound(context, RandomNumberGenerator.rNG(0, 2))
            }
        }

        if (ball.brickCollision) {

            PlayerManager.comboPoints++

            if (PlayerManager.comboPoints == 5 || PlayerManager.comboPoints == 10) {
                SoundEffectManager.playComboAnnouncer(context)
                PlayerManager.textIsOn = true
            }

            for (brick in brickRow) {

                var brickRect =
                    Rect(brick.brickLeft, brick.brickTop, brick.brickRight, brick.brickBottom)

                testBrick =
                    Rect(brickHit.left - 30, brickHit.top, brickHit.left - 10, brickHit.bottom)
                if (testBrick.intersect(brickRect)) {
                    isLeftOccupied = true
                }

                testBrick =
                    Rect(brickHit.right + 10, brickHit.top, brickHit.right + 30, brickHit.bottom)
                if (testBrick.intersect(brickRect)) {
                    isRightOccupied = true
                }

                testBrick =
                    Rect(brickHit.left, brickHit.top - 30, brickHit.right, brickHit.top - 10)
                if (testBrick.intersect(brickRect)) {
                    isTopOccupied = true
                }

                testBrick =
                    Rect(brickHit.left, brickHit.bottom + 10, brickHit.right, brickHit.bottom + 30)
                if (testBrick.intersect(brickRect)) {
                    isBottomOccupied = true
                }
            }

            testBrick =
                Rect(0, AssetManager.getScreenHeight() - 20, 15, AssetManager.getScreenHeight())

        }

        if (toRemove < BrickStructure.totalSumOfBricks) {

            infoBrick = brickRow[toRemove]
        }

        if (ball.brickCollision && !gameManager.isStoryMode) {

            infiniteModePowerUpSpawn(powerUpArray)

        } else if (ball.brickCollision && gameManager.isStoryMode && infoBrick.hasPowerUp) {

            storyModePowerUpSpawn(powerUpArray)
        }

        if (ball.brickCollision && toRemove < BrickStructure.totalSumOfBricks + 1) {
            brickRow.removeAt(toRemove)
            PlayerManager.addPoints(BrickStructure.brickScoreValue)
        }
    }

    private fun storyModePowerUpSpawn(powerUpArray: MutableList<PowerUp>) {

        powerUp = PowerUp(
            //RandomNumberGenerator.rNG(2, 7),
            10,
            brickHit.left,
            brickHit.top,
            brickHit.right,
            brickHit.bottom
        )
        powerUpArray.add(powerUp)

    }

    private fun infiniteModePowerUpSpawn(powerUpArray: MutableList<PowerUp>) {

        if (RandomNumberGenerator.rNG(1, 8) == 2) {

            val rngLimit = if (PlayerManager.lives >= 3) {
                7
            } else {
                8
            }
            powerUp = PowerUp(

                //RandomNumberGenerator.rNG(0, rngLimit),
                5,
                brickHit.left,
                brickHit.top,
                brickHit.right,
                brickHit.bottom
            )
            powerUpArray.add(powerUp)
        }
    }


    fun playerCollision(ball: Ball, player: Player, context: Context) {

        if (ball.ballRect.intersect(player.playerRect)) {

            PlayerManager.comboPoints = 0
            ball.playerCollision = true
            isCollisionDetected = true
            SoundEffectManager.jukebox(context, 0)

        }
    }

    fun ballPhysics(ballsArray: MutableList<Ball>, player: Player, gameManager: GameManager) {

        for (ball in ballsArray) {

            var ballIsBottomOfScreen = if (gameManager.isShieldActive) {
                ball.ballBottom >= player.bottom
            } else {
                ball.ballBottom >= canvasHeight
            }
            val ballIsOutsideTopOfScreen = ball.ballBottom < 0f
            val ballIsOutsideRightOfScreen = ball.ballRight >= canvasWidth
            val ballIsOutsideLeftOfScreen = ball.ballLeft < 0f

            ball.ballRect = Rect(ball.ballLeft, ball.ballTop, ball.ballRight, ball.ballBottom)

            if (ballIsBottomOfScreen && gameStart && !damageTaken) {

                if (!gameManager.isShieldActive) {

                    damageTaken = true
                    ballToEliminate = ballsArray.indexOf(ball)

                } else {

                    gameManager.isShieldActive = false
                    ball.ballBottom = player.bottom.toInt()
                    ball.ballTop = ball.ballBottom - ball.ballsize.toInt()
                    ball.ballSpeedY *= -1f //-ballSpeedY

                }
            }

            if (ballIsOutsideRightOfScreen || ballIsOutsideLeftOfScreen || ballIsOutsideTopOfScreen || ball.playerCollision || ball.brickCollision) {

                if (ballIsOutsideRightOfScreen || ballIsOutsideLeftOfScreen) {

                    if (ballIsOutsideRightOfScreen) {

                        ball.ballLeft = (canvasWidth - ball.ballsize).toInt()
                        ball.ballRight = canvasWidth

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

                        //val ballLeftIsInsideOfBrick = ball.ballLeft + ball.ballsize/2 > brickHit.left
                        //val ballRightIsInsideOfBrick = ball.ballRight - ball.ballsize/2 < brickHit.right

                        //var ballIsOutsideOfBrickTop = ball.ballTop + ball.ballsize < brickHit.top
                        //var ballIsOutsideOfBrickTBottom = ball.ballBottom - ball.ballsize > brickHit.bottom

                        if (!ball.ballGoesRight()) {
                            ball.ballLeft -= ball.ballSpeedX.toInt()
                            ball.ballRight -= ball.ballSpeedX.toInt()

                        } else {
                            ball.ballLeft -= ball.ballSpeedX.toInt()
                            ball.ballRight -= ball.ballSpeedX.toInt()

                        }
                        if (!ball.ballGoesDown()) {
                            ball.ballTop -= ball.ballSpeedY.toInt()
                            ball.ballBottom -= ball.ballSpeedY.toInt()

                        } else {
                            ball.ballTop -= ball.ballSpeedY.toInt()
                            ball.ballBottom -= ball.ballSpeedY.toInt()

                        }

                        // Log.d(TAG,"top: $isTopOccupied, bottom: $isBottomOccupied, left: $isLeftOccupied, right: $isRightOccupied" )
                        // Log.d(TAG, "ballspeed Y = ${ball.ballSpeedY} ")
                        // Log.d(TAG, "ballspeed X = ${ball.ballSpeedX} ")

                        if (!ball.ballGoesRight() && !ball.ballGoesDown()) {

                            if (isBottomOccupied) {
                                //Log.d(TAG, "ballPhysics: right hit 1")
                                ball.ballSpeedX *= -1
                                ball.ballLeft = brickHit.right
                                ball.ballRight = brickHit.right + ball.ballsize.toInt()

                            } else if (isRightOccupied) {
                                //Log.d(TAG, "ballPhysics: bot hit 1")
                                ball.ballSpeedY *= -1
                                ball.ballTop = brickHit.bottom
                                ball.ballBottom = brickHit.bottom + ball.ballsize.toInt()

                            } else if (ball.ballTop + ball.ballsize * 0.75 > brickHit.bottom) {
                                // ball is inside of brick's sides & outside of brick top n Bottom
                                //Log.d(TAG, "ballPhysics: bot hit 1 - v2")
                                ball.ballSpeedY *= -1
                                ball.ballTop = brickHit.bottom
                                ball.ballBottom = brickHit.bottom + ball.ballsize.toInt()

                            } else {                                             // ball is outside of brick's sides
                               // Log.d(TAG, "ballPhysics: right hit 1 - v2")
                                ball.ballSpeedX *= -1
                                ball.ballLeft = brickHit.right
                                ball.ballRight = brickHit.right + ball.ballsize.toInt()
                            }

                        } else if (ball.ballGoesRight() && !ball.ballGoesDown()) {

                            if (isBottomOccupied) {
                               // Log.d(TAG, "ballPhysics: left hit 1")
                                ball.ballSpeedX *= -1
                                ball.ballRight = brickHit.left
                                ball.ballLeft = brickHit.left - ball.ballsize.toInt()

                            } else if (isLeftOccupied) {
                                //Log.d(TAG, "ballPhysics: bot hit 2")
                                ball.ballSpeedY *= -1
                                ball.ballTop = brickHit.bottom
                                ball.ballBottom = brickHit.bottom + ball.ballsize.toInt()

                            } else if (ball.ballTop + ball.ballsize * 0.75 > brickHit.bottom) {
                               // ball is inside of brick's sides & outside of brick top n Bottom
                                // Log.d(TAG, "ballPhysics: bot hit 2 - v2")
                                ball.ballSpeedY *= -1
                                ball.ballTop = brickHit.bottom
                                ball.ballBottom = brickHit.bottom + ball.ballsize.toInt()

                            } else {                                         // ball is outside of brick's sides
                               // Log.d(TAG, "ballPhysics: left hit 1 - v2")
                                ball.ballSpeedX *= -1
                                ball.ballRight = brickHit.left
                                ball.ballLeft = brickHit.left - ball.ballsize.toInt()
                            }

                        } else if (!ball.ballGoesRight() && ball.ballGoesDown()) {

                            if (isTopOccupied) {
                              //  Log.d(TAG, "ballPhysics: right hit 2")
                                ball.ballSpeedX *= -1
                                ball.ballLeft = brickHit.right
                                ball.ballRight = brickHit.right + ball.ballsize.toInt()

                            } else if (isRightOccupied) {
                              //  Log.d(TAG, "ballPhysics: top hit 1")
                                ball.ballSpeedY *= -1
                                ball.ballBottom = brickHit.top
                                ball.ballTop = brickHit.top - ball.ballsize.toInt()

                            } else if (ball.ballBottom - ball.ballsize * 0.75 < brickHit.top) {
                                // ball is inside of brick's sides & outside of brick top n Bottom
                               // Log.d(TAG, "ballPhysics: top hit 1 - v2")
                                ball.ballSpeedY *= -1
                                ball.ballBottom = brickHit.top
                                ball.ballTop = brickHit.top - ball.ballsize.toInt()

                            } else {                                             // ball is outside of brick's sides
                                //Log.d(TAG, "ballPhysics: right hit 2 - v2")
                                ball.ballSpeedX *= -1
                                ball.ballLeft = brickHit.right
                                ball.ballRight = brickHit.right + ball.ballsize.toInt()
                            }

                        } else if (ball.ballGoesRight() && ball.ballGoesDown()) {

                            if (isTopOccupied) {
                               // Log.d(TAG, "ballPhysics: Left hit 2")
                                ball.ballSpeedX *= -1
                                ball.ballRight = brickHit.left
                                ball.ballLeft = brickHit.left - ball.ballsize.toInt()

                            } else if (isLeftOccupied) {
                                // Log.d(TAG, "ballPhysics: top hit 2")
                                ball.ballSpeedY *= -1
                                ball.ballBottom = brickHit.top
                                ball.ballTop = brickHit.top - ball.ballsize.toInt()

                            } else if (ball.ballBottom - ball.ballsize * 0.75 < brickHit.top) {
                                // ball is inside of brick's sides & outside of brick top n Bottom
                                // Log.d(TAG, "ballPhysics: top hit 2 - v2")
                                ball.ballSpeedY *= -1
                                ball.ballBottom = brickHit.top
                                ball.ballTop = brickHit.top - ball.ballsize.toInt()

                            } else {                                             // ball is outside of brick's sides
                                // Log.d(TAG, "ballPhysics: left hit 2 - v2")
                                ball.ballSpeedX *= -1
                                ball.ballRight = brickHit.left
                                ball.ballLeft = brickHit.left - ball.ballsize.toInt()
                            }
                        }

                        isRightOccupied = false
                        isLeftOccupied = false
                        isBottomOccupied = false
                        isTopOccupied = false
                        isDone = false

                        //Log.d(TAG, "ballspeed Y efter upd = ${ball.ballSpeedY} ")
                        // Log.d(TAG, "ballspeed X efter upd = ${ball.ballSpeedX} ")


                    }

                    if (ball.playerCollision) {

                        ball.ballBottom = player.top.toInt()
                        ball.ballTop = (player.top + ball.ballsize).toInt()
                        PlayerManager.comboPoints = 0

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

    fun brickDeathZone(brickRow: MutableList<Bricks>): Boolean {

        for (brick in brickRow) {

            var brickRect =
                Rect(brick.brickLeft, brick.brickTop, brick.brickRight, brick.brickBottom)

            if (brickRect.bottom > (canvasHeight * 0.6)) {

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

    fun gunPhysics(projectile: Gun, brickRow: MutableList<Bricks>, context: Context): Boolean {

        var toRemove = BrickStructure.totalSumOfBricks + 1
        var projHit = false

        for (brick in brickRow) {

            var brickRect =
                Rect(brick.brickLeft, brick.brickTop, brick.brickRight, brick.brickBottom)

            if (projectile.projRect.intersect(brickRect)) {

                toRemove = brickRow.indexOf(brick)
                projHit = true

                SoundEffectManager.playBrickHitSound(context, RandomNumberGenerator.rNG(0, 2))
            }
        }

        if (projHit && toRemove < BrickStructure.totalSumOfBricks + 1) {
            brickRow.removeAt(toRemove)
            PlayerManager.addPoints(BrickStructure.brickScoreValue)
            return true
        } else if (projectile.projRect.bottom < 0) {
            return true
        }


        projectile.projTop -= projectile.projSpeedY.toInt()
        projectile.projBottom -= projectile.projSpeedY.toInt()
        projectile.update()
        return false
    }
}