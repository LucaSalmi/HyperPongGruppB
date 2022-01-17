package com.example.hyperponggruppb.controller

import android.content.Context
import android.media.MediaPlayer
import com.example.hyperponggruppb.model.RandomNumberGenerator

object SoundEffectManager {

    var soundEffectPlayer: MediaPlayer? = null
    var backgroundPlayer: MediaPlayer? = null
    var starSoundPlayer: MediaPlayer? = null
    var brickHitPlayer: MediaPlayer? = null

    fun musicSetup(context: Context, trackId: Int) {
        setupBGMusic(context, trackId)
        playBGMusic()
    }

    private fun playBGMusic() {

        if (backgroundPlayer != null && PlayerManager.isMusicActive) {

            if (!backgroundPlayer!!.isPlaying) {

                backgroundPlayer?.start()

            }
        }
    }

    private fun setupBGMusic(context: Context, trackId: Int) {

        var resID = when (trackId) {
            0 -> context.resources.getIdentifier(
                "hyper_pong_main_theme_3",
                "raw",
                context.packageName
            )
            1 -> context.resources.getIdentifier(
                "crystal_cave_ambience",
                "raw",
                context.packageName
            )
            2 -> context.resources.getIdentifier(
                "theme_part_1",
                "raw",
                context.packageName
            )
            3 -> context.resources.getIdentifier(
                "theme_part_2",
                "raw",
                context.packageName
            )
            4 -> context.resources.getIdentifier(
                "theme_part_3",
                "raw",
                context.packageName
            )
            5 -> context.resources.getIdentifier(
                "theme_part_4",
                "raw",
                context.packageName
            )
            6 -> context.resources.getIdentifier(   // New Theme try
                "hyper_pong_theme_v5_infinity_mode",
                "raw",
                context.packageName
            )
            7 -> context.resources.getIdentifier(  // DwtD blood moon
                "blood_moon",
                "raw",
                context.packageName
            )
            8 -> context.resources.getIdentifier(  // DwtD that house
                "that_house",
                "raw",
                context.packageName
            )
            else -> context.resources.getIdentifier(
                "hyper_pong_main_theme_3",
                "raw",
                context.packageName
            )
        }
        backgroundPlayer = MediaPlayer.create(context, resID)
        backgroundPlayer!!.isLooping = true
    }

    fun stopMusic() {

        backgroundPlayer?.stop()
        backgroundPlayer?.release()
    }

    fun jukebox(context: Context, id: Int) {

        soundEffectPlayer?.stop()
        soundEffectPlayer?.release()


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

        soundEffectPlayer = MediaPlayer.create(context, resID)

        if (PlayerManager.isSoundEffectsActive){
            soundEffectPlayer?.start()
        }
    }

    fun playStarSound(context: Context){

         starSoundPlayer = MediaPlayer.create(context, context.resources.getIdentifier(
        "star_sound_effect",
        "raw",
        context.packageName
        ))

        if (PlayerManager.isSoundEffectsActive) {
            starSoundPlayer?.start()
        }
    }

    fun playBrickHitSound(context: Context, id: Int) {

        brickHitPlayer?.stop()
        brickHitPlayer?.release()

        var resID: Int = when (id){

            0 -> context.resources.getIdentifier(
                "rock_hit_1",
                "raw",
                context.packageName
            )
            1 -> context.resources.getIdentifier(
                "rock_hit_2",
                "raw",
                context.packageName
            )
            2 -> context.resources.getIdentifier(
                "rock_hit_3",
                "raw",
                context.packageName
            )
            3 -> context.resources.getIdentifier(
                "rock_hit_4",
                "raw",
                context.packageName
            )
            4 -> context.resources.getIdentifier(
                "rock_hit_5",
                "raw",
                context.packageName
            )
            else -> {
                context.resources.getIdentifier(
                    "rock_hit_6",
                    "raw",
                    context.packageName
                )
            }
        }

        brickHitPlayer = MediaPlayer.create(context, resID)

        if (PlayerManager.isSoundEffectsActive){
            brickHitPlayer?.start()
        }

    }
}