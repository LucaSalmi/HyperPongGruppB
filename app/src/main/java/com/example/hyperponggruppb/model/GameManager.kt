package com.example.hyperponggruppb.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import com.example.hyperponggruppb.controller.Player
import com.example.hyperponggruppb.controller.PowerUp
import com.example.hyperponggruppb.controller.BrickStructure

class GameManager(var context: Context?){

    var brickRow = mutableListOf<Rect>()
    var brickAssets = mutableListOf<Bitmap>()
    var ballsArray = mutableListOf<Ball>()
    var powerUpArray = mutableListOf<PowerUp>()
    lateinit var ball: Ball
    lateinit var extraBall: Ball
    val ballRadius = 20f
    lateinit var player: Player

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
        ball = Ball(context!!,player.right - player.playerWidth / 2, player.top - ballRadius)
        ballsArray.add(ball)
    }

    fun respawnBall(){

        ball = Ball(context!!, (player.right - player.playerWidth / 2), (player.top - ballRadius))
        ball.paint.color = Color.TRANSPARENT
        ball.hitBoxPaint.color = Color.TRANSPARENT
        ballsArray.add(ball)
        ball.ballPosX = player.right - player.playerWidth / 2
        ball.ballPosY = player.top - ball.radius
        ball.ballSpeedX = 0f
        ball.ballSpeedY = 0f
    }

    fun spawnExtraBall(){
        extraBall = Ball(context!!, (player.right - player.playerWidth / 2), (player.top - ball.radius))
        extraBall.ballSpeedX = 7f
        extraBall.ballSpeedY = -13f
        ballsArray.add(extraBall)
    }

    fun makeBricks(){

        val brickwidth = (AssetManager.getScreenWidth() / 10) - 4
        val brickheight = (brickwidth * 0.6).toInt()
        BrickStructure.left = 7
        BrickStructure.top = 5
        BrickStructure.right = brickwidth + BrickStructure.left
        BrickStructure.bottom = brickheight + BrickStructure.top

        AssetManager.brickwidth = brickwidth
        AssetManager.brickheight = brickheight

        BrickStructure.makeInboundsBricks(brickRow)
        brickRow = BrickStructure.createPattern(brickRow, RandomNumberGenerator.rNG(0,2))
        BrickStructure.makeOOBBricks(brickRow)
    }

    fun makeAssets(){

        AssetManager.prepareAssets(context!!)
        AssetManager.fillAssetArray(brickAssets, brickRow.size, 1)
    }

    fun clearArrays(){

        ballsArray.clear()
        brickRow.clear()
        powerUpArray.clear()
        brickAssets.clear()
    }
}