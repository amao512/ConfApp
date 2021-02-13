package kz.kolesateam.confapp.utils.mappers

import kz.kolesateam.confapp.common.domain.models.BranchData
import kz.kolesateam.confapp.events.data.models.BranchApiData

const val DEFAULT_ID = 0
const val EMPTY_STRING = ""

class BranchApiDataMapper(
    private val eventApiDataMapper: EventApiDataMapper
) {

    fun map(data: List<BranchApiData>?): List<BranchData> {
        data ?: return emptyList()

        return data.map {
            BranchData(
                id = it.id ?: DEFAULT_ID,
                title = it.title ?: EMPTY_STRING,
                events = eventApiDataMapper.map(it.events)
            )
        }
    }
}