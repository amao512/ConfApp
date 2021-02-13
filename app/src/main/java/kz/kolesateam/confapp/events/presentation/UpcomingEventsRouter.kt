package kz.kolesateam.confapp.events.presentation

import android.content.Context
import android.content.Intent

class UpcomingEventsRouter {

    fun createIntent(context: Context): Intent {
        return Intent(context, UpcomingEventsActivity::class.java)
    }
}