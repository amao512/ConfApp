package kz.kolesateam.confapp.event_details.data

import kz.kolesateam.confapp.common.domain.models.EventData
import kz.kolesateam.confapp.event_details.domain.EventDetailsRepository
import kz.kolesateam.confapp.utils.mappers.EventApiDataMapper
import retrofit2.awaitResponse

class DefaultEventDetailsRepository(
    private val eventDetailsDataSource: EventDetailsDataSource,
    private val eventApiDataMapper: EventApiDataMapper
) : EventDetailsRepository {

    override suspend fun getEventDetails(
        result: (EventData) -> Unit,
        fail: (String?) -> Unit,
        eventId: Int
    ) {
        try {
            val response = eventDetailsDataSource.getEventDetails(eventId).awaitResponse()

            if(response.isSuccessful){
                val eventData: EventData = eventApiDataMapper.map(response.body())

                result(eventData)
            } else {
                fail(response.message())
            }
        } catch (e: Exception) {
            fail(e.localizedMessage)
        }
    }
}