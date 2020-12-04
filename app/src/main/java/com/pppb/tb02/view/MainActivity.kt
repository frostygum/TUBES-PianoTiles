package com.pppb.tb02.view

import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.pppb.tb02.R
import com.pppb.tb02.databinding.ActivityMainBinding
import com.pppb.tb02.model.Menu
import com.pppb.tb02.model.Piano
import com.pppb.tb02.presenter.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IMainActivity {
    private lateinit var binding: ActivityMainBinding
    private lateinit var handler: PianoThreadHandler
    private lateinit var presenter: MainPresenter
    private lateinit var timer: StartTimer

    private val adapter: ScoreListAdapter = ScoreListAdapter(this)
    private lateinit var fragments: List<Fragment>
    private lateinit var pianoFragment: FragmentPianoTilesGame
    private lateinit var pauseFragment: FragmentGamePause
    private lateinit var loseFragment: FragmentGameLose
    private lateinit var scoreFragment: FragmentScore
    private var selected = Fragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Timer Initialization
        this.timer = StartTimer(1000, 500)

        //Presenter Initialization
        this.presenter = MainPresenter(this, this.application)

        //Handler Initialization
        this.handler = PianoThreadHandler(this.presenter)

        this.pianoFragment = FragmentPianoTilesGame.newInstance(this.presenter, this.handler)
        this.pauseFragment = FragmentGamePause.newInstance(this.presenter)
        this.loseFragment = FragmentGameLose.newInstance(this.presenter)
        this.scoreFragment = FragmentScore.newInstance(this.presenter, this.adapter)
        this.fragments = listOf(this.pianoFragment, this.pauseFragment, this.loseFragment, this.scoreFragment)

        //Default start page
        this.changePage("GAME")
    }

    override fun updateUIScore(score: Int) {
        this.pianoFragment.updateUIScore(score)
    }

    override fun updatePiano(piano: Piano) {
        this.pianoFragment.drawTiles(piano)
    }

    override fun setGameLost(scoreList: MutableList<Menu>) {
        this.loseFragment.setFinalLevel(this.presenter.getLevel())
        this.loseFragment.setFinalScore(this.presenter.getScore())
        this.adapter.update(scoreList)
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
            }
            "GAME" -> {
                this.selected = this.pianoFragment
            }
            "PAUSE" -> {
                this.selected = this.pauseFragment
            }
            "SCORE" -> {
                this.selected = this.scoreFragment
            }
        }

        for(fragment in this.fragments) {
            if(fragment == this.selected) {
                if (fragment.isAdded) {
                    ft.show(fragment)
                } else {
                    ft.add(container, fragment)
                }
            }
            else {
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