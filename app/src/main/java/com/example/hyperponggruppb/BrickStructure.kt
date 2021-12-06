package com.example.hyperponggruppb

import android.content.ContentValues.TAG
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log

object BrickStructure {

    fun makeBricks(brickRow: MutableList<Rect>): MutableList<Rect> {

        var left = 0
        var top = 5
        var right = 80
        var bottom = 30

        for (i in 0..36) {

            var brick = Bricks(left, top, right, bottom)
            var brickRect =
                Rect(brick.brickLeft, brick.brickTop, brick.brickRight, brick.brickBottom)
            brickRow.add(brickRect)
            left += 80
            right += 80

            if (i == 11 || i == 22 || i == 33) {
                top += 35
                bottom += 35
                left = 0
                right = 80
            }
        }


        return brickRow
    }

    fun rNG(a: Int, b: Int): Int {

        return (a..b).random()
    }

    fun randomColor(id: Int): Int {


        return when (id) {

            1 -> Color.BLUE
            2 -> Color.RED
            3 -> Color.GRAY
            4 -> Color.MAGENTA
            5 -> Color.GREEN
            else -> {
                Color.YELLOW
            }
        }
    }

    fun fillColors(colors: MutableList<Int>): MutableList<Int> {


        for (i in 0..36) {
            colors.add(randomColor(rNG(1, 5)))
        }
        return colors
    }
}