package com.cronosdev.taskmanagerapp.ui.screens.art.cinema
//
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cronosdev.taskmanagerapp.R
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseResultListModel.Companion.isResultListNullOrEmpty
import com.cronosdev.taskmanagerapp.data.model.art.CinemaModel
import com.cronosdev.taskmanagerapp.ui.state.ShowLoadingUiState
import com.cronosdev.taskmanagerapp.ui.state.UiState
import com.cronosdev.taskmanagerapp.ui.components.ApiErrorSnackbar
import com.cronosdev.taskmanagerapp.ui.components.ShowAlertDialogComponent
import com.cronosdev.taskmanagerapp.ui.components.outlinedComponents.buttons.ShowCustomData
import com.cronosdev.taskmanagerapp.ui.components.viewSearchersComponents.ShowTextFieldSearcherComponent
import com.cronosdev.taskmanagerapp.ui.components.ViewTitleComponent
import com.cronosdev.taskmanagerapp.ui.navigation.Destinations
import com.cronosdev.taskmanagerapp.ui.state.error.CinemaScreenErrors.ShowErrorForArtCinemaScreen
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
/**
 *
 */
@Composable
fun CinemaScreen(navController: NavHostController, cinemaViewModel: CinemaViewModel) {
    // Variable que controla el estado de la vista/screen.
    val uiState by cinemaViewModel.cinemaScreenUiState.collectAsState()
    // Conjunto de acciones que se realizan al cargar la vista.
    LaunchedEffect(Unit) {
        // La vista siempre que se inicia(esto incluye hacer clic en el boton de back de cualquier vista)
        // establecera valores por defecto en las variables.
        cinemaViewModel.setCinemaVariables()
        // Se establece que no hay nada que buscar ya que se inicia la vista siempre sin nada que buscar.
        cinemaViewModel.setShowSearchedCinemas(false)
        cinemaViewModel.fetchAvailableCinemas(Destinations.ART_CINEMA_SCREEN_URL)
    }
    // Estructura de la vista/pantalla principal de la aplicacion.
    when (uiState) {
        is UiState.Loading -> ShowLoadingUiState()
        is UiState.Success -> ShowCinemaScreenSuccessUiState(uiState as UiState.Success, navController, cinemaViewModel)
        is UiState.Error -> ShowErrorForArtCinemaScreen(uiState as UiState.Error, cinemaViewModel)
        else -> {}
    }
}
/**
 *
 */
@Composable
fun ShowCinemaScreenSuccessUiState(uiState: UiState.Success, navController: NavHostController, cinemaViewModel: CinemaViewModel) {
    if ((uiState).successType == "screenInit") {
        if ((uiState).successScreenCallName == "artCinemaScreen") {
            if ((uiState).successMethodName == "fetchAvailableCinemas") ShowCinemasScreen(navController, cinemaViewModel)
        }
        if ((uiState).successScreenCallName == "artCreateCinemaScreen") {
            if ((uiState).successMethodName == "fetchUnavailableDates") ShowCreateCinemaScreen(navController, cinemaViewModel)
        }
        if ((uiState).successScreenCallName == "artEditCinemaScreen") {
            if ((uiState).successMethodName == "fetchUnavailableDates") ShowEditCinemaScreen(navController, cinemaViewModel)
        }
    }
    if ((uiState).successType == "screenRunning") {
        if ((uiState).successScreenCallName == "artCreateCinemaScreen") {
            if ((uiState).successMethodName == "createCinema") {
                ShowAlertDialogComponent("¿ Desea crear otra tarea ?",
                    onAlertDialogDismissRequest = { cinemaViewModel.fetchUnavailableDates((uiState).successScreenCallName) },
                    onAlertDialogButtonClick = { buttonValue ->
                        if (buttonValue == "No") navController.popBackStack(Destinations.ART_CINEMA_SCREEN_URL, false)
                        if (buttonValue == "Si") cinemaViewModel.fetchUnavailableDates((uiState).successScreenCallName)
                    }
                )
            }
        }
        if ((uiState).successScreenCallName == "artEditCinemaScreen") {
            if ((uiState).successMethodName == "putCinema") navController.popBackStack(Destinations.ART_CINEMA_SCREEN_URL, false)
            if ((uiState).successMethodName == "deleteCinema") navController.popBackStack(Destinations.ART_CINEMA_SCREEN_URL, false)
        }
        //if ((uiState as UiState.Success).successMethodName == "fetchAvailableDatesOnDayByDate") INDICAR QUE SE MUESTRAN LAS TAREAS PARA LA FECHA INDICADA.
    }
}
@Composable
fun ShowCinemaScreenErrorUiState(uiState: UiState.Error, cinemaViewModel: CinemaViewModel) {
    ApiErrorSnackbar(message = (uiState).errorMessage, onApiErrorSnackBarButtonRetry = {
        if ((uiState).errorType == "throwableErrorType") {
            if ((uiState).errorScreenCallName == "artCinemaScreen") {
                if ((uiState).errorMethodName == "fetchAvailableCinemas") cinemaViewModel.fetchAvailableCinemas((uiState).errorScreenCallName)
            }
            if ((uiState).errorScreenCallName == "artCreateCinemaScreen") {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") cinemaViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "createCinema") cinemaViewModel.createCinema()
            }
            if ((uiState).errorScreenCallName == "artEditCinemaScreen") {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") cinemaViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "putCinema") cinemaViewModel.putCinema()
                if ((uiState).errorMethodName == "deleteCinema") cinemaViewModel.deleteCinema()
            }
        }
        if ((uiState).errorType == "fetchDataErrorType") {
            if ((uiState).errorScreenCallName == "artCinemaScreen") {
                if ((uiState).errorMethodName == "fetchAvailableCinemas") cinemaViewModel.fetchAvailableCinemas((uiState).errorScreenCallName)
            }
            if ((uiState).errorScreenCallName == "artCreateCinemaScreen") {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") cinemaViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "createCinema") cinemaViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
            }
            if ((uiState).errorScreenCallName == "artEditCinemaScreen") {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") cinemaViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "putCinema") cinemaViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "deleteCinema") cinemaViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
            }
        }
    })
    //if ((uiState).errorMethodName == "fetchAvailableTasksDatesByDate") cinemaViewModel.fetchAvailableTasksDatesByDate("artCreateCinemaScreen")
}
/**
 *
 */
@Composable
fun ShowCinemasScreen(navController: NavHostController, cinemaViewModel: CinemaViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.bckcinemawhite), contentDescription = null,
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
        )
        //.border(width = 2.dp, color = Color.Red)
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize().padding(10.dp)) {
            // Mostramos el titulo de la vista utilizando el componente correspondiente llamado 'ViewTitleComponent'.
            ViewTitleComponent(stringResource(R.string.cinemaScreenViewName), Color(0xFF17202a))
            // Mostramos el busador de salas por nombre llamado 'TextFieldSearcherComponent'.
            ShowTextFieldSearcherComponent(
                onValueSearcherChange = { cinemaViewModel.fetchCinemaToSearch(it) },
                onCreateButtonClick = { navController.navigate(Destinations.ART_CREATE_CINEMA_SCREEN_URL) }
            )
            //
            if (cinemaViewModel.availableCinemasList.isResultListNullOrEmpty()) {
                Text(text = cinemaViewModel.availableCinemasList.message,
                    modifier = Modifier.padding(5.dp),
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.W400,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = 0.5.sp
                )
            } else {
                if (cinemaViewModel.showSearchedCinemas) {
                    cinemaViewModel.matchRoomList.data?.let {
                        ShowCinemaList(it) { id ->
                            cinemaViewModel.fetchSelectedCinemaData(id);
                            navController.navigate(Destinations.ART_UPDATE_CINEMA_SCREEN_URL)
                        }
                    }
                } else {
                    cinemaViewModel.availableCinemasList.data?.let {
                        ShowCinemaList(it) { id ->
                            cinemaViewModel.fetchSelectedCinemaData(id);
                            navController.navigate(Destinations.ART_UPDATE_CINEMA_SCREEN_URL)
                        }
                    }
                }
            }
        }
    }
}
/**
 *
 */
@Composable
fun ShowCinemaList(listToShow: List<CinemaModel>, onCardClick: (String) -> Unit) {
    //.border(width = 5.dp, color = Color.Green)
    LazyColumn(modifier = Modifier.fillMaxWidth().padding(15.dp, 10.dp)) {
        items (listToShow.count()) { cinema -> ShowCinemaData(listToShow, cinema, onCardClick = { id -> onCardClick(id) }) }
    }
}
/**
 *
 */
@Composable
fun ShowCinemaData(listToShow: List<CinemaModel>, cinema: Int, onCardClick: (String) -> Unit) {
    val outputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("es", "ES"))
    val formattedDate = LocalDateTime.parse(listToShow[cinema].fechaInicioPelicula, DateTimeFormatter.ISO_DATE_TIME).format(outputFormatter)
    //.border(width = 5.dp, color = Color.Blue)
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color(0xFFFFFFFF),
            contentColor = Color.Black,
            disabledContainerColor = Color(0xFFFFFFFF),
            disabledContentColor = Color.Black
        ), border = BorderStroke(1.dp, Color.LightGray),
        modifier = Modifier.fillMaxWidth().padding(30.dp, 10.dp).clickable(onClick = { onCardClick(listToShow[cinema]._id) })
    ) {
        ShowCustomData("Nombre: ${listToShow[cinema].nombrePelicula}")
        ShowCustomData("Cine: ${listToShow[cinema].lugarPelicula}")
        ShowCustomData("Fecha: $formattedDate")
    }
}