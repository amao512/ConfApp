package kz.kolesateam.confapp.event_details.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.kolesateam.confapp.common.data.model.ProgressState
import kz.kolesateam.confapp.common.data.model.ResponseData
import kz.kolesateam.confapp.common.domain.models.EventData
import kz.kolesateam.confapp.event_details.domain.EventDetailsRepository
import kz.kolesateam.confapp.favorite_events.domain.FavoriteEventsRepository
import kz.kolesateam.confapp.notifications.NotificationAlarmHelper

class EventDetailsViewModel(
    private val eventDetailsRepository: EventDetailsRepository,
    private val favoriteEventsRepository: FavoriteEventsRepository,
    private val notificationAlarmHelper: NotificationAlarmHelper
) : ViewModel() {

    val eventLiveData: MutableLiveData<ResponseData<EventData, String>> = MutableLiveData()
    val toolbarFavoriteButtonLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val progressLiveData: MutableLiveData<ProgressState> = MutableLiveData()

    fun onStart(eventId: Int){
        progressLiveData.value = ProgressState.Loading

        getEventDetails(eventId)
    }

    fun onFavoriteButtonClick(event: EventData){
        when (event.isFavorite) {
            true -> {
                favoriteEventsRepository.removeEvent(eventId = event.id)
                notificationAlarmHelper.cancelNotificationAlarm(eventData = event)
            }
            else -> {
                favoriteEventsRepository.saveEvent(event = event)
                notificationAlarmHelper.createNotificationAlarm(eventData = event)
            }
        }

        toolbarFavoriteButtonLiveData.value = !event.isFavorite
        getEventDetails(eventId = event.id)
    }

    private fun getEventDetails(eventId: Int) = viewModelScope.launch {
        eventDetailsRepository.getEventDetails(
            eventId = eventId,
            result = {
                setFavoriteEvents(event = it)

                eventLiveData.value = ResponseData.Success(it)
                toolbarFavoriteButtonLiveData.value = it.isFavorite
            },
            fail = {
                eventLiveData.value = ResponseData.Error(it.toString())
            }
        )

        progressLiveData.value = ProgressState.Done
    }

    private fun setFavoriteEvents(event: EventData) {
        event.isFavorite = favoriteEventsRepository.isFavoriteEvent(eventId = event.id)
    }
}