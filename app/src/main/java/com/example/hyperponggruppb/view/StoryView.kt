package com.example.hyperponggruppb.view

import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.hyperponggruppb.controller.PhysicsEngine
import com.example.hyperponggruppb.controller.PlayerManager
import com.example.hyperponggruppb.model.AssetManager
import com.example.hyperponggruppb.model.GameManager

class StoryView(context: Context?, var activity: Activity) : SurfaceView(context),
SurfaceHolder.Callback, Runnable {

    private var thread: Thread? = null
    private var running = false
    private lateinit var canvas: Canvas
    var mHolder: SurfaceHolder? = holder
    private val myActivity = context as GameModeStoryActivity
    private val sp = context?.getSharedPreferences("com.example.hyperponggruppb.MyPrefs", Context.MODE_PRIVATE)

    private var storyMode: GameManager
    var isGameOver = false

    private val frameRate = 60
    val deltaTime = 0L
    var timeToUpdate = System.currentTimeMillis()

    init {

        mHolder?.addCallback(this)
        PlayerManager.lives = 3
        PlayerManager.resetPoints()
        myActivity.updateText()
        storyMode = GameManager(context, true)
    }

    override fun run() {

        while (running) {

            if (System.currentTimeMillis() >= timeToUpdate) {
                timeToUpdate += 1000 / frameRate

                if (PhysicsEngine.gameStart){

                    ballInteractions()
                    checkDamage()
                    playerAndBrickInteractions()
                    checkLevelCompleted()
                }

                draw()
            }
        }
    }

    private fun draw(){
        try {

            canvas = mHolder!!.lockCanvas()

            PhysicsEngine.canvasHeight = canvas.height.toFloat()
            PhysicsEngine.canvasWidth = canvas.width.toFloat()

            canvas.drawBitmap(AssetManager.getBackground(1), matrix, null)

            for (ballObj in storyMode.ballsArray) {

                ballObj.draw(canvas)
                canvas.drawBitmap(
                    AssetManager.ballAsset,
                    ballObj.ballLeft.toFloat(),
                    ballObj.ballTop.toFloat(),
                    null
                )
            }

            storyMode.player.draw(canvas)

            when {

                storyMode.player.bigPaddle -> {

                    canvas.drawBitmap(
                        AssetManager.bigPlayerAsset,
                        storyMode.player.playerRect.left.toFloat(),
                        storyMode.player.playerRect.top.toFloat(),
                        null
                    )

                }
                storyMode.player.smallPaddle -> {

                    canvas.drawBitmap(
                        AssetManager.smallPlayerAsset,
                        storyMode.player.playerRect.left.toFloat(),
                        storyMode.player.playerRect.top.toFloat(),
                        null
                    )

                }
                else -> {

                    canvas.drawBitmap(
                        AssetManager.playerAsset,
                        storyMode.player.playerRect.left.toFloat(),
                        storyMode.player.playerRect.top.toFloat(),
                        null
                    )
                }
            }

            for (obj in storyMode.brickRow) {

                var brickColor = Paint()
                brickColor.color = Color.TRANSPARENT
                canvas.drawRect(obj, brickColor)
                canvas.drawBitmap(
                    (storyMode.brickAssets[storyMode.brickRow.indexOf(obj)]),
                    obj.left.toFloat() - 5,
                    obj.top.toFloat() - 5,
                    null
                )
            }

            mHolder!!.unlockCanvasAndPost(canvas)

        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "draw: It's NULL")
        }
    }

    private fun start() {

        running = true
        thread = Thread(this)
        thread?.start()
    }

    private fun stop() {

        running = false

        try {
            thread?.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {


        val sx = event?.x.toString()
        storyMode.player.right = sx.toFloat() + storyMode.player.playerWidth / 2
        storyMode.player.left = sx.toFloat() - storyMode.player.playerWidth / 2
        storyMode.player.update()

        if (!PhysicsEngine.gameStart) {

            storyMode.ball.ballLeft = ((storyMode.player.right - storyMode.player.playerWidth / 2) - storyMode.ball.ballsize/2).toInt()
            storyMode.ball.ballRight = ((storyMode.player.right - storyMode.player.playerWidth / 2) + storyMode.ball.ballsize/2).toInt()
            storyMode.ball.ballTop = (storyMode.player.top - storyMode.ball.ballsize).toInt()
            storyMode.ball.ballBottom = (storyMode.player.top).toInt()
        }

        if (event?.action == MotionEvent.ACTION_UP && !PhysicsEngine.gameStart) {

            storyMode.ball.ballSpeedX = 7f
            storyMode.ball.ballSpeedY = -13f
            PhysicsEngine.gameStart = true

        }
        return true
    }

    private fun ballInteractions(){

        PhysicsEngine.ballPhysics(storyMode.ballsArray, storyMode.player)
    }

    private fun checkDamage(){

        myActivity.updateText()

        if (PhysicsEngine.damageTaken) {

            PhysicsEngine.damageTaken = false
            PlayerManager.loseLife()
            gameEnd()
        }
    }

    private fun playerAndBrickInteractions(){

        for (ballObj in storyMode.ballsArray) {

            PhysicsEngine.playerCollision(ballObj, storyMode.player, context)

            PhysicsEngine.brickCollision(
                storyMode.brickRow,
                storyMode.brickAssets,
                ballObj,
                storyMode.powerUpArray,
                context,
                storyMode
            )
        }
    }

    /**
     * manages the behavior when the player loses a life or the game
     */
    private fun gameEnd() {

        if (isGameOver || PlayerManager.lives <= 0) {

            PlayerManager.saveHighScore(sp)
            PlayerManager.setPlacement()
            PhysicsEngine.gameStart = false
            storyMode.clearArrays()
            PlayerManager.isGameEnded = true
            myActivity.finish()
        }

        if (PlayerManager.lives > 0 && PhysicsEngine.gameStart) {

            PhysicsEngine.gameStart = false
            storyMode.respawnBall()
        }
    }

    private fun checkLevelCompleted(){

        if (storyMode.brickRow.isEmpty()){
            PlayerManager.unlockNextLevel()
            Log.d(TAG, "${PlayerManager.nextLevel}")
            Log.d(TAG, "${PlayerManager.currentLevel}")
            PlayerManager.setLevelScore()
            PlayerManager.saveHighScore(sp)
            PhysicsEngine.gameStart = false
            storyMode.clearArrays()
            myActivity.finish()
        }
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        Log.d(TAG, "${PlayerManager.nextLevel}")
        Log.d(TAG, "${PlayerManager.currentLevel}")
        start()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        stop()
    }


}