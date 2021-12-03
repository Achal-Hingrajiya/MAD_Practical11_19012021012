package src.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import com.example.practical11_19012021012.R

class AlarmBroadcastReceiver : BroadcastReceiver() {
    var mp: MediaPlayer? = null
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            mp = MediaPlayer.create(context, R.raw.alarm);
            mp?.start()
        }
    }
}

