package com.pppb.tb02.model

class Piano() {
    var notes: List<Note> = listOf()

    fun add(top: Int, tilePos: Int) {
        if(notes.isEmpty()) {
            this.notes += Note(top, tilePos)
        }
        else {
            val pos = this.notes[this.notes.size - 1].top - 700
            this.notes += Note(pos, tilePos)
        }
    }
}