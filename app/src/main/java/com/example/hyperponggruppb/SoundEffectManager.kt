package com.example.hyperponggruppb

import android.content.Context
import android.media.MediaPlayer
import java.util.concurrent.CopyOnWriteArrayList

object SoundEffectManager {

        var mediaPlayer: MediaPlayer? = null

    fun jukebox(context: Context, id: Int){
        mediaPlayer?.stop()
        mediaPlayer?.release()
        var isPlaying = false

        if (!isPlaying){

            var resID: Int = when(id){

                0 -> context.resources.getIdentifier("menu_error_8bit_sound_effect_short", "raw", context.packageName)
                1 -> context.resources.getIdentifier("menu_press_pause_sound_effect_short", "raw", context.packageName)
                2 -> context.resources.getIdentifier("classic_arcade_billow_power_up_custom", "raw", context.packageName)
                3 -> context.resources.getIdentifier("robotic_power_down_2", "raw", context.packageName)


                else -> {

                    context.resources.getIdentifier("menu_error_8bit_sound_effect_short", "raw", context.packageName)

                }
            }

            //val mediaPlayer = MediaPlayer.create(context, resID).stop()

            mediaPlayer = MediaPlayer.create(context, resID)
            mediaPlayer?.start()

        }

    }
}