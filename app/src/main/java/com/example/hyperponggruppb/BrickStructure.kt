package com.example.hyperponggruppb

import android.content.ContentValues.TAG
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log

object BrickStructure {

    var firstBrickRow = 12
    var rowOfBricks = 13
    var totalSumOfBricks = firstBrickRow + (rowOfBricks*4)

    fun makeBricks(brickRow: MutableList<Rect>): MutableList<Rect> {

        var left = 10
        var top = 5
        var right = 70
        var bottom = 30


        for (i in 0..(totalSumOfBricks)) {

            var brick = Bricks(left, top, right, bottom)
            var brickRect =
                Rect(brick.brickLeft, brick.brickTop, brick.brickRight, brick.brickBottom)
            brickRow.add(brickRect)
            left += 85
            right += 85

            if (i == firstBrickRow || i == firstBrickRow + rowOfBricks || i == firstBrickRow + rowOfBricks*2
                || i == firstBrickRow + rowOfBricks*3 || i == firstBrickRow + rowOfBricks*4 ) {
                top += 35
                bottom += 35
                left = 10
                right = 70
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



        for (i in 0..(totalSumOfBricks)) {
            colors.add(randomColor(rNG(1, 5)))
        }
        return colors
    }
}