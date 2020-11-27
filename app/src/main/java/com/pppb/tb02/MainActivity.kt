package com.pppb.tb02

import android.graphics.*
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.pppb.tb02.databinding.ActivityMainBinding
import com.pppb.tb02.model.Piano
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnTouchListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var canvas: Canvas
    private lateinit var handler: PianoThreadHandler
    private lateinit var tilesThread: PianoThread
    private lateinit var detector: GestureDetector
    private lateinit var timer: StartTimer
    private var score: Int = 0
    private var savedPos: Piano = Piano()
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

        this.timer = StartTimer(3000, 1000)

        this.binding.btnStart.setOnClickListener {
            //Initiate Canvas if never initiated before
            if(!isCanvasHasInitiated) {
                this.isCanvasHasInitiated = true
                this.initiateCanvas()
            }

            if(this.isThreadHasRunning) {
                this.pauseThread()
            }
            else {
                timer.start()
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

    private fun pauseThread() {
        if(isCanvasHasInitiated && isThreadHasRunning) {
            this.btn_start.text = this.getText(R.string.btn_start)
            this.tilesThread.block()
        }
    }

    private fun startThread() {
        this.btn_start.text = this.getText(R.string.btn_pause)

        this.isThreadHasInitiated = true
        this.tilesThread = PianoThread(
            this.handler,
            Pair(this.canvas.width, this.canvas.height),
            4
        )

        if(isThreadHasBlocked) {
            this.isThreadHasBlocked = false
            this.tilesThread.setLastPos(savedPos)
        }

        this.isThreadHasRunning = true
        //Start Piano Tiles Thread
        this.tilesThread.start()
    }

    fun drawTiles(piano: Piano) {
        this.savedPos = piano
        //Initialize Paint color for tiles
        val fillPaint = Paint()
        fillPaint.style = Paint.Style.FILL
        fillPaint.color = Color.BLACK

        val strokePaint = Paint()
        strokePaint.style = Paint.Style.STROKE
        strokePaint.color = Color.RED
        strokePaint.strokeWidth = 10F

        //Each tiles width
        val bin = this.binding.ivCanvas.width / piano.size

        for((i, tile) in piano.tiles.withIndex()) {
            if(tile.notes.isNotEmpty()) {
                for(note in tile.notes) {
                    if(!note.isHidden) {
                        val rect = Rect(bin * i, note.top, bin * (i + 1), note.bottom)
                        this.canvas.drawRect(rect, fillPaint)
                        this.canvas.drawRect(rect, strokePaint)
                    }
                }
            }
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

    fun giveScore() {
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

    private inner class TilesGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            if (e != null) {
                tilesThread.calculateClickPos(e.x, e.y)
            }
            return super.onDown(e)
        }
    }

    private inner class StartTimer(startTime: Long, interval: Long) : CountDownTimer(startTime, interval) {
        override fun onFinish() {
            startThread()
        }

        override fun onTick(millisUntilFinished: Long) {
            if(millisUntilFinished in 2001..2999) {
                binding.btnStart.text = "3"
            }
            else if(millisUntilFinished in 1001..1999) {
                binding.btnStart.text = "2"
            }
            else {
                binding.btnStart.text = "1"
            }
        }
    }
}