package com.example.hyperponggruppb

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PointF
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast
import com.example.hyperponggruppb.databinding.ActivityGameBinding
import kotlin.math.sqrt

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
    val surfaceView = findViewById<SurfaceView>(R.id.surfaceView)


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
        ball.hitboxPaint.color = Color.TRANSPARENT
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
            player.update()
        }else{
            ball.speedX = 5f
            ball.speedY = -5f
            gameStart = true
        }


        return true


    }

    fun checkCollision(){

        if (ball.ballHitbox.intersect(player.playerRect)){
            ball.speedY = -ball.speedY
            Log.d(TAG, "checkCollision: porcodio")
        }




/*
            var collided = false
            //case 1: the center of the circle c1 is inside the rect 1
            if(player.playerRect.contains(ball.center.x.toInt(), ball.center.y.toInt())){
                collided = true
            } else {
                //case 2: the center is outside the rect1 r1
                var pEdge = PointF()
                //Update x coordinate of pEdge
                if(ball.center.x < player.playerRect.left) {
                    pEdge.x = player.playerRect.left.toFloat()
                } else if(ball.center.x > player.playerRect.right) {
                    pEdge.x = player.playerRect.right.toFloat()
                } else {
                    pEdge.x = ball.center.x
                }
                //Update x coordinate of pEdge
                if(ball.center.y < player.playerRect.top) {
                    pEdge.y = player.playerRect.top.toFloat()
                } else if(ball.center.y > player.playerRect.bottom) {
                    pEdge.y = player.playerRect.bottom.toFloat()
                } else{
                    pEdge.y = ball.center.y
                }
                val deltaX = ball.center.x - pEdge.x
                val deltaY = ball.center.y - pEdge.y
                val distance = sqrt((deltaX*deltaX + deltaY*deltaY).toDouble())
                collided = (distance <= ball.radius)
            }
            if(collided) {
                Toast.makeText(context, "collided", Toast.LENGTH_SHORT).show()
            } else {
                Log.d(TAG, "checkCollision: all good, ${ball.center.x}, ${ball.center.y}")
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