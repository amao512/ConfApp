package kz.kolesateam.confapp.favorite_events.presentation

import android.content.Context
import android.content.Intent

class FavoriteEventsRouter {

    fun createIntent(context: Context): Intent {
        return Intent(context, FavoriteEventsActivity::class.java)
    }
}