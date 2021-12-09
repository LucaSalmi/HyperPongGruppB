package com.example.hyperponggruppb

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import java.util.concurrent.CopyOnWriteArrayList

class MusicManager() : Runnable {

    lateinit var myContext: Context
    lateinit var mediaPlayer: MediaPlayer
    var running = false
    private var thread: Thread? = null

    fun context(context: Context) {

        myContext = context
        var resID = myContext.resources.getIdentifier("crystal_cave_ambience", "raw", myContext.packageName)
        mediaPlayer = MediaPlayer.create(myContext, resID)
        mediaPlayer.start()

    }

    fun start() {
        running = true
        thread = Thread(this)
        thread?.start() // vad gör vi med tråden?
    }

    fun stop() {

        running = false
        try {
            thread?.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    override fun run() {
        Log.v("fellix", "rim")
    }
}