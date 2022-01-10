package com.example.hyperponggruppb.controller

import android.graphics.*
import com.example.hyperponggruppb.model.AssetManager

class PowerUp(var typeID: Int, var left: Int, var top: Int, var right: Int, var bottom: Int) {

    var powerUpColor = Paint()
    var isCatched = false
    var isToDestroy = false


    private val speed = 10
    var powerUpRect = Rect(left, top, right, bottom)

    fun update() {

        this.bottom += speed
        this.top += speed
        powerUpRect = Rect(left, top, right, bottom)
    }

    fun draw(canvas: Canvas) {

        powerUpColor.color = Color.TRANSPARENT
        canvas.drawRect(
            this.left.toFloat(),
            this.top.toFloat(),
            this.right.toFloat(),
            this.bottom.toFloat(),
            powerUpColor
        )

    }


    fun assignAsset(): Bitmap {

        return when (this.typeID) {

            0 -> AssetManager.powerUpAssetSpeedDown
            1 -> AssetManager.powerUpAssetSpeedUp
            2 -> AssetManager.powerUpAssetBigPaddle
            3 -> AssetManager.powerUpAssetSmallPaddle
            4 -> AssetManager.powerUpAssetMultiBall
            5 -> AssetManager.powerUpAssetHealthPlus
            else -> AssetManager.powerUpAssetBigPaddle
        }
    }

    // testing a powerup

    //typeID 0
    fun forceBrickDown(brickRow: MutableList<Rect>): MutableList<Rect>{
        for (obj in brickRow){
            obj.top += BrickStructure.brickHeight
            obj.bottom += BrickStructure.brickHeight
        }
        return brickRow
    }
    /*fun speedDown(timeTicks: Int): Int {
        PlayerManager.playTime - 3
        return timeTicks - 2
    }*/

    //typeID 1
    fun forceBrickUp(brickRow: MutableList<Rect>): MutableList<Rect>{
        for (obj in brickRow){
            obj.top -= BrickStructure.brickHeight
            obj.bottom -= BrickStructure.brickHeight
        }
        return brickRow
    }
    /*
    fun speedUp(timeTicks: Int): Int {
        PlayerManager.playTime + 2
        return timeTicks + +1
    }*/

    //typeID 2
    fun bigPaddle(player: Player) {

        if (player.smallPaddle){

            player.smallPaddle = false

        }else{

            player.bigPaddle = true
        }

    }

    //typeID 3
    fun smallPaddle(player: Player) {

        if (player.bigPaddle){

            player.bigPaddle = false

        }else{

            player.smallPaddle = true
        }
    }
    //TypeID 4


}