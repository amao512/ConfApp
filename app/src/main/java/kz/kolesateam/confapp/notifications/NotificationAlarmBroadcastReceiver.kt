package kz.kolesateam.confapp.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kz.kolesateam.confapp.event_details.presentation.DEFAULT_EVENT_ID

const val COMING_EVENT = "Не пропустите следующий доклад"

class NotificationAlarmBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val content: String = intent?.getStringExtra(NOTIFICATION_CONTENT_KEY).orEmpty()
        val eventId: Int = intent?.getIntExtra(NOTIFICATION_EVENT_ID_KEY, DEFAULT_EVENT_ID) ?: DEFAULT_EVENT_ID

        NotificationHelper.sendNotification(
            title = COMING_EVENT,
            content = content,
            eventId = eventId
        )
    }
}