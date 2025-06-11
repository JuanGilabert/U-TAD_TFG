package com.cronosdev.taskmanagerapp.ui.screens.health.medicalAppointment
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
//
import androidx.navigation.NavHostController
import com.cronosdev.taskmanagerapp.R
import com.cronosdev.taskmanagerapp.data.model.health.MedicalAppointmentModel
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
fun MedicalAppointmentScreen(navController: NavHostController, medicalAppointmentViewModel: MedicalAppointmentViewModel) {
    // Variable que controla el estado de la vista/screen.
    val uiState by medicalAppointmentViewModel.medicalAppointmentScreenUiState.collectAsState()
    // Conjunto de acciones que se realizan al cargar la vista.
    LaunchedEffect(Unit) {
        medicalAppointmentViewModel.fetchAvailableMedicalAppointments(Destinations.HEALTH_MEDICAL_APPOINTMENT_SCREEN_URL)
    }
    // Estructura de la vista/pantalla principal de la aplicacion.
    when (uiState) {
        is UiState.Loading -> ShowLoadingUiState()
        is UiState.Success -> ShowMedicalAppointmentScreenSuccessUiState(uiState as UiState.Success, navController, medicalAppointmentViewModel)
        is UiState.Error -> ShowCinemaScreenErrorUiState(uiState as UiState.Error, medicalAppointmentViewModel)
        else -> {}
    }
}
@Composable
fun ShowMedicalAppointmentScreenSuccessUiState(uiState: UiState.Success, navController: NavHostController, medicalAppointmentViewModel: MedicalAppointmentViewModel) {
    if ((uiState).successType == "screenInit") {
        if ((uiState).successScreenCallName == Destinations.HEALTH_MEDICAL_APPOINTMENT_SCREEN_URL) {
            if ((uiState).successMethodName == "fetchAvailableMedicalAppointments") ShowMedicalAppointmentScreen(navController, medicalAppointmentViewModel)
        }
        if ((uiState).successScreenCallName == Destinations.HEALTH_CREATE_MEDICAL_APPOINTMENT_SCREEN_URL) {
            if ((uiState).successMethodName == "fetchUnavailableDates") ShowCreateMedicalAppointmentScreen(navController, medicalAppointmentViewModel)
        }
        if ((uiState).successScreenCallName == Destinations.HEALTH_EDIT_MEDICAL_APPOINTMENT_SCREEN_URL) {
            if ((uiState).successMethodName == "fetchUnavailableDates") ShowUpdateMedicalAppointmentScreen(navController, medicalAppointmentViewModel)
        }
    }
    if ((uiState).successType == "screenRunning") {
        if ((uiState).successScreenCallName == Destinations.HEALTH_CREATE_MEDICAL_APPOINTMENT_SCREEN_URL) {
            if ((uiState).successMethodName == "createMedicalAppointment") {
                ShowAlertDialogComponent("¿ Desea crear otra tarea ?",
                    onAlertDialogDismissRequest = { medicalAppointmentViewModel.fetchUnavailableDates((uiState).successScreenCallName) },
                    onAlertDialogButtonClick = { buttonValue ->
                        if (buttonValue == "No") navController.popBackStack(Destinations.HEALTH_MEDICAL_APPOINTMENT_SCREEN_URL, false)
                        if (buttonValue == "Si") medicalAppointmentViewModel.fetchUnavailableDates((uiState).successScreenCallName)
                    }
                )
            }
        }
        if ((uiState).successScreenCallName == Destinations.HEALTH_EDIT_MEDICAL_APPOINTMENT_SCREEN_URL) {
            if ((uiState).successMethodName == "putMedicalAppointment") navController.popBackStack(Destinations.HEALTH_MEDICAL_APPOINTMENT_SCREEN_URL, false)
            if ((uiState).successMethodName == "deleteMedicalAppointment") navController.popBackStack(Destinations.HEALTH_MEDICAL_APPOINTMENT_SCREEN_URL, false)
        }
        //if ((uiState as UiState.Success).successMethodName == "fetchAvailableDatesOnDayByDate") INDICAR QUE SE MUESTRAN LAS TAREAS PARA LA FECHA INDICADA.
    }
}
@Composable
fun ShowCinemaScreenErrorUiState(uiState: UiState.Error, medicalAppointmentViewModel: MedicalAppointmentViewModel) {
    ApiErrorSnackbar(message = (uiState).errorMessage, onApiErrorSnackBarButtonRetry = {
        if ((uiState).errorType == "throwableErrorType") {
            if ((uiState).errorScreenCallName == Destinations.HEALTH_MEDICAL_APPOINTMENT_SCREEN_URL) {
                if ((uiState).errorMethodName == "fetchAvailableMedicalAppointments") medicalAppointmentViewModel.fetchAvailableMedicalAppointments((uiState).errorScreenCallName)
            }
            if ((uiState).errorScreenCallName == Destinations.HEALTH_CREATE_MEDICAL_APPOINTMENT_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") medicalAppointmentViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "createMedicalAppointment") medicalAppointmentViewModel.createMedicalAppointment()
            }
            if ((uiState).errorScreenCallName == Destinations.HEALTH_EDIT_MEDICAL_APPOINTMENT_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") medicalAppointmentViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "putMedicalAppointment") medicalAppointmentViewModel.putMedicalAppointment()
                if ((uiState).errorMethodName == "deleteMedicalAppointment") medicalAppointmentViewModel.deleteMedicalAppointment()
            }
        }
        if ((uiState).errorType == "fetchDataErrorType") {
            if ((uiState).errorScreenCallName == Destinations.HEALTH_MEDICAL_APPOINTMENT_SCREEN_URL) {
                if ((uiState).errorMethodName == "fetchAvailableMedicalAppointments") medicalAppointmentViewModel.fetchAvailableMedicalAppointments((uiState).errorScreenCallName)
            }
            if ((uiState).errorScreenCallName == Destinations.HEALTH_CREATE_MEDICAL_APPOINTMENT_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") medicalAppointmentViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "createMedicalAppointment") medicalAppointmentViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
            }
            if ((uiState).errorScreenCallName == Destinations.HEALTH_EDIT_MEDICAL_APPOINTMENT_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") medicalAppointmentViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "putMedicalAppointment") medicalAppointmentViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "deleteMedicalAppointment") medicalAppointmentViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
            }
        }
    })
    //if ((uiState).errorMethodName == "fetchAvailableTasksDatesByDate") cinemaViewModel.fetchAvailableTasksDatesByDate("artCreateCinemaScreen")
}

/**
 *
 */
@Composable
fun ShowMedicalAppointmentScreen(navController: NavHostController, medicalAppointmentViewModel: MedicalAppointmentViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.bckmedicalappointmentwhite), contentDescription = null,
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
        )
        //.border(width = 2.dp, color = Color.Red)
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize().padding(10.dp)) {
            // Mostramos el titulo de la vista utilizando el componente correspondiente llamado 'ViewTitleComponent'.
            ViewTitleComponent(stringResource(R.string.medicalAppointmentScreenViewName), Color.Black)
            // Mostramos el busador de salas por nombre llamado 'TextFieldSearcherComponent'.
            ShowTextFieldSearcherComponent(
                onValueSearcherChange = { medicalAppointmentViewModel.fetchMedicalAppointmentToSearch(it) },
                onCreateButtonClick = { navController.navigate(Destinations.HEALTH_CREATE_MEDICAL_APPOINTMENT_SCREEN_URL) }
            )
            // Mostramos la lista en funcion de la variable 'showSearchedCinemas', la cual indica si hay que mostrar la lista de citas disponibles o la lista de las salas que busca el usuario.
            if (medicalAppointmentViewModel.showSearchedMedicalAppointments) medicalAppointmentViewModel.matchMedicalAppointmentsList.data?.let { ShowMedicalAppointmentList(it,
                onCardClick = { id ->
                    medicalAppointmentViewModel.fetchSelectedPaintingData(id);
                    navController.navigate(Destinations.HEALTH_EDIT_MEDICAL_APPOINTMENT_SCREEN_URL)
                })
            }
            else medicalAppointmentViewModel.availableMedicalAppointmentList.data?.let { ShowMedicalAppointmentList(it,
                onCardClick = { id ->
                    medicalAppointmentViewModel.fetchSelectedPaintingData(id);
                    navController.navigate(Destinations.HEALTH_EDIT_MEDICAL_APPOINTMENT_SCREEN_URL)
                })
            }
        }
    }
}
/**
 *
 */
@Composable
fun ShowMedicalAppointmentList(listToShow: List<MedicalAppointmentModel>, onCardClick: (String) -> Unit) {
    //.border(width = 5.dp, color = Color.Green)
    LazyColumn(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)) {
        items (listToShow.count()) { appointment -> ShowMedicalAppointmentData(listToShow, appointment, onCardClick = { id -> onCardClick(id) }) }
    }
}
/**
 *
 */
@Composable
fun ShowMedicalAppointmentData(listToShow: List<MedicalAppointmentModel>, appointment: Int, onCardClick: (String) -> Unit) {
    val outputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("es", "ES"))
    val formattedDate = LocalDateTime.parse(listToShow[appointment].fechaCitaMedica, DateTimeFormatter.ISO_DATE_TIME).format(outputFormatter)
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.Black
        ), elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier = Modifier.fillMaxWidth().padding(30.dp, 10.dp).clickable(onClick = { onCardClick(listToShow[appointment]._id) })
    ) {
        ShowCustomData("Prueba: ${listToShow[appointment].tipoPruebaCitaMedica}")
        ShowCustomData("Lugar: ${listToShow[appointment].lugarCitaMedica}")
        ShowCustomData("Cita: $formattedDate")
    }
}