package kz.kolesateam.confapp.notifications

import android.app.Application
import android.app.Notification
import android.app.PendingIntent
import android.app.NotificationManager
import android.app.NotificationChannel
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kz.kolesateam.confapp.R
import kz.kolesateam.confapp.event_details.presentation.EventDetailsRouter
import kz.kolesateam.confapp.event_details.presentation.INTENT_EXTRA_EVENT_ID_KEY

const val FAVORITE_NOTIFICATION_CHANNEL = "favorite_notification_channel"

object NotificationHelper {

    private lateinit var application: Application
    private var notificationIdCounter: Int = 0

    fun init(application: Application){
        this.application = application
        initChannel()
    }

    fun sendNotification(
        title: String,
        content: String,
        eventId: Int
    ) {
        val notification: Notification = getNotification(
            title = title,
            content = content,
            eventId = eventId
        )

        NotificationManagerCompat.from(application).notify(notificationIdCounter++, notification)
    }

    private fun getNotification(
        title: String,
        content: String,
        eventId: Int
    ): Notification {

        val intent = EventDetailsRouter().createIntent(
            context = application,
            eventId = eventId
        )

        val pendingIntent = PendingIntent.getActivity(
            application,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(application, FAVORITE_NOTIFICATION_CHANNEL)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_corona)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun initChannel(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channelName = FAVORITE_NOTIFICATION_CHANNEL
        val importance: Int = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(
            channelName,
            channelName,
            importance
        )
        val notificationManager: NotificationManager = application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}