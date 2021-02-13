package kz.kolesateam.confapp.all_events.presentation.view

import android.view.View
import android.widget.TextView
import kz.kolesateam.confapp.R
import kz.kolesateam.confapp.all_events.presentation.models.AllEventsListItem
import kz.kolesateam.confapp.all_events.presentation.models.BranchTitleItem
import kz.kolesateam.confapp.common.view.BaseViewHolder

class BranchTitleViewHolder(layoutItem: View) : BaseViewHolder<AllEventsListItem>(layoutItem) {

    private val branchTitleTextView: TextView = layoutItem.findViewById(R.id.branch_title_item_text_view)

    override fun onBind(data: AllEventsListItem) {
        branchTitleTextView.text = (data as? BranchTitleItem)?.branchTitle
    }
}