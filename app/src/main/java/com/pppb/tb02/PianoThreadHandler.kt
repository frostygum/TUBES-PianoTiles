package com.pppb.tb02

import android.os.Handler
import android.os.Message
import android.util.Log
import com.pppb.tb02.model.Piano

class PianoThreadHandler(private val mainActivity: MainActivity): Handler() {
    private val msgTilesPosition = 0
    private val msgThreadStopped = 1
    private val msgThreadBlocked = 2
    private val msgGiveScore = 3

    override fun handleMessage(msg: Message) {
        when(msg.what) {
            msgTilesPosition -> {
                val piano: Piano = msg.obj as Piano

                this.mainActivity.resetCanvas()
                this.mainActivity.drawTiles(piano)

                for(tile in piano.tiles) {
                    Log.d("DEBUG", tile.notes.toString())
                }
            }
            msgThreadStopped -> {
                Log.d("DEBUG", "Stopped !!")
                this.mainActivity.setThreadStopped()
            }
            msgThreadBlocked -> {
                val piano: Piano = msg.obj as Piano
                this.mainActivity.setThreadBlocked(piano)
            }
            msgGiveScore -> {
                Log.d("DEBUG", "Add Score")
                this.mainActivity.giveScore()
            }
        }
    }

    fun sendTilesLocation(piano: Piano) {
        val msg = Message()
        msg.what = msgTilesPosition
        msg.obj = piano

        this.sendMessage(msg)
    }

    fun threadHasBlocked(piano: Piano) {
        val msg = Message()
        msg.what = msgThreadBlocked
        msg.obj = piano

        this.sendMessage(msg)
    }

    fun threadHasStopped() {
        val msg = Message()
        msg.what = msgThreadStopped

        this.sendMessage(msg)
    }

    fun giveScore() {
        val msg = Message()
        msg.what = msgGiveScore

        this.sendMessage(msg)
    }
}