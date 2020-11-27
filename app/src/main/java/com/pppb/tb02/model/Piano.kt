package com.pppb.tb02.model

class Piano(val size: Int = 4) {
    var tiles: List<Tile> = listOf()

    init {
        this.initialize()
    }

    private fun initialize() {
        for(i in 1..this.size) {
            this.tiles += Tile()
        }
    }
}