package com.cronosdev.taskmanagerapp.ui.screens.meeting
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
import com.cronosdev.taskmanagerapp.ui.components.outlinedComponents.textFields.ShowOutlinedTextFieldUpdateMultipleDataCard
import com.cronosdev.taskmanagerapp.ui.components.timePickerComponent.ShowTimePickerComponentCard
import com.cronosdev.taskmanagerapp.ui.navigation.Destinations
import java.time.Instant
import java.time.ZoneOffset

/**
 *
 */
@Composable
fun UpdateMeetingScreen(navController: NavHostController, meetingViewModel: MeetingViewModel) {
    // Variable que controla el estado de la vista/screen.
    val uiState by meetingViewModel.updateMeetingScreenUiState.collectAsState()
    // Conjunto de acciones que se realizan al cargar la vista, en este caso obtener la lista de salas disponibles para reservar.
    LaunchedEffect(Unit) { meetingViewModel.fetchUnavailableDates(Destinations.EDIT_MEETING_SCREEN_URL) }
    // Controlamos el estado de la vista.
    when (uiState) {
        is UiState.Loading -> ShowLoadingUiState()
        is UiState.Success -> ShowMeetingScreenSuccessUiState(uiState as UiState.Success, navController, meetingViewModel)
        is UiState.Error -> ShowMeetingScreenErrorUiState(uiState as UiState.Error, meetingViewModel)
        else -> {}
    }
}

/**
 *
 */
@Composable
fun ShowUpdateMeetingScreen(navController: NavHostController, meetingViewModel: MeetingViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.bckmeetingblack), contentDescription = null,
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
        )
        Column(modifier = Modifier.fillMaxSize().padding(10.dp)/*.border(width = 2.dp, color = Color.Red)*/,
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
            // Mostramos el titulo de la vista utilizando el componente correspondiente llamado 'ViewTitleComponent'.
            ViewTitleComponent(stringResource(R.string.editMeetingScreenViewName), Color(0xFFFDFEFE))
            //
            ShowEditMeetingScreenCards(meetingViewModel, Modifier.weight(20F))
            // Mostramos el composable que muestra los botones de volver, guardar, y eliminar.
            ShowUpdateScreenButtons(onButtonClick = { userAction ->
                if (userAction == "back") navController.popBackStack(Destinations.MEETING_SCREEN_URL, false)
                if (userAction == "save") meetingViewModel.putMeeting(Destinations.EDIT_MEETING_SCREEN_URL)
                if (userAction == "delete") meetingViewModel.deleteMeeting(Destinations.EDIT_MEETING_SCREEN_URL)
            })
        }
    }
}
/**
 *
 */
@Composable
fun ShowEditMeetingScreenCards(meetingViewModel: MeetingViewModel, modifier: Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.padding(15.dp, 10.dp)
        .verticalScroll(rememberScrollState())/*.border(width = 2.dp, color = Color.Yellow)*/) {
        // Mostramos el composable que recogera.
        ShowOutlinedTextFieldUpdateDataCard("update","Reunion", "", meetingViewModel.tituloReunion, onTextFieldValueChange = { meetingViewModel.setTituloReunion(it) })
        // Mostramos el composable que recogera.
        ShowOutlinedTextFieldUpdateDataCard("update","Tipo", "", meetingViewModel.tipoReunion, onTextFieldValueChange = { meetingViewModel.setTipoReunion(it) })
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateMultipleDataCard("update","Asistentes", "", meetingViewModel.asistentesReunion, onTextFieldValueChange = { meetingViewModel.setAsistentesReunion(it) })
        // Mostramos el composable que recogera.
        ShowDatePickerComponentCard("update",meetingViewModel, "Fecha de Inicio", onInitDateChange2 = {
            val dateTime = Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDateTime()
            meetingViewModel.setFechaInicioReunion(dateTime.format(meetingViewModel.dayTimeFormaterEEUU))
        }, meetingViewModel.fechaInicioReunion)
        // Mostramos el composable que recogera.
        ShowTimePickerComponentCard("update","Hora de Inicio", onInitHourChange = { meetingViewModel.setHoraInicioReunion(it) }, meetingViewModel.horaInicioReunion)
        // Mostramos el composable que recogera.
        ShowDatePickerComponentCard("update",meetingViewModel, "Fecha de Fin", onInitDateChange2 = {
            val dateTime = Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDateTime()
            meetingViewModel.setFechaFinReunion(dateTime.format(meetingViewModel.dayTimeFormaterEEUU))
        }, meetingViewModel.fechaFinReunion)
        // Mostramos el composable que recogera.
        ShowTimePickerComponentCard("update","Hora de Fin", onInitHourChange = { meetingViewModel.setHoraFinReunion(it) }, meetingViewModel.horaFinReunion)
        // Mostramos el composable que recogera.
        ShowOutlinedTextFieldUpdateDataCard("update","Direccion", "", meetingViewModel.lugarReunion, onTextFieldValueChange = { meetingViewModel.setLugarReunion(it) })
        // Mostramos el composable que recogera.
        ShowOutlinedTextFieldUpdateDataCard("update","Organizador", "", meetingViewModel.organizadorReunion, onTextFieldValueChange = { meetingViewModel.setOrganizadorReunion(it) })
        // Mostramos el composable que recogera.
        ShowOutlinedTextFieldUpdateDataCard("update","Notas", "", meetingViewModel.notasReunion, onTextFieldValueChange = { meetingViewModel.setNotasReunion(it) })
    }
}