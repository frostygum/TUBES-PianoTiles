package com.pppb.tb02.storage

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pppb.tb02.model.Menu

class ViewStorage(ctx: Context)  {
    private var sp: SharedPreferences
    private val spName: String = "sp_display"
    private val keyScoreList: String = "SCORE_LIST"

    init {
        this.sp = ctx.getSharedPreferences(spName, 0)
    }

    fun saveScoreList(scoreList: List<Menu>) {
        val scoreListStr = Gson().toJson(scoreList)
        this.sp.edit().putString(keyScoreList, scoreListStr).commit()
    }

    fun getFoodList(): List<Menu> {
        val foodStr = this.sp.getString(keyScoreList, "")
        val sType = object : TypeToken<List<Menu>>() { }.type

        return Gson().fromJson(foodStr, sType) ?: listOf()
    }
}