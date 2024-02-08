import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.erabook.MainActivity
import com.example.erabook.R
import com.example.erabook.util.SnoozeReceiver


private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {


    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    //TODO("Make the intent take me to the fragment and change the textView")

    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val snoozeIntent = Intent(applicationContext, SnoozeReceiver::class.java)
    val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(
        applicationContext,
        REQUEST_CODE,
        snoozeIntent,
        FLAG_ONE_SHOT or PendingIntent.FLAG_MUTABLE
    )

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.erabook_notification_channel_id)
    )
        .setSmallIcon(R.drawable.book_icon)
        .setContentTitle(
            applicationContext
                .getString(R.string.notification_title)
        )
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setContentText(messageBody)
        .addAction(
            R.drawable.book_icon,
            applicationContext.getString(R.string.snooze),
            snoozePendingIntent
        )

    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelNotifications(){
    cancelAll()
}