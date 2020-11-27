package com.pppb.tb02

class Tiles(val size: Int = 4) {
    private var tiles: MutableList<MutableList<Pair<Int, Int>>> = mutableListOf()
    val length: Int = 500

    init {
        this.initialize()
    }

    fun setTiles(tiles: Tiles) {
        this.tiles = tiles.getTiles()
    }

    private fun initialize() {
        for(i in 1..this.size) {
            this.tiles.add(mutableListOf())
        }
    }

    fun removeAt(tilesRegion: Int, tileNum: Int) {
        if(tiles.size > tilesRegion) {
            if(tiles[tilesRegion].size > tileNum) {
                val newTiles= this.tiles
                newTiles[tilesRegion].removeAt(tileNum)
                this.tiles = newTiles
            }
        }
    }

    fun getTiles(): MutableList<MutableList<Pair<Int, Int>>> {
        return this.tiles
    }

    fun addTiles(tilesRegion: Int, startPos: Int) {
        if(tiles.size > tilesRegion) {
            val newTiles= this.tiles
            newTiles[tilesRegion].add(Pair(startPos, startPos + length))
            this.tiles = newTiles
        }
    }

    fun setTiles(tilesRegion: Int, tileNum: Int, startPos: Int) {
        if(tiles.size > tilesRegion) {
            if(tiles[tilesRegion].size > tileNum) {
                val newTiles= this.tiles
                newTiles[tilesRegion][tileNum] = Pair(startPos, startPos + length)
                this.tiles = newTiles
            }
        }
    }
}