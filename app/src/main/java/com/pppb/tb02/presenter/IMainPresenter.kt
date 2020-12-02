package com.pppb.tb02.presenter

import com.pppb.tb02.model.Piano

interface IMainPresenter {
    fun setPiano(piano: Piano)
    fun setThreadBlocked()
    fun setLevel(level: Int)
    fun setGameLost()
    fun getPiano(): Piano
    fun getLevel(): Int
    fun getScore(): Int
    fun addScore(score: Int)
    fun resetGame()
    fun isThreadHasRunning(): Boolean
    fun isThreadHasBlocked(): Boolean
    fun threadIsBlocked(state: Boolean)
    fun threadIsRunning(state: Boolean)
}