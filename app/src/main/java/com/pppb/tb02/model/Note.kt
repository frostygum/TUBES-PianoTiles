package com.pppb.tb02.model

class Note(var top: Int) {
    var isHidden = true
    val length: Int = 500
    var bottom: Int = top + length

    fun set(top: Int) {
        this.top = top
        this.bottom = top + length
    }

    fun hide() {
        this.isHidden = true
    }

    fun unHide() {
        this.isHidden = false
    }

    override fun toString(): String {
        return "[TOP: $top, BOTTOM: $bottom, IS_HIDDEN?: $isHidden]"
    }
}