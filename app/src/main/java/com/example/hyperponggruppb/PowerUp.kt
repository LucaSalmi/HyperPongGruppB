package com.example.hyperponggruppb

import android.graphics.*
import javax.security.auth.Destroyable

class PowerUp(var typeID: Int, var left: Int, var top: Int, var right: Int, var bottom: Int){

    var powerUpColor = Paint()


    private val speed = 15
    var powerUpRect = Rect(left, top, right, bottom)

    fun update(){

        this.bottom += speed
        this.top += speed
        powerUpRect = Rect(left, top, right, bottom)
    }

    fun draw(canvas: Canvas){
        if (PhysicsEngine.isPowerUpLive){

            powerUpColor.color = Color.TRANSPARENT
            canvas.drawRect(this.left.toFloat(), this.top.toFloat(), this.right.toFloat(), this.bottom.toFloat(), powerUpColor)
        }
    }

/*
    fun assignAsset(){

        when(this.typeID){
            0 -> ""//AssetManager.speedDown
            1 -> ""//AssetManager.speedUp
            2 -> ""//AssetManager.BigPaddle
            3 -> ""//AssetManager.SmallPaddle
        }
    }

 */

    //typeID 0
    fun speedDown(timeTicks: Int): Int {

        return timeTicks -3
    }
    //typeID 1
    fun speedUp(timeTicks: Int): Int {

        return timeTicks + 2
    }
    //typeID 2
    fun bigPaddle(player: Player){

        player.smallPaddle = false
        player.bigPaddle = true
    }
    //typeID 3
    fun smallPaddle(player: Player){

        player.bigPaddle = false
        player.smallPaddle = true
    }


}