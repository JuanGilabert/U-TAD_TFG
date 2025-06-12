package com.cronosdev.taskmanagerapp.ui.state.error

import androidx.compose.runtime.Composable
import com.cronosdev.taskmanagerapp.ui.screens.art.cinema.CinemaViewModel
import com.cronosdev.taskmanagerapp.ui.state.UiState

interface ScreenErrors {
    // AUTH
    @Composable
    fun ShowErrorForSigninScreen(uiState: UiState.Error, onScreenErrorEvent: (String) -> Unit) {}
    // CINEMA
    @Composable
    fun ShowErrorForArtCinemaScreen(uiState: UiState.Error, cinemaViewModel: CinemaViewModel) {}
    @Composable
    fun ShowErrorForArtCreateCinemaScreen(uiState: UiState.Error, cinemaViewModel: CinemaViewModel) {}
    @Composable
    fun ShowErrorForArtUpdateCinemaScreen(uiState: UiState.Error, cinemaViewModel: CinemaViewModel) {}
    // MUSIC
}