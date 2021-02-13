package kz.kolesateam.confapp.events.presentation.view

import android.view.View
import android.widget.TextView
import kz.kolesateam.confapp.R
import kz.kolesateam.confapp.events.presentation.models.HeaderItem
import kz.kolesateam.confapp.events.presentation.models.UpcomingEventListItem
import kz.kolesateam.confapp.common.view.BaseViewHolder

class HeaderViewHolder(itemView: View) : BaseViewHolder<UpcomingEventListItem>(itemView) {

    private val userNameTextView: TextView = itemView.findViewById(R.id.header_layout_username)

    override fun onBind(data: UpcomingEventListItem) {
        val userName: String = (data as? HeaderItem)?.userName ?: return

        userNameTextView.text = userNameTextView.context.getString(R.string.hello_user_fmt, userName)
    }
}