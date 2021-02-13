package kz.kolesateam.confapp.events.presentation

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import kz.kolesateam.confapp.R
import kz.kolesateam.confapp.all_events.presentation.AllEventsRouter
import kz.kolesateam.confapp.common.data.model.EventScreenNavigation
import kz.kolesateam.confapp.common.data.model.ProgressState
import kz.kolesateam.confapp.common.data.model.ResponseData
import kz.kolesateam.confapp.common.domain.models.EventData
import kz.kolesateam.confapp.common.view.EventClickListener
import kz.kolesateam.confapp.event_details.presentation.EventDetailsRouter
import kz.kolesateam.confapp.events.presentation.models.UpcomingEventListItem
import kz.kolesateam.confapp.events.presentation.view.BranchAdapter
import kz.kolesateam.confapp.events.presentation.viewModel.UpcomingEventsViewModel
import kz.kolesateam.confapp.favorite_events.presentation.FavoriteEventsRouter
import kz.kolesateam.confapp.utils.extensions.hide
import kz.kolesateam.confapp.utils.extensions.show
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class UpcomingEventsActivity : AppCompatActivity(), EventClickListener {

    private val upcomingEventsViewModel: UpcomingEventsViewModel by viewModel()
    private val favoriteEventsRouter: FavoriteEventsRouter by inject()
    private val eventDetailsRouter: EventDetailsRouter by inject()
    private val allEventsRouter: AllEventsRouter by inject()

    private val branchAdapter: BranchAdapter by lazy {
        BranchAdapter(eventClickListener = this)
    }

    private lateinit var eventsRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView
    private lateinit var favoritesButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upcoming_events)

        initViews()
        observeLiveData()
    }

    override fun onStart() {
        super.onStart()
        upcomingEventsViewModel.onStart()
    }

    override fun onBranchClicked(branchId: Int, branchTitle: String) {
        upcomingEventsViewModel.onBranchClick(
            branchId = branchId,
            branchTitle = branchTitle
        )
    }

    override fun onEventClicked(eventId: Int) {
        upcomingEventsViewModel.onEventClick(eventId = eventId)
    }

    override fun onFavoriteButtonClicked(eventData: EventData) {
        upcomingEventsViewModel.onFavoriteButtonClick(eventData)
    }

    private fun initViews() {
        progressBar = findViewById(R.id.activity_upcoming_events_progress_bar)
        errorTextView = findViewById(R.id.activity_upcoming_events_error)
        eventsRecyclerView = findViewById(R.id.activity_upcoming_events_recycler_view)
        eventsRecyclerView.adapter = branchAdapter

        favoritesButton = findViewById(R.id.activity_upcoming_events_favorites_button)
        favoritesButton.setOnClickListener {
            upcomingEventsViewModel.onFavoriteEventsButtonClick()
        }
    }

    private fun observeLiveData() {
        upcomingEventsViewModel.progressLiveData.observe(this, ::handleProgress)
        upcomingEventsViewModel.loadEventsStateLiveData.observe(this, ::handleResponseEvents)
        upcomingEventsViewModel.eventScreenNavigationLiveData.observe(this, ::handleNavigation)
    }

    private fun handleProgress(progressState: ProgressState) {
        when (progressState) {
            ProgressState.Loading -> progressBar.show()
            ProgressState.Done -> progressBar.hide()
        }
    }

    private fun handleResponseEvents(responseData: ResponseData<List<UpcomingEventListItem>, String>) {
        when (responseData) {
            is ResponseData.Success -> {
                branchAdapter.setList(responseData.result)
            }
            is ResponseData.Error -> {
                errorTextView.text = responseData.error
            }
        }
    }

    private fun handleNavigation(eventScreenNavigation: EventScreenNavigation?) {
        when (eventScreenNavigation) {
            is EventScreenNavigation.AllEvents -> navigateToAllEvents(
                    branchId = eventScreenNavigation.branchId,
                    branchTitle = eventScreenNavigation.branchTitle
            )
            is EventScreenNavigation.EventDetails -> navigateToEventDetails(eventId = eventScreenNavigation.eventId)
            is EventScreenNavigation.FavoriteEvents -> navigateToFavoriteEvents()
        }
    }

    private fun navigateToAllEvents(
        branchId: Int,
        branchTitle: String
    ) {
        startActivity(
            allEventsRouter.createIntent(
                context = this,
                branchId = branchId,
                branchTitle = branchTitle
            )
        )
    }

    private fun navigateToEventDetails(eventId: Int){
        startActivity(
            eventDetailsRouter.createIntent(
                context = this,
                eventId = eventId
            )
        )
    }

    private fun navigateToFavoriteEvents(){
        startActivity(favoriteEventsRouter.createIntent(this))
    }
}