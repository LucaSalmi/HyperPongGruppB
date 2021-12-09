package com.example.hyperponggruppb

import android.content.ContentValues.TAG
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log

object BrickStructure {

    var firstBrickRow = 9
    var rowOfBricks = 10
    var totalSumOfBricks = firstBrickRow + (rowOfBricks * 9)

    fun makeBricks(brickRow: MutableList<Rect>): MutableList<Rect> {

        var left = 15
        var top = 5
        var right = 115
        var bottom = 65


        for (i in 0..(totalSumOfBricks)) {

            var brick = Bricks(left, top, right, bottom)
            var brickRect =
                Rect(brick.brickLeft, brick.brickTop, brick.brickRight, brick.brickBottom)
            brickRow.add(brickRect)
            left += 105
            right += 105

            if (i == firstBrickRow || i == firstBrickRow + rowOfBricks || i == firstBrickRow + rowOfBricks * 2
                || i == firstBrickRow + rowOfBricks * 3 || i == firstBrickRow + rowOfBricks * 4 || i == firstBrickRow + rowOfBricks * 5
                || i == firstBrickRow + rowOfBricks * 6 || i == firstBrickRow + rowOfBricks * 7 || i == firstBrickRow + rowOfBricks * 8
                || i == firstBrickRow + rowOfBricks * 9
            ) {
                top += 65
                bottom += 65
                left = 15
                right = 115
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

    fun fillColors(colors: MutableList<Int>, numberOfBricks: Int): MutableList<Int> {


        for (i in 0..(numberOfBricks)) {
            colors.add(randomColor(rNG(1, 5)))
        }
        return colors
    }

    fun createPattern(brickRow: MutableList<Rect>, id: Int): MutableList<Rect> {

        val patternOne = when(id){

            0 -> "1111111111100011000111101101111011111101100011000110001100011000110001100000000110000000011000000001"
            1 -> "1111111111100000000110000010011000011001101111100110011110011000111101100011111110001100011000100001"
            else -> "Invalid"
        }

        var temBricks = mutableListOf<Rect>()
        var index = 0

        if(patternOne.length < brickRow.size){
            return brickRow
        }
        
        for(element in patternOne){

            if ( element == '1'){
                
                temBricks.add(brickRow[index])
                
            }
            index++
        }
        return temBricks


    }
}