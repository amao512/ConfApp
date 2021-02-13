package kz.kolesateam.confapp.event_details.di

import kz.kolesateam.confapp.event_details.data.DefaultEventDetailsRepository
import kz.kolesateam.confapp.event_details.data.DefaultImageLoader
import kz.kolesateam.confapp.event_details.data.EventDetailsDataSource
import kz.kolesateam.confapp.event_details.domain.EventDetailsRepository
import kz.kolesateam.confapp.event_details.domain.ImageLoader
import kz.kolesateam.confapp.event_details.presentation.EventDetailsRouter
import kz.kolesateam.confapp.event_details.presentation.viewModel.EventDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val eventDetailsModule = module {

    viewModel {
        EventDetailsViewModel(
            eventDetailsRepository = get(),
            favoriteEventsRepository = get(),
            notificationAlarmHelper = get()
        )
    }

    single {
        val retrofit: Retrofit = get()

        retrofit.create(EventDetailsDataSource::class.java)
    }

    factory {
        EventDetailsRouter()
    }

    factory {
        DefaultEventDetailsRepository(
            eventDetailsDataSource = get(),
            eventApiDataMapper = get()
        ) as EventDetailsRepository
    }

    factory {
        DefaultImageLoader() as ImageLoader
    }
}