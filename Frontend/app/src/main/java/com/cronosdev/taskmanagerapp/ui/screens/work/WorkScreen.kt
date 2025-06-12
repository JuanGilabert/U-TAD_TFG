package com.cronosdev.taskmanagerapp.ui.screens.work
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
import com.cronosdev.taskmanagerapp.data.model.work.WorkModel
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
fun WorkScreen(navController: NavHostController, workViewModel: WorkViewModel) {
    // Variable que controla el estado de la vista/screen.
    val uiState by workViewModel.workScreenUiState.collectAsState()
    // Conjunto de acciones que se realizan al cargar la vista.
    LaunchedEffect(Unit) {
        workViewModel.setWorkVariables()
        workViewModel.setShowSearchedWorks(false)
        workViewModel.fetchAvailableWorks(Destinations.WORK_SCREEN_URL)
    }
    // Estructura de la vista/pantalla principal de la aplicacion.
    when (uiState) {
        is UiState.Loading -> ShowLoadingUiState()
        is UiState.Success -> ShowWorkScreenSuccessUiState(uiState as UiState.Success, navController, workViewModel)
        is UiState.Error -> ShowWorkScreenErrorUiState(uiState as UiState.Error, workViewModel)
        else -> {}
    }
}
@Composable
fun ShowWorkScreenSuccessUiState(uiState: UiState.Success, navController: NavHostController, workViewModel: WorkViewModel) {
    if ((uiState).successType == "screenInit") {
        if ((uiState).successScreenCallName == Destinations.WORK_SCREEN_URL) {
            if ((uiState).successMethodName == "fetchAvailableWorks") ShowWorkScreen(navController, workViewModel)
        }
        if ((uiState).successScreenCallName == Destinations.CREATE_WORK_SCREEN_URL) {
            if ((uiState).successMethodName == "fetchUnavailableDates") ShowCreateWorkScreen(navController, workViewModel)
        }
        if ((uiState).successScreenCallName == Destinations.EDIT_WORK_SCREEN_URL) {
            if ((uiState).successMethodName == "fetchUnavailableDates") ShowUpdateWorkScreen(navController, workViewModel)
        }
    }
    if ((uiState).successType == "screenRunning") {
        if ((uiState).successScreenCallName == Destinations.CREATE_WORK_SCREEN_URL) {
            if ((uiState).successMethodName == "createWork") {
                ShowAlertDialogComponent("¿ Desea crear otra tarea ?",
                    onAlertDialogDismissRequest = { workViewModel.fetchUnavailableDates((uiState).successScreenCallName) },
                    onAlertDialogButtonClick = { buttonValue ->
                        if (buttonValue == "No") navController.popBackStack(Destinations.WORK_SCREEN_URL, false)
                        if (buttonValue == "Si") workViewModel.fetchUnavailableDates((uiState).successScreenCallName)
                    }
                )
            }
        }
        if ((uiState).successScreenCallName == Destinations.EDIT_WORK_SCREEN_URL) {
            if ((uiState).successMethodName == "putWork") navController.popBackStack(Destinations.WORK_SCREEN_URL, false)
            if ((uiState).successMethodName == "deleteWork") navController.popBackStack(Destinations.WORK_SCREEN_URL, false)
        }
        //if ((uiState as UiState.Success).successMethodName == "fetchAvailableDatesOnDayByDate") INDICAR QUE SE MUESTRAN LAS TAREAS PARA LA FECHA INDICADA.
    }
}
@Composable
fun ShowWorkScreenErrorUiState(uiState: UiState.Error, workViewModel: WorkViewModel) {
    ApiErrorSnackbar(message = (uiState).errorMessage, onApiErrorSnackBarButtonRetry = {
        if ((uiState).errorType == "throwableErrorType") {
            if ((uiState).errorScreenCallName == Destinations.WORK_SCREEN_URL) {
                if ((uiState).errorMethodName == "fetchAvailableWorks") workViewModel.fetchAvailableWorks((uiState).errorScreenCallName)
            }
            if ((uiState).errorScreenCallName == Destinations.CREATE_WORK_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") workViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "createWork") workViewModel.createWork()
            }
            if ((uiState).errorScreenCallName == Destinations.EDIT_WORK_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") workViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "putWork") workViewModel.putWork()
                if ((uiState).errorMethodName == "deleteWork") workViewModel.deleteWork()
            }
        }
        if ((uiState).errorType == "fetchDataErrorType") {
            if ((uiState).errorScreenCallName == Destinations.WORK_SCREEN_URL) {
                if ((uiState).errorMethodName == "fetchAvailableWorks") workViewModel.fetchAvailableWorks((uiState).errorScreenCallName)
            }
            if ((uiState).errorScreenCallName == Destinations.CREATE_WORK_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") workViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "createWork") workViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
            }
            if ((uiState).errorScreenCallName == Destinations.EDIT_WORK_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") workViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "putWork") workViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "deleteWork") workViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
            }
        }
    })
    //if ((uiState).errorMethodName == "fetchAvailableTasksDatesByDate") cinemaViewModel.fetchAvailableTasksDatesByDate("artCreateCinemaScreen")
}

/**
 *
 */
@Composable
fun ShowWorkScreen(navController: NavHostController, workViewModel: WorkViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.bckworkwhite), contentDescription = null,
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
        )
        //.border(width = 2.dp, color = Color.Red)
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize().padding(10.dp)) {
            // Mostramos el titulo de la vista utilizando el componente correspondiente llamado 'ViewTitleComponent'.
            ViewTitleComponent(stringResource(R.string.workScreenViewName), Color.Black)
            // Mostramos el busador de salas por nombre llamado 'TextFieldSearcherComponent'.
            ShowTextFieldSearcherComponent(
                onValueSearcherChange = { workViewModel.fetchWorkToSearch(it) },
                onCreateButtonClick = { navController.navigate(Destinations.CREATE_WORK_SCREEN_URL) }
            )
            // Mostramos la lista en funcion de la variable 'showSearchedCinemas', la cual indica si hay que mostrar la lista de citas disponibles o la lista de las salas que busca el usuario.
            if (workViewModel.showSearchedWorks) workViewModel.matchWorksList.data?.let { ShowWorkList(it,
                onCardClick = { id -> workViewModel.fetchSelectedWorkData(id); navController.navigate(Destinations.EDIT_WORK_SCREEN_URL) })
            }
            else workViewModel.availableWorkList.data?.let { ShowWorkList(it,
                onCardClick = { id -> workViewModel.fetchSelectedWorkData(id); navController.navigate(Destinations.EDIT_WORK_SCREEN_URL) })
            }
        }
    }
}
/**
 *
 */
@Composable
fun ShowWorkList(listToShow: List<WorkModel>, onCardClick: (String) -> Unit) {
    //.border(width = 5.dp, color = Color.Green)
    LazyColumn(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)) {
        items (listToShow.count()) { work -> ShowWorkData(listToShow, work, onCardClick = { id -> onCardClick(id) }) }
    }
}
/**
 *
 */
@Composable
fun ShowWorkData(listToShow: List<WorkModel>, work: Int, onCardClick: (String) -> Unit) {
    val outputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("es", "ES"))
    val formattedInitDate = LocalDateTime.parse(listToShow[work].fechaInicioTarea, DateTimeFormatter.ISO_DATE_TIME).format(outputFormatter)
    val formattedFinishDate = LocalDateTime.parse(listToShow[work].fechaEntregaTarea, DateTimeFormatter.ISO_DATE_TIME).format(outputFormatter)
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.Black
        ), elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier = Modifier.fillMaxWidth().padding(30.dp, 10.dp).clickable(onClick = { onCardClick(listToShow[work]._id) })
    ) {
        ShowCustomData("Tarea: ${listToShow[work].tituloTarea}")
        ShowCustomData("Inicio: $formattedInitDate")
        ShowCustomData("Entrega: $formattedFinishDate")
    }
}