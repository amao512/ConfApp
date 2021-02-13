package kz.kolesateam.confapp.favorite_events.presentation

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import kz.kolesateam.confapp.R
import kz.kolesateam.confapp.common.data.model.EventScreenNavigation
import kz.kolesateam.confapp.common.domain.models.EventData
import kz.kolesateam.confapp.common.view.EventClickListener
import kz.kolesateam.confapp.event_details.presentation.EventDetailsRouter
import kz.kolesateam.confapp.event_details.presentation.INTENT_EXTRA_EVENT_ID_KEY
import kz.kolesateam.confapp.events.presentation.UpcomingEventsRouter
import kz.kolesateam.confapp.favorite_events.data.model.EventsState
import kz.kolesateam.confapp.favorite_events.presentation.view.FavoriteEventsAdapter
import kz.kolesateam.confapp.favorite_events.presentation.viewModel.FavoriteEventsViewModel
import kz.kolesateam.confapp.utils.extensions.hide
import kz.kolesateam.confapp.utils.extensions.show
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteEventsActivity : AppCompatActivity(), EventClickListener {

    private val favoriteEventsViewModel: FavoriteEventsViewModel by viewModel()
    private val upcomingEventsRouter: UpcomingEventsRouter by inject()
    private val eventDetailsRouter: EventDetailsRouter by inject()

    private val favoriteEventsAdapter: FavoriteEventsAdapter by lazy {
        FavoriteEventsAdapter(eventClickListener = this)
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var homeButton: Button
    private lateinit var emptyEventsView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_events)

        initViews()
        observeLiveData()
    }

    override fun onStart() {
        super.onStart()

        favoriteEventsViewModel.onStart()
    }

    override fun onFavoriteButtonClicked(eventData: EventData) {
        favoriteEventsViewModel.onFavoriteButtonClick(eventData)
    }

    override fun onEventClicked(eventId: Int) {
        favoriteEventsViewModel.onEventClick(eventId)
    }

    private fun initViews() {
        homeButton = findViewById(R.id.activity_all_favorites_events_button_home)
        homeButton.setOnClickListener {
            startActivity(upcomingEventsRouter.createIntent(this))
            finish()
        }

        emptyEventsView = findViewById(R.id.activity_favorite_events_no_events)
        recyclerView = findViewById(R.id.activity_favorite_events_recycler_view)
        recyclerView.adapter = favoriteEventsAdapter
    }

    private fun observeLiveData() {
        favoriteEventsViewModel.favoriteEventsLiveData.observe(this, ::handleFavoriteEvents)
        favoriteEventsViewModel.emptyEventsLiveData.observe(this, ::handleFailEvents)
        favoriteEventsViewModel.eventScreenNavigationLiveData.observe(this, ::handleNavigation)
    }

    private fun handleFavoriteEvents(favoriteEventsList: List<EventData>) {
        favoriteEventsAdapter.setList(favoriteEventsList)
    }

    private fun handleFailEvents(emptyState: EventsState) {
        when (emptyState) {
            EventsState.Empty -> emptyEventsView.show()
            EventsState.Full -> emptyEventsView.hide()
        }
    }

    private fun handleNavigation(eventScreenNavigation: EventScreenNavigation?) {
        when (eventScreenNavigation) {
            is EventScreenNavigation.EventDetails -> navigateToEventDetails(eventId = eventScreenNavigation.eventId)
        }
    }

    private fun navigateToEventDetails(eventId: Int){
        startActivity(
            eventDetailsRouter.createIntent(
                context = this,
                eventId = eventId
            )
        )
    }
}