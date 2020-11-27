package com.pppb.tb02.model

class Tile {
    var notes: List<Note> = listOf()

    fun setNewNotes(notes: List<Note>) {
        this.notes = notes
    }
}