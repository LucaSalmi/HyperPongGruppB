package com.example.hyperponggruppb.controller

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.util.Log
import com.example.hyperponggruppb.model.GameManager
import com.example.hyperponggruppb.model.PlayerData
import com.example.hyperponggruppb.view.fragment.FirstWorldFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PlayerManager {

    var activeUser: PlayerData? = null
    var lives = 0
    var playerPoints = 0
    var ticksToSpeed = 0
    var usersArray = mutableListOf<PlayerData>()
    private val gson = Gson()
    var isGameEnded = false
    var isLevelCompleted = false
    var isInfiniteMode = false
    var isReplaying = false
    var isFirstAccount = false
    var currentTotalBrickScore = 1000
    var starCounter = 0
    var powerUpActivated: Int = 0
    var selectedPowerUp = -1
    var activatePowerUp = false

    val multiBallPrice = 30
    val gunPrice = 40
    val shieldPrice = 20

    var levelTime = 0L
    var levelTimeString = ""
    var levelCountdown = ""

    var levelPowerups = 0
    var levelGems = 0

    var textIsOn = false

    var name = "null"
    var levelScoresArray: MutableList<Int> = mutableListOf()
    var levelStarsArray: MutableList<Int> = mutableListOf()
    var powerUpInventory: MutableList<Int> = mutableListOf(0, 1, 0, 1)
    var isMusicActive = true
    var isSoundEffectsActive = true
    var gems = 0
    var highScore = 0
    var currentLevel = 0
    var nextLevel = 1
    var comboPoints = 0


    fun cleanArrays() {

        levelScoresArray.clear()
        levelStarsArray.clear()
        powerUpInventory = mutableListOf(0, 1, 0, 1)
        gems = 0
        highScore = 0
        currentLevel = 0
    }

    fun createUser(): Boolean {

        for (user in usersArray) {

            if (user.name == name) {
                return false
            }
        }

        activeUser = PlayerData(
            name,
            levelScoresArray,
            levelStarsArray,
            powerUpInventory,
            isMusicActive,
            isSoundEffectsActive,
            gems,
            highScore,
            currentLevel,
            nextLevel
        )
        return true
    }

    fun addPoints(newPoints: Int) {

        playerPoints += newPoints

        if (isInfiniteMode) {

            if (playerPoints > highScore) {

                highScore = playerPoints
            }
        }

    }

    fun removePoints(newPoints: Int) {

        playerPoints -= newPoints

    }

    fun saveUserData(sp: SharedPreferences?) {

        var save = PlayerData(
            name,
            levelScoresArray,
            levelStarsArray,
            powerUpInventory,
            isMusicActive,
            isSoundEffectsActive,
            gems,
            highScore,
            currentLevel,
            nextLevel
        )
        var position = -1

        if (usersArray.size > 0) {

            for (obj in usersArray) {

                if (obj.name == activeUser!!.name) {

                    position = usersArray.indexOf(obj)

                }
            }
        }

        if (position >= 0) {
            usersArray.removeAt(position)
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
                levelScoresArray = user.levelScoresArray
                levelStarsArray = user.levelStarsArray
                powerUpInventory = user.powerUpInventory
                gems = user.gems
                highScore = user.highScore
                currentLevel = user.currentLevel
                nextLevel = currentLevel + 1
                isMusicActive = user.isMusicActive
                isSoundEffectsActive = user.isSoundEffectsActive
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
        levelPowerups = 0
        levelGems = 0
        starCounter = 0

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

            if (levelScoresArray[currentLevel - 1] < playerPoints) {
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

            nextLevel += 1
        }
    }

    fun addStarsToUser() {

        if (levelStarsArray.size < currentLevel) {
            levelStarsArray.add(starCounter)

        } else {

            if (levelStarsArray[currentLevel - 1] < starCounter) {
                levelStarsArray.removeAt(currentLevel - 1)
                levelStarsArray.add(currentLevel - 1, starCounter)
            }
        }
    }

    fun buyPowerUp(price: Int): Boolean {

        return if (price < gems) {
            gems -= price
            true
        } else {
            false
        }
    }


}