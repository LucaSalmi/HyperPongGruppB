package com.example.hyperponggruppb

import android.content.ContentValues.TAG
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.JsonWriter
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import org.json.JSONStringer

object PlayerManager {

    var playerPoints = 0
    var playerHighScore = 0
    private val gson = Gson()


    fun addPoints(newPoints: Int){

        playerPoints += newPoints

        if (playerPoints > playerHighScore){
            playerHighScore = playerPoints
        }
    }

    fun saveHighScore(sp : SharedPreferences?){

        val save = PlayerData(playerPoints, playerHighScore)
        var saveString = gson.toJson(save)
        val editor = sp?.edit()
        editor?.putString("playerData", saveString)
        editor?.apply()


    }

    fun readSave(sp : SharedPreferences?){
        
        val load = sp?.getString("playerData", "null")
        Log.d(TAG, "readSave: $load")
        if (load != "null"){

            val loadPlayer = gson.fromJson(load, PlayerData::class.java)
            playerHighScore = loadPlayer.highScore

        }

    }

}