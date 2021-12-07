package com.example.hyperponggruppb

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.content.ContextCompat
import androidx.core.graphics.scale

class GameView(context: Context, var activity: Activity) : SurfaceView(context),
    SurfaceHolder.Callback, Runnable {

    private var thread: Thread? = null
    private var running = false
    private lateinit var canvas: Canvas
    private lateinit var ball: Ball
    private lateinit var player: Player
    var gameStart = false
    var mHolder: SurfaceHolder? = holder
    var brickRow = mutableListOf<Rect>()
    var brickColors = mutableListOf<Int>()
    var isCollisionDetected = false
    val mainExecutor = ContextCompat.getMainExecutor(context)




    var background: Bitmap =
        BitmapFactory.decodeResource(resources, R.drawable.lava_level_background)
            .scale(1080, 1920, true)
    //var newplayer: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.pong_player_mockup).scale(100,30,true )
    //var newball: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.hyper_ball).scale(15,15,true)

    init {
        mHolder?.addCallback(this)
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
        BrickStructure.fillColors(brickColors)

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
        if (ball.isDestroyed){

            death()
        }

    }

    fun death(){

        Log.d(TAG, "death: ${player.lives}")

        if(player.lives > 0) {

            player.lives -= 1
            ball.isDestroyed = false
            gameStart = false
            isCollisionDetected = false
            ball.ballPosX = 600f
            ball.ballPosY = 1500f
            ball.ballSpeedX = 0f
            ball.ballSpeedY = 0f

        } else {

            activity.finish()
            stop()

        }
    }

    fun draw() {

        try {
            canvas = mHolder!!.lockCanvas()
            canvas.drawBitmap(background, matrix, null)
            //canvas.drawBitmap(newball,matrix,null)
            //canvas.drawBitmap(newplayer,matrix,null)

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
        }catch (e: Exception){
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

    fun checkCollision() {


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

        var toRemove = BrickStructure.totalSumOfBricks+1

        for (rect in brickRow) {

            if (ball.ballHitBox.intersect(rect)) {
                toRemove = brickRow.indexOf(rect)
                ball.brickCollision = true
                SoundEffectManager.jukebox(context, 0)
                

            }
        }

        if (ball.brickCollision && toRemove < BrickStructure.totalSumOfBricks+1) {
            brickRow.removeAt(toRemove)
            Log.d(TAG, "checkCollision: ${brickRow.size}")
            brickColors.removeAt(toRemove)
            PointManager.addPoints(10)

            Log.d(TAG, "checkCollision: ${PointManager.playerPoints}")

            if (brickRow.isEmpty()){
                //player.lives = 0
                //death() - LUCA's snabba lösning
                //kekekekekekkekekekekekke HAMPUS WAS HERE
                activity.finish()
            }
        }

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

            checkCollision()
            update()
            draw()

        }
    }


}