package com.example.hyperponggruppb

import android.content.ContentValues.TAG
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log

object BrickStructure {

    fun makeBricks(brickRow: MutableList<Rect>, top: Int, bottom: Int): MutableList<Rect> {

        var left = 20
        // top 5f
        var right = 70
        //bottom 20f


        for (i  in 0..18){

            var brick = Bricks(left, top, right, bottom)
            var brickRect = Rect(brick.brickLeft, brick.brickTop, brick.brickRight, brick.brickBottom)
            brickRow.add(brickRect)
            left += 55
            right += 55
        }


        return brickRow
    }

    fun rNG(a: Int, b: Int): Int {

        return (a..b).random()
    }

    fun randomColor(id: Int): Int {


        return when (id){

            1 -> Color.BLUE
            2 -> Color.RED
            3 -> Color.GRAY
            4 -> Color.MAGENTA
            5 -> Color.GREEN
            else -> {Color.YELLOW}
        }
    }

    fun fillColors(colors: MutableList<Int>): MutableList<Int>{


        for(i in 0..18){
            colors.add(randomColor(rNG(1,5)))
        }
        return colors
    }
}