package kz.kolesateam.confapp.utils.mappers

import kz.kolesateam.confapp.common.data.models.SpeakerApiData
import kz.kolesateam.confapp.common.domain.models.SpeakerData

class SpeakerApiDataMapper{

    fun map(data: SpeakerApiData?): SpeakerData {
        return SpeakerData(
            id = data?.id ?: DEFAULT_ID,
            fullName = data?.fullName ?: EMPTY_STRING,
            job = data?.job ?: EMPTY_STRING,
            photoUrl = data?.photoUrl ?: EMPTY_STRING,
            isInvited = data?.isInvited ?: false
        )
    }

    fun map(data: SpeakerData?): SpeakerApiData {
        return SpeakerApiData(
            id = data?.id ?: DEFAULT_ID,
            fullName = data?.fullName ?: EMPTY_STRING,
            job = data?.job ?: EMPTY_STRING,
            photoUrl = data?.photoUrl ?: EMPTY_STRING,
            isInvited = data?.isInvited ?: false
        )
    }
}