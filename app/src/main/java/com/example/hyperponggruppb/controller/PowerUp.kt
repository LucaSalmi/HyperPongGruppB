package com.example.hyperponggruppb.controller

import android.graphics.*
import com.example.hyperponggruppb.model.AssetManager
import com.example.hyperponggruppb.model.Bricks

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
            9 -> AssetManager.powerUpAssetGem
            else -> AssetManager.powerUpAssetBigPaddle
        }
    }

    //typeID 0
    fun forceBrickDown(brickRow: MutableList<Bricks>): MutableList<Bricks>{
        for (obj in brickRow){
            obj.brickTop += BrickStructure.brickHeight
            obj.brickBottom += BrickStructure.brickHeight
        }
        return brickRow
    }

    //typeID 1
    fun forceBrickUp(brickRow: MutableList<Bricks>): MutableList<Bricks>{
        for (obj in brickRow){
            obj.brickTop -= BrickStructure.brickHeight
            obj.brickBottom -= BrickStructure.brickHeight
        }
        return brickRow
    }

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
    //TypeID 4 Multiball handled in PsyduckEngine

    //TypeID 5 Gain Life handled in Player Manager

    //TypeID 6
    fun losePoints(){

        if (PlayerManager.playerPoints > 0){
            PlayerManager.removePoints(BrickStructure.brickScoreValue)

            if (PlayerManager.playerPoints < 0){
                PlayerManager.playerPoints = 0
            }
        }
    }

    //TypeID 7
    fun addTime(levelTimeLimit: Long): Long{

        // makes the countdown 5 seconds shorter
        return levelTimeLimit - 5000
    }

    //TypeID 8
    fun removeTime(levelTimeLimit: Long): Long{

        //makes the countdown 5 seconds longer
        return levelTimeLimit + 5000

    }

    //TypeID 9
    fun addGems(){

        PlayerManager.gems += 5
    }

    //TypeID 10
    fun gun(){

    }


}