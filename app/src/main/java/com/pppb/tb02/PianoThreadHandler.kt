package com.pppb.tb02

import android.os.Handler
import android.os.Message
import android.util.Log

class PianoThreadHandler(private val mainActivity: MainActivity): Handler() {
    private val msgTilesPosition = 0
    private val msgThreadStopped = 1
    private val msgThreadBlocked = 2

    override fun handleMessage(msg: Message) {
        when(msg.what) {
            msgTilesPosition -> {
                val arrPos: MutableList<Int> = msg.obj as MutableList<Int>

                this.mainActivity.resetCanvas()
                this.mainActivity.drawTiles(arrPos)
            }
            msgThreadStopped -> {
                this.mainActivity.setThreadStopped()
            }
            msgThreadBlocked -> {
                this.mainActivity.setThreadBlocked()
            }
        }
    }

    fun sendTilesLocation(arrPos: MutableList<Int>) {
        val msg = Message()
        msg.what = msgTilesPosition
        msg.obj = arrPos

        this.sendMessage(msg)
    }

    fun threadHasBlocked() {
        val msg = Message()
        msg.what = msgThreadBlocked

        this.sendMessage(msg)
    }

    fun threadHasStopped() {
        val msg = Message()
        msg.what = msgThreadStopped

        this.sendMessage(msg)
    }
}