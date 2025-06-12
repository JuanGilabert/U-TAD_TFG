package com.cronosdev.taskmanagerapp.ui.state.error

import androidx.compose.runtime.Composable
import com.cronosdev.taskmanagerapp.ui.components.ApiErrorSnackbar
import com.cronosdev.taskmanagerapp.ui.navigation.Destinations
import com.cronosdev.taskmanagerapp.ui.state.UiState
import com.cronosdev.taskmanagerapp.ui.screens.art.cinema.CinemaViewModel

object CinemaScreenErrors {
    private var _cinemaScreenName: String = Destinations.ART_CINEMA_SCREEN_URL
    private var _createCinemaScreenName: String = Destinations.ART_CREATE_CINEMA_SCREEN_URL
    private var _updateCinemaScreenName: String = Destinations.ART_UPDATE_CINEMA_SCREEN_URL
    /**
     *
     */
    @Composable
    fun ShowErrorForArtCinemaScreen(uiState: UiState.Error, cinemaViewModel: CinemaViewModel) {
        ApiErrorSnackbar(message = (uiState).errorMessage, onApiErrorSnackBarButtonRetry = {
            if ((uiState).errorType == "throwableErrorType") {
                if ((uiState).errorScreenCallName == _cinemaScreenName) { // no es necesario. ELIMINAR EN EL FUTURO O REESTRUCTURAR TODOO
                    if ((uiState).errorMethodName == "fetchAvailableCinemas") cinemaViewModel.fetchAvailableCinemas((uiState).errorScreenCallName)
                }
            }
            if ((uiState).errorType == "fetchDataErrorType") {
                if ((uiState).errorScreenCallName == _cinemaScreenName) {
                    if ((uiState).errorMethodName == "fetchAvailableCinemas") cinemaViewModel.fetchAvailableCinemas((uiState).errorScreenCallName)
                }
            }
        })
    }
    /**
     *
     */
    @Composable
    fun ShowErrorForArtCreateCinemaScreen(uiState: UiState.Error, cinemaViewModel: CinemaViewModel) {
        ApiErrorSnackbar(message = (uiState).errorMessage, onApiErrorSnackBarButtonRetry = {
            // Cuando hay un error de red, se vuelven a ejecutar las funciones.
            if ((uiState).errorType == "throwableErrorType") {
                if ((uiState).errorScreenCallName == _createCinemaScreenName) {
                    if ((uiState ).errorMethodName == "fetchUnavailableDates") cinemaViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                    if ((uiState).errorMethodName == "createCinema") cinemaViewModel.createCinema()
                }
            }
            // Cuando hay un error de carga de datos, se vuelven a cargar las vistas de nuevo sin ejecutar metodos que envian datos a una api.
            if ((uiState).errorType == "fetchDataErrorType") {
                if ((uiState).errorScreenCallName == _createCinemaScreenName) {
                    if ((uiState ).errorMethodName == "fetchUnavailableDates") cinemaViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                    if ((uiState).errorMethodName == "createCinema") cinemaViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                }
            }
        })
    }

    @Composable
    fun ShowErrorForArtUpdateCinemaScreen(uiState: UiState.Error, cinemaViewModel: CinemaViewModel) {
        ApiErrorSnackbar(message = (uiState).errorMessage, onApiErrorSnackBarButtonRetry = {
            // Cuando hay un error de red, se vuelven a ejecutar las funciones.
            if ((uiState).errorType == "throwableErrorType") {
                if ((uiState).errorScreenCallName == _updateCinemaScreenName) {
                    if ((uiState ).errorMethodName == "fetchUnavailableDates") cinemaViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                    if ((uiState).errorMethodName == "putCinema") cinemaViewModel.putCinema()
                    if ((uiState).errorMethodName == "deleteCinema") cinemaViewModel.deleteCinema()
                }
            }
            // Cuando hay un error de carga de datos, se vuelven a cargar las vistas de nuevo sin ejecutar metodos que envian datos a una api.
            if ((uiState).errorType == "fetchDataErrorType") {
                if ((uiState).errorScreenCallName == _updateCinemaScreenName) {
                    if ((uiState ).errorMethodName == "fetchUnavailableDates") cinemaViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                    if ((uiState).errorMethodName == "putCinema") cinemaViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                    if ((uiState).errorMethodName == "deleteCinema") cinemaViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                }
            }
        })
    }
}
/*
Si la lógica dentro de onRetry es muy repetitiva (como en tu caso), puedes delegarla a una función pura o una clase helper:


object ErrorRetryDispatcher {
    fun dispatch(uiState: UiState.Error, viewModel: CinemaViewModel) {
        when (uiState.errorScreenCallName to uiState.errorMethodName) {
            "artCinemaScreen" to "fetchAvailableCinemas" -> viewModel.fetchAvailableCinemas("artCinemaScreen")
            "artCreateCinemaScreen" to "fetchUnavailableDates" -> viewModel.fetchUnavailableDates("artCreateCinemaScreen")
            // ...
        }
    }
}

Y luego en tu Composable solo haces:
ApiErrorSnackbar(message = uiState.errorMessage, onRetry = {
    ErrorRetryDispatcher.dispatch(uiState, viewModel)
})

Necesitas...                Solución recomendada

Manejar múltiples vistas    Agrupar en object por dominio
relacionadas por modelo

Reducir lógica repetitiva	Crear dispatcher/helper para onRetry

Escalar mantenible          Separar en archivos organizados
 */