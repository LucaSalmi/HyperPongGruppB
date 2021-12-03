package com.example.hyperponggruppb

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

class Player(context: Context) {

    var left = 500f
    val top = 1790f
    var right = 700f
    val bottom = 1850f
    var paint = Paint()
    var speed = 5f
    val offset = right-left
    var playerRect: Rect = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())

    fun update(){
        playerRect = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
    }

    fun draw(canvas: Canvas?){
        canvas?.drawRect(playerRect, paint)
        //canvas?.drawRect(left, top, right, bottom, paint)
    }
}