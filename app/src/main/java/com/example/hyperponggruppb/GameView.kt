package com.example.hyperponggruppb

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.graphics.scale

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
    var isCollisionDetected = false
    var isBrickHit = true

    var screenSize  = Rect()

    var background: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.lava_level_background).scale(1080,1920,true)
    var newplayer: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.pong_player_mockup).scale(100,30,true )
    var newball: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.hyper_ball).scale(15,15,true)

    init {
        mHolder?.addCallback(this)

        setup()
    }

    fun setup() {

        player = Player(this.context)
        player.paint.color = Color.BLACK
        ball = Ball(this.context)
        ball.paint.color = Color.TRANSPARENT
        ball.hitboxPaint.color = Color.BLACK
        ball.brickCollision = false
        BrickStructure.makeBricks(brickRow)
        BrickStructure.fillColors(brickColors)
    }

    fun start() {

        ball.posX = (screenSize.right/2).toFloat()
        ball.posY = (screenSize.bottom-235f)
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
    }

    fun draw() {

        canvas = mHolder!!.lockCanvas()
        canvas.drawBitmap(background,matrix,null)
        canvas.drawBitmap(newball,matrix,null)
        canvas.drawBitmap(newplayer,matrix,null)

        ball.draw(canvas)
        ball.canvasHeight = canvas.height.toFloat()
        ball.canvasWidth = canvas.width.toFloat()
        player.draw(canvas)


        if (drawline) {


            for (obj in brickRow) {


                var brickColor = Paint()
                brickColor.color = (brickColors[brickRow.indexOf(obj)])
                canvas.drawRect(obj, brickColor)
            }

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

        if (ball.ballHitbox.intersect(player.playerRect)) {


            if (!isCollisionDetected) {
                ball.playerCollision = true
                isCollisionDetected = true
            }
        }

        var toRemove = 37

        for (rect in brickRow) {

            if (ball.ballHitbox.intersect(rect)) {
                toRemove = brickRow.indexOf(rect)
                ball.brickCollision = true

            }
        }

        if (ball.brickCollision && toRemove < 37) {
            brickRow.removeAt(toRemove)
            brickColors.removeAt(toRemove)
        }

    }


    override fun surfaceCreated(p0: SurfaceHolder) {
        screenSize = Rect(0,0,right,bottom)
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, right: Int, bottom: Int) {

        start()

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