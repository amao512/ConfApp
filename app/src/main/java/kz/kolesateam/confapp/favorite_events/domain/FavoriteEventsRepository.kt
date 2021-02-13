package kz.kolesateam.confapp.favorite_events.domain

import kz.kolesateam.confapp.common.domain.models.EventData
import org.threeten.bp.ZonedDateTime

interface FavoriteEventsRepository {

    fun saveEvent(event: EventData)

    fun removeEvent(eventId: Int?)

    fun getAllFavoriteEvents(): List<EventData>

    fun isFavoriteEvent(eventId: Int): Boolean

    fun isCompletedEvent(eventTime: ZonedDateTime): Boolean
}