package com.example.hyperponggruppb.controller

import android.content.ContentValues.TAG
import android.util.Log
import com.example.hyperponggruppb.model.AssetManager
import com.example.hyperponggruppb.model.Bricks
import com.example.hyperponggruppb.model.RandomNumberGenerator

object BrickStructure {

    var firstBrickRow = 9
    var rowOfBricks = 10
    var totalSumOfBricks = firstBrickRow + (rowOfBricks * 9)

    var brickScoreValue = 10

//brick base positions

    var brickWidth = AssetManager.bGWidth/10
    var brickHeight = (brickWidth *0.6).toInt()

    var left = 0
    var top = 0
    var right = brickWidth
    var bottom = brickHeight
    //var brickWidth = right - left

// the amount of tempo the bricks is falling AKA playerSpeed illusion

    var playerSpeed = 1


    /**
     * makes the first ten rows of bricks, visible to the player on startup
     */
    fun makeInboundsBricks(
        brickRow: MutableList<Bricks>,
        isStoryMode: Boolean
    ): MutableList<Bricks> {

        var leftInBounds = left
        var topInBounds = top
        var rightInBounds = right
        var bottomInBounds = bottom

        for (i in 0..(totalSumOfBricks)) {

            var brick = Bricks(
                leftInBounds,
                topInBounds,
                rightInBounds,
                bottomInBounds,
                10
            )

            if (!isStoryMode){
                brick.asset = AssetManager.brickAsset(RandomNumberGenerator.rNG(1,8))
            }

            brickRow.add(brick)
            leftInBounds += right - left
            rightInBounds += right - left

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
    fun makeOOBBricks(brickRow: MutableList<Bricks>): MutableList<Bricks> {

        var oOBLeft = left
        var oOBTop = -(bottom - top)
        var oOBright = right
        var oOBBottom = 0


        for (i in 0..(totalSumOfBricks)) {

            val brick = Bricks(oOBLeft, oOBTop, oOBright, oOBBottom,10)
            //brick.asset = AssetManager.brickAsset(RandomNumberGenerator.rNG(1,8))
            brickRow.add(brick)
            oOBLeft += right - left
            oOBright += right - left

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

    private fun storyModePattern(id: Int): String{
        return when (id) {
            //world 1
            0 -> "ACA0CC0ACAAXA0AA0AXAACA0CC0ACAA0A0YY0A0AACA0CC0ACAA0A0AA0A0AACA0CC0ACAA0A0000A0AAC000000CAA00000000A"//hellgate returns
            1 -> "0EC0EC00ECZC00C00EC0E00EZ0EC00C00EC00EZ0EC00EC0EC00EC00C00EC0EC00EC00E00EC0EC00E0EC00C00ECEC00EC0EC0"//bacon-level
            2 -> "000Z00X00000CCCCCC00DAACCCCAAD0C0ACCA0C00C00AA00C00CDDAADDC0D0CD00DC0D00CCEECC000D00CC00D0D00000000D"//fireball demon level
            3 -> "BBB0ACA0BB0A00CAA00A0A0AXAXA0A0A0DC0AAAA0AAC000A0000CA0C0A0000CACDCAA00ACADADCA00BDADAADB0BB000A00BB" //dabbing goblin level
            4 -> "Z0AA00AA0ZABDBAABDBA0D0DBBD0D00D00BB00D0BDDBBBBDDBB00CCCC00BBC0E00E0CB0CE0000EC00BCC00CCB0000BBBB000"//evil pumpkin level
            //world 2
            5 -> "0000000000000000000001101101101111111111111011011110010010011101111011101000010100100001000011001100" // Alien Invader
            6 -> "0000000000000000000001101101101111111111111011011110010010011101111011101000010100100001000011001100" // Alien Invader??
            7 -> "1111111111000000000000100001000111001110111111111101110011100010000100000000000011111111110000000000" // TwoStar-level
            8 -> "0000000000110000001111000000110011111100001011010000111111000010110100001011010000101101000110000110" // Hjälm-level
            9 -> "0011000011011000011011111000100111111000001111110001111111100111111111111111111111111111110111111110" // Eld-level
            else -> "Invalid"

        }
    }

    private fun infinityModePattern(id: Int): String{

        return when (id) {

            0 -> "ACA0CC0ACAAXA0AA0AXAACA0CC0ACAA0A0YY0A0AACA0CC0ACAA0A0AA0A0AACA0CC0ACAA0A0000A0AAC000000CAA00000000A"//hellgate returns
            1 -> "0EC0EC00ECYC00C00EC0E00EY0EC00C00EC00EY0EC00EC0EC00EC00C00EC0EC00EC00E00EC0EC00E0EC00C00ECEC00EC0EC0"//flames-level
            2 -> "000Y00Y00000CCCCCC00DAACCCCAAD0C0ACCA0C00C00AA00C00CDDAADDC0D0CD00DC0D00CCEECC000D00CC00D0D00000000D"//fireball demon level
            3 -> "BBB0ACA0BB0A00CAA00A0A0AXAXA0A0A0DC0AAAA0AAC000A0000CA0C0A0000CACDCAA00ACADADCA00BDADAADB0BB000A00BB" //dabbing goblin level
            4 -> "Z0AA00AA0ZABDBAABDBA0D0DBBD0D00D00BB00D0BDDBBBBDDBB00CCCC00BBC0E00E0CB0CE0000EC00BCC00CCB0000BBBB000"//evil pumpkin level

            else -> "Invalid"

        }
    }

    private fun oOBPattern(id: Int): String{

        return when (id) {

            0 -> "ACA0CC0ACAAXA0AA0AXAACA0CC0ACAA0A0YY0A0AACA0CC0ACAA0A0AA0A0AACA0CC0ACAA0A0000A0AAC000000CAA00000000A"//hellgate returns
            1 -> "EC00EC0EC00EC00C00EC00EC0EC00E0EC00EC00E0EC00C00ECEC00EC0EC0C00EC00EY0E00EY0EC00YC00C00EC00EC0EC00EC"//flames-level
            2 -> "D00000000D0D00CC00D000CCEECC00D0CD00DC0D0CDDAADDC00C00AA00C00C0ACCA0C0DAACCCCAAD 00CCCCCC00000Y00Y000"//fireball demon level
            3 -> "BB000A00BB0BDADAADB00ACADADCA000CACDCAA000CA0C0A000AAC000A000A0DC0AAAA0A0AXAXA0A0A00CAA00ABBB0ACA0BB" //dabbing goblin level
            4 -> "000BBBB0000BCC00CCB00CE0000EC0BC0E00E0CBB00CCCC00BBDDBBBBDDB0D00BB00D00D0DBBD0D0ABDBAABDBAZ0AA00AA0Z"//evil pumpkin level

            else -> "Invalid"
        }
    }

    /**
     * by eliminating some bricks from the array this function creates a recognizable pattern
     */
    fun createPattern(brickRow: MutableList<Bricks>, id: Int, isStoryMode: Boolean): MutableList<Bricks> {

        val pattern = if (!isStoryMode){
            infinityModePattern(id)
        }else{
            storyModePattern(id)
        }

        var temBricks = mutableListOf<Bricks>()
        var index = 0

        if (pattern.length < brickRow.size) {
            return brickRow
        }

        for (element in pattern) {

            when(element){

                'A' -> {
                    brickRow[index].asset = AssetManager.brickAsset(1)
                    brickRow[index].hasPowerUp = if (RandomNumberGenerator.rNG(1,20) == 1){
                        5
                    }else 0
                    temBricks.add(brickRow[index])
                }
                'B' -> {
                    brickRow[index].asset = AssetManager.brickAsset(2)
                    brickRow[index].hasPowerUp = if (RandomNumberGenerator.rNG(1,20) == 1){
                        5
                    }else 0
                    temBricks.add(brickRow[index])
                }
                'C' -> {
                    brickRow[index].asset = AssetManager.brickAsset(3)
                    brickRow[index].hasPowerUp = if (RandomNumberGenerator.rNG(1,20) == 1){
                        5
                    }else 0
                    temBricks.add(brickRow[index])
                }
                'D' -> {
                    brickRow[index].asset = AssetManager.brickAsset(4)
                    brickRow[index].hasPowerUp = if (RandomNumberGenerator.rNG(1,20) == 1){
                        5
                    }else 0
                    temBricks.add(brickRow[index])
                }
                'E' -> {
                    brickRow[index].asset = AssetManager.brickAsset(5)
                    brickRow[index].hasPowerUp = if (RandomNumberGenerator.rNG(1,20) == 1){
                        5
                    }else 0
                    temBricks.add(brickRow[index])
                }
                'Z' -> {
                    brickRow[index].asset = AssetManager.brickAsset(6)
                    brickRow[index].hasPowerUp = 4 //Multiball
                    temBricks.add(brickRow[index])
                }
                'X' -> {
                    brickRow[index].asset = AssetManager.brickAsset(7)
                    brickRow[index].hasPowerUp = 6 // Gun
                    temBricks.add(brickRow[index])
                }
                'Y' -> {
                    brickRow[index].asset = AssetManager.brickAsset(8)
                    brickRow[index].hasPowerUp = 7 // Shield
                    temBricks.add(brickRow[index])
                }
                '1' ->{
                    temBricks.add(brickRow[index])
                }
            }
            index++
        }

        return temBricks
    }

    /**
     * creates more out of bounds bricks when needed
     */
    fun createOOBBPattern(brickRow: MutableList<Bricks>, id: Int): MutableList<Bricks> {

        val pattern = oOBPattern(id)

        var temOOBBricks = mutableListOf<Bricks>()
        var index = 0

        if (pattern.length < brickRow.size) {
            Log.d(TAG, "createOOBBPattern: pattern too long")
            return brickRow
        }

        for (element in pattern) {

            when(element){

                'A' -> {
                    brickRow[index].asset = AssetManager.brickAsset(1)
                    temOOBBricks.add(brickRow[index])
                }
                'B' -> {
                    brickRow[index].asset = AssetManager.brickAsset(2)
                    temOOBBricks.add(brickRow[index])
                }
                'C' -> {
                    brickRow[index].asset = AssetManager.brickAsset(3)
                    temOOBBricks.add(brickRow[index])
                }
                'D' -> {
                    brickRow[index].asset = AssetManager.brickAsset(4)
                    temOOBBricks.add(brickRow[index])
                }
                'E' -> {
                    brickRow[index].asset = AssetManager.brickAsset(5)
                    temOOBBricks.add(brickRow[index])
                }
                'Z' -> {
                    brickRow[index].asset = AssetManager.brickAsset(6)
                    temOOBBricks.add(brickRow[index])
                }
                'X' -> {
                    brickRow[index].asset = AssetManager.brickAsset(7)
                    temOOBBricks.add(brickRow[index])
                }
                'Y' -> {
                    brickRow[index].asset = AssetManager.brickAsset(8)
                    temOOBBricks.add(brickRow[index])
                }
                '1' ->{
                    temOOBBricks.add(brickRow[index])
                }
            }
            index++
        }
        return temOOBBricks
    }

    fun moveDownRow(brickRow: MutableList<Bricks>): MutableList<Bricks> {

        for (obj in brickRow) {

            obj.brickTop += playerSpeed
            obj.brickBottom += playerSpeed
        }
        return brickRow
    }
}