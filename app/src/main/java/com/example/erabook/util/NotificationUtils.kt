import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.erabook.activities.MainActivity
import com.example.erabook.R


private const val NOTIFICATION_ID = 0
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {


    val contentIntent = Intent(applicationContext, MainActivity::class.java)

    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
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

    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}