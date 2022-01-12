package com.example.hyperponggruppb.model

import android.graphics.Bitmap

data class Bricks(var brickLeft: Int,var brickTop: Int,var brickRight: Int,var brickBottom: Int) {

    var hP = 1
    var hasPowerUp = false
    var asset: Bitmap = AssetManager.randomAsset(RandomNumberGenerator.rNG(1,8))

}