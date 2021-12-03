package com.example.practical11_19012021012

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import src.Notes

class NoteNotificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_notification)

        val index = intent.getIntExtra("index",0)

        val tvNoteTitle = findViewById<TextView>(R.id.tv_note_title)
        val tvNoteSubtitle = findViewById<TextView>(R.id.tv_note_subtitle)
        val tvNoteDesc = findViewById<TextView>(R.id.tv_note_desc)
        val tvNoteModifiedTime = findViewById<TextView>(R.id.note_modified_time)

        val note = Notes.notesArray[index]

        tvNoteTitle.text = note.title
        tvNoteSubtitle.text = note.subTitle
        tvNoteDesc.text = note.Description
        tvNoteModifiedTime.text = note.modifiedTime
    }
}