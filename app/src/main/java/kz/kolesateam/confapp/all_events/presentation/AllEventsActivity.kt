package kz.kolesateam.confapp.all_events.presentation

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import kz.kolesateam.confapp.R
import kz.kolesateam.confapp.all_events.presentation.models.AllEventsListItem
import kz.kolesateam.confapp.all_events.presentation.view.AllEventsAdapter
import kz.kolesateam.confapp.all_events.presentation.viewModel.AllEventsViewModel
import kz.kolesateam.confapp.common.data.EMPTY_KEY
import kz.kolesateam.confapp.common.data.model.EventScreenNavigation
import kz.kolesateam.confapp.common.data.model.ProgressState
import kz.kolesateam.confapp.common.data.model.ResponseData
import kz.kolesateam.confapp.common.domain.models.EventData
import kz.kolesateam.confapp.common.view.EventClickListener
import kz.kolesateam.confapp.event_details.presentation.EventDetailsRouter
import kz.kolesateam.confapp.favorite_events.presentation.FavoriteEventsRouter
import kz.kolesateam.confapp.utils.extensions.hide
import kz.kolesateam.confapp.utils.extensions.show
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

const val DEFAULT_INTENT_EXTRA_VALUE = 0

class AllEventsActivity : AppCompatActivity(), EventClickListener {

    private val allEventsViewModel: AllEventsViewModel by viewModel()
    private val favoriteEventsRouter: FavoriteEventsRouter by inject()
    private val eventDetailsRouter: EventDetailsRouter by inject()

    private val allEventsAdapter: AllEventsAdapter by lazy {
        AllEventsAdapter(eventClickListener = this)
    }

    private lateinit var progressBar: ProgressBar
    private lateinit var allEventsRecyclerView: RecyclerView
    private lateinit var toolbar: Toolbar
    private lateinit var favoritesButton: Button
    private lateinit var errorTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_events)

        initViews()
        observeLiveData()
    }

    override fun onStart() {
        super.onStart()

        allEventsViewModel.onStart(
            branchId = getBranchIdFromIntentExtra(),
            branchTitle = getBranchTitleFromIntentExtra()
        )
    }

    override fun onEventClicked(eventId: Int) {
        allEventsViewModel.onEventClick(eventId = eventId)
    }

    override fun onFavoriteButtonClicked(eventData: EventData) {
        allEventsViewModel.onFavoriteButtonClick(eventData)
    }

    private fun initViews() {
        progressBar = findViewById(R.id.activity_all_events_progress_bar)
        errorTextView = findViewById(R.id.activity_all_events_error)
        allEventsRecyclerView = findViewById(R.id.activity_all_events_recycler_view)
        allEventsRecyclerView.adapter = allEventsAdapter

        toolbar = findViewById(R.id.activity_all_events_toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        favoritesButton = findViewById(R.id.activity_all_favorites_button)
        favoritesButton.setOnClickListener {
            allEventsViewModel.onFavoriteEventsButtonClick()
        }
    }

    private fun observeLiveData(){
        allEventsViewModel.progressLiveData.observe(this, ::handleProgress)
        allEventsViewModel.allEventsLiveData.observe(this, ::handleResponseAllEvents)
        allEventsViewModel.eventScreenNavigationLiveData.observe(this, ::handleNavigation)
    }

    private fun handleProgress(progressState: ProgressState) {
        when(progressState){
            ProgressState.Loading -> progressBar.show()
            ProgressState.Done -> progressBar.hide()
        }
    }

    private fun handleResponseAllEvents(responseData: ResponseData<List<AllEventsListItem>, String>) {
        when(responseData){
            is ResponseData.Success -> allEventsAdapter.setList(responseData.result)
            is ResponseData.Error -> errorTextView.text = responseData.error
        }
    }

    private fun getBranchIdFromIntentExtra(): Int = intent.getIntExtra(
        INTENT_BRANCH_ID_KEY,
        DEFAULT_INTENT_EXTRA_VALUE
    )

    private fun getBranchTitleFromIntentExtra(): String {
        return intent.getStringExtra(INTENT_BRANCH_TITLE_KEY) ?: EMPTY_KEY
    }

    private fun handleNavigation(eventScreenNavigation: EventScreenNavigation?) {
        when(eventScreenNavigation){
            is EventScreenNavigation.EventDetails -> navigateToEventDetails(eventId = eventScreenNavigation.eventId)
            is EventScreenNavigation.FavoriteEvents -> navigateToFavoriteEvents()
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

    private fun navigateToFavoriteEvents(){
        startActivity(favoriteEventsRouter.createIntent(this))
    }
}