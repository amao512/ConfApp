package kz.kolesateam.confapp.favorite_events.data

import android.content.Context
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.MapType
import kz.kolesateam.confapp.common.data.models.EventApiData
import kz.kolesateam.confapp.common.domain.models.EventData
import kz.kolesateam.confapp.favorite_events.domain.FavoriteEventsRepository
import kz.kolesateam.confapp.utils.mappers.EventApiDataMapper
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import java.io.FileInputStream
import java.io.FileOutputStream

const val FAVOURITE_EVENTS_FILE_NAME = "favourite_events.json"

class DefaultFavoriteEventsRepository(
    private val context: Context,
    private val objectMapper: ObjectMapper,
    private val eventApiDataMapper: EventApiDataMapper
) : FavoriteEventsRepository {

    private val favoriteEventsMap: MutableMap<Int, EventData> = mutableMapOf()

    init {
        favoriteEventsMap.putAll(getFavoriteEventsFromFile())
    }

    override fun saveEvent(event: EventData) {
        favoriteEventsMap[event.id] = event
        saveFavoriteEventsToFile()
    }

    override fun removeEvent(eventId: Int?) {
        favoriteEventsMap.remove(eventId)
        saveFavoriteEventsToFile()
    }

    override fun getAllFavoriteEvents(): List<EventData> {
        return favoriteEventsMap.values.toList()
    }

    override fun isFavoriteEvent(eventId: Int): Boolean = favoriteEventsMap.containsKey(eventId)

    override fun isCompletedEvent(eventTime: ZonedDateTime): Boolean {
        val dateNow: ZonedDateTime = ZonedDateTime.now(ZoneOffset.ofHours(6))

        return dateNow.isAfter(eventTime)
    }

    private fun saveFavoriteEventsToFile() {
        val favoriteEventsForSavingToFile: MutableMap<Int, EventApiData> = mutableMapOf()
        favoriteEventsMap.values.forEach {
            favoriteEventsForSavingToFile[it.id] = eventApiDataMapper.map(it)
        }

        val favoriteEventsJsonString: String = objectMapper.writeValueAsString(favoriteEventsForSavingToFile)
        val fileOutputStream: FileOutputStream = context.openFileOutput(
                FAVOURITE_EVENTS_FILE_NAME,
                Context.MODE_PRIVATE
        )
        fileOutputStream.write(favoriteEventsJsonString.toByteArray())
        fileOutputStream.close()
    }

    private fun getFavoriteEventsFromFile(): Map<Int, EventData> {
        var fileInputStream: FileInputStream? = null

        try {
            fileInputStream = context.openFileInput(FAVOURITE_EVENTS_FILE_NAME)
        } catch (e: Exception) {
            fileInputStream?.close()

            return emptyMap()
        }

        val favoriteEventsJsonString: String = fileInputStream.bufferedReader().readLines().joinToString()
        val mapType: MapType = objectMapper.typeFactory.constructMapType(
                Map::class.java,
                Int::class.java,
                EventApiData::class.java
        )

        val favoriteEventsFromFile: MutableMap<Int, EventData> = mutableMapOf()
        val favoriteEventApiDataFromFile = objectMapper.readValue(favoriteEventsJsonString, mapType) as? Map<Int, EventApiData>

        favoriteEventApiDataFromFile?.values?.forEach { eventApiData ->
            eventApiData.id?.let {
                favoriteEventsFromFile[eventApiData.id] = eventApiDataMapper.map(eventApiData)
            } ?: return@forEach
        }

        return favoriteEventsFromFile
    }
}