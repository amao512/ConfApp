package kz.kolesateam.confapp.events.di

import kz.kolesateam.confapp.di.SHARED_PREFS_DATA_SOURCE
import kz.kolesateam.confapp.events.data.DefaultUpcomingEventsRepository
import kz.kolesateam.confapp.events.data.UpcomingEventsDataSource
import kz.kolesateam.confapp.events.domain.UpcomingEventsRepository
import kz.kolesateam.confapp.events.presentation.UpcomingEventsRouter
import kz.kolesateam.confapp.events.presentation.viewModel.UpcomingEventsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val eventScreenModule = module {

    viewModel {
        UpcomingEventsViewModel(
            upcomingEventsRepository = get(),
            userNameSharedPrefsDataSource = get(named(SHARED_PREFS_DATA_SOURCE)),
            favoriteEventsRepository = get(),
            notificationAlarmHelper = get()
        )
    }

    single {
        val retrofit = get<Retrofit>()

        retrofit.create(UpcomingEventsDataSource::class.java)
    }

    factory {
        DefaultUpcomingEventsRepository(
            upcomingEventsDataSource = get(),
            branchApiDataMapper = get()
        ) as UpcomingEventsRepository
    }

    factory {
        UpcomingEventsRouter()
    }
}