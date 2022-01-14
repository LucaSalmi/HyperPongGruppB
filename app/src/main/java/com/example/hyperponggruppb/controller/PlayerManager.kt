package com.example.hyperponggruppb.controller

import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.util.Log
import com.example.hyperponggruppb.model.PlayerData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PlayerManager {

    var activeUser: PlayerData? = null
    var playerPoints = 0
    var lives = 0
    var playTime = 0
    var usersArray = mutableListOf<PlayerData>()
    private val gson = Gson()
    var isGameEnded = false
    var isInfiniteMode = false
    var isReplaying = false
    var isFirstAccount = false
    var currentMaxScore = 1000
    var starCounter = 0

    var name = "null"
    var levelScoresArray: MutableList<Int> = mutableListOf()
    var levelStarsArray: MutableList<Int> = mutableListOf()
    var powerUpInventory: List<Int>? = listOf(0,0,0,0)
    var isMusicActive = true
    var isSoundEffectsActive = true
    var gems = 0
    var highScore = 0
    var currentLevel = 0
    var nextLevel = currentLevel + 1

    fun cleanArrays(){

        levelScoresArray.clear()
        levelStarsArray.clear()
        powerUpInventory = listOf(0,0,0,0)
        gems = 0
        highScore = 0
        currentLevel = 0
    }

    fun createUser(): Boolean{

        for (user in usersArray){

            if (user.name == name){
                return false
            }
        }

        activeUser = PlayerData(name, levelScoresArray, levelStarsArray, powerUpInventory, isMusicActive, isSoundEffectsActive, gems, highScore, currentLevel, nextLevel)
        return true
    }

    fun addPoints(newPoints: Int) {

        playerPoints += newPoints

        if (isInfiniteMode){

            if (playerPoints > activeUser?.highScore!!) {

                activeUser?.highScore = playerPoints
            }
        }

    }

    fun removePoints(newPoints: Int) {

        playerPoints -= newPoints

    }

    fun saveUserData(sp: SharedPreferences?) {

        var save = PlayerData(name, levelScoresArray, levelStarsArray, powerUpInventory, isMusicActive, isSoundEffectsActive, gems, highScore, currentLevel, nextLevel)
        var position = 0

        if (usersArray.size > 0){

            for (obj in usersArray) {

                if (obj.name == activeUser!!.name) {

                    position = usersArray.indexOf(obj)
                    usersArray.removeAt(position)
                }
            }
        }

        usersArray.add(save)
        activeUser = save
        Log.d(TAG, "saved: ${activeUser!!.levelStarsArray}")

        orderArray()

        val saveString = gson.toJson(usersArray)
        val editor = sp?.edit()
        editor?.putString("playerData", saveString)
        editor?.putString("activeAccount", name)
        editor?.apply()
    }

    fun readSave(sp: SharedPreferences?) {

        val load = sp?.getString("playerData", "null")
        if (load != "null") {

            val mutableListPlayerDataType = object : TypeToken<MutableList<PlayerData>>() {}.type
            usersArray = gson.fromJson(load, mutableListPlayerDataType)
            name = sp?.getString("activeAccount", "null")!!

        }
        orderArray()
    }

    fun loadUserData() {

        for (user in usersArray) {

            if (user.name == name) {

                activeUser = user
                levelScoresArray = activeUser!!.levelScoresArray
                levelStarsArray = activeUser!!.levelStarsArray
                powerUpInventory = activeUser!!.powerUpInventory
                gems = activeUser!!.gems
                highScore = activeUser!!.highScore
                currentLevel = activeUser!!.currentLevel
                nextLevel = currentLevel + 1
                isMusicActive = activeUser!!.isMusicActive
                isSoundEffectsActive = activeUser!!.isSoundEffectsActive
            }
        }
    }

    fun setPlacement(): Int {

        var resultPlacement = 1

        orderArray()

        for (obj in usersArray) {

            if (playerPoints < obj.highScore) {
                resultPlacement++
            }
        }

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

    fun orderArray() {

        usersArray.sortBy { it.highScore }
        usersArray.reverse()
    }

    fun setLevel(levelId: Int): Boolean {

        return if (levelId > nextLevel) {

            false

        } else {

            if (currentLevel < levelId) {
                currentLevel = levelId

            } else {

                isReplaying = true
                currentLevel = levelId
            }
            true
        }
    }

    fun setLevelHIghScore() {

        Log.d(TAG, "setLevelHIghScore: $currentLevel")

        if (levelScoresArray.size < currentLevel) {
            levelScoresArray.add(playerPoints)

        } else {

            if (levelScoresArray[currentLevel-1] < playerPoints) {
                levelScoresArray.removeAt(currentLevel - 1)
                levelScoresArray.add(currentLevel - 1, playerPoints)
            }
        }
    }

    fun unlockNextLevel() {

        Log.d(TAG, "unlockNextLevel: $isReplaying")

        if (isReplaying) {

            isReplaying = false

        } else {

            activeUser!!.nextLevel += 1
        }
    }

    fun addStarsToUser(){

        if (levelStarsArray.size < currentLevel){
            levelStarsArray.add(starCounter)

        }else{

            if (levelStarsArray[currentLevel - 1] < starCounter) {
                levelStarsArray.removeAt(currentLevel - 1)
                levelStarsArray.add(currentLevel - 1, starCounter)
            }
        }
    }



}