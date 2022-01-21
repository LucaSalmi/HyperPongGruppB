package com.example.hyperponggruppb.view

import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.*
import android.os.CountDownTimer
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.hyperponggruppb.R
import com.example.hyperponggruppb.controller.PsyduckEngine
import com.example.hyperponggruppb.controller.PlayerManager
import com.example.hyperponggruppb.controller.SoundEffectManager
import com.example.hyperponggruppb.model.AssetManager
import com.example.hyperponggruppb.model.GameManager
import java.util.concurrent.TimeUnit


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

    private val frameRate = 30
    val deltaTime = 0L
    var timeToUpdate = System.currentTimeMillis()

    private var levelTimeLimit = 0L

    var backgroundCode = 1
    var soundCode = 0

    var isCounting = false
    var countdownIsOn = false

    init {

        mHolder?.addCallback(this)
        soundCode = 0
        PlayerManager.resetPoints()
        myActivity.updateText()
        storyMode = GameManager(context, true)
        if (PlayerManager.currentLevel > 5) {
            backgroundCode = 3
        }
        levelTimeLimit = (storyMode.brickRow.size * 5000).toLong()
        PlayerManager.levelTimeLimit = levelTimeLimit.toInt()

        PlayerManager.levelCountdown = context.getString(
            R.string.formatted_time,
            TimeUnit.MILLISECONDS.toMinutes(levelTimeLimit) % 60,
            TimeUnit.MILLISECONDS.toSeconds(levelTimeLimit) % 60
        )
        PlayerManager.lives = 3
        myActivity.checkSelectedPowerup()

    }

    /**
     * usig a total time based on the number of bricks, it counts down from that
     */
    private val levelTimer = object : CountDownTimer(levelTimeLimit, 1000) {

        override fun onTick(millisUntilFinished: Long) {
            PlayerManager.levelCountdown = context.getString(
                R.string.formatted_time,
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
            )

            PlayerManager.levelTime = millisUntilFinished
        }

        override fun onFinish() {

        }
    }

    /**
     * checks that the message coming up when the player scores a combo of 5 or 10 hit disappears after 2 seconds
     */
    private val comboMsgTimer = object : CountDownTimer(2000L, 1000L){
        override fun onTick(p0: Long) {

        }

        override fun onFinish() {
            isCounting = false
            PlayerManager.textIsOn = false
        }

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

                        if (PsyduckEngine.gunPhysics(
                                storyMode.projectile,
                                storyMode.brickRow,
                                context
                            )
                        ) {
                            storyMode.shotCount--

                            if (storyMode.shotCount == 0) {
                                storyMode.isGunLive = false
                            } else {
                                storyMode.gunPowerUp()
                            }
                        }
                    }

                    myActivity.activatePowerup()
                    myActivity.updateComboCounter()

                    if (PlayerManager.activatePowerUp) {
                        when (PlayerManager.selectedPowerUp) {

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

            storyMode.drawBall(canvas)

            storyMode.player.draw(canvas)

            storyMode.drawBricks(canvas)

            storyMode.drawPowerUp(canvas)

            if (storyMode.isGunLive && storyMode.shotCount > 0) {
                storyMode.drawProjectile(canvas)
            }
            if (storyMode.isShieldActive) {
                storyMode.drawShield(canvas)
            }

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

        if (event?.action == MotionEvent.ACTION_DOWN) {
            storyMode.player.playerRect.top -= 10
            storyMode.player.playerRect.bottom -= 10
        }
        if (event?.action == MotionEvent.ACTION_UP) {
            storyMode.player.playerRect.top += 10
            storyMode.player.playerRect.bottom += 10
        }

        if (event?.action == MotionEvent.ACTION_UP && !PsyduckEngine.gameStart) {

            if (!countdownIsOn){
                countdownIsOn = true
                levelTimer.start()
            }

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

        if (PlayerManager.textIsOn && !isCounting) {

            isCounting = true
            comboMsgTimer.start()
        }
    }

    private fun powerUpInteractions() {

        PsyduckEngine.powerUpPhysics(storyMode.powerUpArray, storyMode.player)
        var powerUpToErase: Int? = null
        for (powerUp in storyMode.powerUpArray) {

            if (powerUp.isCatched) {

                if (powerUp.typeID != 5) {

                    PlayerManager.levelPowerups++

                } else {

                    PlayerManager.levelGems += 5
                }

                when (powerUp.typeID) {

                    2 -> {
                        SoundEffectManager.playPowerUpSounds(context, 0)
                        powerUp.bigPaddle(storyMode.player)
                        storyMode.player.update()
                    }
                    3 -> {
                        SoundEffectManager.playPowerUpSounds(context, 1)
                        powerUp.smallPaddle(storyMode.player)
                        storyMode.player.update()
                    }
                    4 -> {
                        SoundEffectManager.playPowerUpSounds(context, 0)
                        storyMode.spawnExtraBall()
                    }
                    5 -> {
                        SoundEffectManager.playPowerUpSounds(context, 2)
                        powerUp.addGems()
                    }
                    6 -> {
                        SoundEffectManager.playPowerUpSounds(context, 0)
                        storyMode.shotCount = 3
                        storyMode.gunPowerUp()
                    }
                    7 -> {
                        SoundEffectManager.playPowerUpSounds(context, 0)
                        storyMode.activateShield()
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

        if (PlayerManager.lives <= 0) {

            levelTimer.cancel()
            PlayerManager.setLevelHIghScore()
            PlayerManager.starCounter = 0
            PlayerManager.saveUserData(sp)
            PsyduckEngine.gameStart = false
            PlayerManager.isGameEnded = true
            PlayerManager.comboPoints = 0
            calculateTime()
            myActivity.finish()

        }

        if (PlayerManager.lives > 0 && PsyduckEngine.gameStart) {

            PsyduckEngine.gameStart = false
            PlayerManager.comboPoints = 0
            storyMode.respawnBall()
        }
    }

    private fun checkLevelCompleted() {

        if (storyMode.brickRow.isEmpty()) {
            levelTimer.cancel()
            PlayerManager.unlockNextLevel()
            PlayerManager.setLevelHIghScore()
            PlayerManager.addStarsToUser()
            PlayerManager.saveUserData(sp)
            PsyduckEngine.gameStart = false
            PlayerManager.isLevelCompleted = true
            PlayerManager.comboPoints = 0
            myActivity.finish()
        }
    }

    private fun calculateTime() {

        val time = levelTimeLimit - PlayerManager.levelTime
        PlayerManager.levelTimeString = context.getString(
            R.string.formatted_time,
            TimeUnit.MILLISECONDS.toMinutes(time) % 60,
            TimeUnit.MILLISECONDS.toSeconds(time) % 60
        )
    }

    /**
     * plays a sound when the player obtains a star
     */
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