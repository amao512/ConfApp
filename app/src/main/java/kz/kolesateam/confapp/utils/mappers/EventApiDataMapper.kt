package kz.kolesateam.confapp.utils.mappers

import kz.kolesateam.confapp.common.data.models.EventApiData
import kz.kolesateam.confapp.common.domain.models.EventData
import org.threeten.bp.ZonedDateTime
import java.text.ParseException

class EventApiDataMapper(
    private val speakerApiDataMapper: SpeakerApiDataMapper
) {

    fun map(data: List<EventApiData>?): List<EventData> {
        data ?: return emptyList()

        return data.map {
            EventData(
                id = it.id ?: DEFAULT_ID,
                startTime = getTimeByFormat(it.startTime ?: EMPTY_STRING),
                endTime = getTimeByFormat(it.endTime ?: EMPTY_STRING),
                title = it.title ?: EMPTY_STRING,
                description = it.description ?: EMPTY_STRING,
                place = it.place ?: EMPTY_STRING,
                speaker = speakerApiDataMapper.map(it.speaker)
            )
        }
    }

    fun map(data: EventApiData?): EventData {
        return EventData(
                id = data?.id ?: DEFAULT_ID,
                startTime = getTimeByFormat(data?.startTime ?: EMPTY_STRING),
                endTime = getTimeByFormat(data?.endTime ?: EMPTY_STRING),
                title = data?.title ?: EMPTY_STRING,
                description = data?.description ?: EMPTY_STRING,
                place = data?.place ?: EMPTY_STRING,
                speaker = speakerApiDataMapper.map(data?.speaker)
        )
    }

    fun map(data: EventData?): EventApiData {
        return EventApiData(
            id = data?.id ?: DEFAULT_ID,
            startTime = data?.startTime.toString(),
            endTime = data?.endTime.toString(),
            title = data?.title ?: EMPTY_STRING,
            description = data?.description ?: EMPTY_STRING,
            place = data?.place ?: EMPTY_STRING,
            speaker = speakerApiDataMapper.map(data?.speaker)
        )
    }

    private fun getTimeByFormat(
        time: String
    ): ZonedDateTime = try {
        ZonedDateTime.parse(time) ?: ZonedDateTime.now()
    } catch (e: ParseException) {
        ZonedDateTime.now()
    }
}