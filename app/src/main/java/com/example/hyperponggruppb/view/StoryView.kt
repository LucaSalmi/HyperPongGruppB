package com.example.hyperponggruppb.view

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.CountDownTimer
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.hyperponggruppb.controller.PsyduckEngine
import com.example.hyperponggruppb.controller.PlayerManager
import com.example.hyperponggruppb.controller.PowerUp
import com.example.hyperponggruppb.controller.SoundEffectManager
import com.example.hyperponggruppb.model.AssetManager
import com.example.hyperponggruppb.model.GameManager


class StoryView(var myContext: Context?, var activity: Activity) : SurfaceView(myContext),
    SurfaceHolder.Callback, Runnable {

    private var thread: Thread? = null
    private var running = false
    private lateinit var canvas: Canvas
    var mHolder: SurfaceHolder? = holder
    private val myActivity = context as GameModeStoryActivity
    private val sp =
        context?.getSharedPreferences("com.example.hyperponggruppb.MyPrefs", Context.MODE_PRIVATE)

    private var storyMode: GameManager
    var isGameOver = false

    private val frameRate = 30
    val deltaTime = 0L
    var timeToUpdate = System.currentTimeMillis()

    var levelSeconds = 0
    var levelMinutes = 0

    private var levelTimeLimit = 0L

    var backgroundCode = 1
    var soundCode = 0

    init {

        mHolder?.addCallback(this)
        PlayerManager.lives = 1
        soundCode = 0
        PlayerManager.resetPoints()
        myActivity.updateText()
        storyMode = GameManager(context, true)
        if (PlayerManager.activeUser!!.currentLevel > 5) {
            backgroundCode = 3
        }
        levelTimeLimit = (storyMode.brickRow.size * 1000).toLong()
        myActivity.checkSelectedPowerup()

    }

    private val levelTimer = object : CountDownTimer(levelTimeLimit, 1000) {

        override fun onTick(p0: Long) {
            levelSeconds++

            if (levelSeconds == 60) {
                levelSeconds = 0
                levelMinutes++
            }
        }

        override fun onFinish() {
            //something when time finishes
        }
    }

    private fun restartLevelTimer() {
        levelTimer.cancel()
        levelTimer.start()
    }

    override fun run() {

        while (running) {

            if (System.currentTimeMillis() >= timeToUpdate) {
                timeToUpdate += 1000 / frameRate

                if (PsyduckEngine.gameStart) {

                    ballInteractions()
                    checkDamage()
                    playerAndBrickInteractions()
                    powerUpInteractions()
                    checkLevelCompleted()

                    if (storyMode.isGunLive) {

                        if (PsyduckEngine.gunPhysics(storyMode.projectile, storyMode.brickRow, context)){

                            storyMode.shotCount --

                            if (storyMode.shotCount == 0) {
                                storyMode.isGunLive = false
                            }else{
                                storyMode.gunPowerUp()
                            }
                        }
                    }

                    myActivity.activatePowerup()

                    if (PlayerManager.activatePowerUp) {
                        when(PlayerManager.selectedPowerUp){

                            0 -> storyMode.multiBall()
                            1 -> {
                                storyMode.shotCount = 3
                                storyMode.gunPowerUp()
                            }
                            2 -> storyMode.activateShield()
                        }

                        PlayerManager.selectedPowerUp = -1
                        PlayerManager.activatePowerUp = false
                    }
                }

                starSound()
                draw()
            }
        }
    }

    private fun draw() {

        try {

            canvas = mHolder!!.lockCanvas()

            PsyduckEngine.canvasHeight = canvas.height
            PsyduckEngine.canvasWidth = canvas.width

            canvas.drawBitmap(AssetManager.getBackground(backgroundCode), matrix, null)

            for (ballObj in storyMode.ballsArray) {

                ballObj.draw(canvas)

                canvas.drawBitmap(
                    AssetManager.ballAsset,
                    ballObj.ballRect.left.toFloat() - ballObj.ballsize / 2,
                    ballObj.ballRect.top.toFloat() - ballObj.ballsize / 2,
                    null
                )
            }

            storyMode.player.draw(canvas)

            when {

                storyMode.player.bigPaddle -> {

                    canvas.drawBitmap(
                        AssetManager.bigPlayerAsset,
                        storyMode.player.playerRect.left.toFloat(),
                        storyMode.player.playerRect.top.toFloat(),
                        null
                    )

                }
                storyMode.player.smallPaddle -> {

                    canvas.drawBitmap(
                        AssetManager.smallPlayerAsset,
                        storyMode.player.playerRect.left.toFloat(),
                        storyMode.player.playerRect.top.toFloat(),
                        null
                    )

                }
                else -> {

                    canvas.drawBitmap(
                        AssetManager.playerAsset,
                        storyMode.player.playerRect.left.toFloat(),
                        storyMode.player.playerRect.top.toFloat(),
                        null
                    )
                }
            }

            for (brick in storyMode.brickRow) {

                var brickColor = Paint()
                brickColor.color = Color.TRANSPARENT
                var brickRect =
                    Rect(brick.brickLeft, brick.brickTop, brick.brickRight, brick.brickBottom)
                canvas.drawRect(brickRect, brickColor)
                canvas.drawBitmap(
                    brick.asset,
                    brick.brickLeft.toFloat() - 5,
                    brick.brickTop.toFloat() - 5,
                    null
                )
            }

            for (powerUp in storyMode.powerUpArray) {

                powerUp.draw(canvas)
                canvas.drawBitmap(
                    powerUp.assignAsset(),
                    powerUp.left.toFloat(),
                    powerUp.top.toFloat(),
                    null
                )
            }

            if (storyMode.isGunLive && storyMode.shotCount > 0) {
                storyMode.projectile.draw(canvas)
            }
            drawComboCounter(canvas)
            mHolder!!.unlockCanvasAndPost(canvas)

        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "draw: It's NULL")
        }
    }

    private fun start() {

        running = true
        thread = Thread(this)
        thread?.start()
    }

    private fun stop() {

        running = false

        try {
            thread?.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun drawComboCounter(canvas: Canvas) {

        val comboTop = 1545f
        val comboLeft = -15f

        val paint = Paint()
        paint.color = Color.YELLOW

        when(PlayerManager.comboPoints){

            0 -> canvas.drawBitmap(AssetManager.comboCounterZero, comboLeft, comboTop, paint)
            1 -> canvas.drawBitmap(AssetManager.comboCounterOne, comboLeft, comboTop, paint)
            2 -> canvas.drawBitmap(AssetManager.comboCounterTwo, comboLeft, comboTop, paint)
            3 -> canvas.drawBitmap(AssetManager.comboCounterThree, comboLeft, comboTop, paint)
            4 -> canvas.drawBitmap(AssetManager.comboCounterFour, comboLeft, comboTop, null)
            5 -> canvas.drawBitmap(AssetManager.comboCounterFive, comboLeft, comboTop, null)
            6 -> canvas.drawBitmap(AssetManager.comboCounterSix, comboLeft, comboTop, null)
            7 -> canvas.drawBitmap(AssetManager.comboCounterSeven, comboLeft, comboTop, null)
            8 -> canvas.drawBitmap(AssetManager.comboCounterEight, comboLeft, comboTop, null)
            9 -> canvas.drawBitmap(AssetManager.comboCounterNine, comboLeft, comboTop, null)
            10 -> canvas.drawBitmap(AssetManager.comboCounterTen, comboLeft, comboTop, null)
            11 -> canvas.drawBitmap(AssetManager.comboCounterTenPlus, comboLeft, comboTop, null)

        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {


        val sx = event?.x.toString()
        storyMode.player.right = sx.toFloat() + storyMode.player.playerWidth / 2
        storyMode.player.left = sx.toFloat() - storyMode.player.playerWidth / 2
        storyMode.player.update()

        if (!PsyduckEngine.gameStart) {

            storyMode.ball.ballLeft =
                ((storyMode.player.right - storyMode.player.playerWidth / 2) - storyMode.ball.ballsize / 2).toInt()
            storyMode.ball.ballRight =
                ((storyMode.player.right - storyMode.player.playerWidth / 2) + storyMode.ball.ballsize / 2).toInt()
            storyMode.ball.ballTop = (storyMode.player.top - storyMode.ball.ballsize).toInt()
            storyMode.ball.ballBottom = (storyMode.player.top).toInt()
            storyMode.ball.update()
        }

        if (event?.action == MotionEvent.ACTION_UP && !PsyduckEngine.gameStart) {

            restartLevelTimer()
            storyMode.ball.ballSpeedX = 7f
            storyMode.ball.ballSpeedY = -13f
            PsyduckEngine.gameStart = true

        }
        return true
    }

    private fun ballInteractions() {

        PsyduckEngine.ballPhysics(storyMode.ballsArray, storyMode.player, storyMode)
    }

    private fun checkDamage() {

        myActivity.updateText()

        if (PsyduckEngine.damageTaken) {

            PsyduckEngine.damageTaken = false
            PlayerManager.loseLife()
            gameEnd()
        }
    }

    private fun playerAndBrickInteractions() {

        for (ballObj in storyMode.ballsArray) {

            PsyduckEngine.playerCollision(ballObj, storyMode.player, context)

            PsyduckEngine.brickCollision(
                storyMode.brickRow,
                ballObj,
                storyMode.powerUpArray,
                context,
                storyMode
            )
        }
    }

    private fun powerUpInteractions() {

        PsyduckEngine.powerUpPhysics(storyMode.powerUpArray, storyMode.player)
        var powerUpToErase: Int? = null
        for (powerUp in storyMode.powerUpArray) {

            if (powerUp.isCatched) {

                when (powerUp.typeID) {

                    0 -> {

                    }
                    1 -> {

                    }
                    2 -> {
                        powerUp.bigPaddle(storyMode.player)
                        SoundEffectManager.jukebox(context, 2)
                        storyMode.player.update()
                    }
                    3 -> {
                        powerUp.smallPaddle(storyMode.player)
                        SoundEffectManager.jukebox(context, 3)
                        storyMode.player.update()
                    }
                    4 -> {
                        storyMode.spawnExtraBall()
                        SoundEffectManager.jukebox(context, 2)
                    }
                    9 -> {
                        powerUp.addGems()
                        SoundEffectManager.jukebox(context, 2)
                    }
                    10 -> {
                        storyMode.shotCount = 3
                        storyMode.gunPowerUp()
                    }
                }
            }

            if (powerUp.isCatched || powerUp.isToDestroy) {

                powerUpToErase = storyMode.powerUpArray.indexOf(powerUp)
            }

        }

        if (powerUpToErase != null) {

            storyMode.powerUpArray.removeAt(powerUpToErase)
        }
    }

    /**
     * manages the behavior when the player loses a life or the game
     */
    private fun gameEnd() {

        if (isGameOver || PlayerManager.lives <= 0) {

            levelTimer.cancel()
            PlayerManager.setLevelHIghScore()
            PlayerManager.saveUserData(sp)
            PsyduckEngine.gameStart = false
            storyMode.clearArrays()
            PlayerManager.isGameEnded = true
            myActivity.finish()

        }

        if (PlayerManager.lives > 0 && PsyduckEngine.gameStart) {

            PsyduckEngine.gameStart = false
            storyMode.respawnBall()
        }
    }

    private fun checkLevelCompleted() {

        if (storyMode.brickRow.isEmpty()) {

            levelTimer.cancel()
            PlayerManager.unlockNextLevel()
            PlayerManager.setLevelHIghScore()
            PlayerManager.starCounter++
            PlayerManager.addStarsToUser()
            PlayerManager.saveUserData(sp)
            PsyduckEngine.gameStart = false
            PlayerManager.isLevelCompleted = true
            storyMode.clearArrays()
            myActivity.finish()
        }
    }

    private fun starSound() {

        if (PlayerManager.starCounter == 1 && soundCode == 0) {
            SoundEffectManager.playStarSound(context)
            soundCode++
        } else if (PlayerManager.starCounter == 2 && soundCode == 1) {
            SoundEffectManager.playStarSound(context)
            soundCode++
        } else if (PlayerManager.starCounter == 3 && soundCode == 2) {
            SoundEffectManager.playStarSound(context)
            soundCode++
        }
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        start()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        stop()
    }


}