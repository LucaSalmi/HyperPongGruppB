package com.example.hyperponggruppb.model

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import androidx.core.graphics.scale
import com.example.hyperponggruppb.R
import com.example.hyperponggruppb.controller.BrickStructure

object AssetManager {
//Backgrounds
    lateinit var lavaBackground: Bitmap
    lateinit var lavaBackgroundTrans: Bitmap
    lateinit var transLavaToStone: Bitmap
    lateinit var transStoneToIce: Bitmap
    lateinit var transIceToLava: Bitmap
    lateinit var iceBackground: Bitmap
    lateinit var stoneBackground: Bitmap
//Player
    lateinit var playerAsset: Bitmap
    lateinit var bigPlayerAsset: Bitmap
    lateinit var smallPlayerAsset: Bitmap
//Ball
    lateinit var ballAsset: Bitmap
//Bricks
    lateinit var brickAssetV1: Bitmap
    lateinit var brickAssetV2: Bitmap
    lateinit var brickAssetV3: Bitmap
    lateinit var brickAssetV4: Bitmap
    lateinit var brickAssetV5: Bitmap
    lateinit var brickAssetBlue: Bitmap
    lateinit var brickAssetGreen: Bitmap
    lateinit var brickAssetYellow: Bitmap
    lateinit var brickAssetHardFullHP: Bitmap
    lateinit var brickAssetHardHalfHP: Bitmap
//Powerups
    lateinit var powerUpAssetSpeedUp: Bitmap
    lateinit var powerUpAssetSpeedDown: Bitmap
    lateinit var powerUpAssetForceUp: Bitmap
    lateinit var powerUpAssetForceDown: Bitmap
    lateinit var powerUpAssetBigPaddle: Bitmap
    lateinit var powerUpAssetSmallPaddle: Bitmap
    lateinit var powerUpAssetMultiBall: Bitmap
    lateinit var powerUpAssetHealthPlus: Bitmap
    lateinit var powerUpAssetGem: Bitmap
    lateinit var powerUpAssetGun: Bitmap
    lateinit var assetGunProjectile: Bitmap
    lateinit var powerUpAssetShield: Bitmap
    lateinit var activeShieldAsset: Bitmap
//Deathzone
    lateinit var darkRectangleDeathZone: Bitmap

    //various measures
    val bGHeight = getScreenHeight()
    val bGWidth = getScreenWidth()
    val transHeight = getScreenHeight()/3
    var bgRectOne = Rect(0, 0, bGWidth, bGHeight)
    var bgRectTransOne = Rect(0, bgRectOne.top - transHeight, bGWidth, bgRectOne.top)
    var bgRectTwo = Rect(0, bgRectTransOne.top - bGHeight, bGWidth, bgRectTransOne.top)
    var bgRectTransTwo = Rect(0, bgRectTwo.top - transHeight, bGWidth, bgRectTwo.top)

    var counterWidth = 220
    var counterHeight = 220


    var playerwidth = 200
    var playerhight = 40

    var ballwidth = 40
    var ballheight = 40

    var brickwidth = 100
    var brickheight = 60

    var powerUpHeight = 70
    var powerUpWidth = 70

    var dangerZoneHeight = 100



    fun prepareAssets(context: Context){
        lavaBackground = BitmapFactory.decodeResource(context.resources, R.drawable.lava_story_bg).scale(bGWidth,bGHeight, true)
        iceBackground = BitmapFactory.decodeResource(context.resources, R.drawable.ice_level_background).scale(bGWidth, bGHeight, true)
        stoneBackground = BitmapFactory.decodeResource(context.resources, R.drawable.cave_background).scale(bGWidth, bGHeight, true)

        lavaBackgroundTrans = BitmapFactory.decodeResource(context.resources, R.drawable.lava_story_bg).scale(bGWidth, transHeight, true)
        transLavaToStone = BitmapFactory.decodeResource(context.resources, R.drawable.lava_to_stone_background).scale(bGWidth, transHeight, true)
        transStoneToIce = BitmapFactory.decodeResource(context.resources, R.drawable.stone_to_ice_background).scale(bGWidth, transHeight, true)
        transIceToLava = BitmapFactory.decodeResource(context.resources, R.drawable.lava_story_bg).scale(bGWidth, transHeight, true)

        playerAsset = BitmapFactory.decodeResource(context.resources, R.drawable.player_pad_normal).scale(playerwidth, playerhight,true )
        bigPlayerAsset = BitmapFactory.decodeResource(context.resources, R.drawable.player_pad_wide).scale(playerwidth + playerwidth/2, playerhight,true )
        smallPlayerAsset = BitmapFactory.decodeResource(context.resources, R.drawable.player_pad_small).scale(playerwidth - playerwidth/2, playerhight,true )

        ballAsset = BitmapFactory.decodeResource(context.resources, R.drawable.ball_glow_simple).scale(ballwidth, ballheight,true)

        brickAssetV1 = BitmapFactory.decodeResource(context.resources, R.drawable.brick_v1).scale(brickwidth, brickheight,true)
        brickAssetV2 = BitmapFactory.decodeResource(context.resources, R.drawable.brick_v2).scale(brickwidth, brickheight,true)
        brickAssetV3 = BitmapFactory.decodeResource(context.resources, R.drawable.brick_v3).scale(brickwidth, brickheight,true)
        brickAssetV4 = BitmapFactory.decodeResource(context.resources, R.drawable.brick_v4).scale(brickwidth, brickheight,true)
        brickAssetV5 = BitmapFactory.decodeResource(context.resources, R.drawable.brick_v5).scale(brickwidth, brickheight,true)
        brickAssetBlue = BitmapFactory.decodeResource(context.resources, R.drawable.brick_blue_glow).scale(brickwidth, brickheight,true)
        brickAssetGreen = BitmapFactory.decodeResource(context.resources, R.drawable.brick_green_glow).scale(brickwidth, brickheight,true)
        brickAssetYellow = BitmapFactory.decodeResource(context.resources, R.drawable.brick_yellow_glow).scale(brickwidth, brickheight,true)
        brickAssetHardFullHP = BitmapFactory.decodeResource(context.resources, R.drawable.brick_hard_full_hp).scale(brickwidth, brickheight,true)
        brickAssetHardHalfHP = BitmapFactory.decodeResource(context.resources, R.drawable.brick_hard_half_hp).scale(brickwidth, brickheight,true)

        powerUpAssetSpeedUp = BitmapFactory.decodeResource(context.resources, R.drawable.speed_plus_simple).scale(powerUpWidth, powerUpHeight,true)
        powerUpAssetSpeedDown = BitmapFactory.decodeResource(context.resources, R.drawable.speed_minus_simple).scale(powerUpWidth, powerUpHeight,true)
        powerUpAssetBigPaddle = BitmapFactory.decodeResource(context.resources, R.drawable.playersize_plus_simple).scale(powerUpWidth, powerUpHeight,true)
        powerUpAssetSmallPaddle = BitmapFactory.decodeResource(context.resources, R.drawable.playersize_minus_simple).scale(powerUpWidth, powerUpHeight,true)
        powerUpAssetMultiBall = BitmapFactory.decodeResource(context.resources, R.drawable.multiball_plus_simple).scale(powerUpWidth, powerUpHeight,true)
        powerUpAssetHealthPlus = BitmapFactory.decodeResource(context.resources, R.drawable.hp_plus_simple).scale(powerUpWidth, powerUpHeight,true)
        powerUpAssetGem = BitmapFactory.decodeResource(context.resources, R.drawable.gem).scale(powerUpWidth, powerUpHeight,true)
        powerUpAssetGun = BitmapFactory.decodeResource(context.resources, R.drawable.gun_powerup).scale(powerUpWidth, powerUpHeight,true)
        assetGunProjectile = BitmapFactory.decodeResource(context.resources, R.drawable.gun_shot_projectile).scale(powerUpWidth, powerUpHeight,true)
        powerUpAssetShield = BitmapFactory.decodeResource(context.resources, R.drawable.powerup_shield_drop).scale(powerUpWidth, powerUpHeight,true)

        darkRectangleDeathZone = BitmapFactory.decodeResource(context.resources, R.drawable.dangerzone).scale(bGWidth, dangerZoneHeight, true)
        activeShieldAsset = BitmapFactory.decodeResource(context.resources, R.drawable.powerup_shield_effect).scale(bGWidth, dangerZoneHeight, true)
    }

    fun brickAsset(id: Int): Bitmap {

        return when (id) {

            1 -> brickAssetV1 //A
            2 -> brickAssetV2 //B
            3 -> brickAssetV3 //C
            4 -> brickAssetV4 //D
            5 -> brickAssetV5 //E
            6 -> brickAssetBlue //Z
            7 -> brickAssetGreen //X
            8 -> brickAssetYellow //Y
            9 -> brickAssetHardFullHP
            10 -> brickAssetHardHalfHP

            else -> {

                brickAssetV1
            }
        }
    }

    fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    fun moveBackGround(){

        bgRectOne.top += BrickStructure.playerSpeed
        bgRectOne.bottom += BrickStructure.playerSpeed
        bgRectTransOne.top += BrickStructure.playerSpeed
        bgRectTransOne.bottom += BrickStructure.playerSpeed
        bgRectTwo.top += BrickStructure.playerSpeed
        bgRectTwo.bottom += BrickStructure.playerSpeed
        bgRectTransTwo.top += BrickStructure.playerSpeed
        bgRectTransTwo.bottom += BrickStructure.playerSpeed
    }

    fun resetBackGround(){

        bgRectOne = Rect(0, 0, bGWidth, bGHeight)
        bgRectTransOne = Rect(0, bgRectOne.top - transHeight, bGWidth, bgRectOne.top)
        bgRectTwo = Rect(0, bgRectTransOne.top - bGHeight, bGWidth, bgRectTransOne.top)
        bgRectTransTwo = Rect(0, bgRectTwo.top - transHeight, bGWidth, bgRectTwo.top)
    }

    fun getBackground(id: Int): Bitmap{

        return when (id) {

            1 -> lavaBackground
            2 -> stoneBackground
            3 -> iceBackground
            else -> lavaBackground
        }
    }
    fun getTransBackground(id: Int): Bitmap{

        return when (id) {

            1 -> transLavaToStone
            2 -> transStoneToIce
            3 -> transIceToLava
            else -> transLavaToStone
        }
    }
}