package kz.kolesateam.confapp.events.data

import kz.kolesateam.confapp.common.domain.models.BranchData
import kz.kolesateam.confapp.events.data.models.BranchApiData
import kz.kolesateam.confapp.events.domain.UpcomingEventsRepository
import kz.kolesateam.confapp.utils.mappers.BranchApiDataMapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DefaultUpcomingEventsRepository(
    private val upcomingEventsDataSource: UpcomingEventsDataSource,
    private val branchApiDataMapper: BranchApiDataMapper
): UpcomingEventsRepository {

    override fun getUpcomingEvents(
            result: (List<BranchData>) -> Unit,
            fail: (String?) -> Unit
    ) {
        upcomingEventsDataSource.getUpcomingEvents()
            .enqueue(object : Callback<List<BranchApiData>> {
                override fun onResponse(
                    call: Call<List<BranchApiData>>,
                    response: Response<List<BranchApiData>>
                ) {
                    if (response.isSuccessful) {
                        val branchDataList: List<BranchData> = branchApiDataMapper.map(response.body())

                        result(branchDataList)
                    } else {
                        fail(response.message())
                    }
                }

                override fun onFailure(call: Call<List<BranchApiData>>, t: Throwable) {
                    fail(t.localizedMessage)
                }
            })
    }
}