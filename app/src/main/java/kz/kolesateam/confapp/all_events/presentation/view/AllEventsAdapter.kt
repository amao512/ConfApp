package kz.kolesateam.confapp.all_events.presentation.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.kolesateam.confapp.R
import kz.kolesateam.confapp.all_events.presentation.models.AllEventsListItem
import kz.kolesateam.confapp.all_events.presentation.models.BRANCH_TITLE_TYPE
import kz.kolesateam.confapp.common.view.BaseViewHolder
import kz.kolesateam.confapp.common.view.EventClickListener

class AllEventsAdapter(
    private val eventClickListener: EventClickListener
) : RecyclerView.Adapter<BaseViewHolder<AllEventsListItem>>(){

    private val eventApiDataList: MutableList<AllEventsListItem> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<AllEventsListItem> {
        return when(viewType){
            BRANCH_TITLE_TYPE -> createBranchTitleViewHolder(parent)
            else -> createEventViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<AllEventsListItem>, position: Int) {
        holder.onBind(eventApiDataList[position])
    }

    override fun getItemViewType(position: Int): Int = eventApiDataList[position].type

    override fun getItemCount(): Int = eventApiDataList.size

    fun setList(eventApiDataList: List<AllEventsListItem>){
        this.eventApiDataList.clear()
        this.eventApiDataList.addAll(eventApiDataList)
        notifyDataSetChanged()
    }

    private fun createBranchTitleViewHolder(parent: ViewGroup): BaseViewHolder<AllEventsListItem> {
        return BranchTitleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.branch_title_item,
                parent,
                false
            )
        )
    }

    private fun createEventViewHolder(parent: ViewGroup): BaseViewHolder<AllEventsListItem> {
        return EventViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.events_card_layout,
                parent,
                false
            ),
            eventClickListener
        )
    }
}