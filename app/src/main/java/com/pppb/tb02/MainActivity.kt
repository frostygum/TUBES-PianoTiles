package com.pppb.tb02

import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.pppb.tb02.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnTouchListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var canvas: Canvas
    private lateinit var handler: PianoThreadHandler
    private lateinit var tilesThread: PianoThread
    private lateinit var detector: GestureDetector
    private var score: Int = 0
    private var savedPos: MutableList<Int> = mutableListOf()
    private var isCanvasHasInitiated: Boolean = false
    private var isThreadHasInitiated: Boolean = false
    private var isThreadHasBlocked: Boolean = false
    private var isThreadHasRunning: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Handler Initialization
        this.handler = PianoThreadHandler(this)

        //create gesture detector + listener
        this.detector = GestureDetector(this, TilesGestureListener())
        this.binding.ivCanvas.setOnTouchListener(this)

        this.binding.btnStart.setOnClickListener {
            //Initiate Canvas if never initiated before
            if(!isCanvasHasInitiated) {
                this.initiateCanvas()
                this.isCanvasHasInitiated = true
            }

            if(this.isThreadHasRunning) {
                this.btn_start.text = this.getText(R.string.btn_start)
                this.pause()
            }
            else {
                this.btn_start.text = this.getText(R.string.btn_pause)
                this.start()
            }
        }
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        return if(isCanvasHasInitiated) {
            this.detector.onTouchEvent(event)
        } else {
            false
        }
    }

    private fun pause() {
        if(isCanvasHasInitiated && isThreadHasRunning) {
            this.tilesThread.block()
        }
    }

    private fun start(idx: Int = -1) {
        if(!isThreadHasInitiated) {
            this.isThreadHasInitiated = true
            this.tilesThread = PianoThread(this.handler, Pair(this.canvas.width, this.canvas.height), 4)
        }

        if(isThreadHasBlocked) {
            this.isThreadHasBlocked = false
            this.tilesThread.setLastPos(savedPos)

            if(idx > -1) {
                this.tilesThread.clickAt(idx)
            }
        }

        if(!isThreadHasRunning) {
            this.isThreadHasRunning = true
            //Start Piano Tiles Thread
            this.tilesThread.start()
        }
    }

    fun drawTiles(arrPos: MutableList<Int>) {
        this.savedPos = arrPos
        //Initialize Paint color for tiles
        val paint = Paint()
        paint.color = ResourcesCompat.getColor(resources, R.color.tiles_bg, null)

        //Each tiles width
        val bin = this.binding.ivCanvas.width /  this.savedPos.size

        //Desired Tiles Height
        val tilesHeight = 500

        for(i in 0 until  this.savedPos.size) {
            val rect = Rect(bin * i,  this.savedPos[i], bin * (i + 1),  this.savedPos[i] + tilesHeight)
            canvas.drawRect(rect, paint)
        }
    }

    private fun initiateCanvas() {
        //Get imageView width and height
        val width = this.binding.ivCanvas.width
        val height = this.binding.ivCanvas.height

        //Create Bitmap
        val mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        //Associate the bitmap to the ImageView
        this.binding.ivCanvas.setImageBitmap(mBitmap)

        //Create a canvas with the bitmap
        this.canvas = Canvas(mBitmap)

        //Reset canvas
        this.resetCanvas()
    }

    private fun giveScore() {
        this.score += 10
        this.binding.tvScore.text = this.score.toString()
    }

    fun resetCanvas() {
        //Draw canvas background
        this.canvas.drawColor(ResourcesCompat.getColor(resources, R.color.canvas_bg, null))

        //Force re-draw
        this.binding.ivCanvas.invalidate()
    }

    fun setThreadBlocked() {
        this.isThreadHasRunning = false
        this.isThreadHasBlocked = true
        this.isThreadHasInitiated = false
    }

    fun setThreadStopped() {
        this.isThreadHasRunning = false
        this.isThreadHasInitiated = false
    }

    private fun calculateClickPos(x: Float, y: Float) {
        if(this.isThreadHasRunning) {
            //Each tiles width
            val bin = this.binding.ivCanvas.width / savedPos.size
            //Desired Tiles Height
            val tilesHeight = 500

            for((i, pos) in this.savedPos.withIndex()) {
                val start = bin * i
                val end = bin * (i + 1)

                if(x > start && x < end) {
                    Log.d("DEBUG", "loc : $i")
                    if(y > pos && y < (pos + tilesHeight)) {
                        Log.d("DEBUG", "Match!!")
                        this.tilesThread.clickAt(i)
                        this.giveScore()
                        break
                    }
                    break
                }
            }
        }
    }

    private inner class TilesGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            if (e != null) {
                Log.d("DEBUG", "onDown ${e.x}, ${e.y}")
                calculateClickPos(e.x, e.y)
            }
            return super.onDown(e)
        }
    }
}