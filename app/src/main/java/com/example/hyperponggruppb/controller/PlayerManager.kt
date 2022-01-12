package com.example.hyperponggruppb.controller

import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.util.Log
import com.example.hyperponggruppb.model.GameManager
import com.example.hyperponggruppb.model.PlayerData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PlayerManager {

    var activeUser: PlayerData? = null
    var playerPoints = 0
    var lives = 0
    var name = "null"
    var isMusicActive = true
    var isSoundEffectsActive = true
    var playTime = 0
    var usersArray = mutableListOf<PlayerData>()
    private val gson = Gson()
    var isGameEnded = false
    var isInfiniteMode = false
    var isReplaying = false
    var currentMaxScore = 0

    fun createUser(): Boolean{

        for (user in usersArray){

            if (user.name == name){
                return false
            }
        }

        activeUser = PlayerData(name, 0, 0, 1, 0)
        return true
    }

    fun addPoints(newPoints: Int) {

        playerPoints += newPoints

        if (playerPoints > activeUser?.highScore!!) {

            activeUser?.highScore = playerPoints

        }
    }

    fun removePoints(newPoints: Int) {

        playerPoints -= newPoints

    }

    fun saveUserData(sp: SharedPreferences?) {

        val save = activeUser
        var position = 0

        if (usersArray.size > 0){

            for (obj in usersArray) {

                if (obj.name == save?.name) {

                    position = usersArray.indexOf(obj)
                    usersArray.removeAt(position)
                }
            }
        }

        usersArray.add(save!!)

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
            }
        }

        isMusicActive = activeUser!!.isMusicActive
        isSoundEffectsActive = activeUser!!.isSoundEffectsActive
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

        return if (levelId > activeUser?.nextLevel!!) {

            false

        } else {

            if (activeUser?.currentLevel!! < levelId) {
                activeUser?.currentLevel = levelId

            } else {

                isReplaying = true
                activeUser?.currentLevel = levelId
            }
            true
        }
    }

    fun setLevelHIghScore() {

        if (activeUser?.levelScoresArray?.size!! < activeUser!!.currentLevel) {
            activeUser?.levelScoresArray!!.add(playerPoints)

        } else {

            if (activeUser?.levelScoresArray!![activeUser!!.currentLevel - 1] < playerPoints) {
                activeUser?.levelScoresArray!!.removeAt(activeUser!!.currentLevel - 1)
                activeUser?.levelScoresArray!!.add(activeUser!!.currentLevel - 1, playerPoints)
            }
        }
    }

    fun unlockNextLevel() {

        Log.d(TAG, "unlockNextLevel: $isReplaying")

        if (isReplaying) {

            isReplaying = false

        } else {

            activeUser?.nextLevel!! + 1
        }

    }

}