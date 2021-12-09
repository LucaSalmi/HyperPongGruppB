package com.example.hyperponggruppb

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.graphics.scale
import java.lang.System.currentTimeMillis


class GameView(context: Context?, var activity: Activity) : SurfaceView(context),
    SurfaceHolder.Callback, Runnable {

    private var thread: Thread? = null
    private var running = false
    private lateinit var canvas: Canvas
    private lateinit var ball: Ball
    private lateinit var player: Player
    var pointsString = "Score : ${PointManager.playerPoints}"
    var gameStart = false
    var mHolder: SurfaceHolder? = holder
    var brickRow = mutableListOf<Rect>()
    var brickColors = mutableListOf<Int>()
    var isCollisionDetected = false
    var levelCompleted = false
    val myActivity = context as GameMode1Activity
    val sp =
        context?.getSharedPreferences("com.example.hyperponggruppb.MyPrefs", Context.MODE_PRIVATE)

    val frameRate = 60
    val deltaTime = 0L
    var timeToUpdate = currentTimeMillis()


    var background: Bitmap =
        BitmapFactory.decodeResource(resources, R.drawable.lava_level_background)
            .scale(getScreenWidth(), getScreenHeight(), true)
    //var newplayer: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.pong_player_mockup).scale(100,30,true )
    //var newball: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.hyper_ball).scale(15,15,true)

    init {
        mHolder?.addCallback(this)
        PointManager.readSave(sp)
        setup()
    }

    fun setup() {

        player = Player(this.context)
        player.paint.color = Color.WHITE
        ball = Ball(this.context)
        ball.paint.color = Color.WHITE
        ball.hitboxPaint.color = Color.TRANSPARENT
        ball.brickCollision = false
        BrickStructure.makeBricks(brickRow)
        brickRow = BrickStructure.createPattern(brickRow, 1)
        BrickStructure.fillColors(brickColors, brickRow.size)

    }

    fun start() {

        ball.ballPosX = player.top
        ball.ballPosY = player.playerSize
        running = true
        thread = Thread(this)
        thread?.start()
    }


    fun stop() {

        running = false

        try {
            thread?.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }


    }

    fun update() {
        ball.update(player)
        if (ball.isDestroyed) {

            gameEnd()
        }

    }

    fun gameEnd() {

        if (levelCompleted) {

            PointManager.saveHighScore(sp)
            activity.finish()
        }

        if (player.lives > 0) {

            player.lives -= 1
            ball.isDestroyed = false
            gameStart = false
            isCollisionDetected = false
            ball.ballPosX = 600f
            ball.ballPosY = 1500f
            ball.ballSpeedX = 0f
            ball.ballSpeedY = 0f

        } else {

            PointManager.saveHighScore(sp)
            activity.finish()

        }
    }

    fun draw() {

        try {
            canvas = mHolder!!.lockCanvas()
            canvas.drawBitmap(background, matrix, null)

            ball.draw(canvas)
            ball.canvasHeight = canvas.height.toFloat()
            ball.canvasWidth = canvas.width.toFloat()

            player.draw(canvas)

            for (obj in brickRow) {


                var brickColor = Paint()
                brickColor.color = (brickColors[brickRow.indexOf(obj)])
                canvas.drawRect(obj, brickColor)
            }

            mHolder!!.unlockCanvasAndPost(canvas)
        } catch (e: Exception) {
            Log.e(TAG, "draw: It's NULL")
        }


    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {


        val sx = event?.x.toString()
        if (gameStart) {
            player.right = sx.toFloat()
            player.left = sx.toFloat() - player.playerSize
            player.update()
        } else {
            ball.ballSpeedX = 7f
            ball.ballSpeedY = -13f
            gameStart = true
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

                update()

                PhysicsEngine.playerCollision(ball, player, context)
                PhysicsEngine.brickCollision(brickRow, brickColors, ball, context)
                myActivity.updateText()


                if (brickRow.isEmpty()) {
                    levelCompleted = true
                    gameEnd()
                }

                draw()
            }


        }
    }

    fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }


}