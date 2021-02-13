package kz.kolesateam.confapp.favorite_events.di

import kz.kolesateam.confapp.favorite_events.data.DefaultFavoriteEventsRepository
import kz.kolesateam.confapp.favorite_events.domain.FavoriteEventsRepository
import kz.kolesateam.confapp.favorite_events.presentation.FavoriteEventsRouter
import kz.kolesateam.confapp.favorite_events.presentation.viewModel.FavoriteEventsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoriteEventsModule = module {

    viewModel {
        FavoriteEventsViewModel(
            favoriteEventsRepository = get(),
            notificationAlarmHelper = get()
        )
    }

    single {
        DefaultFavoriteEventsRepository(
            context = androidApplication(),
            objectMapper = get(),
            eventApiDataMapper = get()
        ) as FavoriteEventsRepository
    }

    factory {
        FavoriteEventsRouter()
    }
}