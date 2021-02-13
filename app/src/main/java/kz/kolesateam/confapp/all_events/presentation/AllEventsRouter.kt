package kz.kolesateam.confapp.all_events.presentation

import android.content.Context
import android.content.Intent

const val INTENT_BRANCH_ID_KEY = "branch_id"
const val INTENT_BRANCH_TITLE_KEY = "branch_title"

class AllEventsRouter {

    fun createIntent(
        context: Context,
        branchId: Int,
        branchTitle: String
    ): Intent {
        return Intent(context, AllEventsActivity::class.java).apply {
            putExtra(INTENT_BRANCH_ID_KEY, branchId)
            putExtra(INTENT_BRANCH_TITLE_KEY, branchTitle)
        }
    }
}