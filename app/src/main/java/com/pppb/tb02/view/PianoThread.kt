package com.pppb.tb02.view

import android.graphics.Color
import android.graphics.Rect
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
    private var piano: Piano = Piano()

    override fun run() {
        try {
            while(isRunning) {
                Thread.sleep(10)
                var hiddenFound = 0

                for(note in piano.notes) {
//                    if(note.top > 0) {
//                        note.unHide()
//                    }
                    if(!note.isHidden) {
                        if(note.top > canvasSize.second) {
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

                if(hiddenFound == piano.notes.size) {
//                    this.isRunning = false
                    this.generateTiles()
                }

                this.handler.sendTilesLocation(this.piano)
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun calculateTilesMovement(rectStartPos: Int, movementLength: Int): Int {
        //multiplier: multiplier for speed/ note movement
        val multiplier = 2
        return rectStartPos + (movementLength * multiplier)
    }

    private fun generateTiles() {
        val piano = Piano()
        for(i in 0..20) {
            val tilePos = (-1..3).random()
            piano.add(-500, tilePos)
        }
        this.piano = piano
    }

    fun setLastPos(piano: Piano) {
        this.piano = piano
        this.isHasInitiated = true
    }

    fun start() {
        if(!isHasInitiated) {
//            this.generateTiles()
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
}