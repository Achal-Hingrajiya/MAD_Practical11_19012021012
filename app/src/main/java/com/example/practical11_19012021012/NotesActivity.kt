package com.example.practical11_19012021012

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.AnimationDrawable
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ListView
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import src.DatabaseHandler
import src.ListBaseAdapter
import src.Notes
import src.receivers.AlarmBroadcastReceiver
import src.receivers.NotificationBroadcastReceiver
import java.util.*

class NotesActivity : AppCompatActivity() {


    val channelId = "notesNotificationChannel"
    val channelName = "Notes Notification Channel"
    private lateinit var sharedPreferences : SharedPreferences


    private val databaseHandler = DatabaseHandler(this)


    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()

        setContentView(R.layout.activity_notes)

        sharedPreferences = getSharedPreferences("UserInfo",Context.MODE_PRIVATE)

        val bNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val fabAddNote = findViewById<FloatingActionButton>(R.id.fab_add_note)
        val lvNotes = findViewById<ListView>(R.id.lv_notes)

        bNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.dashboard_page -> {
                    Intent(this, DashboardActivity::class.java).apply {
                        startActivity(this)
                    }
                    return@setOnItemSelectedListener true
                }
                else ->
                    return@setOnItemSelectedListener true
            }
        }

        val lvAdapter = ListBaseAdapter(this)

        lvNotes.adapter =lvAdapter


        fabAddNote.setOnClickListener {
            showAddNoteDialog(lvAdapter)
        }


        val ivPhotos = findViewById<View>(R.id.iv_photos_notes_activity)
        ivPhotos.setBackgroundResource(R.drawable.photos_animation_list)
        val photoAnim = ivPhotos.background as AnimationDrawable
        photoAnim.start()


    }


    private fun createNotificationChannel() {
        val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val description = "Notification channel for notes notification"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance)
            channel.description = description
            channel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun getMillis(hour:Int, min:Int):Long
    {
        val setcalendar = Calendar.getInstance()
        setcalendar[Calendar.HOUR_OF_DAY] = hour
        setcalendar[Calendar.MINUTE] = min
        setcalendar[Calendar.SECOND] = 0
        return setcalendar.timeInMillis
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    fun getCurrentDateTime(remindertime : Long): String {

        val df: SimpleDateFormat = SimpleDateFormat("MMM, dd yyyy hh:mm:ss a")
        return df.format(Date(remindertime))
    }



    @RequiresApi(Build.VERSION_CODES.N)
    private fun showAddNoteDialog(lvAdapter: ListBaseAdapter){
        val dialogTitle = "Add Note"
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(dialogTitle)

        val customLayout: View = LayoutInflater.from(this).inflate(R.layout.dialog_note, null)

        builder.setView(customLayout)
        builder.setPositiveButton(
                "Save",
                DialogInterface.OnClickListener {
                    dialog, which ->

                    val title = customLayout.findViewById<TextInputEditText>(R.id.tiet_note_title).text.toString()
                    val subtitle = customLayout.findViewById<TextInputEditText>(R.id.tiet_note_subtitle).text.toString()
                    val desc = customLayout.findViewById<TextInputEditText>(R.id.tiet_note_desc).text.toString()
                    val reminderSwitch = customLayout . findViewById <SwitchMaterial>(R.id.reminderSwitch)
                    val timePicker = customLayout.findViewById<TimePicker>(R.id.reminder_time_picker)
                    val isReminder = reminderSwitch.isChecked
                    val hour = timePicker.hour
                    val minute = timePicker.minute
                    val remTimeInMillis = getMillis(hour, minute)
                    val reminderTime = getCurrentDateTime(remTimeInMillis)

                    if(title.isBlank() or subtitle.isBlank() or desc.isBlank()){
                        Toast.makeText(this, "All Fields are required.",Toast.LENGTH_LONG).show()
                    }
                    else{
                        var id : Int = sharedPreferences.getInt("id",0)
                        val note = Notes(id = id,
                            title= title,
                            subTitle= subtitle,
                            description = desc,
                            isReminder = isReminder,
                            reminderTime = reminderTime,
                        )


                        databaseHandler.insertNote(note)
                        setReminder(this, note, remTimeInMillis)
                        id += 1
                        val editor = sharedPreferences.edit()
                        editor.putInt("id", id)
                        editor.apply()

                        lvAdapter.notifyDataSetChanged()

                    }


                })
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }



    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun setReminder(context: Context, notes: Notes, timeInMillis :Long)
    {
        val index = sharedPreferences.getInt("id",0)
        val intent = Intent(context, NotificationBroadcastReceiver::class.java)
        intent.putExtra("index", index)
        Log.i("Index_Info","Index from Notes Activity While setting reminder: $index")
        val pendingIntent = PendingIntent.getBroadcast(
                context,
                index,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        if (notes.isReminder) {
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent
            )
        }
        else
            alarmManager.cancel(pendingIntent)
    }

}