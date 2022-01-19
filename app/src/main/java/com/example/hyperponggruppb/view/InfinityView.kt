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
import com.example.hyperponggruppb.controller.PsyduckEngine
import com.example.hyperponggruppb.controller.PsyduckEngine.gameStart
import com.example.hyperponggruppb.controller.PlayerManager
import com.example.hyperponggruppb.controller.SoundEffectManager
import com.example.hyperponggruppb.controller.BrickStructure
import com.example.hyperponggruppb.controller.GameModeOneActivity
import com.example.hyperponggruppb.model.AssetManager
import com.example.hyperponggruppb.model.GameManager
import java.lang.System.currentTimeMillis


class InfinityView(context: Context?, var activity: Activity) : SurfaceView(context),
    SurfaceHolder.Callback, Runnable {


    var deathZoneTop = PsyduckEngine.canvasHeight *0.6.toFloat()
    private var thread: Thread? = null
    private var running = false
    private lateinit var canvas: Canvas
    private var infiniteMode: GameManager
    var mHolder: SurfaceHolder? = holder
    private val myActivity = context as GameModeOneActivity
    private val sp =
        context?.getSharedPreferences("com.example.hyperponggruppb.MyPrefs", Context.MODE_PRIVATE)
    var timeTicks = 0
    var millisSpawnTimer = 176L
    var millisPowerUpTimer = 7000L
    var isGameOver = false

    private val frameRate = 30
    val deltaTime = 0L
    var timeToUpdate = currentTimeMillis()
    var spawnNewRow = false

    var backgroundIdOne = 1
    var backgroundIdTwo = 2
    var transBackroundIdOne = 1
    var transBackgroundIdTwo = 2


    init {

        mHolder?.addCallback(this)
        backgroundIdOne = 1
        backgroundIdTwo = 2
        transBackroundIdOne = 1
        transBackgroundIdTwo = 2
        PlayerManager.lives = 1
        PlayerManager.resetPoints()
        myActivity.updateText()
        infiniteMode = GameManager(context, false)

        if (PlayerManager.isMusicActive){
            SoundEffectManager.musicSetup(context!!,6)
        }
    }

    /**
     * timer counts down until the next row should spawn
     * restartTimer() restarts it and updates timeTicks
     * changeTimerLength() reduces the time between each spawn
     */
    private val spawnTimer = object : CountDownTimer(30000, millisSpawnTimer) {

        override fun onTick(p0: Long) {
            spawnNewRow = true
            AssetManager.moveBackGround()
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
        if (timeTicks in 3..6) {
            BrickStructure.playerSpeed = 2
        }
        if (timeTicks in 7..9) {
            BrickStructure.playerSpeed = 3
        } else if (timeTicks == 10) {
            BrickStructure.playerSpeed = 4
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

            PlayerManager.saveUserData(sp)
            PlayerManager.setPlacement()
            PlayerManager.starCounter = 0
            gameStart = false
            BrickStructure.playerSpeed = 1
            infiniteMode.clearArrays()
            PlayerManager.isGameEnded = true
            spawnTimer.cancel()
            powerUpTimer.cancel()
            Thread.sleep(1500)
            AssetManager.resetBackGround()
            PlayerManager.comboPoints = 0
            myActivity.finish()
        }

        if (PlayerManager.lives > 0 && gameStart) {

            gameStart = false
            PlayerManager.comboPoints = 0
            infiniteMode.respawnBall()

        }
    }

    private fun draw() {


        try {
            canvas = mHolder!!.lockCanvas()

            PsyduckEngine.canvasHeight = canvas.height
            PsyduckEngine.canvasWidth = canvas.width

            canvas.drawBitmap(AssetManager.getBackground(backgroundIdOne), AssetManager.bgRectOne.left.toFloat(), AssetManager.bgRectOne.top.toFloat(), null)
            canvas.drawBitmap(AssetManager.getTransBackground(transBackroundIdOne), AssetManager.bgRectTransOne.left.toFloat(), AssetManager.bgRectTransOne.top.toFloat(), null)
            canvas.drawBitmap(AssetManager.getBackground(backgroundIdTwo), AssetManager.bgRectTwo.left.toFloat(), AssetManager.bgRectTwo.top.toFloat(), null)
            canvas.drawBitmap(AssetManager.getTransBackground(transBackgroundIdTwo), AssetManager.bgRectTransTwo.left.toFloat(), AssetManager.bgRectTransTwo.top.toFloat(), null)
            canvas.drawBitmap(AssetManager.darkRectangleDeathZone, 0f, deathZoneTop, null) //deathZone

            for (ballObj in infiniteMode.ballsArray) {

                ballObj.draw(canvas)

                canvas.drawBitmap(
                    AssetManager.ballAsset,
                    ballObj.ballRect.left.toFloat()-ballObj.ballsize/2,
                    ballObj.ballRect.top.toFloat()-ballObj.ballsize/2,
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

            for (brick in infiniteMode.brickRow) {

                var brickColor = Paint()
                brickColor.color = Color.TRANSPARENT
                var brickRect = Rect(brick.brickLeft, brick.brickTop, brick.brickRight, brick.brickBottom)
                canvas.drawRect(brickRect, brickColor)
                canvas.drawBitmap(
                    brick.asset,
                    brick.brickLeft.toFloat() - 5,
                    brick.brickTop.toFloat() - 5,
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

            if (infiniteMode.isGunLive && infiniteMode.shotCount > 0) {
                infiniteMode.projectile.draw(canvas)
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

            infiniteMode.ball.ballLeft = ((infiniteMode.player.right - infiniteMode.player.playerWidth / 2) - infiniteMode.ball.ballsize/2).toInt()
            infiniteMode.ball.ballRight = ((infiniteMode.player.right - infiniteMode.player.playerWidth / 2) + infiniteMode.ball.ballsize/2).toInt()
            infiniteMode.ball.ballTop = (infiniteMode.player.top - infiniteMode.ball.ballsize).toInt()
            infiniteMode.ball.ballBottom = (infiniteMode.player.top).toInt()
            infiniteMode.ball.update()
        }

        if (event?.action == MotionEvent.ACTION_UP && !gameStart) {

            infiniteMode.ball.ballSpeedX = 7f
            infiniteMode.ball.ballSpeedY = -13f
            gameStart = true
            restartSpawnTimer()

        }
        Log.d(TAG, "onTouchEvent: $gameStart")
        return true
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        start()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, right: Int, bottom: Int) {

    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        Log.d(TAG, "surfaceDestroyed: we here")
        gameStart = false
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

                    var size = infiniteMode.brickRow.size - 1

                    if (size > 0){
                        if(infiniteMode.brickRow[size].brickTop > 0 ){
                            produceExtraBricks()
                        }
                    }

                    powerUpInteractions()

                    if (infiniteMode.isGunLive) {

                        if (PsyduckEngine.gunPhysics(infiniteMode.projectile, infiniteMode.brickRow, context)){

                            infiniteMode.shotCount --

                            if (infiniteMode.shotCount == 0) {
                                infiniteMode.isGunLive = false
                            }else{
                                infiniteMode.gunPowerUp()
                            }
                        }
                    }
                }

                draw()
                checkBackGroundPosition()
                checkDeath()
            }
        }
    }

    private fun checkBackGroundPosition(){

        if (AssetManager.bgRectOne.top > AssetManager.bGHeight){

            AssetManager.bgRectOne.bottom = AssetManager.bgRectTransTwo.top
            AssetManager.bgRectOne.top = AssetManager.bgRectOne.bottom - AssetManager.bGHeight

            when (backgroundIdOne){
                1 -> {
                    backgroundIdOne = 3
                    transBackroundIdOne = 3
                }
                2 -> {
                    backgroundIdOne = 1
                    transBackroundIdOne = 1
                }
                3 -> {
                    backgroundIdOne = 2
                    transBackroundIdOne = 2
                }
            }

        }
        if (AssetManager.bgRectTransOne.top > AssetManager.bGHeight){
            AssetManager.bgRectTransOne.bottom = AssetManager.bgRectOne.top
            AssetManager.bgRectTransOne.top = AssetManager.bgRectOne.top - AssetManager.transHeight
        }
        if (AssetManager.bgRectTwo.top > AssetManager.bGHeight){

            AssetManager.bgRectTwo.bottom = AssetManager.bgRectTransOne.top
            AssetManager.bgRectTwo.top = AssetManager.bgRectTwo.bottom - AssetManager.bGHeight

            when(backgroundIdTwo){

                1-> {
                    backgroundIdTwo = 3
                    transBackgroundIdTwo= 3
                }
                2-> {
                    backgroundIdTwo = 1
                    transBackgroundIdTwo= 1
                }
                3-> {
                    backgroundIdTwo = 2
                    transBackgroundIdTwo= 2
                }
            }

        }
        if (AssetManager.bgRectTransTwo.top > AssetManager.bGHeight){
            AssetManager.bgRectTransTwo.bottom = AssetManager.bgRectTwo.top
            AssetManager.bgRectTransTwo.top = AssetManager.bgRectTwo.top - AssetManager.transHeight
        }
    }

    private fun ballInteractions(){

        PsyduckEngine.ballPhysics(infiniteMode.ballsArray, infiniteMode.player, infiniteMode)
    }

    private fun checkDamage(){

        myActivity.updateText()

        if (PsyduckEngine.damageTaken) {

            PsyduckEngine.damageTaken = false
            PlayerManager.loseLife()
            gameEnd()
        }
    }

    private fun playerAndBrickInteractions(){

        for (ballObj in infiniteMode.ballsArray) {

            PsyduckEngine.playerCollision(ballObj, infiniteMode.player, context)

            PsyduckEngine.brickCollision(
                infiniteMode.brickRow,
                ballObj,
                infiniteMode.powerUpArray,
                context,
                infiniteMode
            )
        }
    }

    private fun produceExtraBricks(){

        infiniteMode.makeOOBBricks()
    }

    private fun powerUpInteractions(){

        PsyduckEngine.powerUpPhysics(infiniteMode.powerUpArray, infiniteMode.player)
        var powerUpToErase: Int? = null
        for (powerUp in infiniteMode.powerUpArray) {

            if (powerUp.isCatched) {

                when (powerUp.typeID) {

                    0 -> {
                        powerUp.forceBrickDown(infiniteMode.brickRow)
                        restartSpawnTimer()
                        SoundEffectManager.jukebox(context, 3)
                    }
                    1 -> {
                        powerUp.forceBrickUp(infiniteMode.brickRow)
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
                    5->{
                        powerUp.addGems()
                        SoundEffectManager.jukebox(context, 2)
                    }
                    6 ->{
                        infiniteMode.shotCount = 3
                        infiniteMode.gunPowerUp()
                        SoundEffectManager.jukebox(context, 2)
                    }
                    7 -> {
                        SoundEffectManager.jukebox(context, 2)
                        infiniteMode.activateShield()
                    }
                    8 -> {
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

        if (PsyduckEngine.brickDeathZone(infiniteMode.brickRow) || PlayerManager.lives <= 0) {   // BrickDeathZone + 0 Lives condition

            isGameOver = true
            gameEnd()
        }
    }

}