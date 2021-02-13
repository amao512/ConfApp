package kz.kolesateam.confapp.events.domain

import androidx.annotation.WorkerThread
import kz.kolesateam.confapp.common.domain.models.BranchData

interface UpcomingEventsRepository {

    @WorkerThread
    fun getUpcomingEvents(
            result: (List<BranchData>) -> Unit,
            fail: (String?) -> Unit
    )
}
