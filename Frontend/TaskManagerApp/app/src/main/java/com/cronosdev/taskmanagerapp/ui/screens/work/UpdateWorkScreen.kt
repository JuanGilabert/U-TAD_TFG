package com.cronosdev.taskmanagerapp.ui.screens.work
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
import com.cronosdev.taskmanagerapp.ui.screens.art.cinema.CinemaViewModel
import java.time.Instant
import java.time.ZoneOffset

@Composable
fun UpdateWorkScreen(navController: NavHostController, workViewModel: WorkViewModel) {
    // Variable que controla el estado de la vista/screen.
    val uiState by workViewModel.editWorkScreenUiState.collectAsState()
    // Conjunto de acciones que se realizan al cargar la vista, en este caso obtener la lista de salas disponibles para reservar.
    LaunchedEffect(Unit) { workViewModel.fetchUnavailableDates(Destinations.EDIT_WORK_SCREEN_URL) }
    // Controlamos el estado de la vista.
    when (uiState) {
        is UiState.Loading -> ShowLoadingUiState()
        is UiState.Success -> ShowWorkScreenSuccessUiState(uiState as UiState.Success, navController, workViewModel)
        is UiState.Error -> ShowWorkScreenErrorUiState(uiState as UiState.Error, workViewModel)
        else -> {}
    }
}

/** Funcion composable utilizada para mostrar un cuadro de diálogo para editar o eliminar una reserva.
 *
 * **Parameters;
 * @param workViewModel ViewModel con la lógica de negocio.
 * @return No retorna un valor directo, ya que es un Composable.
 * @sample ShowUpdateWorkScreen()
 * @see CinemaViewModel
 * @since 1.0
 */
@Composable
fun ShowUpdateWorkScreen(navController: NavHostController, workViewModel: WorkViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.bckworkblack), contentDescription = null,
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
        )
        Column(modifier = Modifier.fillMaxSize().padding(10.dp)/*.border(width = 2.dp, color = Color.Red)*/,
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
            // Mostramos el titulo de la vista utilizando el componente correspondiente llamado 'ViewTitleComponent'.
            ViewTitleComponent(stringResource(R.string.editWorkScreenViewName), Color(0xFFFDFEFE))
            //
            ShowEditWorkScreenCards(workViewModel, Modifier.weight(20F))
            // Mostramos el composable que muestra los botones de volver, guardar, y eliminar.
            ShowUpdateScreenButtons(onButtonClick = { userAction ->
                if (userAction == "back") navController.popBackStack()
                if (userAction == "save") workViewModel.putWork(Destinations.EDIT_WORK_SCREEN_URL)
                if (userAction == "delete") workViewModel.deleteWork(Destinations.EDIT_WORK_SCREEN_URL)
            })
        }
    }
}
/**
 *
 */
@Composable
fun ShowEditWorkScreenCards(workViewModel: WorkViewModel, modifier: Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.padding(15.dp, 10.dp)
        .verticalScroll(rememberScrollState())/*.border(width = 2.dp, color = Color.Yellow)*/) {
        // Mostramos el nombre de la pellicula.
        ShowOutlinedTextFieldUpdateDataCard("update","Tarea", "", workViewModel.tituloTarea, onTextFieldValueChange = { workViewModel.setTituloTarea(it) })
        // Mostramos la descripcion de la pellicula
        ShowOutlinedTextFieldUpdateDataCard("update","Descripcion", "", workViewModel.descripcionTarea, onTextFieldValueChange = { workViewModel.setDescripcionTarea(it) })
        // Mostramos el composable que recogera la fecha.
        ShowDatePickerComponentCard("update",workViewModel, "Fecha de Inicio", onInitDateChange2 = {
            val dateTime = Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDateTime()
            workViewModel.setFechaInicioTarea(dateTime.format(workViewModel.dayTimeFormaterEEUU))
        }, workViewModel.fechaInicioTarea)
        // Mostramos el composable que recogera la hora.
        ShowTimePickerComponentCard("update","Hora de Inicio", onInitHourChange = { workViewModel.setHoraInicioTarea(it) }, workViewModel.horaInicioTarea)
        // Mostramos el composable que recogera la fecha.
        ShowDatePickerComponentCard("update",workViewModel, "Fecha de Entrega", onInitDateChange2 = {
            val dateTime = Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDateTime()
            workViewModel.setFechaEntregaTarea(dateTime.format(workViewModel.dayTimeFormaterEEUU))
        }, workViewModel.fechaEntregaTarea)
        // Mostramos el composable que recogera la hora.
        ShowTimePickerComponentCard("update","Hora de Entrega", onInitHourChange = { workViewModel.setHoraEntregaTarea(it) }, workViewModel.horaEntregaTarea)
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard("update","Organizador", "", workViewModel.organizadorTarea, onTextFieldValueChange = { workViewModel.setOrganizadorTarea(it) })
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard("update","Prioridad", "", workViewModel.prioridadTarea, onTextFieldValueChange = { workViewModel.setPrioridadTarea(it) })
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard("update","Notas", "", workViewModel.notasTarea, onTextFieldValueChange = { workViewModel.setNotasTarea(it) })
    }
}