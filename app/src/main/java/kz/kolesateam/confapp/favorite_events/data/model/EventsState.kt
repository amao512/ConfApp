package kz.kolesateam.confapp.favorite_events.data.model

sealed class EventsState {
    object Empty : EventsState()
    object Full : EventsState()
}