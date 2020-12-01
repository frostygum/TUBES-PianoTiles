package com.pppb.tb02.view

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.*
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.pppb.tb02.R
import com.pppb.tb02.databinding.FragmentPianoTilesGameBinding
import com.pppb.tb02.model.Note
import com.pppb.tb02.model.Piano
import com.pppb.tb02.presenter.MainPresenter
import java.lang.ClassCastException

class FragmentPianoTilesGame: Fragment(R.layout.fragment_piano_tiles_game), View.OnTouchListener {
    private lateinit var binding: FragmentPianoTilesGameBinding
    private lateinit var listener: IMainActivity
    private lateinit var tilesThread: PianoThread
    private lateinit var canvas: Canvas
    private lateinit var detector: GestureDetector
    private lateinit var handler: PianoThreadHandler
    private lateinit var presenter: MainPresenter

    companion object {
        fun newInstance(presenter: MainPresenter, handler: PianoThreadHandler): FragmentPianoTilesGame {
            val fragment = FragmentPianoTilesGame()
            fragment.presenter = presenter
            fragment.handler = handler
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = FragmentPianoTilesGameBinding.inflate(inflater, container, false)

        //create gesture detector + listener
        this.detector = GestureDetector(this.activity, TilesGestureListener())
        this.binding.ivCanvas.setOnTouchListener(this)

        this.binding.btnPause.setOnClickListener {
            if(this.presenter.isThreadHasRunning) {
                this.tilesThread.block()
                this.listener.changePage("PAUSE")
            }
        }

        return this.binding.root
    }

    fun updateUIScore(score: Int) {
        this.binding.tvScore.text = score.toString()
    }

    private fun startThread(piano: Piano) {
        this.presenter.isThreadHasInitiated = true
        this.tilesThread = PianoThread(
            this.handler,
            Pair(this.canvas.width, this.canvas.height),
            4
        )

        if(this.presenter.isThreadHasBlocked) {
            this.presenter.isThreadHasBlocked = false
            this.tilesThread.setLastPos(this.presenter.getPiano())
        }
        else {
            this.tilesThread.setLastPos(piano)
        }

        this.presenter.isThreadHasRunning = true
        //Start Piano Tiles Thread
        this.tilesThread.start()
    }

    private fun createPiano(): Piano {
        val piano = Piano()
        for(i in 0..20) {
            val tilePos = (-1..3).random()
            piano.add(500, tilePos)
        }
        return piano
    }

    fun drawTiles(piano: Piano) {
        this.resetCanvas()
        //Initialize Paint color for tiles
        val fillPaint = Paint()
        fillPaint.style = Paint.Style.FILL

        val startFillPaint = Paint()
        startFillPaint.style = Paint.Style.FILL

        val strokePaint = Paint()
        strokePaint.style = Paint.Style.STROKE
        strokePaint.color = Color.RED
        strokePaint.strokeWidth = 10F

        //Each tiles width
        val bin = this.binding.ivCanvas.width / 4

        for((i, note) in piano.notes.withIndex()) {
            if(!note.isHidden) {
                fillPaint.color = Color.BLACK
                if(i == 0 && !this.presenter.isThreadHasRunning) {
                    fillPaint.color = Color.CYAN
                }
                if(note.isClicked) {
                    fillPaint.color = Color.GRAY
                }

                if(note.tilePos >= 0) {
                    val rect = Rect(bin * note.tilePos, note.top, bin * (note.tilePos + 1), note.bottom)
                    this.canvas.drawRect(rect, fillPaint)
                    this.canvas.drawRect(rect, strokePaint)
                }
            }
        }
    }

    private fun initiateCanvas() {
        //Get imageView width and height
        val width = this.binding.ivCanvas.width
        val height = this.binding.ivCanvas.height
        Log.d("SIZE", "$width, $height")

        //Create Bitmap
        val mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        //Associate the bitmap to the ImageView
        this.binding.ivCanvas.setImageBitmap(mBitmap)

        //Create a canvas with the bitmap
        this.canvas = Canvas(mBitmap)

        //Reset canvas
        this.resetCanvas()
    }

    private fun resetCanvas() {
        //Draw canvas background
        this.canvas.drawColor(ResourcesCompat.getColor(resources, R.color.canvas_bg, null))
        //Force re-draw
        this.binding.ivCanvas.invalidate()
    }

    fun calculateClickPos(x: Float, y: Float) {
        //bin : col length/ tile length
        val bin = this.binding.ivCanvas.width / 4

        for((i, note) in this.presenter.getPiano().notes.withIndex()) {
            val start = bin * note.tilePos
            val end = bin * (note.tilePos + 1)

            //if in tile-i x range
            if(x > start && x < end) {
                //if in note-i y range
                if(y > (note.top - 150) && y < note.bottom) {
                    //if note hasn't hidden
                    if(!this.presenter.isThreadHasInitiated && !this.presenter.isThreadHasRunning) {
                        if(i == 0) {
                            this.startThread(this.presenter.getPiano())
                            note.clicked()
                            break
                        }
                    }
                    else {
                        if(!note.isHidden && !note.isClicked) {
                            //hide node & add score to ui activity
                            note.clicked()
                            Log.d("click", "match")
                            this.presenter.addScore(10)
                            break
                        }
                    }
                }
            }
        }
    }

    fun initialization() {
        this.initiateCanvas()
        val piano = this.createPiano()
        this.drawTiles(piano)
        this.presenter.setPiano(piano)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden) {
            this.startThread(this.presenter.getPiano())
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if(context is IMainActivity) {
            this.listener = context
        }
        else {
            throw ClassCastException("$context must implement FragmentListener")
        }
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        return this.detector.onTouchEvent(event)
    }

    private inner class TilesGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            if (e != null) {
                calculateClickPos(e.x, e.y)
            }
            return super.onDown(e)
        }
    }
}