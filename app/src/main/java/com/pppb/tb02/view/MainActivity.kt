package com.pppb.tb02.view

import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.pppb.tb02.databinding.ActivityMainBinding
import com.pppb.tb02.model.Piano
import com.pppb.tb02.presenter.MainPresenter

class MainActivity : AppCompatActivity(), IMainActivity {
    private lateinit var binding: ActivityMainBinding
    private lateinit var handler: PianoThreadHandler
    private lateinit var presenter: MainPresenter
    private lateinit var timer: StartTimer

    private lateinit var fragments: List<Fragment>
    private lateinit var pianoFragment: FragmentPianoTilesGame
    private lateinit var pauseFragment: FragmentGamePause
    private lateinit var loseFragment: FragmentGameLose
    private var selected = Fragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Timer Initialization
        this.timer = StartTimer(1000, 500)

        //Presenter Initialization
        this.presenter = MainPresenter(this)

        //Handler Initialization
        this.handler = PianoThreadHandler(this.presenter)

        this.pianoFragment = FragmentPianoTilesGame.newInstance(this.presenter, this.handler)
        this.pauseFragment = FragmentGamePause.newInstance(this.presenter)
        this.loseFragment = FragmentGameLose.newInstance(this.presenter)
        this.fragments = listOf(this.pianoFragment, this.pauseFragment, this.loseFragment)

        //Default start page
        this.changePage("GAME")
    }

    override fun updateUIScore(score: Int) {
        this.pianoFragment.updateUIScore(score)
    }

    override fun updatePiano(piano: Piano) {
        this.pianoFragment.drawTiles(piano)
    }

    override fun setGameLost() {
        this.loseFragment.setFinalLevel(this.presenter.getLevel())
        this.loseFragment.setFinalScore(this.presenter.getScore())
        this.timer.start()
    }

    override fun setGameLevel(level: Int) {
        this.pianoFragment.updateGameLevel(level)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if(hasFocus) {
            this.pianoFragment.initialization()
        }
    }

    override fun changePage(page: String) {
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        val container: Int = this.binding.fragmentContainer.id
        this.selected = Fragment()

        when (page) {
            "LOSE" -> {
                this.selected = this.loseFragment
                if (this.loseFragment.isAdded) {
                    ft.show(this.loseFragment)
                } else {
                    ft.add(container, this.loseFragment)
                }
            }
            "GAME" -> {
                this.selected = this.pianoFragment
                if (this.pianoFragment.isAdded) {
                    ft.show(this.pianoFragment)
                } else {
                    ft.add(container, this.pianoFragment)
                }
            }
            "PAUSE" -> {
                this.selected = this.pauseFragment
                if (this.pauseFragment.isAdded) {
                    ft.show(this.pauseFragment)
                } else {
                    ft.add(container, this.pauseFragment)
                }
            }
        }

        for(fragment in this.fragments) {
            if(fragment != this.selected) {
                if(fragment.isAdded) {
                    ft.hide(fragment)
                }
            }
        }

        ft.commit()
    }

    private inner class StartTimer(startTime: Long, interval: Long) : CountDownTimer(startTime, interval) {
        override fun onFinish() {
            changePage("LOSE")
        }

        override fun onTick(millisUntilFinished: Long) {
        }
    }
}