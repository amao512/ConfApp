package kz.kolesateam.confapp.event_details.presentation

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kz.kolesateam.confapp.R
import kz.kolesateam.confapp.common.data.model.ProgressState
import kz.kolesateam.confapp.common.data.model.ResponseData
import kz.kolesateam.confapp.common.domain.models.EventData
import kz.kolesateam.confapp.event_details.domain.ImageLoader
import kz.kolesateam.confapp.event_details.presentation.viewModel.EventDetailsViewModel
import kz.kolesateam.confapp.events.presentation.view.TIME_AND_PLACE_FORMAT
import kz.kolesateam.confapp.utils.extensions.getEventFormattedDateTime
import kz.kolesateam.confapp.utils.extensions.hide
import kz.kolesateam.confapp.utils.extensions.show
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

const val DEFAULT_EVENT_ID = 0

class EventDetailsActivity : AppCompatActivity() {

    private val eventDetailsViewModel: EventDetailsViewModel by viewModel()
    private val imageLoader: ImageLoader by inject()

    private lateinit var toolbar: Toolbar
    private lateinit var speakerPhotoImageView: ImageView
    private lateinit var invitedSpeakerTextView: TextView
    private lateinit var speakerNameTextView: TextView
    private lateinit var speakerJobTextView: TextView
    private lateinit var eventDateAndPlaceTextView: TextView
    private lateinit var eventTitleTextView: TextView
    private lateinit var eventDescriptionTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        initViews()
        observeLiveData()
    }

    override fun onStart() {
        super.onStart()

        eventDetailsViewModel.onStart(eventId = getEventIdFromIntentExtra())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.event_details_toolbar_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    private fun initViews() {
        toolbar = findViewById(R.id.activity_event_details_toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        speakerPhotoImageView = findViewById(R.id.activity_event_details_speaker_photo)
        invitedSpeakerTextView = findViewById(R.id.activity_event_details_invited_speaker)
        speakerNameTextView = findViewById(R.id.activity_event_details_speaker_name)
        speakerJobTextView = findViewById(R.id.activity_event_details_speaker_job)
        eventDateAndPlaceTextView = findViewById(R.id.activity_event_details_date_and_place)
        eventTitleTextView = findViewById(R.id.activity_event_details_event_title)
        eventDescriptionTextView = findViewById(R.id.activity_event_details_description)
        progressBar = findViewById(R.id.activity_event_details_progress_bar)
        errorTextView = findViewById(R.id.activity_event_details_error)
    }

    private fun getEventIdFromIntentExtra(): Int = intent.getIntExtra(INTENT_EXTRA_EVENT_ID_KEY, DEFAULT_EVENT_ID)

    private fun observeLiveData() {
        eventDetailsViewModel.eventLiveData.observe(this, ::handleEventDetails)
        eventDetailsViewModel.toolbarFavoriteButtonLiveData.observe(this, ::handleFavoriteEvent)
        eventDetailsViewModel.progressLiveData.observe(this, ::handleProgress)
    }

    private fun handleEventDetails(responseData: ResponseData<EventData, String>) {
        when (responseData) {
            is ResponseData.Success -> fillEventData(eventData = responseData.result)
            is ResponseData.Error -> showError(error = responseData.error)
        }
    }

    private fun handleFavoriteEvent(isFavorite: Boolean) {
        val menuItem: MenuItem = toolbar.menu.findItem(R.id.event_details_toolbar_menu_favorite)
        val menuItemIcon = if (isFavorite) {
            R.drawable.ic_favourite_fill_white
        } else {
            R.drawable.ic_favourite_border_white
        }

        menuItem.setIcon(menuItemIcon)
    }

    private fun handleProgress(progressState: ProgressState) {
        when (progressState) {
            is ProgressState.Loading -> progressBar.show()
            else -> progressBar.hide()
        }
    }

    private fun fillEventData(eventData: EventData) {
        invitedSpeakerTextView.visibility = getInvitedVisibility(eventData.speaker.isInvited)
        speakerNameTextView.text = eventData.speaker.fullName
        speakerJobTextView.text = eventData.speaker.job
        eventDateAndPlaceTextView.text = getFormattedDateAndPlace(eventData)
        eventTitleTextView.text = eventData.title
        eventDescriptionTextView.text = eventData.description

        loadSpeakerPhoto(url = eventData.speaker.photoUrl)
        initToolbarItemListener(eventData = eventData)
    }

    private fun getInvitedVisibility(isInvited: Boolean): Int = when (isInvited) {
        true -> View.VISIBLE
        else -> View.GONE
    }

    private fun getFormattedDateAndPlace(eventData: EventData): String {
        return TIME_AND_PLACE_FORMAT.format(
            eventData.startTime.getEventFormattedDateTime(),
            eventData.endTime.getEventFormattedDateTime(),
            eventData.place
        )
    }

    private fun loadSpeakerPhoto(url: String) {
        imageLoader.load(
            url = url,
            target = speakerPhotoImageView
        )
    }

    private fun initToolbarItemListener(eventData: EventData) {
        val menuItem: MenuItem = toolbar.menu.findItem(R.id.event_details_toolbar_menu_favorite)

        menuItem.setOnMenuItemClickListener {
            eventDetailsViewModel.onFavoriteButtonClick(event = eventData)

            true
        }
    }

    private fun showError(error: String) {
        errorTextView.show()
        errorTextView.text = error
    }
}