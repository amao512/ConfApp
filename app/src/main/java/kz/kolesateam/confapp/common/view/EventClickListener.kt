package kz.kolesateam.confapp.common.view

import kz.kolesateam.confapp.common.domain.models.EventData

interface EventClickListener {

    fun onBranchClicked(
            branchId: Int,
            branchTitle: String
    ) = Unit

    fun onEventClicked(eventId: Int) = Unit

    fun onFavoriteButtonClicked(eventData: EventData) = Unit
}