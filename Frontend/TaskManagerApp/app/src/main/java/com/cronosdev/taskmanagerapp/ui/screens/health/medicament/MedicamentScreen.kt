package com.cronosdev.taskmanagerapp.ui.screens.health.medicament
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
import com.cronosdev.taskmanagerapp.data.model.health.MedicamentModel
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
fun MedicamentScreen (navController: NavHostController, medicamentViewModel: MedicamentViewModel) {
    // Variable que controla el estado de la vista/screen.
    val uiState by medicamentViewModel.medicamentScreenUiState.collectAsState()
    // Conjunto de acciones que se realizan al cargar la vista.
    LaunchedEffect(Unit) { medicamentViewModel.fetchAvailableMedicaments(Destinations.HEALTH_MEDICAMENT_SCREEN_URL) }
    // Estructura de la vista/pantalla principal de la aplicacion.
    when (uiState) {
        is UiState.Loading -> ShowLoadingUiState()
        is UiState.Success -> ShowMedicamentScreenSuccessUiState(uiState as UiState.Success, navController, medicamentViewModel)
        is UiState.Error -> ShowMedicamentScreenErrorUiState(uiState as UiState.Error, medicamentViewModel)
        else -> {}
    }
}
@Composable
fun ShowMedicamentScreenSuccessUiState(uiState: UiState.Success, navController: NavHostController, medicamentViewModel: MedicamentViewModel) {
    if ((uiState).successType == "screenInit") {
        if ((uiState).successScreenCallName == Destinations.HEALTH_MEDICAMENT_SCREEN_URL) {
            if ((uiState).successMethodName == "fetchAvailableMedicaments") ShowMedicamentScreen(navController, medicamentViewModel)
        }
        if ((uiState).successScreenCallName == Destinations.HEALTH_CREATE_MEDICAMENT_SCREEN_URL) {
            if ((uiState).successMethodName == "fetchUnavailableDates") ShowCreateMedicamentScreen(navController, medicamentViewModel)
        }
        if ((uiState).successScreenCallName == Destinations.HEALTH_EDIT_MEDICAMENT_SCREEN_URL) {
            if ((uiState).successMethodName == "fetchUnavailableDates") ShowUpdateMedicamentScreen(navController, medicamentViewModel)
        }
    }
    if ((uiState).successType == "screenRunning") {
        if ((uiState).successScreenCallName == Destinations.HEALTH_CREATE_MEDICAMENT_SCREEN_URL) {
            if ((uiState).successMethodName == "createMedicament") {
                ShowAlertDialogComponent("¿ Desea crear otra tarea ?",
                    onAlertDialogDismissRequest = { medicamentViewModel.fetchUnavailableDates((uiState).successScreenCallName) },
                    onAlertDialogButtonClick = { buttonValue ->
                        if (buttonValue == "No") navController.popBackStack(Destinations.HEALTH_MEDICAMENT_SCREEN_URL, false)
                        if (buttonValue == "Si") medicamentViewModel.fetchUnavailableDates((uiState).successScreenCallName)
                    }
                )
            }
        }
        if ((uiState).successScreenCallName == Destinations.HEALTH_EDIT_MEDICAMENT_SCREEN_URL) {
            if ((uiState).successMethodName == "putMedicament")
                navController.popBackStack(Destinations.HEALTH_MEDICAMENT_SCREEN_URL, false)
            if ((uiState).successMethodName == "deleteMedicament")
                navController.popBackStack(Destinations.HEALTH_MEDICAMENT_SCREEN_URL, false)
        }
        //if ((uiState as UiState.Success).successMethodName == "fetchAvailableDatesOnDayByDate") INDICAR QUE SE MUESTRAN LAS TAREAS PARA LA FECHA INDICADA.
    }
}
@Composable
fun ShowMedicamentScreenErrorUiState(uiState: UiState.Error, medicamentViewModel: MedicamentViewModel) {
    ApiErrorSnackbar(message = (uiState).errorMessage, onApiErrorSnackBarButtonRetry = {
        if ((uiState).errorType == "throwableErrorType") {
            if ((uiState).errorScreenCallName == Destinations.HEALTH_MEDICAMENT_SCREEN_URL) {
                if ((uiState).errorMethodName == "fetchAvailableMedicaments") medicamentViewModel.fetchAvailableMedicaments((uiState).errorScreenCallName)
            }
            if ((uiState).errorScreenCallName == Destinations.HEALTH_CREATE_MEDICAMENT_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") medicamentViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "createMedicament") medicamentViewModel.createMedicament()
            }
            if ((uiState).errorScreenCallName == Destinations.HEALTH_EDIT_MEDICAMENT_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") medicamentViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "putMedicament") medicamentViewModel.putMedicament()
                if ((uiState).errorMethodName == "deleteMedicament") medicamentViewModel.deleteMedicament()
            }
        }
        if ((uiState).errorType == "fetchDataErrorType") {
            if ((uiState).errorScreenCallName == Destinations.HEALTH_MEDICAMENT_SCREEN_URL) {
                if ((uiState).errorMethodName == "fetchAvailableMedicaments") medicamentViewModel.fetchAvailableMedicaments((uiState).errorScreenCallName)
            }
            if ((uiState).errorScreenCallName == Destinations.HEALTH_CREATE_MEDICAMENT_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") medicamentViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "createMedicament") medicamentViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
            }
            if ((uiState).errorScreenCallName == Destinations.HEALTH_EDIT_MEDICAMENT_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") medicamentViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "putMedicament") medicamentViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "deleteMedicament") medicamentViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
            }
        }
    })
    //if ((uiState).errorMethodName == "fetchAvailableTasksDatesByDate") cinemaViewModel.fetchAvailableTasksDatesByDate("artCreateCinemaScreen")
}
/**
 *
 */
@Composable
fun ShowMedicamentScreen(navController: NavHostController, medicamentViewModel: MedicamentViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.bckmedicamentwhite), contentDescription = null,
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
        )
        //.border(width = 2.dp, color = Color.Red)
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize().padding(10.dp)) {
            // Mostramos el titulo de la vista utilizando el componente correspondiente llamado 'ViewTitleComponent'.
            ViewTitleComponent(stringResource(R.string.medicamentScreenViewName), Color.Black)
            // Mostramos el busador de salas por nombre llamado 'TextFieldSearcherComponent'.
            ShowTextFieldSearcherComponent(
                onValueSearcherChange = { medicamentViewModel.fetchMedicamentToSearch(it) },
                onCreateButtonClick = { navController.navigate(Destinations.HEALTH_CREATE_MEDICAMENT_SCREEN_URL) }
            )
            // Mostramos la lista en funcion de la variable 'showSearchedCinemas', la cual indica si hay que mostrar la lista de citas disponibles o la lista de las salas que busca el usuario.
            if (medicamentViewModel.showSearchedMedicaments) medicamentViewModel.matchMedicamentsList.data?.let { ShowMedicamentList(it,
                onCardClick = { id -> medicamentViewModel.fetchSelectedMedicamentData(id); navController.navigate(Destinations.HEALTH_EDIT_MEDICAMENT_SCREEN_URL) })
            }
            else medicamentViewModel.availableMedicamentsList.data?.let { ShowMedicamentList(it,
                onCardClick = { id -> medicamentViewModel.fetchSelectedMedicamentData(id); navController.navigate(Destinations.HEALTH_EDIT_MEDICAMENT_SCREEN_URL) })
            }
        }
    }
}
/**
 *
 */
@Composable
fun ShowMedicamentList(listToShow: List<MedicamentModel>, onCardClick: (String) -> Unit) {
    //.border(width = 5.dp, color = Color.Green)
    LazyColumn(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)) {
        items (listToShow.count()) { medicament -> ShowMedicamentData(listToShow, medicament, onCardClick = { id -> onCardClick(id) }) }
    }
}
/**
 *
 */
@Composable
fun ShowMedicamentData(listToShow: List<MedicamentModel>, medicament: Int, onCardClick: (String) -> Unit) {
    val outputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("es", "ES"))
    val formattedDate = LocalDateTime.parse(listToShow[medicament].fechaCaducidadMedicamento, DateTimeFormatter.ISO_DATE_TIME).format(outputFormatter)
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.Black
        ), elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier = Modifier.fillMaxWidth().padding(30.dp, 10.dp).clickable(onClick = { onCardClick(listToShow[medicament]._id) })
    ) {
        ShowCustomData("Medicamento: ${listToShow[medicament].nombreMedicamento}")
        ShowCustomData("Cantidad: ${listToShow[medicament].cantidadTotalCajaMedicamento}")
        ShowCustomData("Caducidad: $formattedDate")
    }
}