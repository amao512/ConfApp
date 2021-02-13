package kz.kolesateam.confapp.favorite_events.presentation.view

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kz.kolesateam.confapp.R
import kz.kolesateam.confapp.common.domain.models.EventData
import kz.kolesateam.confapp.common.view.EventClickListener
import kz.kolesateam.confapp.events.presentation.view.TIME_AND_PLACE_FORMAT
import kz.kolesateam.confapp.utils.extensions.getEventFormattedDateTime
import kz.kolesateam.confapp.utils.extensions.show

class FavoriteEventViewHolder(
    itemView: View,
    private val eventClickListener: EventClickListener
): RecyclerView.ViewHolder(itemView) {

    private val completedStateTextView: TextView = itemView.findViewById(R.id.events_card_layout_completed)
    private val dateAndPlaceTextView: TextView = itemView.findViewById(R.id.events_card_date_and_place)
    private val favoriteButton: ImageView = itemView.findViewById(R.id.events_card_favourite_btn)
    private val speakerNameTextView: TextView = itemView.findViewById(R.id.events_card_speaker_name)
    private val speakerJobTextView: TextView = itemView.findViewById(R.id.events_card_speaker_job)
    private val eventTitleTextView: TextView = itemView.findViewById(R.id.events_card_event_title)


    fun onBind(event: EventData){
        fillData(event)
        initListeners(event)

        if(event.isCompleted){
            onEventComplete()
        }
    }

    private fun fillData(event: EventData){
        dateAndPlaceTextView.text = TIME_AND_PLACE_FORMAT.format(
            event.startTime.getEventFormattedDateTime(),
            event.endTime.getEventFormattedDateTime(),
            event.place
        )
        speakerNameTextView.text = event.speaker.fullName
        speakerJobTextView.text = event.speaker.job
        eventTitleTextView.text = event.title

        val favoriteButtonResource = getFavoriteButtonResource(event.isFavorite)
        favoriteButton.setImageResource(favoriteButtonResource)
    }

    private fun initListeners(event: EventData) {
        itemView.setOnClickListener {
            eventClickListener.onEventClicked(eventId = event.id)
        }

        favoriteButton.setOnClickListener {
            val favoriteButtonResource = getFavoriteButtonResource(event.isFavorite)
            favoriteButton.setImageResource(favoriteButtonResource)

            event.isFavorite = !event.isFavorite
            eventClickListener.onFavoriteButtonClicked(eventData = event)
        }
    }

    private fun onEventComplete(){
        completedStateTextView.show()
        itemView.setBackgroundResource(R.drawable.bg_events_card_dark)
    }

    private fun getFavoriteButtonResource(isFavorite: Boolean): Int = when(isFavorite){
        true -> R.drawable.ic_favourite_fill
        else -> R.drawable.ic_favourite_border
    }
}