package kz.kolesateam.confapp.event_details.presentation

import android.content.Context
import android.content.Intent

const val INTENT_EXTRA_EVENT_ID_KEY = "event_id"

class EventDetailsRouter {

    fun createIntent(
        context: Context,
        eventId: Int
    ): Intent = Intent(context, EventDetailsActivity::class.java).apply {
        putExtra(INTENT_EXTRA_EVENT_ID_KEY, eventId)
    }
}