package com.example.hyperponggruppb

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback, Runnable {


    private var thread: Thread? = null
    private var running = false
    private lateinit var canvas: Canvas
    private lateinit var ball: Ball
    private lateinit var player: Player
    var gameStart = false
    var drawline = true
    var mHolder: SurfaceHolder? = holder
    var brickRow = mutableListOf<Rect>()
    var brickColors = mutableListOf<Int>()
    var collisionDetected = false
    var counter = true


    var brickRect = Rect()



    init {
        mHolder?.addCallback(this)
        setup()
    }

    fun setup() {
        player = Player(this.context)
        player.paint.color = Color.BLACK
        ball = Ball(this.context)
        ball.posX = 550f
        ball.posY = 1750f
        ball.paint.color = Color.BLACK
        ball.hitboxPaint.color = Color.TRANSPARENT
        BrickStructure.makeBricks(brickRow)
        //BrickStructure.fillColors(brickColors)



    }

    fun start() {
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
        ball.update()
    }

    fun draw() {

        canvas = mHolder!!.lockCanvas()
        canvas.drawColor(Color.WHITE)
        ball.draw(canvas)
        ball.canvasHeight = canvas.height.toFloat()
        ball.canvasWidth = canvas.width.toFloat()
        player.draw(canvas)

        var brickColor = Paint()
        brickColor.color = Color.RED
        if (drawline){
            

            for(obj in brickRow){

                //(brickColors[brickRow.indexOf(obj)])
                canvas.drawRect(obj,brickColor)
                Log.d(TAG, "draw: $obj")
            }

        }

        mHolder!!.unlockCanvasAndPost(canvas)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {


        val sx = event?.x.toString()
        if (gameStart){
            player.right = sx.toFloat()
            player.left = sx.toFloat() - player.offset
            player.update()
        }else{
            ball.speedX = 8f
            ball.speedY = -8f
            gameStart = true
        }


        return true


    }

    fun checkCollision(){


        if (ball.posY < 1500f){
            collisionDetected = false
        }

        if (ball.ballHitbox.intersect(player.playerRect)){

            counter = true

            if (!collisionDetected){
                ball.playerCollision = true
                collisionDetected = true
            }

        }

        var toRemove = 0

        for (rect in brickRow){

            if (ball.ballHitbox.intersect(rect) && counter){
                toRemove = brickRow.indexOf(rect)
                ball.brickCollision = true
                counter = false
            }
        }
/*
        if (ball.brickCollision && toRemove != 0){
            brickRow.removeAt(toRemove)

        }

 */



    }


    override fun surfaceCreated(p0: SurfaceHolder) {
        start()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

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