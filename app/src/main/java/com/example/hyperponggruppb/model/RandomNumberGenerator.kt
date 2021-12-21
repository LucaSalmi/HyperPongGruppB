package com.example.hyperponggruppb.model

object RandomNumberGenerator {

    fun rNG(a: Int, b: Int): Int {

        return (a..b).random()
    }
}