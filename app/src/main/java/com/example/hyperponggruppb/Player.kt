package com.example.hyperponggruppb

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

class Player(context: Context) {


    var left = 0f
    var top = 0f
    var right = 200f
    var bottom = 30f
    var paint = Paint()
    var playerHeight = bottom - top
    var playerWidth = right - left
    var playerRect: Rect = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())

    fun update(){
        playerRect = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
    }

    fun draw(canvas: Canvas?){
        canvas?.drawRect(playerRect, paint)
    }
}