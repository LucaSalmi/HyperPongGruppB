package com.example.hyperponggruppb

import android.content.Context
import android.media.MediaPlayer
import java.util.concurrent.CopyOnWriteArrayList

object SoundEffectManager {

    lateinit var mediaPlayer: MediaPlayer


    // ImpactSoundEffect
    fun playImpactSound (id: Int, context: Context): Int {

        var resID = context.resources.getIdentifier("menu_error_8bit_sound_effect", "raw", context.packageName)
        mediaPlayer = MediaPlayer.create(context, resID)
        mediaPlayer.start()
        return resID
    }

    // menuPress SoundEffect
    fun menuPress (id: Int, context: Context): Int {

        var resID = context.resources.getIdentifier("menu_press_pause_sound_effect3", "raw", context.packageName)
        mediaPlayer = MediaPlayer.create(context, resID)
        mediaPlayer.start()
        return resID
    }
}