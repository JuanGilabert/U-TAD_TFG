package com.cronosdev.taskmanagerapp.ui.screens.work
//
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
fun CreateWorkScreen(navController: NavHostController, workViewModel: WorkViewModel) {
    // Obtenemos el estado de la vista.
    val uiState by workViewModel.createWorkScreenUiState.collectAsState()
    // Conjunto de acciones que se realizan al cargar la vista, en este caso obtener la lista de salas disponibles para reservar.
    LaunchedEffect(Unit) { workViewModel.fetchUnavailableDates(Destinations.CREATE_WORK_SCREEN_URL) }
    // Controlamos el estado de la vista.
    when (uiState) {
        is UiState.Loading -> ShowLoadingUiState()
        is UiState.Success -> ShowWorkScreenSuccessUiState(uiState as UiState.Success, navController, workViewModel)
        is UiState.Error -> ShowWorkScreenErrorUiState(uiState as UiState.Error, workViewModel)
        else -> {}
    }
}
/**
 *
 */
@Composable
fun ShowCreateWorkScreen(navController: NavHostController, workViewModel: WorkViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.bckworkblack), contentDescription = null,
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
        )
        Column(modifier = Modifier.fillMaxSize().padding(10.dp)/*.border(width = 2.dp, color = Color.Red)*/,
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
            // Mostramos el titulo de la vista utilizando el componente correspondiente llamado 'ViewTitleComponent'.
            ViewTitleComponent(stringResource(R.string.createWorkScreenViewName), Color(0xFFFDFEFE))
            //
            ShowCreateWorkScreenCards(workViewModel, Modifier.weight(20F))
            // Mostramos el composable que muestra el boton de cancelar/volver y el boton encargado de guardar.
            ShowCreateScreenButtons(onButtonClick = { userAction ->
                if (userAction == "back") navController.popBackStack()
                if (userAction == "create") workViewModel.createWork(Destinations.CREATE_WORK_SCREEN_URL)
            })
        }
    }
}

/**
 *
 */
@Composable
fun ShowCreateWorkScreenCards(workViewModel: WorkViewModel, modifier: Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.padding(15.dp, 10.dp).verticalScroll(rememberScrollState())) {
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard("create","Tarea", "Escriba el titulo ...", "", onTextFieldValueChange = { workViewModel.setTituloTarea(it) })
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard("create","Descripcion", "Escriba la descripcion ...", "", onTextFieldValueChange = { workViewModel.setDescripcionTarea(it) })
        // Mostramos el composable que recogera
        ShowDatePickerComponentCard("create",workViewModel, "Fecha de Inicio", onInitDateChange2 = {
            val dateTime = Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDateTime()
            workViewModel.setFechaInicioTarea(dateTime.format(workViewModel.dayTimeFormaterEEUU))
        }, workViewModel.fechaInicioTarea)
        // Mostramos el composable que recogera la hora de inicio.
        ShowTimePickerComponentCard("create","Hora de Inicio", onInitHourChange = { workViewModel.setHoraInicioTarea(it) }, workViewModel.horaInicioTarea)
        // Mostramos el composable que recogera
        ShowDatePickerComponentCard("create",workViewModel, "Fecha de Entrega", onInitDateChange2 = {
            val dateTime = Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDateTime()
            workViewModel.setFechaEntregaTarea(dateTime.format(workViewModel.dayTimeFormaterEEUU))
        }, workViewModel.fechaEntregaTarea)
        // Mostramos el composable que recogera la hora de inicio.
        ShowTimePickerComponentCard("create","Hora de Entrega", onInitHourChange = { workViewModel.setHoraEntregaTarea(it) }, workViewModel.horaEntregaTarea)
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard("create","Organizador", "Escriba el organizador ...", "", onTextFieldValueChange = { workViewModel.setOrganizadorTarea(it) })
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard("create","Prioridad", "Escriba la prioridad ...", "", onTextFieldValueChange = { workViewModel.setPrioridadTarea(it) })
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard("create","Notas", "Escriba las notas ...", "", onTextFieldValueChange = { workViewModel.setNotasTarea(it) })
    }
}

/**
 *
 */
@Composable
fun ShowDatePickerComponentCard(outlinedTextFieldColorTypeToShow: String, workViewModel: WorkViewModel, textFieldLabelToShow: String, onInitDateChange2:(Long)-> Unit, selectedDateTextToShow: String) {
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
        workViewModel.unavailableWorkDatesList.data?.dates?.let {
            DatePickerComponent(it, onInitDateChange= { startDate ->
                selectedDateText = workViewModel.dayTimeFormaterES.format(Date(startDate))
                onInitDateChange2(startDate)
                showDatePickerComponent = false
                // Cargamos las fechas del dia seleccionado por el usuario.
                //cinemaViewModel.fetchAvailableTasksDatesByDate(screenCallName)
            }, onDatePickerButtonClick={ value -> showDatePickerComponent = value }/*, cinemaViewModel.availableCinemaTasksDatesByDateList*/
            )
        }
    }
}