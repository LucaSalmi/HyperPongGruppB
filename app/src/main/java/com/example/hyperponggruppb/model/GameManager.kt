package com.example.hyperponggruppb.model

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.util.Log
import com.example.hyperponggruppb.controller.Player
import com.example.hyperponggruppb.controller.PowerUp
import com.example.hyperponggruppb.controller.BrickStructure
import com.example.hyperponggruppb.controller.PlayerManager

class GameManager(var context: Context?, var isStoryMode: Boolean){

    var brickRow = mutableListOf<Rect>()
    var brickAssets = mutableListOf<Bitmap>()
    var ballsArray = mutableListOf<Ball>()
    var powerUpArray = mutableListOf<PowerUp>()
    lateinit var ball: Ball
    var ballLeft = 0
    var ballRight = 0
    var ballTop = 0
    var ballBottom = 0
    lateinit var extraBall: Ball
    lateinit var player: Player
    var patternId = 0

    init {
        clearArrays()
        makePlayer()
        makeBall()
        makeBricks()
        makeAssets()
    }

    fun makePlayer(){

        player = Player()
        player.left = AssetManager.getScreenWidth() / 2 - player.playerWidth / 2
        player.right = AssetManager.getScreenWidth() / 2 + player.playerWidth / 2
        player.top =
            AssetManager.getScreenHeight() - (AssetManager.getScreenHeight() * 0.2).toFloat() - player.playerHeight / 2 - 100
        player.bottom =
            AssetManager.getScreenHeight() - (AssetManager.getScreenHeight() * 0.2).toFloat() + player.playerHeight / 2 - 100
        player.update()
    }

    fun makeBall(){
        ball = Ball()
        ball.ballLeft = player.right.toInt() - (player.playerWidth / 2).toInt() - (ball.ballsize/2).toInt()
        ball.ballRight = player.right.toInt() - (player.playerWidth / 2).toInt() + (ball.ballsize/2).toInt()
        ball.ballTop = player.right.toInt() - (player.playerWidth / 2).toInt() - ball.ballsize.toInt()
        ball.ballBottom = player.right.toInt() - (player.playerWidth / 2).toInt() + ball.ballsize.toInt()
        ballsArray.add(ball)
    }

    fun respawnBall(){

        ball = Ball()
        ball.paint.color = Color.WHITE
        ballsArray.add(ball)

        ball.ballSpeedX = 0f
        ball.ballSpeedY = 0f
    }

    fun spawnExtraBall(){
        extraBall = Ball()
        ballsArray.add(extraBall)
        extraBall.ballLeft = player.right.toInt() - (player.playerWidth / 2).toInt() - (ball.ballsize/2).toInt()
        extraBall.ballRight = player.right.toInt() - (player.playerWidth / 2).toInt() + (ball.ballsize/2).toInt()
        extraBall.ballTop = player.right.toInt() - (player.playerWidth / 2).toInt() - ball.ballsize.toInt()
        extraBall.ballBottom = player.right.toInt() - (player.playerWidth / 2).toInt() + ball.ballsize.toInt()
        extraBall.ballSpeedX = 7f
        extraBall.ballSpeedY = -13f
        ballsArray.add(extraBall)
    }

    private fun makeBricks() {

        val brickwidth = (AssetManager.getScreenWidth() / 10) - 4
        val brickheight = (brickwidth * 0.6).toInt()
        BrickStructure.left = 7
        BrickStructure.top = 5
        BrickStructure.right = brickwidth + BrickStructure.left
        BrickStructure.bottom = brickheight + BrickStructure.top

        AssetManager.brickwidth = brickwidth
        AssetManager.brickheight = brickheight

        BrickStructure.makeInboundsBricks(brickRow)

        patternId = if (!isStoryMode) {
            RandomNumberGenerator.rNG(0, 13)
        } else {
            PlayerManager.currentLevel - 1
        }
        brickRow = BrickStructure.createPattern(brickRow, patternId)

        if (!isStoryMode) {
            BrickStructure.makeOOBBricks(brickRow)
        }
    }

    fun makeOOBBricks(){

        var tempArray = mutableListOf<Rect>()
        tempArray = BrickStructure.makeOOBBricks(tempArray)
        tempArray = BrickStructure.createOOBBPattern(tempArray, RandomNumberGenerator.rNG(0,13))
        brickRow.addAll(tempArray)
    }

    fun makeAssets(){

        AssetManager.fillAssetArray(brickAssets, brickRow.size, patternId)
    }

    fun clearArrays(){

        ballsArray.clear()
        brickRow.clear()
        powerUpArray.clear()
        brickAssets.clear()
    }
}