package kz.kolesateam.confapp.all_events.di

import kz.kolesateam.confapp.all_events.data.AllEventsDataSource
import kz.kolesateam.confapp.all_events.data.DefaultAllEventsRepository
import kz.kolesateam.confapp.all_events.domain.AllEventsRepository
import kz.kolesateam.confapp.all_events.presentation.AllEventsRouter
import kz.kolesateam.confapp.all_events.presentation.viewModel.AllEventsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val allEventsModule = module {

    viewModel {
        AllEventsViewModel(
            allEventsRepository = get(),
            favoriteEventsRepository = get(),
            notificationAlarmHelper = get()
        )
    }

    single {
        val retrofit = get<Retrofit>()

        retrofit.create(AllEventsDataSource::class.java)
    }

    factory {
        DefaultAllEventsRepository(
            allEventsDataSource = get(),
            eventApiDataMapper = get()
        ) as AllEventsRepository
    }

    factory {
        AllEventsRouter()
    }
}