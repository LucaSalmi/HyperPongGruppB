package com.example.hyperponggruppb

object PointManager {

    var playerPoints = 0
    var playerHighScore = 0


    fun addPoints(newPoints: Int){

        playerPoints += newPoints

        if (playerPoints > playerHighScore){
            playerHighScore = playerPoints
        }

    }

}