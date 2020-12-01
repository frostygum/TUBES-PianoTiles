package com.pppb.tb02.storage

import android.content.Context
import android.content.SharedPreferences

class ViewStorage(ctx: Context)  {
    private var sp: SharedPreferences
    private val spName: String = "sp_display"

    init {
        this.sp = ctx.getSharedPreferences(spName, 0)
    }
}