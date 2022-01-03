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
    var currentLevel = 0
    var nextLevel = 1
    var lives = 0
    var name = "null"
    var playTime = 0
    var highScoreArray = mutableListOf<PlayerData>()
    private val gson = Gson()
    var isGameEnded = false


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

        orderArray()

        var saveString = gson.toJson(highScoreArray)
        val editor = sp?.edit()
        editor?.putString("playerData", saveString)
        editor?.putString("activeAccount",save.name)
        editor?.apply()
    }

    fun readSave(sp: SharedPreferences?) {

        val load = sp?.getString("playerData", "null")
        if (load != "null") {

            val mutableListPlayerDataType = object : TypeToken<MutableList<PlayerData>>() {}.type
            highScoreArray = gson.fromJson(load, mutableListPlayerDataType)
            name = sp?.getString("activeAccount", "null")!!
            setHighScore()
        }

        orderArray()
    }

    fun setHighScore() {

        for (obj in highScoreArray) {

            if (obj.highScore > playerHighScore && obj.name == name) {
                playerHighScore = obj.highScore
            }
        }

        orderArray()
    }

    fun setPlacement(): Int {

        var resultPlacement = 1

        orderArray()

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

    fun resetHighScore(){
        playerHighScore = 0
    }

    fun loseLife() {
        lives--
    }

    fun gainLife() {
        if (lives < 3) {
            lives++
        } else playerPoints += 10

    }

    fun orderArray(){

        highScoreArray.sortBy { it.highScore}
        highScoreArray.reverse()
    }

    fun setLevel(levelId: Int): Boolean {
        return if (levelId > nextLevel){
            false
        }else{
            currentLevel = levelId
            true
        }

    }


}