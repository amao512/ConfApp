package kz.kolesateam.confapp.events.presentation.models

import kz.kolesateam.confapp.common.domain.models.BranchData

const val HEADER_TYPE = 0
const val EVENT_TYPE = 1

sealed class UpcomingEventListItem (
      val type: Int
)

data class HeaderItem(
      val userName: String
): UpcomingEventListItem(HEADER_TYPE)

data class BranchListItem(
      val data: BranchData
): UpcomingEventListItem(EVENT_TYPE)