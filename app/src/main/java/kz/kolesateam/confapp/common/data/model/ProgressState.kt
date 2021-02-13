package kz.kolesateam.confapp.common.data.model

sealed class ProgressState {
    object Loading : ProgressState()
    object Done : ProgressState()
}