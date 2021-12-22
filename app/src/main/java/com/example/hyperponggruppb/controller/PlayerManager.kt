package com.example.hyperponggruppb.controller

import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.util.Log
import com.example.hyperponggruppb.model.PlayerData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PlayerManager {

    var playerPoints = 0
    var playerHighScore = 0
    var lives = 0
    var name = "AAA"
    var playTime = 0
    private var highScoreArray = mutableListOf<PlayerData>()
    private val gson = Gson()
    var thread: Thread? = null


    fun addPoints(newPoints: Int) {

        playerPoints += newPoints

        if (playerPoints > playerHighScore) {
            playerHighScore = playerPoints
        }
    }

    fun saveHighScore(sp: SharedPreferences?) {

        val save = PlayerData(name, playerPoints, playerHighScore)
        var isNew = true

        for (obj in highScoreArray){

            if (obj.name == save.name){

                if (obj.highScore < save.highScore){
                    obj.highScore = save.highScore
                }
                isNew = false
            }
        }

        if (isNew){
            highScoreArray.add(save)
        }

        var saveString = gson.toJson(highScoreArray)
        val editor = sp?.edit()
        editor?.putString("playerData", saveString)
        editor?.apply()
    }

    fun readSave(sp: SharedPreferences?) {

        val load = sp?.getString("playerData", "null")
        if (load != "null") {

            val mutableListPlayerDataType = object : TypeToken<MutableList<PlayerData>>() {}.type
            highScoreArray = gson.fromJson(load, mutableListPlayerDataType)
            setHighScore()

        }
    }

    private fun setHighScore() {

        for (obj in highScoreArray) {

            if (obj.highScore > playerHighScore && obj.name == name) {
                playerHighScore = obj.highScore
            }
        }
    }

    fun setPlacement(): Int {

        var resultPlacement = 1
        highScoreArray.sortBy { it.highScore}
        highScoreArray.reverse()

        Log.d(TAG, "setPlacement: $highScoreArray")
        
        for (obj in highScoreArray){
            
            if (playerPoints < obj.highScore){
                resultPlacement++
            }
        }

        Log.d(TAG, "setPlacement: $resultPlacement")
        return resultPlacement
    }

    fun resetPoints() {
        playerPoints = 0
    }

    fun loseLife() {
        lives--
    }

    fun gainLife() {
        if (lives < 3) {
            lives++
        } else playerPoints += 10

    }


}