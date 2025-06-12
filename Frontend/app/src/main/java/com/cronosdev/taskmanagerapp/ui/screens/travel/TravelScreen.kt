package com.cronosdev.taskmanagerapp.ui.screens.travel

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cronosdev.taskmanagerapp.R
import com.cronosdev.taskmanagerapp.data.model.travel.TravelModel
import com.cronosdev.taskmanagerapp.ui.state.ShowLoadingUiState
import com.cronosdev.taskmanagerapp.ui.state.UiState
import com.cronosdev.taskmanagerapp.ui.components.ApiErrorSnackbar
import com.cronosdev.taskmanagerapp.ui.components.ShowAlertDialogComponent
import com.cronosdev.taskmanagerapp.ui.components.outlinedComponents.buttons.ShowCustomData
import com.cronosdev.taskmanagerapp.ui.components.viewSearchersComponents.ShowTextFieldSearcherComponent
import com.cronosdev.taskmanagerapp.ui.components.ViewTitleComponent
import com.cronosdev.taskmanagerapp.ui.navigation.Destinations
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 *
 */
@Composable
fun TravelScreen (navController: NavHostController, travelViewModel: TravelViewModel) {
    // Variable que controla el estado de la vista/screen.
    val uiState by travelViewModel.travelScreenUiState.collectAsState()
    // Conjunto de acciones que se realizan al cargar la vista.
    LaunchedEffect(Unit) {
        travelViewModel.setTravelVariables()
        travelViewModel.setShowSearchedTravels(false)
        travelViewModel.fetchAvailableTravels(Destinations.TRAVEL_SCREEN_URL)
    }
    // Estructura de la vista/pantalla principal de la aplicacion.
    when (uiState) {
        is UiState.Loading -> ShowLoadingUiState()
        is UiState.Success -> ShowTravelScreenSuccessUiState(uiState as UiState.Success, navController, travelViewModel)
        is UiState.Error -> ShowTravelScreenErrorUiState(uiState as UiState.Error, travelViewModel)
        else -> {}
    }
}
@Composable
fun ShowTravelScreenSuccessUiState(uiState: UiState.Success, navController: NavHostController, travelViewModel: TravelViewModel) {
    if ((uiState).successType == "screenInit") {
        if ((uiState).successScreenCallName == Destinations.TRAVEL_SCREEN_URL) {
            if ((uiState).successMethodName == "fetchAvailableTravels") ShowTravelScreen(navController, travelViewModel)
        }
        if ((uiState).successScreenCallName == Destinations.CREATE_TRAVEL_SCREEN_URL) {
            if ((uiState).successMethodName == "fetchUnavailableDates") ShowCreateTravelScreen(navController, travelViewModel)
        }
        if ((uiState).successScreenCallName == Destinations.EDIT_TRAVEL_SCREEN_URL) {
            if ((uiState).successMethodName == "fetchUnavailableDates") ShowUpdateTravelScreen(navController, travelViewModel)
        }
    }
    if ((uiState).successType == "screenRunning") {
        if ((uiState).successScreenCallName == Destinations.CREATE_TRAVEL_SCREEN_URL) {
            if ((uiState).successMethodName == "createTravel") {
                ShowAlertDialogComponent("¿ Desea crear otra tarea ?",
                    onAlertDialogDismissRequest = { travelViewModel.fetchUnavailableDates((uiState).successScreenCallName) },
                    onAlertDialogButtonClick = { buttonValue ->
                        if (buttonValue == "No") navController.popBackStack(Destinations.TRAVEL_SCREEN_URL, false)
                        if (buttonValue == "Si") travelViewModel.fetchUnavailableDates((uiState).successScreenCallName)
                    }
                )
            }
        }
        if ((uiState).successScreenCallName == Destinations.EDIT_TRAVEL_SCREEN_URL) {
            if ((uiState).successMethodName == "putTravel") navController.popBackStack(Destinations.TRAVEL_SCREEN_URL, false)
            if ((uiState).successMethodName == "deleteTravel") navController.popBackStack(Destinations.TRAVEL_SCREEN_URL, false)
        }
        //if ((uiState as UiState.Success).successMethodName == "fetchAvailableDatesOnDayByDate") INDICAR QUE SE MUESTRAN LAS TAREAS PARA LA FECHA INDICADA.
    }
}
@Composable
fun ShowTravelScreenErrorUiState(uiState: UiState.Error, travelViewModel: TravelViewModel) {
    ApiErrorSnackbar(message = (uiState).errorMessage, onApiErrorSnackBarButtonRetry = {
        if ((uiState).errorType == "throwableErrorType") {
            if ((uiState).errorScreenCallName == Destinations.TRAVEL_SCREEN_URL) {
                if ((uiState).errorMethodName == "fetchAvailableTravels") travelViewModel.fetchAvailableTravels((uiState).errorScreenCallName)
            }
            if ((uiState).errorScreenCallName == Destinations.CREATE_TRAVEL_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") travelViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "createTravel") travelViewModel.createTravel()
            }
            if ((uiState).errorScreenCallName == Destinations.EDIT_TRAVEL_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") travelViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "putTravel") travelViewModel.putTravel()
                if ((uiState).errorMethodName == "deleteTravel") travelViewModel.deleteTravel()
            }
        }
        if ((uiState).errorType == "fetchDataErrorType") {
            if ((uiState).errorScreenCallName == Destinations.TRAVEL_SCREEN_URL) {
                if ((uiState).errorMethodName == "fetchAvailableTravels") travelViewModel.fetchAvailableTravels((uiState).errorScreenCallName)
            }
            if ((uiState).errorScreenCallName == Destinations.CREATE_TRAVEL_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") travelViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "createTravel") travelViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
            }
            if ((uiState).errorScreenCallName == Destinations.EDIT_TRAVEL_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") travelViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "putTravel") travelViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "deleteTravel") travelViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
            }
        }
    })
    //if ((uiState).errorMethodName == "fetchAvailableTasksDatesByDate") cinemaViewModel.fetchAvailableTasksDatesByDate("artCreateCinemaScreen")
}

/**
 *
 */
@Composable
fun ShowTravelScreen(navController: NavHostController, travelViewModel: TravelViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.bcktravelwhite), contentDescription = null,
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
        )
        //.border(width = 2.dp, color = Color.Red)
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize().padding(10.dp)) {
            // Mostramos el titulo de la vista utilizando el componente correspondiente llamado 'ViewTitleComponent'.
            ViewTitleComponent(stringResource(R.string.travelScreenViewName), Color.Black)
            // Mostramos el busador de salas por nombre llamado 'TextFieldSearcherComponent'.
            ShowTextFieldSearcherComponent(
                onValueSearcherChange = { travelViewModel.fetchTravelToSearch(it) },
                onCreateButtonClick = { navController.navigate(Destinations.CREATE_TRAVEL_SCREEN_URL) }
            )
            // Mostramos la lista en funcion de la variable 'showSearchedCinemas', la cual indica si hay que mostrar la lista de citas disponibles o la lista de las salas que busca el usuario.
            if (travelViewModel.showSearchedTravels) travelViewModel.matchTravelsList.data?.let { ShowTravelList(it,
                onCardClick = { id -> travelViewModel.fetchSelectedTravelData(id); navController.navigate(Destinations.EDIT_TRAVEL_SCREEN_URL) })
            }
            else travelViewModel.availableTravelList.data?.let { ShowTravelList(it,
                onCardClick = { id -> travelViewModel.fetchSelectedTravelData(id); navController.navigate(Destinations.EDIT_TRAVEL_SCREEN_URL) })
            }
        }
    }
}
/**
 *
 */
@Composable
fun ShowTravelList(listToShow: List<TravelModel>, onCardClick: (String) -> Unit) {
    //.border(width = 5.dp, color = Color.Green)
    LazyColumn(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)) {
        items (listToShow.count()) { travel -> ShowTravelData(listToShow, travel, onCardClick = { id -> onCardClick(id) }) }
    }
}
/**
 *
 */
@Composable
fun ShowTravelData(listToShow: List<TravelModel>, travel: Int, onCardClick: (String) -> Unit) {
    val outputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("es", "ES"));
    var formattedDepartureDate = LocalDateTime.parse(listToShow[travel].fechaSalidaViaje, DateTimeFormatter.ISO_DATE_TIME).format(outputFormatter)
    val formattedArrivalDate = LocalDateTime.parse(listToShow[travel].fechaRegresoViaje, DateTimeFormatter.ISO_DATE_TIME).format(outputFormatter)
    //.border(width = 5.dp, color = Color.Blue)
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.Black
        ), elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier = Modifier.fillMaxWidth().padding(30.dp, 10.dp).clickable(onClick = { onCardClick(listToShow[travel]._id) })
    ) {
        ShowCustomData("Ciudad: ${listToShow[travel].nombreDestinoViaje}")
        ShowCustomData("Destino: ${listToShow[travel].lugarDestinoViaje}")
        ShowCustomData("Salida: $formattedDepartureDate")
        ShowCustomData("Regreso: $formattedArrivalDate")
    }
}