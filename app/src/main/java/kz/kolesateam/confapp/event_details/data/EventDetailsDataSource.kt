package kz.kolesateam.confapp.event_details.data

import kz.kolesateam.confapp.common.data.models.EventApiData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface EventDetailsDataSource {

    @GET("/events/{eventId}")
    fun getEventDetails(@Path("eventId") eventId: Int): Call<EventApiData>
}