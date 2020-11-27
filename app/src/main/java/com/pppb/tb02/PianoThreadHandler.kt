package com.pppb.tb02

import android.os.Handler
import android.os.Message
import android.util.Log
import com.pppb.tb02.model.Piano

class PianoThreadHandler(private val mainActivity: MainActivity): Handler() {
    private val msgTilesPosition = 0
    private val msgThreadBlocked = 1
    private val msgGiveScore = 2

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
            msgThreadBlocked -> {
                this.mainActivity.setThreadBlocked()
            }
            msgGiveScore -> {
                val point: Int = msg.obj as Int
                this.mainActivity.giveScore(point)
            }
        }
    }

    fun sendTilesLocation(piano: Piano) {
        val msg = Message()
        msg.what = msgTilesPosition
        msg.obj = piano

        this.sendMessage(msg)
    }

    fun threadHasBlocked() {
        val msg = Message()
        msg.what = msgThreadBlocked

        this.sendMessage(msg)
    }

    fun giveScore(point: Int) {
        val msg = Message()
        msg.what = msgGiveScore
        msg.obj = point

        this.sendMessage(msg)
    }
}