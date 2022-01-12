package com.example.hyperponggruppb.view

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.CountDownTimer
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.hyperponggruppb.R
import com.example.hyperponggruppb.controller.DialogManager
import com.example.hyperponggruppb.controller.PsyduckEngine
import com.example.hyperponggruppb.controller.PlayerManager
import com.example.hyperponggruppb.model.AssetManager
import com.example.hyperponggruppb.model.GameManager


class StoryView(var myContext: Context?, var activity: Activity) : SurfaceView(myContext),
    SurfaceHolder.Callback, Runnable {

    private var thread: Thread? = null
    private var running = false
    private lateinit var canvas: Canvas
    var mHolder: SurfaceHolder? = holder
    private val myActivity = context as GameModeStoryActivity
    private val sp =
        context?.getSharedPreferences("com.example.hyperponggruppb.MyPrefs", Context.MODE_PRIVATE)

    private var storyMode: GameManager
    var isGameOver = false

    private val frameRate = 30
    val deltaTime = 0L
    var timeToUpdate = System.currentTimeMillis()

    var levelSeconds = 0
    var levelMinutes = 0

    var backgroundCode = 1

    init {

        mHolder?.addCallback(this)
        PlayerManager.lives = 1
        PlayerManager.resetPoints()
        myActivity.updateText()
        storyMode = GameManager(context, true)
        if (PlayerManager.currentLevel > 5 ){
            backgroundCode = 3
        }
    }

    private val levelTimer = object : CountDownTimer(60000, 1000) {

        override fun onTick(p0: Long) {
            levelSeconds++
        }

        override fun onFinish() {
            restartLevelTimer()
        }
    }

    private fun restartLevelTimer() {
        levelTimer.cancel()
        levelSeconds = 0
        levelMinutes++
        levelTimer.start()
    }

    override fun run() {

        while (running) {

            if (System.currentTimeMillis() >= timeToUpdate) {
                timeToUpdate += 1000 / frameRate

                if (PsyduckEngine.gameStart) {

                    ballInteractions()
                    checkDamage()
                    playerAndBrickInteractions()
                    checkLevelCompleted()
                }

                draw()
            }
        }
    }

    private fun draw() {

        try {

            canvas = mHolder!!.lockCanvas()

            PsyduckEngine.canvasHeight = canvas.height
            PsyduckEngine.canvasWidth = canvas.width

            canvas.drawBitmap(AssetManager.getBackground(backgroundCode), matrix, null)

            for (ballObj in storyMode.ballsArray) {

                ballObj.draw(canvas)

                canvas.drawBitmap(
                    AssetManager.ballAsset,
                    ballObj.ballRect.left.toFloat()-ballObj.ballsize/2,
                    ballObj.ballRect.top.toFloat()-ballObj.ballsize/2,
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

        /*
        val animation = AnimationUtils.loadAnimation(context, R.anim.bounce)
        val player = storyMode.player
        player.startAnimation(animation)

         */

        /*

        val animator = ValueAnimator.ofFloat(0f, -200f)
        animator.duration = 2000
        animator.start()
        animator.addUpdateListener(object:ValueAnimator.AnimatorUpdateListener) {

            override fun onAnimationUpdate(animation: ValueAnimator?){
                val animatedvalue = animation?.animatedValue as Float
            }
        }
         */

        //val objectAnimator = ObjectAnimator.ofFloat()

        if (!PsyduckEngine.gameStart) {

            storyMode.ball.ballLeft =
                ((storyMode.player.right - storyMode.player.playerWidth / 2) - storyMode.ball.ballsize / 2).toInt()
            storyMode.ball.ballRight =
                ((storyMode.player.right - storyMode.player.playerWidth / 2) + storyMode.ball.ballsize / 2).toInt()
            storyMode.ball.ballTop = (storyMode.player.top - storyMode.ball.ballsize).toInt()
            storyMode.ball.ballBottom = (storyMode.player.top).toInt()
            storyMode.ball.update()
        }

        if (event?.action == MotionEvent.ACTION_UP && !PsyduckEngine.gameStart) {

            restartLevelTimer()
            storyMode.ball.ballSpeedX = 7f
            storyMode.ball.ballSpeedY = -13f
            PsyduckEngine.gameStart = true

        }
        return true
    }

    private fun ballInteractions() {

        PsyduckEngine.ballPhysics(storyMode.ballsArray, storyMode.player)
    }

    private fun checkDamage() {

        myActivity.updateText()

        if (PsyduckEngine.damageTaken) {

            PsyduckEngine.damageTaken = false
            PlayerManager.loseLife()
            gameEnd()
        }
    }

    private fun playerAndBrickInteractions() {

        for (ballObj in storyMode.ballsArray) {

            PsyduckEngine.playerCollision(ballObj, storyMode.player, context)

            PsyduckEngine.brickCollision(
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

            levelTimer.cancel()
            PlayerManager.setLevelHIghScore()
            PlayerManager.saveUserData(sp)
            PsyduckEngine.gameStart = false
            storyMode.clearArrays()
            PlayerManager.isGameEnded = true
            myActivity.finish()

        }

        if (PlayerManager.lives > 0 && PsyduckEngine.gameStart) {

            PsyduckEngine.gameStart = false
            storyMode.respawnBall()
        }
    }

    private fun checkLevelCompleted() {

        if (storyMode.brickRow.isEmpty()) {

            levelTimer.cancel()
            PlayerManager.unlockNextLevel()
            PlayerManager.setLevelHIghScore()
            PlayerManager.saveUserData(sp)
            PsyduckEngine.gameStart = false
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