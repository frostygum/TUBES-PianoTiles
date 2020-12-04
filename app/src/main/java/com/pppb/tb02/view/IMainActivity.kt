package com.pppb.tb02.view

import com.pppb.tb02.model.Menu
import com.pppb.tb02.model.Piano

interface IMainActivity {
    fun updateUIScore(score: Int)
    fun changePage(page: String)
    fun updatePiano(piano: Piano)
    fun setGameLevel(level: Int)
    fun setGameLost(scoreList: MutableList<Menu>)
}