package com.example.hyperponggruppb

import android.content.ContentValues.TAG
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.JsonWriter
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONStringer

object PlayerManager {

    var playerPoints = 0
    var playerHighScore = 0
    var lives = 0
    var name = "AAA"
    var playTime = 0
    private var highScoreArray = mutableListOf<PlayerData>()
    private val gson = Gson()


    fun addPoints(newPoints: Int){

        playerPoints += newPoints

        if (playerPoints > playerHighScore){
            playerHighScore = playerPoints
        }
    }

    fun saveHighScore(sp : SharedPreferences?){

        val save = PlayerData(name, playerPoints, playerHighScore)
        highScoreArray.add(save)
        var saveString = gson.toJson(highScoreArray)
        val editor = sp?.edit()
        editor?.putString("playerData", saveString)
        editor?.apply()
        highScoreArray.clear()
    }

    fun readSave(sp : SharedPreferences?){
        
        val load = sp?.getString("playerData", "null")
        if (load != "null"){

            val mutableListPlayerDataType = object : TypeToken<MutableList<PlayerData>>() {}.type
            highScoreArray = gson.fromJson(load, mutableListPlayerDataType)
            setHighScore()

        }
        Log.d(TAG, "readSave: $highScoreArray")
        highScoreArray.sortBy { it.highScore}
        highScoreArray.reverse()
        Log.d(TAG, "seriamente: $highScoreArray")
    }

    private fun setHighScore(){

        for (obj in highScoreArray){

            if (obj.highScore > playerHighScore && obj.name == name){
                playerHighScore = obj.highScore
            }
        }
    }

    fun resetPoints(){
        playerPoints = 0
    }

    fun loseLife(){
        lives --
    }

    fun gainLife(){
        if (lives < 3){
            lives ++
        }else playerPoints += 10

    }

}