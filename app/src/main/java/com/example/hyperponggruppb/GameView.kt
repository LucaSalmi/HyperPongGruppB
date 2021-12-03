package com.example.hyperponggruppb

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.*
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.hyperponggruppb.BrickStructure.rNG
import com.example.hyperponggruppb.BrickStructure.randomColor

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
    var brickRow2 = mutableListOf<Rect>()
    var brickColors = mutableListOf<Int>()
    var brickColors2 = mutableListOf<Int>()
    var collisionDetected = false
    var counter = 0


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
        BrickStructure.makeBricks(brickRow, 5, 20)
        BrickStructure.makeBricks(brickRow2, 25, 40)
        BrickStructure.fillColors(brickColors)
        BrickStructure.fillColors(brickColors2)



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

        if (drawline){

            for(obj in brickRow){
                var brickColor = Paint()
                brickColor.color = (brickColors[brickRow.indexOf(obj)])
                canvas.drawRect(obj,brickColor)
            }
            for(obj in brickRow2){
                var pos = 0
                var brickColor = Paint()
                brickColor.color = (brickColors2[brickRow2.indexOf(obj)])
                canvas.drawRect(obj,brickColor)

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
            counter = 0
        }

        if (ball.ballHitbox.intersect(player.playerRect)){


            if (!collisionDetected){
                ball.collision = true
                collisionDetected = true
            }

        }

        for (rect in brickRow2){
            if (ball.ballHitbox.intersect(rect) && counter == 0){
                ball.collision = true
                counter++
            }
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

    override fun run() {
        while (running) {

            checkCollision()
            update()
            draw()


        }
    }


}