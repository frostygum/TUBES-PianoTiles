package com.pppb.tb02

class Tiles(val size: Int = 4) {
    private val tiles: MutableList<MutableList<Int>> = mutableListOf()

    init {
        this.initialize()
    }

    private fun initialize() {
        for(i in 0..this.size) {
            this.tiles.add(mutableListOf())
        }
    }
}