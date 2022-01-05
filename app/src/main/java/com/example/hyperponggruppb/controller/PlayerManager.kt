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
    var usersArray = mutableListOf<PlayerData>()
    private val gson = Gson()
    var isGameEnded = false
    var levelScoresArray = mutableListOf<Int>()
    var isInfiniteMode = false
    var isReplaying = false


    fun addPoints(newPoints: Int) {

        playerPoints += newPoints

        if (playerPoints > playerHighScore) {
            playerHighScore = playerPoints
        }
    }

    fun saveHighScore(sp: SharedPreferences?) {

        val save = PlayerData(name, playerPoints, playerHighScore, currentLevel, levelScoresArray)
        var isNew = true

        for (obj in usersArray) {

            if (obj.name == save.name) {

                if (obj.highScore < save.highScore) {
                    obj.highScore = save.highScore
                }
                if (currentLevel> obj.currentLevel){

                    obj.currentLevel = currentLevel
                }

                obj.levelScoresArray = levelScoresArray
                isNew = false
            }
        }

        if (isNew) {
            usersArray.add(save)
        }

        orderArray()

        var saveString = gson.toJson(usersArray)
        val editor = sp?.edit()
        editor?.putString("playerData", saveString)
        editor?.putString("activeAccount", save.name)
        editor?.apply()
    }

    fun readSave(sp: SharedPreferences?) {

        val load = sp?.getString("playerData", "null")
        if (load != "null") {

            val mutableListPlayerDataType = object : TypeToken<MutableList<PlayerData>>() {}.type
            usersArray = gson.fromJson(load, mutableListPlayerDataType)
            name = sp?.getString("activeAccount", "null")!!

            for (obj in usersArray) {

                if (obj.name == name) {
                    levelScoresArray = obj.levelScoresArray
                    currentLevel = obj.currentLevel
                    nextLevel = obj.currentLevel + 1
                }
            }
            Log.d(TAG, "users: $usersArray")
            Log.d(TAG, "current: $currentLevel")
            Log.d(TAG, "next: $nextLevel")
        }

        orderArray()
    }

    fun setHighScore() {

        for (obj in usersArray) {

            if (obj.highScore > playerHighScore && obj.name == name) {

                playerHighScore = if (isInfiniteMode) {
                    obj.highScore
                } else {
                    levelScoresArray[levelScoresArray.size-1]
                }
            }
        }
        orderArray()
    }

    fun setLevelHIghScore(){

    }

    fun setPlacement(): Int {

        var resultPlacement = 1

        orderArray()

        Log.d(TAG, "setPlacement: $usersArray")

        for (obj in usersArray) {

            if (playerPoints < obj.highScore) {
                resultPlacement++
            }
        }

        Log.d(TAG, "setPlacement: $resultPlacement")
        return resultPlacement
    }

    fun resetPoints() {
        playerPoints = 0
    }

    fun resetHighScore() {
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

    fun orderArray() {

        usersArray.sortBy { it.highScore }
        usersArray.reverse()
    }

    fun setLevel(levelId: Int): Boolean {

        return if (levelId > nextLevel) {

            false

        } else {

            if (currentLevel < levelId){
                currentLevel = levelId

            }else{

                isReplaying = true
            }
            true
        }
    }

    fun setLevelScore() {

        if (levelScoresArray.isEmpty() || levelScoresArray.size < nextLevel){
            levelScoresArray.add(playerPoints)

        }else if (levelScoresArray[currentLevel-1] < playerPoints) {

            levelScoresArray.add(nextLevel, playerPoints)
        }
    }

    fun unlockNextLevel(){

        if (isReplaying){

            isReplaying = false

        }else{

            nextLevel++
        }

    }

}