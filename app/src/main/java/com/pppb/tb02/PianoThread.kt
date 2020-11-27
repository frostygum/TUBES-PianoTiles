package com.pppb.tb02

import android.util.Log
import com.pppb.tb02.model.Note
import com.pppb.tb02.model.Piano
import kotlin.math.abs

class PianoThread(
    private val handler: PianoThreadHandler,
    private val canvasSize: Pair<Int, Int>,
    private val level: Int = 1
): Runnable {
    private var thread: Thread = Thread(this)
    private var isRunning: Boolean = false
    private var isHasInitiated: Boolean = false
    private var piano: Piano = Piano(4)

    override fun run() {
        try {
            while(isRunning) {
                Thread.sleep(15)
                var emptyFound = 0

                for((i, tile) in this.piano.tiles.withIndex()) {
                    var hidden = 0
                    for((j, note) in tile.notes.withIndex()) {
                        if(!note.isHidden) {
                            if(note.top > canvasSize.second) {
                                this.piano.tiles[i].notes[j].hide()
                                break
                            } else {
                                this.piano.tiles[i].notes[j].set(this.calculateTilesMovement(note.top,10))
                            }
                        }
                        else {
                            hidden++
                        }
                    }
                    if(hidden >= this.piano.tiles[i].notes.size) {
                        emptyFound++
                    }
                }

                if(emptyFound >= this.piano.size) {
                    Log.d("GENERATOR", "RE-GEN!!!")
                    this.generateTiles()
                }

                this.handler.sendTilesLocation(this.piano)
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun calculateTilesMovement(rectStartPos: Int, movementLength: Int): Int {
        val multiplier = 3
        return rectStartPos + (movementLength * multiplier)
    }

    private fun generateTiles() {
        val tiles = this.piano.tiles

        for((i, tile) in tiles.withIndex()) {
            val noteNum = (0..5).random()
            if(noteNum > 0) {
                tiles[i].setNewNotes(List(noteNum){(Note(0))})
                var pos = 0
                for((j, note) in tiles[i].notes.withIndex()) {
                    if(note.isHidden) {
                        var move = 0

                        if(j > 0) {
                            val posNoteBefore = tile.notes[j - 1].top
                            pos = randomStart(posNoteBefore)

                            if(abs(pos - posNoteBefore) < 700) {
                                move += -700
                            }
                        }
                        else {
                            pos = randomStart()
                        }

                        if(i > 0) {
                            if(tiles[i - 1].notes.size > j) {
                                val neighborPos = tiles[i - 1].notes[j].top
                                if(abs(neighborPos - pos) < 700) {
                                    move += -500
                                }
                            }
                        }

                        tiles[i].notes[j].set(pos + move)
                        tiles[i].notes[j].unHide()
                    }
                }
            }
        }
    }

    fun calculateClickPos(x: Float, y: Float) {
        val bin = this.canvasSize.first / this.piano.size

        for((i, tile) in this.piano.tiles.withIndex()) {
            val start = bin * i
            val end = bin * (i + 1)

            if(x > start && x < end) {
                Log.d("DEBUG", "tile loc : $i")
                for((j, note) in tile.notes.withIndex()) {
                    if(y > (note.top - 150) && y < note.bottom) {
                        if(!note.isHidden) {
                            Log.d("DEBUG", "Match !!")
                            this.piano.tiles[i].notes[j].hide()
                            this.handler.giveScore()
                            break
                        }
                        else {
                            Log.d("DEBUG", "Hidden !!")
                        }
                    }
                }
            }
        }
    }

    private fun randomStart(start: Int = 0): Int {
        val distance = -100
        var startPos = start
        if(startPos < 0) {
            startPos += -1000 + distance
        }
        val endPos = startPos + -500

        return (endPos..startPos).random()
    }

    fun setLastPos(piano: Piano) {
        this.piano = piano
        this.isHasInitiated = true
    }

    fun start() {
        if(!isHasInitiated) {
            this.generateTiles()
            this.isHasInitiated = true
        }

        if(!isRunning) {
            this.isRunning = true
            this.thread.start()
        }
    }

    fun block() {
        this.handler.threadHasBlocked(this.piano)
        this.isRunning = false
    }
}