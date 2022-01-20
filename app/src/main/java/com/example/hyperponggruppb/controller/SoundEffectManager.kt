package com.example.hyperponggruppb.controller

import android.content.ContentValues.TAG
import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.example.hyperponggruppb.model.RandomNumberGenerator

object SoundEffectManager {

    var soundEffectPlayer: MediaPlayer? = null
    var backgroundPlayer: MediaPlayer? = null
    var starSoundPlayer: MediaPlayer? = null
    var brickHitPlayer: MediaPlayer? = null
    var comboAnnouncer: MediaPlayer? = null
    var powerUpSoundEffectPlayer: MediaPlayer? = null

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

    fun checkIfPlaying(): Boolean{

        if (backgroundPlayer != null){
            return backgroundPlayer?.isPlaying!!
        }
        return false
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
            2 -> context.resources.getIdentifier(   // New Theme try
                "hyper_pong_theme_v5_infinity_mode",
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

        if (backgroundPlayer?.isPlaying!!){

            backgroundPlayer?.stop()
            backgroundPlayer?.release()
        }

    }

    fun pauseMusic() {
        backgroundPlayer?.pause()
    }

    fun resumeMusic() {
        backgroundPlayer?.start()
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
            2 -> context.resources.getIdentifier(  // Menu Select sound
                "short_select_nice_for_menu",
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

        if (PlayerManager.isSoundEffectsActive) {
            soundEffectPlayer?.start()
        }
    }

    fun playPowerUpSounds(context: Context, id: Int) {

        var resID: Int = when (id) {

            0 -> context.resources.getIdentifier(
                "classic_arcade_billow_power_up_custom",
                "raw",
                context.packageName
            )
            1 -> context.resources.getIdentifier(
                "robotic_power_down_2",
                "raw",
                context.packageName
            )
            2 -> context.resources.getIdentifier(
                "gem_updated_sound_one",
                "raw",
                context.packageName
            )
            else -> {
                context.resources.getIdentifier(
                    "classic_arcade_billow_power_up_custom",
                    "raw",
                    context.packageName
                )
            }
        }

        powerUpSoundEffectPlayer = MediaPlayer.create(context, resID)

        if (PlayerManager.isSoundEffectsActive) {
            powerUpSoundEffectPlayer?.start()
        }
    }

    fun playStarSound(context: Context) {

        starSoundPlayer = MediaPlayer.create(
            context, context.resources.getIdentifier(
                "star_sound_effect",
                "raw",
                context.packageName
            )
        )

        if (PlayerManager.isSoundEffectsActive) {
            starSoundPlayer?.start()
        }
    }

    fun playComboAnnouncer(context: Context) {


        var resID = context.resources.getIdentifier(
            "hyper_combo_ultra_version_two",
            "raw",
            context.packageName
        )

        comboAnnouncer = MediaPlayer.create(context, resID)
        if (PlayerManager.isSoundEffectsActive) {
            comboAnnouncer?.start()
        }
    }

    fun playBrickHitSound(context: Context, id: Int) {

        brickHitPlayer?.stop()
        brickHitPlayer?.release()

        var resID: Int = when (id) {

            0 -> context.resources.getIdentifier(
                "rock_hit_final_extra",
                "raw",
                context.packageName
            )
            1 -> context.resources.getIdentifier(
                "rock_hit_final_standard",
                "raw",
                context.packageName
            )
            else -> {
                context.resources.getIdentifier(
                    "rock_hit_final_standard",
                    "raw",
                    context.packageName
                )
            }
        }

        brickHitPlayer = MediaPlayer.create(context, resID)

        if (PlayerManager.isSoundEffectsActive) {
            brickHitPlayer?.start()
        }

    }
}