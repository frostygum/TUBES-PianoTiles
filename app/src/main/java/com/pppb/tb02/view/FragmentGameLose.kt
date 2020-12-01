package com.pppb.tb02.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pppb.tb02.R
import com.pppb.tb02.databinding.FragmentGameLoseBinding
import com.pppb.tb02.model.Piano
import com.pppb.tb02.presenter.MainPresenter
import java.lang.ClassCastException

class FragmentGameLose: Fragment(R.layout.fragment_game_paused) {
    private lateinit var binding: FragmentGameLoseBinding
    private lateinit var listener: IMainActivity
    private lateinit var presenter: MainPresenter

    companion object {
        fun newInstance(presenter: MainPresenter): FragmentGameLose {
            val fragment = FragmentGameLose()
            fragment.presenter = presenter
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = FragmentGameLoseBinding.inflate(inflater, container, false)

        this.binding.btnStart.setOnClickListener {
            this.listener.changePage("GAME")
        }

        return this.binding.root
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
}