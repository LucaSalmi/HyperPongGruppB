package com.example.hyperponggruppb

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint

class Player(context: Context) {

    var left = 500f
    val top = 1790f
    var right = 600f
    val bottom = 1800f
    var paint = Paint()
    var speed = 5f
    val offset = right-left

    fun update(){
        left -= speed
        right += speed
    }

    fun draw(canvas: Canvas?){
        canvas?.drawRect(left, top, right, bottom, paint)
    }
}