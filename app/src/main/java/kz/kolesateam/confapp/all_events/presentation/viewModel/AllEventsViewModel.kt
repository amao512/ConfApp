package kz.kolesateam.confapp.all_events.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.kolesateam.confapp.all_events.domain.AllEventsRepository
import kz.kolesateam.confapp.all_events.presentation.models.AllEventsListItem
import kz.kolesateam.confapp.all_events.presentation.models.BranchTitleItem
import kz.kolesateam.confapp.all_events.presentation.models.EventListItem
import kz.kolesateam.confapp.common.data.model.EventScreenNavigation
import kz.kolesateam.confapp.common.data.model.ProgressState
import kz.kolesateam.confapp.common.data.model.ResponseData
import kz.kolesateam.confapp.common.domain.models.EventData
import kz.kolesateam.confapp.favorite_events.domain.FavoriteEventsRepository
import kz.kolesateam.confapp.notifications.NotificationAlarmHelper

class AllEventsViewModel(
    private val allEventsRepository: AllEventsRepository,
    private val favoriteEventsRepository: FavoriteEventsRepository,
    private val notificationAlarmHelper: NotificationAlarmHelper
) : ViewModel() {

    val allEventsLiveData: MutableLiveData<ResponseData<List<AllEventsListItem>, String>> = MutableLiveData()
    val progressLiveData: MutableLiveData<ProgressState> = MutableLiveData()
    val eventScreenNavigationLiveData: MutableLiveData<EventScreenNavigation> = MutableLiveData()

    fun onStart(branchId: Int, branchTitle: String) {
        getAllEvents(
                branchId = branchId,
                branchTitle = branchTitle
        )
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

    fun onFavoriteEventsButtonClick(){
        eventScreenNavigationLiveData.value = EventScreenNavigation.FavoriteEvents
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

    private fun getAllEvents(
        branchId: Int,
        branchTitle: String
    ) = viewModelScope.launch {
        progressLiveData.value = ProgressState.Loading

        allEventsRepository.getAllEvents(
            result = {
                setFavoriteEvents(events = it)
                checkEventsTime(events = it)

                allEventsLiveData.value = ResponseData.Success(
                        prepareAllEvents(
                            eventsList = it,
                            branchTitle = branchTitle
                        )
                )
            },
            fail = {
                allEventsLiveData.value = ResponseData.Error(it.toString())
            },
            branchId = branchId
        )

        progressLiveData.value = ProgressState.Done
    }

    private fun setFavoriteEvents(events: List<EventData>) = events.forEach { event ->
        event.isFavorite = favoriteEventsRepository.isFavoriteEvent(eventId = event.id)
    }

    private fun prepareAllEvents(
        eventsList: List<EventData>,
        branchTitle: String
    ): List<AllEventsListItem> = listOf(getBranchTitleItem(branchTitle)) + getEventsItem(eventsList)

    private fun getBranchTitleItem(
        branchTitle: String
    ): AllEventsListItem = BranchTitleItem(branchTitle = branchTitle)

    private fun getEventsItem(events: List<EventData>): List<AllEventsListItem> = events.map {
            EventListItem(data = it)
        }

    private fun checkEventsTime(events: List<EventData>) = events.forEach {
        it.isCompleted = favoriteEventsRepository.isCompletedEvent(eventTime = it.endTime)
    }
}