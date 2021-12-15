package com.example.hyperponggruppb

object RandomNumberGenerator {

    fun rNG(a: Int, b: Int): Int {

        return (a..b).random()
    }
}