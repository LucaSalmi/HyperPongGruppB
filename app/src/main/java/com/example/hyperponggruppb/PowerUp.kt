package com.example.hyperponggruppb

import android.graphics.*
import javax.security.auth.Destroyable

class PowerUp(var typeID: Int, var left: Int, var top: Int, var right: Int, var bottom: Int): Destroyable {

    var color = Paint()

    //var powerUpImg: Bitmap = AssetManager.PowerUpType(TypeID)
    private val speed = 15
    var powerUpRect = Rect(left, top, right, bottom)

    fun update(){

        this.bottom += speed
        this.top += speed
        powerUpRect = Rect(left, top, right, bottom)
    }

    fun draw(canvas: Canvas){
        if (PhysicsEngine.isPowerUpLive){

            color.color = Color.BLACK
            canvas.drawRect(this.left.toFloat(), this.top.toFloat(), this.right.toFloat(), this.bottom.toFloat(), color)
        }

    }
    //typeID 1
    fun speedUp(timeTicks: Int): Int {

        return timeTicks + 2
    }
    //typeID 0
    fun speedDown(timeTicks: Int): Int {

        return timeTicks -3
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