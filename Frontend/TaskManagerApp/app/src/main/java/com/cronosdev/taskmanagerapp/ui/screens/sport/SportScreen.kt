package com.cronosdev.taskmanagerapp.ui.screens.sport
//
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
import com.cronosdev.taskmanagerapp.data.model.sport.SportModel
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
fun SportScreen (navController: NavHostController, sportViewModel: SportViewModel) {
    // Variable que controla el estado de la vista/screen.
    val uiState by sportViewModel.sportScreenUiState.collectAsState()
    // Conjunto de acciones que se realizan al cargar la vista.
    LaunchedEffect(Unit) { sportViewModel.fetchAvailableSports(Destinations.SPORT_SCREEN_URL) }
    // Estructura de la vista/pantalla principal de la aplicacion.
    when (uiState) {
        is UiState.Loading -> ShowLoadingUiState()
        is UiState.Success -> ShowSportScreenSuccessUiState(uiState as UiState.Success, navController, sportViewModel)
        is UiState.Error -> ShowSportScreenErrorUiState(uiState as UiState.Error, sportViewModel)
        else -> {}
    }
}
@Composable
fun ShowSportScreenSuccessUiState(uiState: UiState.Success, navController: NavHostController, sportViewModel: SportViewModel) {
    if ((uiState).successType == "screenInit") {
        if ((uiState).successScreenCallName == Destinations.SPORT_SCREEN_URL) {
            if ((uiState).successMethodName == "fetchAvailableSports") ShowSportScreen(navController, sportViewModel)
        }
        if ((uiState).successScreenCallName == Destinations.CREATE_SPORT_SCREEN_URL) {
            if ((uiState).successMethodName == "fetchUnavailableDates") ShowCreateSportScreen(navController, sportViewModel)
        }
        if ((uiState).successScreenCallName == Destinations.EDIT_SPORT_SCREEN_URL) {
            if ((uiState).successMethodName == "fetchUnavailableDates") ShowUpdateSportScreen(navController, sportViewModel)
        }
    }
    if ((uiState).successType == "screenRunning") {
        if ((uiState).successScreenCallName == Destinations.CREATE_SPORT_SCREEN_URL) {
            if ((uiState).successMethodName == "createSport") {
                ShowAlertDialogComponent("¿ Desea crear otra tarea ?",
                    onAlertDialogDismissRequest = { sportViewModel.fetchUnavailableDates((uiState).successScreenCallName) },
                    onAlertDialogButtonClick = { buttonValue ->
                        if (buttonValue == "No") navController.popBackStack(Destinations.SPORT_SCREEN_URL, false)
                        if (buttonValue == "Si") sportViewModel.fetchUnavailableDates((uiState).successScreenCallName)
                    }
                )
            }
        }
        if ((uiState).successScreenCallName == Destinations.EDIT_SPORT_SCREEN_URL) {
            if ((uiState).successMethodName == "putSport") navController.popBackStack(Destinations.SPORT_SCREEN_URL, false)
            if ((uiState).successMethodName == "deleteSport") navController.popBackStack(Destinations.SPORT_SCREEN_URL, false)
        }
        //if ((uiState as UiState.Success).successMethodName == "fetchAvailableDatesOnDayByDate") INDICAR QUE SE MUESTRAN LAS TAREAS PARA LA FECHA INDICADA.
    }
}
@Composable
fun ShowSportScreenErrorUiState(uiState: UiState.Error, sportViewModel: SportViewModel) {
    ApiErrorSnackbar(message = (uiState).errorMessage, onApiErrorSnackBarButtonRetry = {
        if ((uiState).errorType == "throwableErrorType") {
            if ((uiState).errorScreenCallName == Destinations.SPORT_SCREEN_URL) {
                if ((uiState).errorMethodName == "fetchAvailableSports") sportViewModel.fetchAvailableSports((uiState).errorScreenCallName)
            }
            if ((uiState).errorScreenCallName == Destinations.CREATE_SPORT_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") sportViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "createSport") sportViewModel.createSport()
            }
            if ((uiState).errorScreenCallName == Destinations.EDIT_SPORT_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") sportViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "putSport") sportViewModel.putSport()
                if ((uiState).errorMethodName == "deleteSport") sportViewModel.deleteSport()
            }
        }
        if ((uiState).errorType == "fetchDataErrorType") {
            if ((uiState).errorScreenCallName == Destinations.SPORT_SCREEN_URL) {
                if ((uiState).errorMethodName == "fetchAvailableSports") sportViewModel.fetchAvailableSports((uiState).errorScreenCallName)
            }
            if ((uiState).errorScreenCallName == Destinations.CREATE_SPORT_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") sportViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "createSport") sportViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
            }
            if ((uiState).errorScreenCallName == Destinations.EDIT_SPORT_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") sportViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "putSport") sportViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "deleteSport") sportViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
            }
        }
    })
    //if ((uiState).errorMethodName == "fetchAvailableTasksDatesByDate") cinemaViewModel.fetchAvailableTasksDatesByDate("artCreateCinemaScreen")
}

/**
 *
 */
@Composable
fun ShowSportScreen(navController: NavHostController, sportViewModel: SportViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.bcksportwhite), contentDescription = null,
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
        )
        //.border(width = 2.dp, color = Color.Red)
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize().padding(10.dp)) {
            // Mostramos el titulo de la vista utilizando el componente correspondiente llamado 'ViewTitleComponent'.
            ViewTitleComponent(stringResource(R.string.sportScreenViewName), Color.Black)
            // Mostramos el busador de salas por nombre llamado 'TextFieldSearcherComponent'.
            ShowTextFieldSearcherComponent(
                onValueSearcherChange = { sportViewModel.fetchSportToSearch(it) },
                onCreateButtonClick = { navController.navigate(Destinations.CREATE_SPORT_SCREEN_URL) }
            )
            // Mostramos la lista en funcion de la variable 'showSearchedCinemas', la cual indica si hay que mostrar la lista de citas disponibles o la lista de las salas que busca el usuario.
            if (sportViewModel.showSearchedSports) sportViewModel.matchSportsList.data?.let { ShowPaintingList(it,
                onCardClick = { id -> sportViewModel.fetchSelectedPaintingData(id); navController.navigate(Destinations.EDIT_SPORT_SCREEN_URL) })
            }
            else sportViewModel.availableSportList.data?.let { ShowPaintingList(it,
                onCardClick = { id -> sportViewModel.fetchSelectedPaintingData(id); navController.navigate(Destinations.EDIT_SPORT_SCREEN_URL) })
            }
        }
    }
}
/**
 *
 */
@Composable
fun ShowPaintingList(listToShow: List<SportModel>, onCardClick: (String) -> Unit) {
    //.border(width = 5.dp, color = Color.Green)
    LazyColumn(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)) {
        items (listToShow.count()) { sport -> ShowPaintingData(listToShow, sport, onCardClick = { id -> onCardClick(id) }) }
    }
}
/**
 *
 */
@Composable
fun ShowPaintingData(listToShow: List<SportModel>, sport: Int, onCardClick: (String) -> Unit) {
    val outputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("es", "ES"))
    val formattedDate = LocalDateTime.parse(listToShow[sport].fechaInicioActividad, DateTimeFormatter.ISO_DATE_TIME).format(outputFormatter)
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.Black
        ), elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier = Modifier.fillMaxWidth().padding(30.dp, 10.dp).clickable(onClick = { onCardClick(listToShow[sport]._id) })
    ) {
        ShowCustomData("Actividad: ${listToShow[sport].nombreActividad}")
        ShowCustomData("Tiempo: ${listToShow[sport].duracionActividadMinutos}")
        ShowCustomData("Fecha: $formattedDate")
    }
}