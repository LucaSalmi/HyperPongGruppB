package com.example.hyperponggruppb

import android.content.ContentValues.TAG
import android.util.Log

object PointManager {

    var player: PlayerPoints = initPlayerPoints()

    private fun initPlayerPoints(): PlayerPoints {
         return PlayerPoints(0, 9999)
    }

    fun addPoints(newPoints: Int){

        player.points += newPoints

        if (player.points > player.highScore){
            player.highScore = player.points
        }

    }
}