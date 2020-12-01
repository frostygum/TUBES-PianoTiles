package com.pppb.tb02.view

import android.os.Bundle
import android.util.Log
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

    private lateinit var fragments: List<Fragment>
    private lateinit var pianoFragment: FragmentPianoTilesGame
    private lateinit var pauseFragment: FragmentGamePause
    private lateinit var loseFragment: FragmentGameLose
    private var selected = Fragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Handler Initialization
        this.handler = PianoThreadHandler(this)

        this.presenter = MainPresenter(this)
        this.pianoFragment = FragmentPianoTilesGame.newInstance(this.presenter, this.handler)
        this.pauseFragment = FragmentGamePause.newInstance(this.presenter)
        this.loseFragment = FragmentGameLose.newInstance(this.presenter)
        this.fragments = listOf(this.pianoFragment, this.pauseFragment, this.loseFragment)

        this.changePage("GAME")
    }

    fun giveScore(point: Int) {
        this.presenter.addScore(point)
    }

    override fun updateUIScore(score: Int) {
        this.pianoFragment.updateUIScore(score)
    }

    fun updatePiano(piano: Piano) {
        this.presenter.setPiano(piano)
        this.pianoFragment.drawTiles(piano)
    }

    fun setThreadBlocked() {
        this.presenter.setThreadBlocked()
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
}