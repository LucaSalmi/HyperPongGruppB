package com.example.hyperponggruppb

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.os.CountDownTimer
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.hyperponggruppb.PhysicsEngine.gameStart
import java.lang.System.currentTimeMillis
import kotlin.math.log


class GameView(context: Context?, var activity: Activity) : SurfaceView(context),
    SurfaceHolder.Callback, Runnable {

    private var thread: Thread? = null
    private var running = false
    private lateinit var canvas: Canvas
    private lateinit var ball: Ball
    private lateinit var player: Player
    var mHolder: SurfaceHolder? = holder
    var brickRow = mutableListOf<Rect>()
    var brickAssets = mutableListOf<Bitmap>()
    var isCollisionDetected = false
    private var levelCompleted = false
    private val myActivity = context as GameMode1Activity
    private val sp = context?.getSharedPreferences("com.example.hyperponggruppb.MyPrefs", Context.MODE_PRIVATE)
    private val ballHeightSpawnModifier = 650f
    var timeTicks = 0
    var millisTimer = 20000L

    private val frameRate = 60
    val deltaTime = 0L
    var timeToUpdate = currentTimeMillis()
    var spawnNewRow = false


    init {
        mHolder?.addCallback(this)
        PlayerManager.readSave(sp)
        PlayerManager.lives = 3
        setup()
    }

    private val timer = object: CountDownTimer(millisTimer, 1000) {

        override fun onTick(p0: Long) {
        }

        override fun onFinish() {
            spawnNewRow = true
            restartTimer()
        }
    }

    fun restartTimer(){

        timer.cancel()
        timeTicks ++
        changeTimerLength()
        timer.start()
    }
    fun changeTimerLength(){

        when(timeTicks){

            3 -> millisTimer = 15000L
            5 -> millisTimer = 10000L
            7 -> millisTimer = 7000L
            9 -> millisTimer = 5000L
        }
    }

    private fun setup() {

        player = Player(this.context)
        player.paint.color = Color.TRANSPARENT
        player.left = getScreenWidth()/2 - player.playerWidth/2
        player.right = getScreenWidth()/2 + player.playerWidth/2
        player.top = getScreenHeight() - (getScreenHeight()*0.2).toFloat() - player.playerHeight/2
        player.bottom = getScreenHeight() - (getScreenHeight()*0.2).toFloat() + player.playerHeight/2
        player.update()
        player.update()

        ball = Ball(this.context)
        ball.ballPosX = player.right-player.playerWidth/2
        ball.ballPosY = player.top-ball.radius
        ball.paint.color = Color.TRANSPARENT
        ball.hitBoxPaint.color = Color.TRANSPARENT

        val brickwidth = (getScreenWidth()/10) - 4
        val brickheight = (brickwidth * 0.6).toInt()
        BrickStructure.left = 7
        BrickStructure.top = 5
        BrickStructure.right = brickwidth + BrickStructure.left
        BrickStructure.bottom = brickheight + BrickStructure.top

        AssetManager.brickwidth = brickwidth
        AssetManager.brickheight = brickheight

        BrickStructure.makeBricks(brickRow)
        brickRow = BrickStructure.createPattern(brickRow, RandomNumberGenerator.rNG(0,13))
        BrickStructure.makeOOBBricks(brickRow)

        AssetManager.prepareAssets(this.context)

        AssetManager.fillAssetArray(brickAssets, brickRow.size, 1)
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

    private fun gameEnd() {

        if (levelCompleted) {

            PlayerManager.saveHighScore(sp)
            activity.finish()
        }

        if (PlayerManager.lives > 0 && gameStart) {

            PlayerManager.lives -= 1
            ball.isDestroyed = false
            gameStart = false
            isCollisionDetected = false
            ball.ballPosX = player.right-player.playerWidth/2
            ball.ballPosY = player.top-ball.radius
            ball.ballSpeedX = 0f
            ball.ballSpeedY = 0f

        } else {

            PlayerManager.saveHighScore(sp)
            activity.finish()

        }
    }

    private fun draw() {


        try {
            canvas = mHolder!!.lockCanvas()

            PhysicsEngine.canvasHeight = canvas.height.toFloat()
            PhysicsEngine.canvasWidth = canvas.width.toFloat()

            canvas.drawBitmap(AssetManager.lavaBackground, matrix, null)

            ball.draw(canvas)
            canvas.drawBitmap(AssetManager.ballAsset, ball.ballPosX-20, ball.ballPosY-20,null)

            player.draw(canvas)
            canvas.drawBitmap(AssetManager.playerAsset, player.playerRect.left.toFloat(), player.playerRect.top.toFloat(), null)

            if (spawnNewRow){

                BrickStructure.moveDownRow(brickRow)
                spawnNewRow = false
            }

            for (obj in brickRow) {

                var brickColor = Paint()
                brickColor.color = Color.TRANSPARENT
                canvas.drawRect(obj, brickColor)
                canvas.drawBitmap((brickAssets[brickRow.indexOf(obj)]), obj.left.toFloat()-5, obj.top.toFloat()-5, null)
            }

            mHolder!!.unlockCanvasAndPost(canvas)

        } catch (e: Exception) {
            Log.e(TAG, "draw: It's NULL")
        }
    }



    override fun onTouchEvent(event: MotionEvent?): Boolean {


        val sx = event?.x.toString()
        player.right = sx.toFloat() + player.playerWidth/2
        player.left = sx.toFloat() - player.playerWidth/2
        player.update()

        if (!gameStart){

            ball.ballPosX = player.right-player.playerWidth/2
            ball.ballPosY = player.top-ball.radius
        }

        if(event?.action == MotionEvent.ACTION_UP && !gameStart){

            ball.ballSpeedX = 7f
            ball.ballSpeedY = -13f
            gameStart = true
            restartTimer()

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

                if (ball.isDestroyed && gameStart) {

                    gameEnd()
                }
                if (gameStart){
                    ball.update(player)

                    PhysicsEngine.playerCollision(ball, player, context)
                    PhysicsEngine.brickCollision(brickRow, brickAssets, ball, context)

                    if (PlayerManager.lives > 0){

                        myActivity.updateText()
                    }

                    if (brickRow.size < 50){

                        BrickStructure.makeBricks(brickRow)
                        BrickStructure.makeOOBBricks(brickRow)
                        AssetManager.fillAssetArray(brickAssets, brickRow.size,1)
                    }
                }

                draw()

                if (PhysicsEngine.brickDeathZone(brickRow)) {   // BrickDeathZone condition

                    PlayerManager.lives = 0
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