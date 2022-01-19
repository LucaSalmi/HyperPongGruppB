package com.example.hyperponggruppb.model

import android.content.Context
import android.graphics.*
import com.example.hyperponggruppb.controller.Player
import com.example.hyperponggruppb.controller.PowerUp
import com.example.hyperponggruppb.controller.BrickStructure
import com.example.hyperponggruppb.controller.PlayerManager

class GameManager(var context: Context?, var isStoryMode: Boolean) {

    var brickRow = mutableListOf<Bricks>()
    var ballsArray = mutableListOf<Ball>()
    var powerUpArray = mutableListOf<PowerUp>()
    lateinit var ball: Ball
    lateinit var extraBall: Ball
    lateinit var player: Player
    lateinit var projectile: Gun
    var projectileWidth = 5
    var projectileHeight = 10
    var isGunLive = false
    var shotCount = 0
    var patternId = 0

    var isShieldActive = false

    init {

        clearArrays()
        makePlayer()
        makeBall()
        makeBricks()

        if (isStoryMode) {
            calculateMaxScore()
        }
    }

    private fun calculateMaxScore() {
        var totalBricks = brickRow.size
        PlayerManager.currentMaxScore = totalBricks * BrickStructure.brickScoreValue

    }

    fun makePlayer() {

        player = Player()
        player.left = AssetManager.bGWidth / 2 - player.playerWidth / 2
        player.right = AssetManager.bGWidth / 2 + player.playerWidth / 2
        player.top =
            AssetManager.getScreenHeight() - (AssetManager.getScreenHeight() * 0.2).toFloat() - player.playerHeight / 2 - 100
        player.bottom =
            AssetManager.getScreenHeight() - (AssetManager.getScreenHeight() * 0.2).toFloat() + player.playerHeight / 2 - 100
        player.update()
    }

    fun makeBall() {
        ball = Ball()
        ball.ballLeft = ((player.right - player.playerWidth / 2) - ball.ballsize / 2).toInt()
        ball.ballRight = ((player.right - player.playerWidth / 2) + ball.ballsize / 2).toInt()
        ball.ballTop = (player.top - ball.ballsize).toInt()
        ball.ballBottom = (player.top).toInt()
        ball.update()
        ballsArray.add(ball)
    }

    fun respawnBall() {

        ball = Ball()
        ball.ballLeft = ((player.right - player.playerWidth / 2) - ball.ballsize / 2).toInt()
        ball.ballRight = ((player.right - player.playerWidth / 2) + ball.ballsize / 2).toInt()
        ball.ballTop = (player.top - ball.ballsize).toInt()
        ball.ballBottom = (player.top).toInt()
        ball.update()
        ballsArray.add(ball)

        ball.ballSpeedX = 0f
        ball.ballSpeedY = 0f
    }

    fun spawnExtraBall() {
        extraBall = Ball()
        extraBall.ballLeft = ((player.right - player.playerWidth / 2) - ball.ballsize / 2).toInt()
        extraBall.ballRight = ((player.right - player.playerWidth / 2) + ball.ballsize / 2).toInt()
        extraBall.ballTop = (player.top - ball.ballsize).toInt()
        extraBall.ballBottom = (player.top).toInt()
        extraBall.update()
        extraBall.ballSpeedX = 7f
        extraBall.ballSpeedY = -13f
        ballsArray.add(extraBall)
    }

    fun multiBall() {
        extraBall = Ball()
        extraBall.ballLeft = ((player.right - player.playerWidth / 2) - ball.ballsize / 2).toInt()
        extraBall.ballRight = ((player.right - player.playerWidth / 2) + ball.ballsize / 2).toInt()
        extraBall.ballTop = (player.top - ball.ballsize).toInt()
        extraBall.ballBottom = (player.top).toInt()
        extraBall.update()
        extraBall.ballSpeedX = 7f
        extraBall.ballSpeedY = -13f
        ballsArray.add(extraBall)
        extraBall = Ball()

        extraBall.ballLeft = ((player.right - player.playerWidth / 2) - ball.ballsize / 2).toInt()
        extraBall.ballRight = ((player.right - player.playerWidth / 2) + ball.ballsize / 2).toInt()
        extraBall.ballTop = (player.top - ball.ballsize).toInt()
        extraBall.ballBottom = (player.top).toInt()
        extraBall.update()
        extraBall.ballSpeedX = -7f
        extraBall.ballSpeedY = -13f
        ballsArray.add(extraBall)

    }

    private fun makeBricks() {

        val brickwidth = (AssetManager.bGWidth / 10) - 4
        val brickheight = (brickwidth * 0.6).toInt()
        BrickStructure.left = 7
        BrickStructure.top = 5
        BrickStructure.right = brickwidth + BrickStructure.left
        BrickStructure.bottom = brickheight + BrickStructure.top

        AssetManager.brickwidth = brickwidth
        AssetManager.brickheight = brickheight

        BrickStructure.makeInboundsBricks(brickRow, this.isStoryMode)

        patternId = if (!isStoryMode) {
            RandomNumberGenerator.rNG(0, 13)
        } else {
            PlayerManager.currentLevel - 1
        }

        brickRow = BrickStructure.createPattern(brickRow, patternId)

        if (!isStoryMode) {
            makeOOBBricks()
        }
    }

    fun makeOOBBricks() {

        var tempArray = mutableListOf<Bricks>()
        tempArray = BrickStructure.makeOOBBricks(tempArray)
        tempArray = BrickStructure.createOOBBPattern(tempArray, RandomNumberGenerator.rNG(0, 13))
        brickRow.addAll(tempArray)
    }


    fun gunPowerUp() {

        projectile = Gun(
            ((player.right - player.playerWidth / 2) - projectileWidth).toInt(),
            (player.top - projectileHeight).toInt(),
            ((player.right - player.playerWidth / 2) + projectileWidth).toInt(),
            player.top.toInt()
        )
        projectile.update()

        isGunLive = true
    }

    fun activateShield(){
        isShieldActive = true
    }

    fun drawBricks(canvas: Canvas){

        for (brick in brickRow) {

            val brickColor = Paint()
            brickColor.color = Color.TRANSPARENT
            var brickRect =
                Rect(brick.brickLeft, brick.brickTop, brick.brickRight, brick.brickBottom)
            canvas.drawRect(brickRect, brickColor)
            canvas.drawBitmap(
                brick.asset,
                brick.brickLeft.toFloat() - 5,
                brick.brickTop.toFloat() - 5,
                null
            )
        }
    }

    fun drawPowerUp(canvas: Canvas){

        for (powerUp in powerUpArray) {

            powerUp.draw(canvas)
            canvas.drawBitmap(
                powerUp.assignAsset(),
                powerUp.left.toFloat(),
                powerUp.top.toFloat(),
                null
            )
        }
    }

    fun drawBall(canvas: Canvas){

        for (ballObj in ballsArray) {

            ballObj.draw(canvas)

            canvas.drawBitmap(
                AssetManager.ballAsset,
                ballObj.ballRect.left.toFloat() - ballObj.ballsize / 2,
                ballObj.ballRect.top.toFloat() - ballObj.ballsize / 2,
                null
            )
        }
    }

    fun drawProjectile(canvas: Canvas){
        projectile.draw(canvas)
    }

    fun clearArrays() {

        ballsArray.clear()
        brickRow.clear()
        powerUpArray.clear()
    }
}