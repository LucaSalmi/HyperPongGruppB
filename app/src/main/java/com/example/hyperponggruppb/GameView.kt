package com.example.hyperponggruppb

import android.app.Activity
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.os.CountDownTimer
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.Window
import android.widget.ImageView
import androidx.fragment.app.commit
import com.example.hyperponggruppb.PhysicsEngine.gameStart
import java.lang.System.currentTimeMillis


class GameView(context: Context?, var activity: Activity) : SurfaceView(context),
    SurfaceHolder.Callback, Runnable {

    private var thread: Thread? = null
    private var running = false
    private lateinit var canvas: Canvas
    private lateinit var ball: Ball
    private lateinit var extraBall: Ball
    private lateinit var player: Player
    var mHolder: SurfaceHolder? = holder
    var brickRow = mutableListOf<Rect>()
    var brickAssets = mutableListOf<Bitmap>()
    var ballsArray = mutableListOf<Ball>()
    var isCollisionDetected = false
    private val myActivity = context as GameMode1Activity
    private val sp =
        context?.getSharedPreferences("com.example.hyperponggruppb.MyPrefs", Context.MODE_PRIVATE)
    var timeTicks = 0
    var millisSpawnTimer = 1000L
    var millisPowerUpTimer = 7000L
    var isGameOver = false
    private var isPowerUpActive = false

    private val frameRate = 60
    val deltaTime = 0L
    var timeToUpdate = currentTimeMillis()
    var spawnNewRow = false


    init {
        mHolder?.addCallback(this)
        PlayerManager.readSave(sp)
        PlayerManager.lives = 1
        myActivity.updateText()
        setup()
    }

    /**
     * timer counts down until the next row should spawn
     * restartTimer() restarts it and updates timeTicks
     * changeTimerLength() reduces the time between each spawn
     */
    private val spawnTimer = object : CountDownTimer(millisSpawnTimer, 1000) {

        override fun onTick(p0: Long) {
        }

        override fun onFinish() {
            spawnNewRow = true
            restartSpawnTimer()
        }
    }

    fun restartSpawnTimer() {

        spawnTimer.cancel()
        timeTicks++
        changeSpawnTimerLength()
        spawnTimer.start()
    }

    private fun changeSpawnTimerLength() {

        when (timeTicks) {

            1 -> millisSpawnTimer = 900L
            2 -> millisSpawnTimer = 800L
            3 -> millisSpawnTimer = 700L
            4 -> millisSpawnTimer = 600L
            5 -> millisSpawnTimer = 500L
            6 -> millisSpawnTimer = 400L
            7 -> millisSpawnTimer = 300L
            8 -> millisSpawnTimer = 200L
        }
    }

    private val powerUpTimer = object : CountDownTimer(millisPowerUpTimer, 1000) {

        override fun onTick(p0: Long) {
        }

        override fun onFinish() {

            player.bigPaddle = false
            player.smallPaddle = false
            isPowerUpActive = false

        }

    }

    private fun restartPowerUpTimer() {

        powerUpTimer.cancel()
        powerUpTimer.start()
    }

    private fun setup() {

        player = Player(this.context)
        player.paint.color = Color.TRANSPARENT
        player.left = getScreenWidth() / 2 - player.playerWidth / 2
        player.right = getScreenWidth() / 2 + player.playerWidth / 2
        player.top =
            getScreenHeight() - (getScreenHeight() * 0.2).toFloat() - player.playerHeight / 2 - 100
        player.bottom =
            getScreenHeight() - (getScreenHeight() * 0.2).toFloat() + player.playerHeight / 2 - 100
        player.update()

        ball = Ball(this.context)
        ball.ballPosX = player.right - player.playerWidth / 2
        ball.ballPosY = player.top - ball.radius
        ball.paint.color = Color.TRANSPARENT
        ball.hitBoxPaint.color = Color.TRANSPARENT
        ballsArray.add(ball)

        val brickwidth = (getScreenWidth() / 10) - 4
        val brickheight = (brickwidth * 0.6).toInt()
        BrickStructure.left = 7
        BrickStructure.top = 5
        BrickStructure.right = brickwidth + BrickStructure.left
        BrickStructure.bottom = brickheight + BrickStructure.top

        AssetManager.brickwidth = brickwidth
        AssetManager.brickheight = brickheight

        BrickStructure.makeInboundsBricks(brickRow)
        brickRow = BrickStructure.createPattern(brickRow, 0)//RandomNumberGenerator.rNG(0,13))
        BrickStructure.makeOOBBricks(brickRow)

        AssetManager.prepareAssets(this.context)

        AssetManager.fillAssetArray(brickAssets, brickRow.size, 1)

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

            PlayerManager.saveHighScore(sp)
            gameStart = false
            myActivity.scoreBoard()
            stop()
        }

        if (PlayerManager.lives > 0 && gameStart) {

            gameStart = false
            isCollisionDetected = false
            ball = Ball(this.context)
            ball.paint.color = Color.TRANSPARENT
            ball.hitBoxPaint.color = Color.TRANSPARENT
            ballsArray.add(ball)
            ball.ballPosX = player.right - player.playerWidth / 2
            ball.ballPosY = player.top - ball.radius
            ball.ballSpeedX = 0f
            ball.ballSpeedY = 0f


        }

    }

    private fun draw() {


        try {
            canvas = mHolder!!.lockCanvas()

            PhysicsEngine.canvasHeight = canvas.height.toFloat()
            PhysicsEngine.canvasWidth = canvas.width.toFloat()

            canvas.drawBitmap(AssetManager.lavaBackground, matrix, null)

            for (ballObj in ballsArray) {

                ballObj.draw(canvas)
                canvas.drawBitmap(
                    AssetManager.ballAsset,
                    ballObj.ballPosX - 20,
                    ballObj.ballPosY - 20,
                    null
                )
            }

            player.draw(canvas)

            when {
                player.bigPaddle -> {

                    canvas.drawBitmap(
                        AssetManager.bigPlayerAsset,
                        player.playerRect.left.toFloat(),
                        player.playerRect.top.toFloat(),
                        null
                    )

                }
                player.smallPaddle -> {

                    canvas.drawBitmap(
                        AssetManager.smallPlayerAsset,
                        player.playerRect.left.toFloat(),
                        player.playerRect.top.toFloat(),
                        null
                    )

                }
                else -> {

                    canvas.drawBitmap(
                        AssetManager.playerAsset,
                        player.playerRect.left.toFloat(),
                        player.playerRect.top.toFloat(),
                        null
                    )
                }
            }

            if (spawnNewRow) {

                BrickStructure.moveDownRow(brickRow)
                spawnNewRow = false
            }

            for (obj in brickRow) {

                var brickColor = Paint()
                brickColor.color = Color.TRANSPARENT
                canvas.drawRect(obj, brickColor)
                canvas.drawBitmap(
                    (brickAssets[brickRow.indexOf(obj)]),
                    obj.left.toFloat() - 5,
                    obj.top.toFloat() - 5,
                    null
                )
            }

            if (PhysicsEngine.isPowerUpLive) {

                PhysicsEngine.powerUp.draw(canvas)

                if (!isPowerUpActive) {

                    canvas.drawBitmap(
                        PhysicsEngine.powerUp.assignAsset(),
                        PhysicsEngine.powerUp.left.toFloat(),
                        PhysicsEngine.powerUp.top.toFloat(),
                        null
                    )
                }
            }

            mHolder!!.unlockCanvasAndPost(canvas)

        } catch (e: Exception) {
            Log.e(TAG, "draw: It's NULL")
        }
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {


        val sx = event?.x.toString()
        player.right = sx.toFloat() + player.playerWidth / 2
        player.left = sx.toFloat() - player.playerWidth / 2
        player.update()

        if (!gameStart) {

            ball.ballPosX = player.right - player.playerWidth / 2
            ball.ballPosY = player.top - ball.radius
        }

        if (event?.action == MotionEvent.ACTION_UP && !gameStart) {

            ball.ballSpeedX = 7f
            ball.ballSpeedY = -13f
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
        stop()
    }

    override fun run() {

        while (running) {

            if (currentTimeMillis() >= timeToUpdate) {
                timeToUpdate += 1000 / frameRate

                if (gameStart) {

                    PhysicsEngine.ballPhysics(ballsArray, player)

                    if (PhysicsEngine.damageTaken) {

                        PhysicsEngine.damageTaken = false
                        PlayerManager.loseLife()
                        gameEnd()
                    }

                    for (ballObj in ballsArray) {

                        PhysicsEngine.playerCollision(ballObj, player, context)

                        PhysicsEngine.brickCollision(brickRow, brickAssets, ballObj, context)
                    }

                    PhysicsEngine.powerUpPhysics(player)

                    if (PhysicsEngine.isPowerUpCatch) {

                        when (PhysicsEngine.powerUp.typeID) {

                            0 -> {
                                timeTicks = PhysicsEngine.powerUp.speedDown(timeTicks)
                                SoundEffectManager.jukebox(context, 3)
                            }
                            1 -> {
                                timeTicks = PhysicsEngine.powerUp.speedUp(timeTicks)
                                SoundEffectManager.jukebox(context, 2)
                            }
                            2 -> {
                                PhysicsEngine.powerUp.bigPaddle(player)
                                SoundEffectManager.jukebox(context, 2)
                                player.update()
                            }
                            3 -> {
                                PhysicsEngine.powerUp.smallPaddle(player)
                                SoundEffectManager.jukebox(context, 3)
                                player.update()
                            }
                            4 -> {
                                if (!isPowerUpActive) {

                                    extraBall = Ball(this.context)
                                    extraBall.ballPosX = player.right - player.playerWidth / 2
                                    extraBall.ballPosY = player.top - ball.radius
                                    extraBall.paint.color = Color.TRANSPARENT
                                    extraBall.hitBoxPaint.color = Color.TRANSPARENT
                                    extraBall.ballSpeedX = 7f
                                    extraBall.ballSpeedY = -13f
                                    ballsArray.add(extraBall)
                                    isPowerUpActive = true
                                    SoundEffectManager.jukebox(context, 2)
                                }

                            }
                        }

                        PhysicsEngine.isPowerUpLive = false
                        PhysicsEngine.isPowerUpCatch = false
                        restartPowerUpTimer()
                    }

                    if (PlayerManager.lives > 0) {

                        myActivity.updateText()
                    }

                    if (brickRow.size < 30) {

                        BrickStructure.makeOOBBricks(brickRow)
                        AssetManager.fillAssetArray(brickAssets, brickRow.size, 1)
                    }
                }

                draw()

                if (PhysicsEngine.brickDeathZone(brickRow) || PlayerManager.lives <= 0) {   // BrickDeathZone + 0 Lives condition

                    isGameOver = true
                    gameEnd()
                }
            }
        }
    }

    private fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    private fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

}