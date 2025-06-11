package com.cronosdev.taskmanagerapp.ui.screens.health.medicalAppointment
//
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.cronosdev.taskmanagerapp.ui.state.ShowLoadingUiState
import com.cronosdev.taskmanagerapp.ui.state.UiState
import com.cronosdev.taskmanagerapp.ui.components.ViewTitleComponent
import com.cronosdev.taskmanagerapp.ui.components.outlinedComponents.buttons.ShowUpdateScreenButtons
import com.cronosdev.taskmanagerapp.ui.components.outlinedComponents.textFields.ShowOutlinedTextFieldUpdateDataCard
import com.cronosdev.taskmanagerapp.ui.components.timePickerComponent.ShowTimePickerComponentCard
import com.cronosdev.taskmanagerapp.ui.navigation.Destinations
import java.time.Instant
import java.time.ZoneOffset

/**
 *
 */
@Composable
fun UpdateMedicalAppointmentScreen(navController: NavHostController, medicalAppointmentViewModel: MedicalAppointmentViewModel) {
    // Variable que controla el estado de la vista/screen.
    val uiState by medicalAppointmentViewModel.editMedicalAppointmentScreenUiState.collectAsState()
    // Conjunto de acciones que se realizan al cargar la vista, en este caso obtener la lista de salas disponibles para reservar.
    LaunchedEffect(Unit) {
        medicalAppointmentViewModel.fetchUnavailableDates(Destinations.HEALTH_EDIT_MEDICAL_APPOINTMENT_SCREEN_URL)
        //cinemaViewModel.fetchAvailableDatesOnDayByDate("editCinemaScreen")
    }
    // Controlamos el estado de la vista.
    when (uiState) {
        is UiState.Loading -> ShowLoadingUiState()
        is UiState.Success -> ShowMedicalAppointmentScreenSuccessUiState(uiState as UiState.Success, navController, medicalAppointmentViewModel)
        is UiState.Error -> ShowCinemaScreenErrorUiState(uiState as UiState.Error, medicalAppointmentViewModel)
        else -> {}
    }
}

/**
 *
 */
@Composable
fun ShowUpdateMedicalAppointmentScreen(navController: NavHostController, medicalAppointmentViewModel: MedicalAppointmentViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.bckcinemablack), contentDescription = null,
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
        )
        Column(modifier = Modifier.fillMaxSize().padding(10.dp)/*.border(width = 2.dp, color = Color.Red)*/,
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
            // Mostramos el titulo de la vista utilizando el componente correspondiente llamado 'ViewTitleComponent'.
            ViewTitleComponent(stringResource(R.string.editMedicalAppointmentScreenViewName), Color(0xFFFDFEFE))
            //
            ShowEditMedicalAppointmentScreenCards(medicalAppointmentViewModel, Modifier.weight(20F))
            // Mostramos el composable que muestra los botones de volver, guardar, y eliminar.
            ShowUpdateScreenButtons(onButtonClick = { userAction ->
                if (userAction == "back") navController.popBackStack(Destinations.HEALTH_MEDICAL_APPOINTMENT_SCREEN_URL, false)
                if (userAction == "save") medicalAppointmentViewModel.putMedicalAppointment(
                    Destinations.HEALTH_EDIT_MEDICAL_APPOINTMENT_SCREEN_URL
                )
                if (userAction == "delete") medicalAppointmentViewModel.deleteMedicalAppointment(
                    Destinations.HEALTH_EDIT_MEDICAL_APPOINTMENT_SCREEN_URL
                )
            })
        }
    }
}
/**
 *
 */
@Composable
fun ShowEditMedicalAppointmentScreenCards(medicalAppointmentViewModel: MedicalAppointmentViewModel, modifier: Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.padding(15.dp, 10.dp)
        .verticalScroll(rememberScrollState())/*.border(width = 2.dp, color = Color.Yellow)*/) {
        // Mostramos el composable que recogera.
        ShowDatePickerComponentCard("update", medicalAppointmentViewModel, "Fecha de la Cita", onInitDateChange2 = {
            val dateTime = Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDateTime()
            medicalAppointmentViewModel.setFechaCitaMedica(dateTime.format(medicalAppointmentViewModel.dayTimeFormaterEEUU))
        }, medicalAppointmentViewModel.fechaCitaMedica)
        // Mostramos el composable que recogera
        ShowTimePickerComponentCard("update","Hora de la Cita", onInitHourChange = { medicalAppointmentViewModel.setHoraCitaMedica(it) }, medicalAppointmentViewModel.horaCitaMedica)
        // Mostramos el composable que recogera.
        ShowOutlinedTextFieldUpdateDataCard("update","Servicio", "", medicalAppointmentViewModel.servicioCitaMedica, onTextFieldValueChange = { medicalAppointmentViewModel.setServicioCitaMedica(it) })
        // Mostramos el composable que recogera.
        ShowOutlinedTextFieldUpdateDataCard("update","Prueba", "", medicalAppointmentViewModel.tipoPruebaCitaMedica, onTextFieldValueChange = { medicalAppointmentViewModel.setTipoPruebaCitaMedica(it) })
        // Mostramos el composable que recogera.
        ShowOutlinedTextFieldUpdateDataCard("update","Hospital", "", medicalAppointmentViewModel.lugarCitaMedica, onTextFieldValueChange = { medicalAppointmentViewModel.setLugarCitaMedica(it) })
        // Mostramos el composable que recogera.
        ShowOutlinedTextFieldUpdateDataCard("update","Notas", "", medicalAppointmentViewModel.notasCitaMedica, onTextFieldValueChange = { medicalAppointmentViewModel.setNotasCitaMedica(it) })
    }
}