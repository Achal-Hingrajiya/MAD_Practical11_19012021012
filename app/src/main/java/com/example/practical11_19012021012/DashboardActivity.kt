package com.example.practical11_19012021012

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import src.LoginInfo
import src.receivers.AlarmBroadcastReceiver

class DashboardActivity : AppCompatActivity() {

    lateinit var btnSetAlarm : Button;

//    private  lateinit var binding :
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        var logoutBtn = findViewById<Button>(R.id.tbtn_logout)
        btnSetAlarm = findViewById(R.id.tbtn_set_alarm)

        val name = LoginInfo.full_name
        val email = LoginInfo.email
        val phone = LoginInfo.phone_number
        val city = LoginInfo.city

        val tvName = findViewById<TextView>(R.id.dash_name)
        val tvEmail = findViewById<TextView>(R.id.dash_email)
        val tvFullName = findViewById<TextView>(R.id.tv_full_name)
        val tvEmailInfo = findViewById<TextView>(R.id.tv_email)
        val tvPhone = findViewById<TextView>(R.id.tv_phn_no)
        val tvCity = findViewById<TextView>(R.id.tv_city)

        tvName.text = name
        tvFullName.text = name
        tvEmail.text = email
        tvEmailInfo.text = email
        tvCity.text = city
        tvPhone.text = phone


        var bNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.notes_page ->{
                    Intent(this, NotesActivity :: class.java).apply {
                        startActivity(this)
                    }
                    return@setOnItemSelectedListener true
                }
                else -> {
                    return@setOnItemSelectedListener true
                }
            }
        }


    logoutBtn.setOnClickListener {
            Toast.makeText(this, "Logged Out!", Toast.LENGTH_SHORT).show()
            LoginInfo.logout()
            Intent(this, LoginActivity::class.java).apply {
                startActivity(this)
            }
        }

        btnSetAlarm.setOnClickListener {
            showTimerDialog()
        }


        val vHeart = findViewById<View>(R.id.iv_heart)
        vHeart.setBackgroundResource(R.drawable.heart_animation_list)
        val heartAnim = vHeart.background as AnimationDrawable
        heartAnim.start()

        val ivPhotos = findViewById<View>(R.id.iv_photos)
        ivPhotos.setBackgroundResource(R.drawable.photos_animation_list)
        val photoAnim = ivPhotos.background as AnimationDrawable
        photoAnim.start()


    }



    @RequiresApi(Build.VERSION_CODES.N)
    fun showTimerDialog()
    {
        val cldr: Calendar = Calendar.getInstance()
        val hour: Int = cldr.get(Calendar.HOUR_OF_DAY)
        val minutes: Int = cldr.get(Calendar.MINUTE)
// time picker dialog
        val picker = TimePickerDialog(
                this@DashboardActivity,
                { _, sHour, sMinute -> sendDialogDataToActivity(sHour, sMinute) },
                hour,
                minutes,
                false
        )

        picker.show()
    }


    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun sendDialogDataToActivity(hour: Int, minute: Int) {
        val textAlarmTime = findViewById<TextView>(R.id.txt_alarmtime)
        val alarmCalendar = Calendar.getInstance()
        val year: Int = alarmCalendar.get(Calendar.YEAR)
        val month: Int = alarmCalendar.get(Calendar.MONTH)
        val day: Int = alarmCalendar.get(Calendar.DATE)
        alarmCalendar.set(year, month, day, hour, minute, 0)
        textAlarmTime.text = SimpleDateFormat("hh:mm ss a").format(alarmCalendar.time)

        setAlarm(alarmCalendar.timeInMillis, "Start")
        btnSetAlarm.visibility = View.GONE

        Toast.makeText(
                this,
                "Time: hours:${hour}, minutes:${minute}, millis:${alarmCalendar.timeInMillis}",
                Toast.LENGTH_SHORT
        ).show()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun setAlarm(millisTime: Long, str: String)
    {
        val intent = Intent(this, AlarmBroadcastReceiver::class.java)
        intent.putExtra("Service1", str)
        val pendingIntent = PendingIntent.getBroadcast(applicationContext, 234324243, intent, 0)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        if(str == "Start") {
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    millisTime,
                    pendingIntent
            )
        }else if(str == "Stop")
        {
            alarmManager.cancel(pendingIntent)
        }
    }


}