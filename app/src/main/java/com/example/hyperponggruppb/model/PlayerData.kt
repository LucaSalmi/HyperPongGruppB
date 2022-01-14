package com.example.hyperponggruppb.model

data class PlayerData(
    var name: String
    ){
    var levelScoresArray = mutableListOf<Int>()
    var levelStarsArray = mutableListOf<Int>()
    var powerUpInventory = listOf<Int>()
    var isMusicActive = true
    var isSoundEffectsActive = true
    var gems = 0
    var highScore = 0
    var currentLevel = 0
    var nextLevel = currentLevel+1
}

