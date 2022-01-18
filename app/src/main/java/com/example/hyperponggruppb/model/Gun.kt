package com.example.hyperponggruppb.model

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

class Gun(var projLeft: Int,var projTop: Int,var projRight: Int,var projBottom: Int) {

    var projSpeedY = 20f
    var paint = Paint()
    var projRect = Rect()

    init {
        paint.color = Color.GREEN
    }

    fun update(){
        projRect = Rect(projLeft,projTop,projRight,projBottom)
    }

    fun draw(canvas: Canvas?) {
        canvas?.drawRect(projRect,paint)
    }
}