package kz.kolesateam.confapp.common.domain.models

data class SpeakerData (
    val id: Int,
    val fullName: String,
    val job: String,
    val photoUrl: String,
    val isInvited: Boolean
)