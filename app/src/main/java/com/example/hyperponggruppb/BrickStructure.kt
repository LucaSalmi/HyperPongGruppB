package com.example.hyperponggruppb

import android.content.ContentValues.TAG
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log

object BrickStructure {

    fun makeBricks(brickRow: MutableList<Bricks>, top: Float, bottom: Float): MutableList<Bricks> {

        var left = 20f
        // top 5f
        var right = 70f
        //bottom 20f


        for (i  in 0..18){

            var color = Paint()
            color.color = (randomColor(rNG(1,5)))
            brickRow.add(Bricks(left, top, right, bottom,color))
            left += 55f
            right += 55f
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
}