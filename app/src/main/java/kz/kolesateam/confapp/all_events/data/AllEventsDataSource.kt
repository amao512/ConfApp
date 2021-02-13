package kz.kolesateam.confapp.all_events.data

import kz.kolesateam.confapp.common.data.models.EventApiData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AllEventsDataSource {

    @GET("/branch_events/{branch_id}")
    fun getAllEvents(@Path("branch_id") branchId: Int): Call<List<EventApiData>>
}