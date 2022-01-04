package com.example.hyperponggruppb.view

import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Canvas
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView

class StoryView(context: Context?, var activity: Activity) : SurfaceView(context),
SurfaceHolder.Callback, Runnable {

    private var thread: Thread? = null
    private var running = false
    private lateinit var canvas: Canvas
    var mHolder: SurfaceHolder? = holder
    private val myActivity = context as GameModeStoryActivity
    private val sp = context?.getSharedPreferences("com.example.hyperponggruppb.MyPrefs", Context.MODE_PRIVATE)

    private val frameRate = 60
    val deltaTime = 0L
    var timeToUpdate = System.currentTimeMillis()

    init {
        mHolder?.addCallback(this)
    }

    override fun run() {

        while (running) {

            if (System.currentTimeMillis() >= timeToUpdate) {
                timeToUpdate += 1000 / frameRate

                draw()

            }
        }
    }

    private fun draw(){
        try {

            canvas = mHolder!!.lockCanvas()





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

    override fun surfaceCreated(p0: SurfaceHolder) {
        Log.d(TAG, "start: storyView works")
        start()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        stop()
    }


}