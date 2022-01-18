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

    lateinit var lavaBackground: Bitmap
    lateinit var lavaBackgroundTrans: Bitmap
    lateinit var transLavaToStone: Bitmap
    lateinit var transStoneToIce: Bitmap
    lateinit var transIceToLava: Bitmap
    lateinit var iceBackground: Bitmap
    lateinit var stoneBackground: Bitmap

    lateinit var playerAsset: Bitmap
    lateinit var bigPlayerAsset: Bitmap
    lateinit var smallPlayerAsset: Bitmap

    lateinit var ballAsset: Bitmap

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

    lateinit var powerUpAssetSpeedUp: Bitmap
    lateinit var powerUpAssetSpeedDown: Bitmap
    lateinit var powerUpAssetForceUp: Bitmap
    lateinit var powerUpAssetForceDown: Bitmap
    lateinit var powerUpAssetBigPaddle: Bitmap
    lateinit var powerUpAssetSmallPaddle: Bitmap
    lateinit var powerUpAssetMultiBall: Bitmap
    lateinit var powerUpAssetHealthPlus: Bitmap
    lateinit var powerUpAssetGem: Bitmap

    lateinit var comboCounterZero: Bitmap
    lateinit var comboCounterOne: Bitmap
    lateinit var comboCounterTwo: Bitmap
    lateinit var comboCounterThree: Bitmap
    lateinit var comboCounterFour: Bitmap
    lateinit var comboCounterFive: Bitmap
    lateinit var comboCounterSix: Bitmap
    lateinit var comboCounterSeven: Bitmap
    lateinit var comboCounterEight: Bitmap
    lateinit var comboCounterNine: Bitmap
    lateinit var comboCounterTen: Bitmap
    lateinit var comboCounterTenPlus: Bitmap

    lateinit var darkRectangleDeathZone: Bitmap
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

    var brickwidth = 110
    var brickheight = 70

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

        comboCounterZero = BitmapFactory.decodeResource(context.resources, R.drawable.x_zero).scale(counterWidth, counterHeight, true)
        comboCounterOne = BitmapFactory.decodeResource(context.resources, R.drawable.x_one).scale(counterWidth, counterHeight, true)
        comboCounterTwo = BitmapFactory.decodeResource(context.resources, R.drawable.x_two).scale(counterWidth, counterHeight, true)
        comboCounterThree = BitmapFactory.decodeResource(context.resources, R.drawable.x_three).scale(counterWidth, counterHeight, true)
        comboCounterFour = BitmapFactory.decodeResource(context.resources, R.drawable.x_four).scale(counterWidth, counterHeight, true)
        comboCounterFive = BitmapFactory.decodeResource(context.resources, R.drawable.x_five).scale(counterWidth, counterHeight, true)
        comboCounterSix = BitmapFactory.decodeResource(context.resources, R.drawable.x_six).scale(counterWidth, counterHeight, true)
        comboCounterSeven = BitmapFactory.decodeResource(context.resources, R.drawable.x_seven).scale(counterWidth, counterHeight, true)
        comboCounterEight = BitmapFactory.decodeResource(context.resources, R.drawable.x_eight).scale(counterWidth, counterHeight, true)
        comboCounterNine = BitmapFactory.decodeResource(context.resources, R.drawable.x_nine).scale(counterWidth, counterHeight, true)
        comboCounterTen = BitmapFactory.decodeResource(context.resources, R.drawable.x_ten).scale(counterWidth, counterHeight, true)
        comboCounterTenPlus = BitmapFactory.decodeResource(context.resources, R.drawable.x_ten_plus).scale(counterWidth, counterHeight, true)



        darkRectangleDeathZone = BitmapFactory.decodeResource(context.resources, R.drawable.dangerzone).scale(bGWidth, dangerZoneHeight, true)
    }

    fun fillAssetArray(
        assets: MutableList<Bitmap>,
        numberOfBricks: Int,
        id: Int
    ): MutableList<Bitmap> {
        val pattern = when (id) {

            1 -> "333333333323322113311221155112544554455445555555"
            else -> "111111111112321155511114114111141111114116781177711876111211456111571155555555116666666611123456781"
        }


        for (i in 0..(numberOfBricks)) {
            for (c in pattern) {
                assets.add(randomAsset((c.toString()).toInt()))

            }
        }
        return assets
    }

    fun randomAsset(id: Int): Bitmap {

        return when (id) {

            1 -> brickAssetV1
            2 -> brickAssetV2
            3 -> brickAssetV3
            4 -> brickAssetV4
            5 -> brickAssetV5
            6 -> brickAssetBlue
            7 -> brickAssetGreen
            8 -> brickAssetYellow
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