package com.example.hyperponggruppb.model

data class PlayerData(
    var name: String,
    var levelScoresArray: MutableList<Int> = mutableListOf(),
    var levelStarsArray: MutableList<Int> = mutableListOf(),
    var powerUpInventory: MutableList<Int> = mutableListOf(0,0,0,0),
    var isMusicActive: Boolean,
    var isSoundEffectsActive: Boolean,
    var gems: Int,
    var highScore: Int,
    var currentLevel: Int,
    var nextLevel: Int
    ){

}

