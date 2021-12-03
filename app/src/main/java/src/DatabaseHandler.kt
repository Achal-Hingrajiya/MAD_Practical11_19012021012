package src

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.util.*
import kotlin.collections.ArrayList

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "Pratical11_19012021012"
        private val TABLE_NOTES = "NotesTable2"
        private val KEY_ID = "id"
        private const val KEY_TITLE = "title"
        private val KEY_SUBTITLE = "subtitle"
        private const val KEY_REMINDERTIME = "reminder_time"
        private const val KEY_MODIFIEDTIME = "modified_time"

        private const val KEY_DESC = "description"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //creating table with fields
        val CREATE_NOTES_TABLE: String = ("CREATE TABLE " + TABLE_NOTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_SUBTITLE + " TEXT,"
                + KEY_DESC + " TEXT,"
                + KEY_REMINDERTIME + " TEXT,"
                + KEY_MODIFIEDTIME + " TEXT"+ ")")
        db?.execSQL(CREATE_NOTES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES)
        onCreate(db)
    }


    fun insertNote(note: Notes): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_ID, note.id)
        contentValues.put(KEY_TITLE, note.title)
        contentValues.put(KEY_SUBTITLE, note.subTitle)
        contentValues.put(KEY_DESC, note.description)
        contentValues.put(KEY_REMINDERTIME, note.reminderTime)
        contentValues.put(KEY_MODIFIEDTIME, note.modifiedTime)


        val success = db.insert(TABLE_NOTES, null, contentValues)
        db.close()
        return success
    }

    fun getNoteById(id: Int): Notes {

        val selectQuery = "SELECT  * FROM $TABLE_NOTES WHERE $KEY_ID = $id"

        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return Notes(0, "Error", "Something Went Wrong","Error Occured during fetching note form database",reminderTime = "now")
        }

        val noteID: Int
        val noteTitle: String
        val noteSubtitle: String
        val noteDesc: String
        val noteRemTime: String
        val noteModifiedTime: String
        val note : Notes

        if (cursor.moveToFirst()) {

            noteID = cursor.getInt(cursor.getColumnIndex(KEY_ID))
            noteTitle = cursor.getString(cursor.getColumnIndex(KEY_TITLE))
            noteSubtitle = cursor.getString(cursor.getColumnIndex(KEY_SUBTITLE))
            noteDesc = cursor.getString(cursor.getColumnIndex(KEY_DESC))
            noteRemTime = cursor.getString(cursor.getColumnIndex(KEY_REMINDERTIME))
            noteModifiedTime = cursor.getString(cursor.getColumnIndex(KEY_MODIFIEDTIME))

            note = Notes(noteID, noteTitle, noteSubtitle, noteDesc, noteModifiedTime, noteRemTime)

            Log.i("Index_Info","Index: $id noteId: $noteID noteTitle: $noteTitle")
            cursor.close()

            return  note

        }

        return Notes(0, "Error", "Something Went Wrong","No note having id $id is present in database",reminderTime = "now")
    }

    fun getAllNotes(): ArrayList<Notes> {
        val noteList: ArrayList<Notes> = ArrayList<Notes>()

        val selectQuery = "SELECT  * FROM $TABLE_NOTES"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var noteID: Int
        var noteTitle: String
        var noteSubtitle: String
        var noteDesc: String
        var noteRemTime: String
        var noteModifiedTime: String


        if (cursor.moveToFirst()) {
            do {
                noteID = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                noteTitle = cursor.getString(cursor.getColumnIndex(KEY_TITLE))
                noteSubtitle = cursor.getString(cursor.getColumnIndex(KEY_SUBTITLE))
                noteDesc = cursor.getString(cursor.getColumnIndex(KEY_DESC))
                noteRemTime = cursor.getString(cursor.getColumnIndex(KEY_REMINDERTIME))
                noteModifiedTime = cursor.getString(cursor.getColumnIndex(KEY_MODIFIEDTIME))


                val note = Notes(noteID, noteTitle, noteSubtitle, noteDesc, noteModifiedTime, noteRemTime)
                noteList.add(note)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return noteList
    }

    fun getNotesCount() : Int{

        val selectQuery = "SELECT COUNT($KEY_ID) FROM $TABLE_NOTES"
        val db = this.readableDatabase

        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return 0
        }

        var numberOfNotes : Int = 0

        if(cursor.moveToFirst()){
            numberOfNotes = cursor.getInt(0)
        }

        cursor.close()
        db.close()

        return  numberOfNotes
    }



    fun updateNote(note: Notes): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, note.id)
        contentValues.put(KEY_TITLE, note.title)
        contentValues.put(KEY_SUBTITLE, note.subTitle)
        contentValues.put(KEY_DESC, note.description)
        contentValues.put(KEY_REMINDERTIME, note.reminderTime)
        contentValues.put(KEY_MODIFIEDTIME, note.modifiedTime)

        val success = db.update(TABLE_NOTES, contentValues, KEY_ID + "=" + note.id, null)

        db.close()
        return success
    }


    fun deleteNote(note: Notes): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, note.id)

        val success = db.delete(TABLE_NOTES, KEY_ID + "=" + note.id, null)

        db.close()

        return success
    }
}