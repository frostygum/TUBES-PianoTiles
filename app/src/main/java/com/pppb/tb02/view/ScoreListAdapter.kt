package com.pppb.tb02.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.pppb.tb02.databinding.ScoreItemBinding
import com.pppb.tb02.model.Menu

class ScoreListAdapter(private val view: Context) : BaseAdapter() {
    private var scoreList: MutableList<Menu> = mutableListOf()

    override fun getItem(position: Int): Menu {
        return scoreList[position]
    }

    override fun getCount(): Int {
        return scoreList.size
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    fun update(scoreList: MutableList<Menu>) {
        this.scoreList = scoreList
        this.notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {val viewHolder: ViewHolder
        val itemView: View

        if (convertView == null) {
            itemView = ScoreItemBinding.inflate(LayoutInflater.from(this.view)).root
            viewHolder = ViewHolder(itemView)
            itemView.tag = viewHolder
        } else {
            itemView = convertView
            viewHolder = convertView.tag as ViewHolder
        }

        viewHolder.updateView(this.getItem(position))

        return itemView
    }

    private class ViewHolder(view: View) {
        private val binding: ScoreItemBinding = ScoreItemBinding.bind(view)

        fun updateView(score: Menu) {
            this.binding.tvLevelValue.text = score.level.toString()
            this.binding.tvScoreValue.text = score.score.toString()
        }
    }
}