package com.cronosdev.taskmanagerapp.ui.screens.meeting

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.cronosdev.taskmanagerapp.ui.components.outlinedComponents.buttons.ShowCreateScreenButtons
import com.cronosdev.taskmanagerapp.ui.components.outlinedComponents.textFields.ShowOutlinedTextFieldUpdateDataCard
import com.cronosdev.taskmanagerapp.ui.components.outlinedComponents.textFields.ShowOutlinedTextFieldUpdateMultipleDataCard
import com.cronosdev.taskmanagerapp.ui.components.ViewTitleComponent
import com.cronosdev.taskmanagerapp.ui.components.datePickerComponent.DatePickerComponent
import com.cronosdev.taskmanagerapp.ui.components.outlinedComponents.textFields.getOutlinedTextFieldColor
import com.cronosdev.taskmanagerapp.ui.components.timePickerComponent.ShowTimePickerComponentCard
import com.cronosdev.taskmanagerapp.ui.navigation.Destinations
import java.time.Instant
import java.time.ZoneOffset
import java.util.Date

/**
 *
 */
@Composable
fun CreateMeetingScreen(navController: NavHostController, meetingViewModel: MeetingViewModel) {
    // Obtenemos el estado de la vista.
    val uiState by meetingViewModel.createMeetingScreenUiState.collectAsState()
    // Conjunto de acciones que se realizan al cargar la vista, en este caso obtener la lista de salas disponibles para reservar.
    LaunchedEffect(Unit) { meetingViewModel.fetchUnavailableDates(Destinations.CREATE_MEETING_SCREEN_URL) }
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
fun ShowCreateMeetingScreen(navController: NavHostController, meetingViewModel: MeetingViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.bckmeetingblue), contentDescription = null,
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
        )
        Column(modifier = Modifier.fillMaxSize().padding(10.dp)/*.border(width = 2.dp, color = Color.Red)*/,
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
            // Mostramos el titulo de la vista utilizando el componente correspondiente llamado 'ViewTitleComponent'.
            ViewTitleComponent(stringResource(R.string.createMeetingScreenViewName), Color(0xFFFDFEFE))
            //
            ShowCreateMeetingScreenCards(meetingViewModel, Modifier.weight(20F))
            // Mostramos el composable que muestra el boton de cancelar/volver y el boton encargado de guardar la reserva y redirigir al usuario a la vista 'artCinema' en calquiera de ambos casos.
            ShowCreateScreenButtons(onButtonClick = { userAction ->
                if (userAction == "back") navController.popBackStack()
                if (userAction == "create") meetingViewModel.createMeeting(Destinations.CREATE_MEETING_SCREEN_URL)
            })
        }
    }
}
/**
 *
 */
@Composable
fun ShowCreateMeetingScreenCards(meetingViewModel: MeetingViewModel, modifier: Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.padding(15.dp, 10.dp).verticalScroll(rememberScrollState())) {
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard("create","Reunion", "Escriba el titulo de la reunion ...", "", onTextFieldValueChange = { meetingViewModel.setTituloReunion(it) })
        /// Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard("create","Tipo", "Escriba el tipo ...", "", onTextFieldValueChange = { meetingViewModel.setTipoReunion(it) })
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateMultipleDataCard("create","Asistentes", "Indique los asistentes separados por  ', ' ", onTextFieldValueChange = { meetingViewModel.setAsistentesReunion(it) })
        // Mostramos el composable que recogera la fecha.
        ShowDatePickerComponentCard("create",meetingViewModel, "Fecha de Inicio", onInitDateChange2 = {
            val dateTime = Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDateTime()
            meetingViewModel.setFechaInicioReunion(dateTime.format(meetingViewModel.dayTimeFormaterEEUU))
        }, meetingViewModel.fechaInicioReunion)
        // Mostramos el composable que recogera la hora.
        ShowTimePickerComponentCard("create","Hora de Inicio", onInitHourChange = { meetingViewModel.setHoraInicioReunion(it) }, meetingViewModel.fechaInicioReunion)
        // Mostramos el composable que recogera la fecha.
        ShowDatePickerComponentCard("create",meetingViewModel, "Fecha de Fin", onInitDateChange2 = {
            val dateTime = Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDateTime()
            meetingViewModel.setFechaFinReunion(dateTime.format(meetingViewModel.dayTimeFormaterEEUU))
        }, meetingViewModel.fechaFinReunion)
        // Mostramos el composable que recogera la hora.
        ShowTimePickerComponentCard("create","Hora de Fin", onInitHourChange = { meetingViewModel.setHoraFinReunion(it) }, meetingViewModel.fechaFinReunion)
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard("create","Direccion", "Escriba el lugar ...", "", onTextFieldValueChange = { meetingViewModel.setLugarReunion(it) })
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard("create","Organizador", "Escriba el organizador ...", "", onTextFieldValueChange = { meetingViewModel.setOrganizadorReunion(it) })
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard("create","Notas", "Escriba las notas ...", "", onTextFieldValueChange = { meetingViewModel.setNotasReunion(it) })
    }
}
/**
 *
 */
@Composable
fun ShowDatePickerComponentCard(outlinedTextFieldColorTypeToShow: String, meetingViewModel: MeetingViewModel, textFieldLabelToShow: String, onInitDateChange2:(Long)-> Unit, selectedDateTextToShow: String) {
    var showDatePickerComponent by remember { mutableStateOf(false) }
    var selectedDateText by remember { mutableStateOf(selectedDateTextToShow) }
    //
    OutlinedTextField(value = selectedDateText, onValueChange = { selectedDateText = it },
        readOnly = true, label = { Text(text = textFieldLabelToShow) }, //placeholder = { Text(text = newHourToSHow) },
        shape = RoundedCornerShape(12.dp), singleLine = true,
        colors = getOutlinedTextFieldColor(outlinedTextFieldColorTypeToShow),
        trailingIcon = {
            IconButton(onClick = { showDatePickerComponent = true }) {
                Icon(imageVector = Icons.Filled.DateRange, contentDescription = "Seleccionar la hora", tint = Color.White)
            }
        },
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)
    )
    // Dialog para mostrar el DatePciker comun de la aplicacion llamado 'DatePickerComponent'.
    if (showDatePickerComponent) {
        meetingViewModel.unavailableMeetingDatesList.data?.dates?.let {
            DatePickerComponent(it, onInitDateChange= { startDate ->
                selectedDateText = meetingViewModel.dayTimeFormaterES.format(Date(startDate))
                onInitDateChange2(startDate)
                showDatePickerComponent = false
                // Cargamos las fechas del dia seleccionado por el usuario.
                //meetingViewModel.fetchAvailableTasksOnDayByDate(screenCallName)
            }, onDatePickerButtonClick={ value -> showDatePickerComponent = value }/*, meetingViewModel.availableCinemaDatesOnDayByDateList*/
            )
        }
    }
}