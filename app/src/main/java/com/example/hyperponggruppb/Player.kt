package com.example.hyperponggruppb

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log

class Player(context: Context) {

// 500 -- 700

    var left = 500f
    var top = 1720f
    var right = 700f
    var bottom = 1750f
    var paint = Paint()
    var speed = 5f
    val offset = right-left
    var playerRect: Rect = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())

    fun update(){
        playerRect = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
    }

    fun draw(canvas: Canvas?){
        canvas?.drawRect(playerRect, paint)
    }
}