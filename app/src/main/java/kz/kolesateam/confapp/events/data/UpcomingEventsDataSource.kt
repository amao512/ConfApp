package kz.kolesateam.confapp.events.data

import kz.kolesateam.confapp.events.data.models.BranchApiData
import retrofit2.Call
import retrofit2.http.GET

interface UpcomingEventsDataSource {

    @GET("/upcoming_events")
    fun getUpcomingEvents(): Call<List<BranchApiData>>
}