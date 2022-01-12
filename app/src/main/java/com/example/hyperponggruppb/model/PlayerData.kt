package com.example.hyperponggruppb.model

data class PlayerData(
    var name: String,
    var points: Int,
    var highScore: Int,
    var currentLevel: Int,
    var levelScoresArray: MutableList<Int>,
    var levelStarsArray: MutableList<Int>,
    var coins: Int,
    var powerUpInventory: Array<Int>
)
