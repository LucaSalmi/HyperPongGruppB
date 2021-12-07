package com.example.hyperponggruppb

import android.content.Context
import android.media.MediaPlayer
import java.util.concurrent.CopyOnWriteArrayList

object SoundEffectManager {

    fun jukebox(context: Context, id: Int){
        var isPlaying = false

        if (!isPlaying){

            var resID: Int = when(id){

                0 -> context.resources.getIdentifier("menu_error_8bit_sound_effect", "raw", context.packageName)
                1 -> context.resources.getIdentifier("menu_press_pause_sound_effect3", "raw", context.packageName)

                else -> {
                    context.resources.getIdentifier("menu_error_8bit_sound_effect", "raw", context.packageName)
                }
            }

            val mediaPlayer = MediaPlayer.create(context, resID).start()

        }

    }
}