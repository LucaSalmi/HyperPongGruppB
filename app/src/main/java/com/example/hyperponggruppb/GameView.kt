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
    var mHolder: SurfaceHolder? = holder

    init {
        mHolder?.addCallback(this)
        setup()
    }

    fun setup() {
        ball = Ball(this.context)
        ball.posX = 100f
        ball.posY = 100f
        ball.paint.color = BrickStructure.randomColor(BrickStructure.rNG(1, 5))
        player = Player(this.context)
        player.paint.color = Color.BLACK
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
        player.draw(canvas)
        mHolder!!.unlockCanvasAndPost(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        val sx = event?.x.toString()

        player.right = sx.toFloat()
        player.left = (sx.toFloat() - player.offset)

        return true
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
        }
    }


}