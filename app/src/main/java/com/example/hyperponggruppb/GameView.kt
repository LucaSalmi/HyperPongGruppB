package com.example.hyperponggruppb

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
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
    var mHolder: SurfaceHolder? = holder
    val brickRow = mutableListOf<Bricks>()
    val brickRow2 = mutableListOf<Bricks>()


    init {
        mHolder?.addCallback(this)
        setup()
    }

    fun setup() {
        player = Player(this.context)
        player.paint.color = Color.BLACK
        ball = Ball(this.context)
        ball.posX = 550f
        ball.posY = 1780f
        ball.paint.color = Color.BLACK
        BrickStructure.makeBricks(brickRow, 5f, 20f)
        BrickStructure.makeBricks(brickRow2, 25f, 40f)
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
        //player.update()
    }

    fun draw() {

        canvas = mHolder!!.lockCanvas()
        canvas.drawColor(Color.WHITE)
        ball.draw(canvas)
        ball.canvasHeight = canvas.height.toFloat()
        ball.canvasWidth = canvas.width.toFloat()
        player.draw(canvas)

        for(obj in brickRow){

            canvas.drawRect(obj.brickLeft, obj.brickTop, obj.brickRight, obj.brickBottom, obj.brickPaint)
        }
        for(obj in brickRow2){

            canvas.drawRect(obj.brickLeft, obj.brickTop, obj.brickRight, obj.brickBottom, obj.brickPaint)
        }

        mHolder!!.unlockCanvasAndPost(canvas)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {

        val sx = event?.x.toString()
        if (gameStart){
            player.right = sx.toFloat()
            player.left = sx.toFloat() - player.offset
        }else{
            ball.speedX = 5f
            ball.speedY = -5f
            gameStart = true
        }


        return true
    }

    fun checkCollision(){

        if (ball.posX+ball.size == player.top){

                ball.collision = true
                ball.speedY = - ball.speedY

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

            update()
            draw()
            checkCollision()

        }
    }


}