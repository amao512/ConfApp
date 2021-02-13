package kz.kolesateam.confapp.favorite_events.presentation.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.kolesateam.confapp.R
import kz.kolesateam.confapp.common.domain.models.EventData
import kz.kolesateam.confapp.common.view.EventClickListener

class FavoriteEventsAdapter(
    private val eventClickListener: EventClickListener
): RecyclerView.Adapter<FavoriteEventViewHolder>() {

    private val favoriteEventsList: MutableList<EventData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteEventViewHolder {
        return createEventViewHolder(parent)
    }

    override fun onBindViewHolder(holderFavorite: FavoriteEventViewHolder, position: Int) {
        holderFavorite.onBind(favoriteEventsList[position])
    }

    override fun getItemCount(): Int = favoriteEventsList.size

    fun setList(events: List<EventData>){
        this.favoriteEventsList.clear()
        this.favoriteEventsList.addAll(events)

        notifyDataSetChanged()
    }

    private fun createEventViewHolder(parent: ViewGroup): FavoriteEventViewHolder {
        return FavoriteEventViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.events_card_layout,
                parent,
                false
            ),
            eventClickListener
        )
    }
}