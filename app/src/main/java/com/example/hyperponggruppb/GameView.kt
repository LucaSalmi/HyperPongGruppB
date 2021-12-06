package com.example.hyperponggruppb

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.*
import android.util.Log
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
    var collisionDetected = false
    var counter = true
    var screenSize  = Rect()

    var background: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.lava_level_background).scale(1080,1920,true)
    var newplayer: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.pong_player_mockup).scale(100,30,true )
    var newball: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.hyper_ball).scale(15,15,true)


    var brickRect = Rect()



    init {
        mHolder?.addCallback(this)

        setup()
    }

    fun setup() {
        player = Player(this.context)
        player.paint.color = Color.TRANSPARENT
        ball = Ball(this.context)

        ball.paint.color = Color.BLACK
        ball.hitboxPaint.color = Color.TRANSPARENT
        BrickStructure.makeBricks(brickRow)
        //BrickStructure.fillColors(brickColors)



    }

    fun start() {
        player.left = ((screenSize.right/2)-100f)
        player.right = ((screenSize.right/2)+100f)
        player.top = (screenSize.bottom-230f)
        player.bottom = (screenSize.bottom-200f)
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
        ball.update()
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

    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, right: Int, bottom: Int) {
        screenSize = Rect(0,0,right,bottom)
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