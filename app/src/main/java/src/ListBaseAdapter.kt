package src

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import com.example.practical11_19012021012.R
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import src.receivers.AlarmBroadcastReceiver
import java.text.SimpleDateFormat
import java.util.*

class ListBaseAdapter(private val context: Context, private val dataSource: ArrayList<Notes>) : BaseAdapter() {


    @RequiresApi(Build.VERSION_CODES.N)
    fun getHour(remindertime: Long): Int {
        val cal = Calendar.getInstance()
        cal.time = Date(remindertime)
        return cal[Calendar.HOUR_OF_DAY
        ]
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getMinute(remindertime: Long): Int {
        val cal = Calendar.getInstance()
        cal.time = Date(remindertime)
        return cal[Calendar.MINUTE]
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun getMillis(hour: Int, min: Int): Long {
        val setcalendar = Calendar.getInstance()
        setcalendar[Calendar.HOUR_OF_DAY] = hour
        setcalendar[Calendar.MINUTE] = min
        setcalendar[Calendar.SECOND] = 0
        return setcalendar.timeInMillis
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    fun getCurrentDateTime(remindertime: Long): String {

        val df: SimpleDateFormat = SimpleDateFormat("MMM, dd yyyy hh:mm:ss a")
        return df.format(Date(remindertime))
    }



    @RequiresApi(Build.VERSION_CODES.N)
    private fun showEditNoteDialog(note: Notes) {
        val dialogTitle = "Edit Note"
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle(dialogTitle)

        val customLayout: View = LayoutInflater.from(context).inflate(R.layout.dialog_note, null)

        val title = customLayout.findViewById<TextInputEditText>(R.id.tiet_note_title)
        val subtitle = customLayout.findViewById<TextInputEditText>(R.id.tiet_note_subtitle)
        val desc = customLayout.findViewById<TextInputEditText>(R.id.tiet_note_desc)
        val reminderSwitch = customLayout.findViewById<SwitchMaterial>(R.id.reminderSwitch)
        val timePicker = customLayout.findViewById<TimePicker>(R.id.reminder_time_picker)
//
        title.setText(note.title)
        subtitle.setText(note.subTitle)
        desc.setText(note.Description)
        reminderSwitch.isChecked = note.isReminder

        timePicker.hour = getHour(note.remindertime)
        timePicker.minute = getMinute(note.remindertime)

        builder.setView(customLayout)
        builder.setPositiveButton(
                "Save Changes",
                DialogInterface.OnClickListener { dialog, which ->

                    val hour = timePicker.hour
                    val minute = timePicker.minute
                    val remTimeInMillis = getMillis(hour, minute)
                    val modifiedTime = getCurrentDateTime(remTimeInMillis)

                    note.title = title.text.toString()
                    note.subTitle = subtitle.text.toString()
                    note.Description = desc.text.toString()
                    note.isReminder = reminderSwitch.isChecked
                    note.modifiedTime = modifiedTime


                    notifyDataSetChanged()

                })

        val dialog: AlertDialog = builder.create()
        dialog.show()

    }


    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return dataSource[position].id.toLong()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val rowView = inflater.inflate(R.layout.card_note, parent, false)

        val tvNoteTitle = rowView.findViewById<TextView>(R.id.tv_note_title)
        val tvNoteSubtitle = rowView.findViewById<TextView>(R.id.tv_note_subtitle)
        val tvNoteDesc = rowView.findViewById<TextView>(R.id.tv_note_desc)
        val tvNoteModifiedTime = rowView.findViewById<TextView>(R.id.note_modified_time)
        val btnEdit = rowView.findViewById<Button>(R.id.btn_edit_note)
        val btnDelete = rowView.findViewById<Button>(R.id.btn_delete_note)

        val note = getItem(position) as Notes

        tvNoteTitle.text = note.title
        tvNoteSubtitle.text = note.subTitle
        tvNoteDesc.text = note.Description
        tvNoteModifiedTime.text = note.modifiedTime

        btnEdit.setOnClickListener {

            showEditNoteDialog(note)
        }


        btnDelete.setOnClickListener {
            Notes.notesArray.removeAt(position)
            notifyDataSetChanged()
        }
        return rowView
    }
}