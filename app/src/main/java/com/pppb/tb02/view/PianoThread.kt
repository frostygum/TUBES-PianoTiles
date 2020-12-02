package com.pppb.tb02.view


import com.pppb.tb02.model.Piano
import com.pppb.tb02.util.PianoGenerator
import kotlin.random.Random

class PianoThread(
    private val handler: PianoThreadHandler,
    private val canvasSize: Pair<Int, Int>,
    private var level: Int = 1
): Runnable {
    private var thread: Thread = Thread(this)
    private var isRunning: Boolean = false
    private var isHasInitiated: Boolean = false
    private var piano: Piano = Piano()

    override fun run() {
        try {
            while(this.isRunning) {
                Thread.sleep(10)
                var hiddenFound = 0

                for(note in this.piano.notes) {
                    if(!note.isHidden) {
                        if(!note.isClicked && note.bottom > this.canvasSize.second) {
                            note.lose()
                            this.lost()
                        }

                        if(note.top > this.canvasSize.second) {
                            note.hide()
                        }

                        if(note.tilePos >= 0) {
                            val newPos = calculateTilesMovement(note.top, 10)
                            note.top = newPos
                            note.bottom = newPos + note.length
                        }
                        else {
                            note.hide()
                        }
                    }
                    else {
                        hiddenFound++
                    }
                }

                if(hiddenFound == this.piano.notes.size) {
                    this.generateTiles()
                }

                this.handler.sendTilesLocation(this.piano)
                this.handler.sendGameLevel(this.level)
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun calculateTilesMovement(rectStartPos: Int, movementLength: Int): Int {
        //multiplier: multiplier for speed/ note movement
        val multiplier = 2
        return rectStartPos + (movementLength * multiplier) + this.level
    }

    private fun generateTiles() {
        this.level++
        this.piano = PianoGenerator.createPiano(25 + (this.level * 2), -500, Random.nextBoolean())
    }

    fun setLastPos(piano: Piano) {
        this.piano = piano
        this.isHasInitiated = true
    }

    fun setLastLevel(level: Int) {
        this.level = level
    }

    fun start() {
        if(!isHasInitiated) {
            this.isHasInitiated = true
        }

        if(!isRunning) {
            this.isRunning = true
            this.thread.start()
        }
    }

    fun block() {
        this.handler.threadHasBlocked()
        this.isRunning = false
    }

    private fun lost() {
        this.handler.gameLost()
        this.isRunning = false
    }
}