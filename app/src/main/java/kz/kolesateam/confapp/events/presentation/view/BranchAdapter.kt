package kz.kolesateam.confapp.events.presentation.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.kolesateam.confapp.R
import kz.kolesateam.confapp.events.presentation.models.HEADER_TYPE
import kz.kolesateam.confapp.events.presentation.models.UpcomingEventListItem
import kz.kolesateam.confapp.common.view.BaseViewHolder
import kz.kolesateam.confapp.common.view.EventClickListener

class BranchAdapter(
    private val eventClickListener: EventClickListener
) : RecyclerView.Adapter<BaseViewHolder<UpcomingEventListItem>>() {

    private val branchApiDataList: MutableList<UpcomingEventListItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<UpcomingEventListItem> {
        return when (viewType){
            HEADER_TYPE -> createHeaderViewHolder(parent)
            else -> createBranchViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<UpcomingEventListItem>, position: Int) {
        holder.onBind(branchApiDataList[position])
    }

    override fun getItemCount(): Int = branchApiDataList.size

    override fun getItemViewType(position: Int): Int = branchApiDataList[position].type

    fun setList(branchApiDataList: List<UpcomingEventListItem>){
        this.branchApiDataList.clear()
        this.branchApiDataList.addAll(branchApiDataList)
        notifyDataSetChanged()
    }

    private fun createHeaderViewHolder(parent: ViewGroup): BaseViewHolder<UpcomingEventListItem> {
        return HeaderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.header_layout,
                parent,
                false
            )
        )
    }
    private fun createBranchViewHolder(parent: ViewGroup): BaseViewHolder<UpcomingEventListItem> {
        return BranchViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.branch_item,
                parent,
                false
            ),
            eventClickListener
        )
    }
}