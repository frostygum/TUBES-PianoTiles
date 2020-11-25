package com.pppb.tb02

class PianoThread(private val handler: PianoThreadHandler, private val canvasSize: Pair<Int, Int>, private val level: Int = 1): Runnable {
    private var thread: Thread = Thread(this)
    private val tilesNum: Int = 4
    private var isRunning: Boolean = false
    private var isHasInitiated: Boolean = false
    private var arrPos: MutableList<Int> = mutableListOf()
    private val tiles: Tiles = Tiles(4)

    override fun run() {
        try {
            while(isRunning) {
                Thread.sleep(10)

                for((i, rectStartPos) in arrPos.withIndex()) {
                    val newPos = this.calculateTilesMovement(rectStartPos, 10)
                    this.arrPos[i] = newPos

                    if(newPos > canvasSize.second) {
                        this.arrPos[i] = randomStart()
                    }
                }

                this.handler.sendTilesLocation(this.arrPos)
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun calculateTilesMovement(rectStartPos: Int, movementLength: Int): Int {
        val multiplier = 2
        return rectStartPos + (movementLength * multiplier)
    }

    private fun initiateAllPosition() {
        for(i in 1..tilesNum) {
            this.arrPos.add(randomStart())
        }
    }

    private fun randomStart(): Int {
        return (-1500..-500).random()
    }

    fun setLastPos(arrPos: MutableList<Int>) {
        this.arrPos = arrPos
        this.isHasInitiated = true
    }

    fun clickAt(loc: Int) {
       this.arrPos[loc] = randomStart()
    }

    fun start() {
        if(!isRunning) {
            this.isRunning = true
            this.thread.start()
        }

        if(!isHasInitiated) {
            this.isHasInitiated = true
            this.initiateAllPosition()
        }
    }

    fun block() {
        this.isRunning = false
        this.handler.threadHasBlocked()
    }

    fun stop() {
        this.isRunning = false
        this.handler.threadHasStopped()
    }
}