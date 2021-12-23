package com.example.hyperponggruppb.view

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.*
import android.os.CountDownTimer
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.hyperponggruppb.controller.PhysicsEngine
import com.example.hyperponggruppb.controller.PhysicsEngine.gameStart
import com.example.hyperponggruppb.controller.PlayerManager
import com.example.hyperponggruppb.controller.SoundEffectManager
import com.example.hyperponggruppb.controller.BrickStructure
import com.example.hyperponggruppb.controller.GameModeOneActivity
import com.example.hyperponggruppb.model.AssetManager
import com.example.hyperponggruppb.model.GameManager
import java.lang.System.currentTimeMillis


class GameView(context: Context?, var activity: Activity) : SurfaceView(context),
    SurfaceHolder.Callback, Runnable {


    var deathZoneTop : Float = 1200f
    private var thread: Thread? = null
    private var running = false
    private lateinit var canvas: Canvas
    private var infiniteMode: GameManager
    var mHolder: SurfaceHolder? = holder
    var isCollisionDetected = false
    private val myActivity = context as GameModeOneActivity
    private val sp =
        context?.getSharedPreferences("com.example.hyperponggruppb.MyPrefs", Context.MODE_PRIVATE)
    var timeTicks = 0
    var millisSpawnTimer = 176L
    var millisPowerUpTimer = 7000L
    var isGameOver = false

    private val frameRate = 60
    val deltaTime = 0L
    var timeToUpdate = currentTimeMillis()
    var spawnNewRow = false



    init {

        mHolder?.addCallback(this)
        PlayerManager.readSave(sp)
        PlayerManager.lives = 1
        myActivity.updateText()
        infiniteMode = GameManager(context)
    }

    /**
     * timer counts down until the next row should spawn
     * restartTimer() restarts it and updates timeTicks
     * changeTimerLength() reduces the time between each spawn
     */
    private val spawnTimer = object : CountDownTimer(30000, millisSpawnTimer) {

        override fun onTick(p0: Long) {
            spawnNewRow = true
        }

        override fun onFinish() {

            restartSpawnTimer()
        }
    }

    fun restartSpawnTimer() {

        spawnTimer.cancel()
        if (timeTicks < 10){

            timeTicks++
            changeSpawnTimerLength()
        }

        PlayerManager.playTime = timeTicks
        myActivity.updateText()
        spawnTimer.start()
    }

    private fun changeSpawnTimerLength() {

        when (timeTicks) {

            1 -> millisSpawnTimer = 160L
            2 -> millisSpawnTimer = 144L
            3 -> millisSpawnTimer = 128L
            4 -> millisSpawnTimer = 112L
            5 -> millisSpawnTimer = 96L
            6 -> millisSpawnTimer = 80L
            7 -> millisSpawnTimer = 64L
            8 -> millisSpawnTimer = 48L
            9 -> millisSpawnTimer = 32L
            10 -> millisSpawnTimer = 16L
        }
    }

    /**
     * manages the lenght of the effects of powerups
     */
    private val powerUpTimer = object : CountDownTimer(millisPowerUpTimer, 1000) {

        override fun onTick(p0: Long) {
        }

        override fun onFinish() {

            infiniteMode.player.bigPaddle = false
            infiniteMode.player.smallPaddle = false

        }
    }

    private fun restartPowerUpTimer() {

        powerUpTimer.cancel()
        powerUpTimer.start()
    }

    private fun start() {

        running = true
        thread = Thread(this)
        PlayerManager.thread = thread
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

    /**
     * manages the behavior when the player loses a life or the game
     */
    private fun gameEnd() {

        if (isGameOver || PlayerManager.lives <= 0) {

            PlayerManager.saveHighScore(sp)
            PlayerManager.setPlacement()
            gameStart = false
            infiniteMode.clearArrays()
            myActivity.scoreBoard()
            stop()
        }

        if (PlayerManager.lives > 0 && gameStart) {

            gameStart = false
            isCollisionDetected = false
            infiniteMode.respawnBall()

        }
    }

    private fun draw() {


        try {
            canvas = mHolder!!.lockCanvas()

            PhysicsEngine.canvasHeight = canvas.height.toFloat()
            PhysicsEngine.canvasWidth = canvas.width.toFloat()

            canvas.drawBitmap(AssetManager.lavaBackground, matrix, null)
            canvas.drawBitmap(AssetManager.darkRectangleDeathZone, 0f, deathZoneTop, null) //deathZone

            for (ballObj in infiniteMode.ballsArray) {

                ballObj.draw(canvas)
                canvas.drawBitmap(
                    AssetManager.ballAsset,
                    ballObj.ballPosX - 20,
                    ballObj.ballPosY - 20,
                    null
                )
            }

            infiniteMode.player.draw(canvas)

            when {

                infiniteMode.player.bigPaddle -> {

                    canvas.drawBitmap(
                        AssetManager.bigPlayerAsset,
                        infiniteMode.player.playerRect.left.toFloat(),
                        infiniteMode.player.playerRect.top.toFloat(),
                        null
                    )

                }
                infiniteMode.player.smallPaddle -> {

                    canvas.drawBitmap(
                        AssetManager.smallPlayerAsset,
                        infiniteMode.player.playerRect.left.toFloat(),
                        infiniteMode.player.playerRect.top.toFloat(),
                        null
                    )

                }
                else -> {

                    canvas.drawBitmap(
                        AssetManager.playerAsset,
                        infiniteMode.player.playerRect.left.toFloat(),
                        infiniteMode.player.playerRect.top.toFloat(),
                        null
                    )
                }
            }

            if (spawnNewRow) {

                BrickStructure.moveDownRow(infiniteMode.brickRow)
                spawnNewRow = false
            }

            for (obj in infiniteMode.brickRow) {

                var brickColor = Paint()
                brickColor.color = Color.TRANSPARENT
                canvas.drawRect(obj, brickColor)
                canvas.drawBitmap(
                    (infiniteMode.brickAssets[infiniteMode.brickRow.indexOf(obj)]),
                    obj.left.toFloat() - 5,
                    obj.top.toFloat() - 5,
                    null
                )
            }

            for (powerUp in infiniteMode.powerUpArray) {

                powerUp.draw(canvas)
                canvas.drawBitmap(
                    powerUp.assignAsset(),
                    powerUp.left.toFloat(),
                    powerUp.top.toFloat(),
                    null
                )
            }

            mHolder!!.unlockCanvasAndPost(canvas)

        } catch (e: Exception) {
            Log.e(TAG, "draw: It's NULL")
        }
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {


        val sx = event?.x.toString()
        infiniteMode.player.right = sx.toFloat() + infiniteMode.player.playerWidth / 2
        infiniteMode.player.left = sx.toFloat() - infiniteMode.player.playerWidth / 2
        infiniteMode.player.update()

        if (!gameStart) {

            infiniteMode.ball.ballPosX = infiniteMode.player.right - infiniteMode.player.playerWidth / 2
            infiniteMode.ball.ballPosY = infiniteMode.player.top - infiniteMode.ball.radius
        }

        if (event?.action == MotionEvent.ACTION_UP && !gameStart) {

            infiniteMode.ball.ballSpeedX = 7f
            infiniteMode.ball.ballSpeedY = -13f
            gameStart = true
            restartSpawnTimer()

        }

        return true
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        start()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, right: Int, bottom: Int) {

    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        Log.d(TAG, "surfaceDestroyed: we here")
        stop()
    }

    override fun run() {

        while (running) {

            if (currentTimeMillis() >= timeToUpdate) {
                timeToUpdate += 1000 / frameRate

                if (gameStart){

                    ballInteractions()
                    checkDamage()

                    playerAndBrickInteractions()


                    if (infiniteMode.brickRow.size < 50) {
                        produceExtraBricks()
                    }
                    powerUpInteractions()
                }

                if (PlayerManager.lives > 0) {

                    myActivity.updateText()
                }

                draw()
                checkDeath()
            }
        }
    }

    private fun ballInteractions(){

        PhysicsEngine.ballPhysics(infiniteMode.ballsArray, infiniteMode.player)
    }

    private fun checkDamage(){

        if (PhysicsEngine.damageTaken) {

            PhysicsEngine.damageTaken = false
            PlayerManager.loseLife()
            gameEnd()
        }
    }

    private fun playerAndBrickInteractions(){

        for (ballObj in infiniteMode.ballsArray) {

            PhysicsEngine.playerCollision(ballObj, infiniteMode.player, context)

            PhysicsEngine.brickCollision(
                infiniteMode.brickRow,
                infiniteMode.brickAssets,
                ballObj,
                infiniteMode.powerUpArray,
                context
            )
        }
    }

    private fun produceExtraBricks(){

        BrickStructure.makeOOBBricks(infiniteMode.brickRow)
        AssetManager.fillAssetArray(infiniteMode.brickAssets, infiniteMode.brickRow.size, 1)
    }

    private fun powerUpInteractions(){

        PhysicsEngine.powerUpPhysics(infiniteMode.powerUpArray, infiniteMode.player)
        var powerUpToErase: Int? = null
        for (powerUp in infiniteMode.powerUpArray) {

            if (powerUp.isCatched) {

                when (powerUp.typeID) {

                    0 -> {
                        timeTicks = powerUp.speedDown(timeTicks)
                        restartSpawnTimer()
                        SoundEffectManager.jukebox(context, 3)
                    }
                    1 -> {
                        timeTicks = powerUp.speedUp(timeTicks)
                        restartSpawnTimer()
                        SoundEffectManager.jukebox(context, 2)
                    }
                    2 -> {
                        powerUp.bigPaddle(infiniteMode.player)
                        SoundEffectManager.jukebox(context, 2)
                        restartPowerUpTimer()
                        infiniteMode.player.update()
                    }
                    3 -> {
                        powerUp.smallPaddle(infiniteMode.player)
                        SoundEffectManager.jukebox(context, 3)
                        restartPowerUpTimer()
                        infiniteMode.player.update()
                    }
                    4 -> {
                        infiniteMode.spawnExtraBall()
                        SoundEffectManager.jukebox(context, 2)
                    }
                    5 -> {
                        PlayerManager.gainLife()
                        SoundEffectManager.jukebox(context, 2)
                        myActivity.updateText()
                    }
                }
            }

            if (powerUp.isCatched || powerUp.isToDestroy) {

                powerUpToErase = infiniteMode.powerUpArray.indexOf(powerUp)
            }

        }

        if (powerUpToErase != null) {

            infiniteMode.powerUpArray.removeAt(powerUpToErase)
        }
    }

    private fun checkDeath(){

        if (PhysicsEngine.brickDeathZone(infiniteMode.brickRow) || PlayerManager.lives <= 0) {   // BrickDeathZone + 0 Lives condition

            isGameOver = true
            gameEnd()
        }
    }
}