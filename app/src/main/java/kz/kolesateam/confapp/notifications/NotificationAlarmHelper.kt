package kz.kolesateam.confapp.notifications

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import kz.kolesateam.confapp.common.domain.models.EventData

const val NOTIFICATION_CONTENT_KEY = "notification_title"
const val NOTIFICATION_EVENT_ID_KEY = "notification_event_id"

class NotificationAlarmHelper(
    private val application: Application
) {

    fun createNotificationAlarm(
        eventData: EventData
    ){
        val alarmManager: AlarmManager? = application.getSystemService(
            Context.ALARM_SERVICE
        ) as? AlarmManager

        val pendingIntent: PendingIntent = Intent(application, NotificationAlarmBroadcastReceiver::class.java).apply {
            putExtra(NOTIFICATION_CONTENT_KEY, eventData.title)
            putExtra(NOTIFICATION_EVENT_ID_KEY, eventData.id)
        }.let {
            PendingIntent.getBroadcast(application, eventData.id, it, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val eventStartTimeInMilliseconds: Long = eventData.startTime.toEpochSecond() * 1000

        alarmManager?.setExact(
            AlarmManager.RTC_WAKEUP,
            eventStartTimeInMilliseconds,
            pendingIntent
        )
    }

    fun cancelNotificationAlarm(
        eventData: EventData
    ){
        val alarmManager: AlarmManager? = application.getSystemService(
            Context.ALARM_SERVICE
        ) as? AlarmManager

        val pendingIntent: PendingIntent = Intent(application, NotificationAlarmBroadcastReceiver::class.java).let {
            PendingIntent.getBroadcast(application, eventData.id, it, PendingIntent.FLAG_CANCEL_CURRENT)
        }

        alarmManager?.cancel(pendingIntent)
    }
}