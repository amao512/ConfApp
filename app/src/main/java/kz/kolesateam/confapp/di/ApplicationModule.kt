package kz.kolesateam.confapp.di

import android.content.Context
import com.fasterxml.jackson.databind.ObjectMapper
import kz.kolesateam.confapp.common.data.UserNameSharedPrefsDataSource
import kz.kolesateam.confapp.common.domain.UserNameDataSource
import kz.kolesateam.confapp.notifications.NotificationAlarmHelper
import kz.kolesateam.confapp.utils.mappers.BranchApiDataMapper
import kz.kolesateam.confapp.utils.mappers.EventApiDataMapper
import kz.kolesateam.confapp.utils.mappers.SpeakerApiDataMapper
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

private const val BASE_URL = "http://37.143.8.68:2020"

const val SHARED_PREFERENCES_KEY = "shared_prefs"
const val SHARED_PREFS_DATA_SOURCE = "shared_prefs_data_source"

val applicationModule = module {

    single {
        Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build()
    }

    single {
        val context = androidApplication()

        context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
    }

    single {
        ObjectMapper()
    }

    single {
        NotificationAlarmHelper(
            application = androidApplication()
        )
    }

    single {
        BranchApiDataMapper(
            eventApiDataMapper = get()
        )
    }

    single {
        EventApiDataMapper(
            speakerApiDataMapper = get()
        )
    }

    single {
        SpeakerApiDataMapper()
    }

    factory(named(SHARED_PREFS_DATA_SOURCE)) {
        UserNameSharedPrefsDataSource(
            sharedPreferences = get()
        ) as UserNameDataSource
    }
}