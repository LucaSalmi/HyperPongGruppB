package com.example.hyperponggruppb

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

class Player(context: Context) {


    var left = 500f
    var top = 1620f
    var right = 700f
    var bottom = 1650f
    var paint = Paint()
    val playerSize = right-left
    var playerRect: Rect = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())

    fun update(){
        playerRect = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
    }

    fun draw(canvas: Canvas?){
        canvas?.drawRect(playerRect, paint)
    }
}