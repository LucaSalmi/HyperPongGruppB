package com.example.hyperponggruppb

object PointManager {

    var playerPoints: PlayerPoints = initPlayerPoints()

    private fun initPlayerPoints(): PlayerPoints {
         return PlayerPoints(0, 0)
    }

    fun addPoints(newPoints: Int){

        playerPoints.points += newPoints

        if (playerPoints.points > playerPoints.highScore){
            playerPoints.highScore = playerPoints.points
        }

    }
}