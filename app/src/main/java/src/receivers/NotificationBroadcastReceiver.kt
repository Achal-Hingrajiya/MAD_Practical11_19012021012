package src.receivers

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.practical11_19012021012.NoteNotificationActivity
import com.example.practical11_19012021012.R
import src.DatabaseHandler
import src.Notes

class NotificationBroadcastReceiver : BroadcastReceiver() {
    val channelId = "notesNotificationChannel"

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {


        val index : Int = intent!!.getIntExtra("index", 0)
        Log.i("Index_Info","Index from NotificationBroadcast receiver While onReceive: $index")

        val intentOpenActivity = Intent(context, NoteNotificationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("index", index) }


        val contentIntent = PendingIntent.getActivity(
                context,
                index,
                intentOpenActivity,
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        val databaseHandler = DatabaseHandler(context!!)
        val note = databaseHandler.getNoteById(index)


        val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("title: ${note.title} i: $index")
                .setContentText(note.description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
        with(NotificationManagerCompat.from(context)) {
            notify(index, builder.build())
        }
    }
}