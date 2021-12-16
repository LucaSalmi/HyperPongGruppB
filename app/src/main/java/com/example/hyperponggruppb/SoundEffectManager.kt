package com.example.hyperponggruppb

import android.content.Context
import android.media.MediaPlayer

object SoundEffectManager {

    var soundEffectPlayer: MediaPlayer? = null
    var backgroundPlayer: MediaPlayer? = null

    fun bGMusic(context: Context){
        backgroundPlayer = MediaPlayer.create(context, context.resources.getIdentifier("hyper_pong_main_theme_3", "raw", context.packageName))
        backgroundPlayer?.start()
    }

    fun jukebox(context: Context, id: Int) {
        soundEffectPlayer?.stop()
        soundEffectPlayer?.release()
        var isPlaying = false

        if (!isPlaying) {

            var resID: Int = when (id) {

                0 -> context.resources.getIdentifier(
                    "menu_error_8bit_sound_effect_short",
                    "raw",
                    context.packageName
                )
                1 -> context.resources.getIdentifier(
                    "menu_press_pause_sound_effect_short",
                    "raw",
                    context.packageName
                )
                2 -> context.resources.getIdentifier(
                    "classic_arcade_billow_power_up_custom",
                    "raw",
                    context.packageName
                )
                3 -> context.resources.getIdentifier(
                    "robotic_power_down_2",
                    "raw",
                    context.packageName
                )


                else -> {

                    context.resources.getIdentifier(
                        "menu_error_8bit_sound_effect_short",
                        "raw",
                        context.packageName
                    )

                }
            }

            //val mediaPlayer = MediaPlayer.create(context, resID).stop()

            soundEffectPlayer = MediaPlayer.create(context, resID)
            soundEffectPlayer?.start()

        }

    }
}