package com.example.hyperponggruppb.controller

import android.graphics.Rect
import com.example.hyperponggruppb.model.Bricks

object BrickStructure {

    var firstBrickRow = 9
    var rowOfBricks = 10
    var totalSumOfBricks = firstBrickRow + (rowOfBricks * 9)

//brick base positions

    var left = 15
    var top = 5
    var right = 115
    var bottom = 65

    /**
     * makes the first ten rows of bricks, visible to the player on startup
     */
    fun makeInboundsBricks(brickRow: MutableList<Rect>): MutableList<Rect> {

        var leftInBounds = left
        var topInBounds = top
        var rightInBounds = right
        var bottomInBounds = bottom

        for (i in 0..(totalSumOfBricks)) {

            var brick = Bricks(leftInBounds, topInBounds, rightInBounds, bottomInBounds)
            var brickRect =
                Rect(brick.brickLeft, brick.brickTop, brick.brickRight, brick.brickBottom)
            brickRow.add(brickRect)
            leftInBounds += right - left + 4
            rightInBounds += right - left + 4

            if (i == firstBrickRow || i == firstBrickRow + rowOfBricks || i == firstBrickRow + rowOfBricks * 2
                || i == firstBrickRow + rowOfBricks * 3 || i == firstBrickRow + rowOfBricks * 4 || i == firstBrickRow + rowOfBricks * 5
                || i == firstBrickRow + rowOfBricks * 6 || i == firstBrickRow + rowOfBricks * 7 || i == firstBrickRow + rowOfBricks * 8
                || i == firstBrickRow + rowOfBricks * 9
            ) {
                topInBounds += bottom
                bottomInBounds += bottom
                leftInBounds = left
                rightInBounds = right
            }
        }

        return brickRow
    }

    /**
     * makes ten more rows outside of the canvas (and the player's sight) that will then roll down.
     */
    fun makeOOBBricks(brickRow: MutableList<Rect>): MutableList<Rect> {

        var oOBLeft = left
        var oOBTop = -(bottom - top)
        var oOBright = right
        var oOBBottom = 0


        for (i in 0..(totalSumOfBricks)) {

            var brick = Bricks(oOBLeft, oOBTop, oOBright, oOBBottom)
            var brickRect =
                Rect(brick.brickLeft, brick.brickTop, brick.brickRight, brick.brickBottom)
            brickRow.add(brickRect)
            oOBLeft += right - left + 4
            oOBright += right - left + 4

            if (i == firstBrickRow || i == firstBrickRow + rowOfBricks || i == firstBrickRow + rowOfBricks * 2
                || i == firstBrickRow + rowOfBricks * 3 || i == firstBrickRow + rowOfBricks * 4 || i == firstBrickRow + rowOfBricks * 5
                || i == firstBrickRow + rowOfBricks * 6 || i == firstBrickRow + rowOfBricks * 7 || i == firstBrickRow + rowOfBricks * 8
                || i == firstBrickRow + rowOfBricks * 9
            ) {
                oOBTop -= bottom
                oOBBottom -= bottom
                oOBLeft = left
                oOBright = right
            }
        }

        return brickRow
    }

    /**
     * by eliminating some bricks from the array this function creates a recognizable pattern
     */
    fun createPattern(brickRow: MutableList<Rect>, id: Int): MutableList<Rect> {

        val patternOne = when(id){

            0 -> "1111111111100011000111101101111011111101100011000110001100011000110001100000000110000000011000000001" // HellGate-level
            1 -> "1111111111100000000110000010011000011001101111100110011110011000111101100011111110001100011000100001" // Shuriken-Level
            2 -> "1000110001111011011100111111000000110000000011000000001100000001111000001111110000011110000000110000" // HangingDroplet-level
            3 -> "0000000000100000000111100001111111111111101111110100101101000000110000001111110000011110000000110000" // Alien Pedestal
            4 -> "1000000001101000010110101101011010110101101011010110101101011010110101101011010110100001011000000001" // Straight Row-level
            5 -> "0000000000000000000001101101101111111111111011011110010010011101111011101000010100100001000011001100" // Alien Invader
            6 -> "0000000000000000000001101101101111111111111011011110010010011101111011101000010100100001000011001100" // Alien Invader??
            7 -> "1111111111000000000000100001000111001110111111111101110011100010000100000000000011111111110000000000" // TwoStar-level
            8 -> "0000000000110000001111000000110011111100001011010000111111000010110100001011010000101101000110000110" // Hjälm-level
            9 -> "0011000011011000011011111000100111111000001111110001111111100111111111111111111111111111110111111110" // Eld-level
            10 -> "1111111111010011001010101101010100110010111100111100101101000001111000000011000011111111110000000000" // Shield-level
            11 -> "1111111111000011000000010010000010000100011111111000100001000001001000000011000011111111110000000000" // BigBall-level
            12 -> "1000000001010111101000100001000101001010010011001001001100100101001010001000010001011110101000000001" // CrissCross-Level
            13 -> "1100000011010100101001010010100101001010010100101001010010100101001010010100101001010010101100000011" // Allmän Rows-Level
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

    fun moveDownRow(brickRow: MutableList<Rect>): MutableList<Rect>{

        for (obj in brickRow){
            obj.top += (bottom /32.5).toInt()
            obj.bottom += (bottom /32.5).toInt()
        }
        return brickRow
    }
}