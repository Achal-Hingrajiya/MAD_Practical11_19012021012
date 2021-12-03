package com.example.practical11_19012021012

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import src.DatabaseHandler
import src.Notes

class NoteNotificationActivity : AppCompatActivity() {

    val databaseHandler = DatabaseHandler(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_notification)

        val index = intent.getIntExtra("index",0)
        Log.i("Index_Info","Index from Notes Notification Activity While getting records: $index")


        val tvNoteTitle = findViewById<TextView>(R.id.tv_note_title)
        val tvNoteSubtitle = findViewById<TextView>(R.id.tv_note_subtitle)
        val tvNoteDesc = findViewById<TextView>(R.id.tv_note_desc)

        val tvNoteModifiedTime = findViewById<TextView>(R.id.note_modified_time)

        val note = databaseHandler.getNoteById(index)

        tvNoteTitle.text = note.title
        tvNoteSubtitle.text = note.subTitle
        tvNoteDesc.text = note.description
        tvNoteModifiedTime.text = note.reminderTime
    }
}