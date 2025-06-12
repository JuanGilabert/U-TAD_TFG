package com.cronosdev.taskmanagerapp.ui.screens.art.painting
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cronosdev.taskmanagerapp.R
import com.cronosdev.taskmanagerapp.data.model.art.PaintingModel
import com.cronosdev.taskmanagerapp.ui.state.ShowLoadingUiState
//
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
fun PaintingScreen(navController: NavHostController, paintingViewModel: PaintingViewModel) {
    // Variable que controla el estado de la vista/screen.
    val uiState by paintingViewModel.paintingScreenUiState.collectAsState()
    // Conjunto de acciones que se realizan al cargar la vista.
    LaunchedEffect(Unit) {
        paintingViewModel.setPaintingVariables()
        paintingViewModel.setShowSearchedPaintings(false)
        paintingViewModel.fetchAvailablePaintings(Destinations.ART_PAINTING_SCREEN_URL)
    }
    // Estructura de la vista/pantalla principal de la aplicacion.
    when (uiState) {
        is UiState.Loading -> ShowLoadingUiState()
        is UiState.Success -> ShowPaintingScreenSuccessUiState(uiState as UiState.Success, navController, paintingViewModel)
        is UiState.Error -> ShowPaintingScreenErrorUiState(uiState as UiState.Error, paintingViewModel)
        else -> {}
    }
}
@Composable
fun ShowPaintingScreenSuccessUiState(uiState: UiState.Success, navController: NavHostController, paintingViewModel: PaintingViewModel) {
    if ((uiState).successType == "screenInit") {
        if ((uiState).successScreenCallName == Destinations.ART_PAINTING_SCREEN_URL) {
            if ((uiState).successMethodName == "fetchAvailablePaintings") ShowPaintingScreen(navController, paintingViewModel)
        }
        if ((uiState).successScreenCallName == Destinations.ART_CREATE_PAINTING_SCREEN_URL) {
            if ((uiState).successMethodName == "fetchUnavailableDates") ShowCreatePaintingScreen(navController, paintingViewModel)
        }
        if ((uiState).successScreenCallName == Destinations.ART_UPDATE_PAINTING_SCREEN_URL) {
            if ((uiState).successMethodName == "fetchUnavailableDates") ShowUpdatePaintingScreen(navController, paintingViewModel)
        }
    }
    if ((uiState).successType == "screenRunning") {
        //if ((uiState).successScreenCallName == Destinations.ART_PAINTING_SCREEN_URL) {}
        if ((uiState).successScreenCallName == Destinations.ART_CREATE_PAINTING_SCREEN_URL) {
            if ((uiState).successMethodName == "createPainting") {
                ShowAlertDialogComponent("¿ Desea crear otra tarea ?",
                    onAlertDialogDismissRequest = { paintingViewModel.fetchUnavailableDates((uiState).successScreenCallName) },
                    onAlertDialogButtonClick = { buttonValue ->
                        if (buttonValue == "No") navController.popBackStack(Destinations.ART_PAINTING_SCREEN_URL, false)
                        if (buttonValue == "Si") paintingViewModel.fetchUnavailableDates((uiState).successScreenCallName)
                    }
                )
            }
        }
        if ((uiState).successScreenCallName == Destinations.ART_UPDATE_PAINTING_SCREEN_URL) {
            if ((uiState).successMethodName == "putPainting") navController.popBackStack(Destinations.ART_PAINTING_SCREEN_URL, false)
            if ((uiState).successMethodName == "deletePainting") navController.popBackStack(Destinations.ART_PAINTING_SCREEN_URL, false)
        }
        //if ((uiState as UiState.Success).successMethodName == "fetchAvailableDatesOnDayByDate") INDICAR QUE SE MUESTRAN LAS TAREAS PARA LA FECHA INDICADA.
    }
}
@Composable
fun ShowPaintingScreenErrorUiState(uiState: UiState.Error, paintingViewModel: PaintingViewModel) {
    ApiErrorSnackbar(message = (uiState).errorMessage, onApiErrorSnackBarButtonRetry = {
        if ((uiState).errorType == "throwableErrorType") {
            if ((uiState).errorScreenCallName == Destinations.ART_PAINTING_SCREEN_URL) {
                if ((uiState).errorMethodName == "fetchAvailablePaintings") paintingViewModel.fetchAvailablePaintings((uiState).errorScreenCallName)
            }
            if ((uiState).errorScreenCallName == Destinations.ART_CREATE_PAINTING_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") paintingViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "createPainting") paintingViewModel.createPainting()
            }
            if ((uiState).errorScreenCallName == Destinations.ART_UPDATE_PAINTING_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") paintingViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "putPainting") paintingViewModel.putPainting()
                if ((uiState).errorMethodName == "deletePainting") paintingViewModel.deletePainting()
            }
        }
        if ((uiState).errorType == "fetchDataErrorType") {
            if ((uiState).errorScreenCallName == Destinations.ART_PAINTING_SCREEN_URL) {
                if ((uiState).errorMethodName == "fetchAvailablePaintings") paintingViewModel.fetchAvailablePaintings((uiState).errorScreenCallName)
            }
            if ((uiState).errorScreenCallName == Destinations.ART_CREATE_PAINTING_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") paintingViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "createPainting") paintingViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
            }
            if ((uiState).errorScreenCallName == Destinations.ART_UPDATE_PAINTING_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") paintingViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "putPainting") paintingViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "deletePainting") paintingViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
            }
        }
    })
    //if ((uiState).errorMethodName == "fetchAvailableTasksDatesByDate") cinemaViewModel.fetchAvailableTasksDatesByDate("artCreateCinemaScreen")
}

/**
 *
 */
@Composable
fun ShowPaintingScreen(navController: NavHostController, paintingViewModel: PaintingViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.bckpaintingwhite), contentDescription = null,
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
        )
        //.border(width = 2.dp, color = Color.Red)
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize().padding(10.dp)) {
            // Mostramos el titulo de la vista utilizando el componente correspondiente llamado 'ViewTitleComponent'.
            ViewTitleComponent(stringResource(R.string.paintingScreenViewName), Color.Black)
            // Mostramos el busador de salas por nombre llamado 'TextFieldSearcherComponent'.
            ShowTextFieldSearcherComponent(
                onValueSearcherChange = { paintingViewModel.fetchPaintingToSearch(it) },
                onCreateButtonClick = { navController.navigate(Destinations.ART_CREATE_PAINTING_SCREEN_URL) }
            )
            // Mostramos la lista en funcion de la variable 'showSearchedCinemas', la cual indica si hay que mostrar la lista de citas disponibles o la lista de las salas que busca el usuario.
            if (paintingViewModel.showSearchedPaintings) paintingViewModel.matchPaintingsList.data?.let { ShowPaintingList(it,
                onCardClick = { id -> paintingViewModel.fetchSelectedPaintingData(id); navController.navigate(Destinations.ART_UPDATE_PAINTING_SCREEN_URL) })
            }
            else paintingViewModel.availablePaintingList.data?.let { ShowPaintingList(it,
                onCardClick = { id -> paintingViewModel.fetchSelectedPaintingData(id); navController.navigate(Destinations.ART_UPDATE_PAINTING_SCREEN_URL) })
            }
        }
    }
}
/**
 *
 */
@Composable
fun ShowPaintingList(listToShow: List<PaintingModel>, onCardClick: (String) -> Unit) {
    //.border(width = 5.dp, color = Color.Green)
    LazyColumn(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)) {
        items (listToShow.count()) { painting -> ShowPaintingData(listToShow, painting, onCardClick = { id -> onCardClick(id) }) }
    }
}
/**
 *
 */
@Composable
fun ShowPaintingData(listToShow: List<PaintingModel>, painting: Int, onCardClick: (String) -> Unit) {
    val outputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("es", "ES"))
    val formattedDate = LocalDateTime.parse(listToShow[painting].fechaInicioExposicionArte, DateTimeFormatter.ISO_DATE_TIME).format(outputFormatter)
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.Black
        ), elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier = Modifier.fillMaxWidth().padding(30.dp, 10.dp).clickable(onClick = { onCardClick(listToShow[painting]._id) })
    ) {
        ShowCustomData("Exposicion: ${listToShow[painting].nombreExposicionArte}")
        ShowCustomData("Direccion: ${listToShow[painting].lugarExposicionArte}")
        ShowCustomData("Fecha: $formattedDate")
    }
}