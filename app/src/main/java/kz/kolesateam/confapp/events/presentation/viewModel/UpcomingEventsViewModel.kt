package kz.kolesateam.confapp.events.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.kolesateam.confapp.common.data.model.EventScreenNavigation
import kz.kolesateam.confapp.common.data.model.ProgressState
import kz.kolesateam.confapp.common.data.model.ResponseData
import kz.kolesateam.confapp.common.domain.models.BranchData
import kz.kolesateam.confapp.common.domain.models.EventData
import kz.kolesateam.confapp.events.domain.UpcomingEventsRepository
import kz.kolesateam.confapp.common.domain.UserNameDataSource
import kz.kolesateam.confapp.events.presentation.models.BranchListItem
import kz.kolesateam.confapp.events.presentation.models.HeaderItem
import kz.kolesateam.confapp.events.presentation.models.UpcomingEventListItem
import kz.kolesateam.confapp.favorite_events.domain.FavoriteEventsRepository
import kz.kolesateam.confapp.notifications.NotificationAlarmHelper

class UpcomingEventsViewModel(
    private val upcomingEventsRepository: UpcomingEventsRepository,
    private val userNameSharedPrefsDataSource: UserNameDataSource,
    private val favoriteEventsRepository: FavoriteEventsRepository,
    private val notificationAlarmHelper: NotificationAlarmHelper
) : ViewModel() {

    val loadEventsStateLiveData: MutableLiveData<ResponseData<List<UpcomingEventListItem>, String>> = MutableLiveData()
    val progressLiveData: MutableLiveData<ProgressState> = MutableLiveData()
    val eventScreenNavigationLiveData: MutableLiveData<EventScreenNavigation> = MutableLiveData()

    fun onStart() {
        progressLiveData.value = ProgressState.Loading

        upcomingEventsRepository.getUpcomingEvents(
            result = {
                loadEventsStateLiveData.value = ResponseData.Success(prepareAdapterList(it))
                progressLiveData.value = ProgressState.Done
            },
            fail = {
                loadEventsStateLiveData.value = ResponseData.Error(it.toString())
                progressLiveData.value = ProgressState.Done
            }
        )
    }

    fun onFavoriteButtonClick(eventData: EventData) {
        when(eventData.isFavorite){
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

    fun onBranchClick(
        branchId: Int,
        branchTitle: String
    ) {
        eventScreenNavigationLiveData.value = EventScreenNavigation.AllEvents(
            branchId = branchId,
            branchTitle = branchTitle
        )
    }

    fun onEventClick(eventId: Int){
        eventScreenNavigationLiveData.value = EventScreenNavigation.EventDetails(eventId = eventId)
    }

    fun onFavoriteEventsButtonClick(){
        eventScreenNavigationLiveData.value = EventScreenNavigation.FavoriteEvents
    }

    private fun scheduleEvent(eventApiData: EventData){
        notificationAlarmHelper.createNotificationAlarm(
            eventData = eventApiData
        )
    }

    private fun cancelEvent(eventApiData: EventData) {
        notificationAlarmHelper.cancelNotificationAlarm(
            eventData = eventApiData
        )
    }

    private fun prepareAdapterList(branchList: List<BranchData>): List<UpcomingEventListItem> {
        return listOf(getHeaderItem()) + getBranchItems(branchList)
    }

    private fun getHeaderItem(): UpcomingEventListItem = HeaderItem(
        userName = userNameSharedPrefsDataSource.getUserName()
    )

    private fun getBranchItems(branchList: List<BranchData>): List<UpcomingEventListItem> = branchList.map { branchData ->
        branchData.events.forEach {
            it.isFavorite = favoriteEventsRepository.isFavoriteEvent(eventId = it.id)
        }

        BranchListItem(data = branchData)
    }
}