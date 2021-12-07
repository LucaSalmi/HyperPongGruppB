package com.example.hyperponggruppb

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
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

        ball.posX = 800f
        ball.posY = 800f
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

        if(player.lives > 0) {

            ball.isDestroyed = false
            gameStart = false
            isCollisionDetected = false
            ball.posX = 600f
            ball.posY = 1500f
            ball.speedX = 0f
            ball.speedY = 0f

        } else {
            player.lives = 3
            activity.finish()
            stop()
        }
    }

    fun draw() {

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
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {


        val sx = event?.x.toString()
        if (gameStart) {
            player.right = sx.toFloat()
            player.left = sx.toFloat() - player.offset
            player.update()
        } else {
            ball.speedX = 8f
            ball.speedY = -8f
            gameStart = true
        }

        return true
    }

    fun checkCollision() {


        if (ball.posY < 1500f) {
            isCollisionDetected = false
        }

        if (ball.ballHitBox.intersect(player.playerRect)) {


            if (!isCollisionDetected) {
                ball.playerCollision = true
                isCollisionDetected = true
            }
        }

        var toRemove = 37

        for (rect in brickRow) {

            if (ball.ballHitBox.intersect(rect)) {
                toRemove = brickRow.indexOf(rect)
                ball.brickCollision = true

            }
        }

        if (ball.brickCollision && toRemove < 37) {
            brickRow.removeAt(toRemove)
            brickColors.removeAt(toRemove)
            if (brickRow.isEmpty()){
                death()
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