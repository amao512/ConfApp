package kz.kolesateam.confapp.favorite_events.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.kolesateam.confapp.common.data.model.EventScreenNavigation
import kz.kolesateam.confapp.common.domain.models.EventData
import kz.kolesateam.confapp.favorite_events.data.model.EventsState
import kz.kolesateam.confapp.favorite_events.domain.FavoriteEventsRepository
import kz.kolesateam.confapp.notifications.NotificationAlarmHelper

class FavoriteEventsViewModel(
    private val favoriteEventsRepository: FavoriteEventsRepository,
    private val notificationAlarmHelper: NotificationAlarmHelper
) : ViewModel() {

    val favoriteEventsLiveData: MutableLiveData<List<EventData>> = MutableLiveData()
    val emptyEventsLiveData: MutableLiveData<EventsState> = MutableLiveData()
    val eventScreenNavigationLiveData: MutableLiveData<EventScreenNavigation> = MutableLiveData()

    fun onStart(){
        getFavoriteEvents()
    }

    fun onFavoriteButtonClick(eventData: EventData) {
        when (eventData.isFavorite) {
            true -> {
                favoriteEventsRepository.removeEvent(eventId = eventData.id)
                cancelEvent(eventData)
            }
            else -> {
                favoriteEventsRepository.saveEvent(event = eventData)
                scheduleEvent(eventData)
            }
        }
    }

    fun onEventClick(eventId: Int){
        eventScreenNavigationLiveData.value = EventScreenNavigation.EventDetails(eventId = eventId)
    }

    private fun scheduleEvent(eventData: EventData){
        notificationAlarmHelper.createNotificationAlarm(
            eventData = eventData
        )
    }

    private fun cancelEvent(eventData: EventData) {
        notificationAlarmHelper.cancelNotificationAlarm(
            eventData = eventData
        )
    }

    private fun getFavoriteEvents(){
        val favoriteEventsList: List<EventData> = favoriteEventsRepository.getAllFavoriteEvents()

        if(favoriteEventsList.isEmpty()){
            emptyEventsLiveData.value = EventsState.Empty
        } else {
            setFavoriteEvents(favoriteEventsList)
            checkEventsTime(favoriteEventsList)

            favoriteEventsLiveData.value = favoriteEventsList
            emptyEventsLiveData.value = EventsState.Full
        }
    }

    private fun setFavoriteEvents(events: List<EventData>) = events.forEach { event ->
        event.isFavorite = favoriteEventsRepository.isFavoriteEvent(eventId = event.id)
    }

    private fun checkEventsTime(events: List<EventData>) = events.forEach {
        it.isCompleted = favoriteEventsRepository.isCompletedEvent(eventTime = it.endTime)
    }
}