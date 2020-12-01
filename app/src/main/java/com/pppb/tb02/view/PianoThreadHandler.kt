package com.pppb.tb02.view

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
                this.mainActivity.updatePiano(piano)

                for(i in piano.notes) {
                    Log.d("debug", i.toString())
                }
            }
            msgThreadBlocked -> {
                this.mainActivity.setThreadBlocked()
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
}