package src

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.icu.util.Calendar
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.practical11_19012021012.R
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class ListBaseAdapter(private val context: Context) : BaseAdapter() {

    private val databaseHandler = DatabaseHandler(context.applicationContext)

    private var dataSource: ArrayList<Notes> = databaseHandler.getAllNotes()


    @RequiresApi(Build.VERSION_CODES.N)
    fun getHour(): Int {
        val cal = Calendar.getInstance()
        return cal[Calendar.HOUR_OF_DAY
        ]
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getMinute(): Int {
        val cal = Calendar.getInstance()
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
        desc.setText(note.description)
        reminderSwitch.isChecked = note.isReminder

        timePicker.hour = getHour()
        timePicker.minute = getMinute()

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
                    note.description = desc.text.toString()
                    note.isReminder = reminderSwitch.isChecked
                    note.modifiedTime = modifiedTime


                    databaseHandler.updateNote(note)
                    notifyDataSetChanged()

                })

        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    override fun notifyDataSetChanged() {
        dataSource = databaseHandler.getAllNotes()
        super.notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Notes {
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
        val tvNoteReminderTime = rowView.findViewById<TextView>(R.id.note_reminder_time)
        val btnEdit = rowView.findViewById<Button>(R.id.btn_edit_note)
        val btnDelete = rowView.findViewById<Button>(R.id.btn_delete_note)
        val llSection = rowView.findViewById<LinearLayout>(R.id.ll_note_section2) as ViewGroup



        val note = getItem(position) as Notes


        if(note.modifiedTime.isNotBlank()){

            inflater.inflate(R.layout.tv_modified_time, llSection)
            val tvNoteModifiedTime = rowView.findViewById<TextView>(R.id.note_modified_time)

            tvNoteModifiedTime.text = note.modifiedTime
        }
        tvNoteTitle.text = note.title
        tvNoteSubtitle.text = note.subTitle
        tvNoteDesc.text = note.description
        tvNoteReminderTime.text = note.reminderTime

        btnEdit.setOnClickListener {

            showEditNoteDialog(note)
        }


        btnDelete.setOnClickListener {

            databaseHandler.deleteNote(getItem(position))
            dataSource.removeAt(position)
            notifyDataSetChanged()
        }
        return rowView
    }
}