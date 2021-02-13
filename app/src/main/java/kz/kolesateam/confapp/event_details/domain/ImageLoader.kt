package kz.kolesateam.confapp.event_details.domain

import android.widget.ImageView

interface ImageLoader {

    fun load(
        url: String,
        target: ImageView
    )
}