package kz.kolesateam.confapp.common.domain.models

import org.threeten.bp.ZonedDateTime

data class EventData(
    val id: Int,
    val startTime: ZonedDateTime,
    val endTime: ZonedDateTime,
    val title: String,
    val description: String,
    val place: String,
    val speaker: SpeakerData
) {
    var isFavorite: Boolean = false
    var isCompleted: Boolean = false
}