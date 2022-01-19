package com.example.hyperponggruppb.controller

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.example.hyperponggruppb.model.AssetManager

class Player() {


    var left = 0f
    var top = 0f
    var right = 200f
    var bottom = 60f
    var paint = Paint()
    var bigPaddle = false
    var smallPaddle = false
    var playerWidth = right - left
    var playerHeight = bottom
    var playerRect: Rect = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())


    init {
        paint.color = Color.TRANSPARENT
    }

    fun update() {

        playerRect = when {

            bigPaddle -> {
                Rect(left.toInt()-50, top.toInt(), right.toInt()+50, bottom.toInt())
            }
            smallPaddle -> {
                Rect(left.toInt()+50, top.toInt(), right.toInt()-50, bottom.toInt())
            }
            else -> {
                Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
            }
        }
    }

    fun draw(canvas: Canvas?) {

        canvas?.drawRect(playerRect, paint)

        when {

            bigPaddle -> {

                canvas?.drawBitmap(
                    AssetManager.bigPlayerAsset,
                    playerRect.left.toFloat(),
                    playerRect.top.toFloat(),
                    null
                )

            }
            smallPaddle -> {

                canvas?.drawBitmap(
                    AssetManager.smallPlayerAsset,
                    playerRect.left.toFloat(),
                    playerRect.top.toFloat(),
                    null
                )

            }
            else -> {

                canvas?.drawBitmap(
                    AssetManager.playerAsset,
                    playerRect.left.toFloat(),
                    playerRect.top.toFloat(),
                    null
                )
            }
        }
    }
}