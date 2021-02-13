package kz.kolesateam.confapp.all_events.data

import kz.kolesateam.confapp.all_events.domain.AllEventsRepository
import kz.kolesateam.confapp.common.domain.models.EventData
import kz.kolesateam.confapp.utils.mappers.EventApiDataMapper
import retrofit2.awaitResponse

class DefaultAllEventsRepository(
    private val allEventsDataSource: AllEventsDataSource,
    private val eventApiDataMapper: EventApiDataMapper
) : AllEventsRepository {

    override suspend fun getAllEvents(
            result: (List<EventData>) -> Unit,
            fail: (String?) -> Unit,
            branchId: Int
    ) {
        try {
            val response = allEventsDataSource.getAllEvents(branchId).awaitResponse()

            if(response.isSuccessful){
                val eventDataList: List<EventData> = eventApiDataMapper.map(response.body())

                result(eventDataList)
            } else {
                fail(response.message())
            }
        } catch (e: Exception){
            fail(e.localizedMessage)
        }
    }
}