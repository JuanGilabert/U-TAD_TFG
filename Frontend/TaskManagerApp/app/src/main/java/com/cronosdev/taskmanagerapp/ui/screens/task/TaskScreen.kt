package com.cronosdev.taskmanagerapp.ui.screens.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cronosdev.taskmanagerapp.R
import com.cronosdev.taskmanagerapp.ui.state.ShowLoadingUiState
import com.cronosdev.taskmanagerapp.ui.state.UiState
import com.cronosdev.taskmanagerapp.ui.components.ApiErrorSnackbar
import com.cronosdev.taskmanagerapp.ui.components.outlinedComponents.buttons.ShowCustomData
import com.cronosdev.taskmanagerapp.ui.components.ViewTitleComponent
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 *
 */
@Composable
fun TaskScreen(taskViewModel: TaskViewModel) {
    var showTasksOnScreen by remember { mutableIntStateOf(0) }
    // Variable que controla el estado de la vista/screen.
    val uiState by taskViewModel.allTaskScreenUiStateCinema.collectAsState()
    // Conjunto de acciones que se realizan al cargar la vista, en este caso obtener la lista de salas disponibles para reservar.
    LaunchedEffect(Unit) { fetchAllTasks(taskViewModel) }
    when (uiState) {
        is UiState.Loading -> ShowLoadingUiState()
        is UiState.Success -> {
            ShowTasksScreen(taskViewModel)
            /*if ((uiState as UiState.Success).successType == "screenInit") {
                if (showTasksOnScreen == 10) ShowTasksScreen(taskViewModel)
            }*/
            //if ((uiState as UiState.Success).successType == "screenRunning") {}
        }
        is UiState.Error -> ApiErrorSnackbar( message = (uiState as UiState.Error).errorMessage, onApiErrorSnackBarButtonRetry = {
            if ((uiState as UiState.Error).errorMethodName == "fetchAvailableCinemaTasks") taskViewModel.fetchAvailableCinemaTasks("allTaskScreen");
            if ((uiState as UiState.Error).errorMethodName == "fetchAvailableMusicTasks") taskViewModel.fetchAvailableMusicTasks("allTaskScreen");
            if ((uiState as UiState.Error).errorMethodName == "fetchAvailablePaintingTasks") taskViewModel.fetchAvailablePaintingTasks("allTaskScreen");
            if ((uiState as UiState.Error).errorMethodName == "fetchAvailableMedicamentTasks") taskViewModel.fetchAvailableMedicamentTasks("allTaskScreen");
            if ((uiState as UiState.Error).errorMethodName == "fetchAvailableMedicalAppointmentTasks") taskViewModel.fetchAvailableMedicalAppointmentTasks("allTaskScreen");
            if ((uiState as UiState.Error).errorMethodName == "fetchAvailableFoodTasks") taskViewModel.fetchAvailableFoodTasks("allTaskScreen");
            if ((uiState as UiState.Error).errorMethodName == "fetchAvailableMeetingTasks") taskViewModel.fetchAvailableMeetingTasks("allTaskScreen");
            if ((uiState as UiState.Error).errorMethodName == "fetchAvailableTravelTasks") taskViewModel.fetchAvailableTravelTasks("allTaskScreen");
            if ((uiState as UiState.Error).errorMethodName == "fetchAvailableSportTasks") taskViewModel.fetchAvailableSportTasks("allTaskScreen");
            if ((uiState as UiState.Error).errorMethodName == "fetchAvailableWorkTasks") taskViewModel.fetchAvailableWorkTasks("allTaskScreen");
        } )
        else -> {}
    }
}
/**
 *
 */
fun fetchAllTasks(taskViewModel: TaskViewModel) {
    taskViewModel.fetchAvailableCinemaTasks("allTaskScreen");
    taskViewModel.fetchAvailableMusicTasks("allTaskScreen");
    taskViewModel.fetchAvailablePaintingTasks("allTaskScreen");
    taskViewModel.fetchAvailableMedicamentTasks("allTaskScreen");
    taskViewModel.fetchAvailableMedicalAppointmentTasks("allTaskScreen");
    taskViewModel.fetchAvailableFoodTasks("allTaskScreen");
    taskViewModel.fetchAvailableMeetingTasks("allTaskScreen");
    taskViewModel.fetchAvailableTravelTasks("allTaskScreen");
    taskViewModel.fetchAvailableSportTasks("allTaskScreen");
    taskViewModel.fetchAvailableWorkTasks("allTaskScreen");
}
//
@Composable
fun ShowTasksScreen(taskViewModel: TaskViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Mostramos el titulo de la vista utilizando el componente correspondiente llamado 'ViewTitleComponent'.
        ViewTitleComponent(stringResource(R.string.taskScreenViewName), Color.Black)
        // Cinema
        ShowCinemaTasks(taskViewModel)
        // Music
        ShowMusicTasks(taskViewModel)
        // Painting
        ShowPaintingTasks(taskViewModel)
        // Medicament
        ShowMedicamentTasks(taskViewModel)
        // Medical Appointment
        ShowMedicalAppointmentTasks(taskViewModel)
        // Food
        ShowFoodTasks(taskViewModel)
        // Meeting
        ShowMeetingTasks(taskViewModel)
        // Travel
        ShowTravelTasks(taskViewModel)
        // Sport
        ShowSportTasks(taskViewModel)
        // Work
        ShowWorkTasks(taskViewModel)
    }
}
/**
 *
 */
@Composable
fun ShowCinemaTasks(taskViewModel: TaskViewModel) {
    taskViewModel.availableCinemasList.data?.let {
        LazyColumn(modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 25.dp, vertical = 20.dp)) {
            items (it.count()) { cinema ->
                Card(colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 10.dp)//.border(5.dp, Color.Yellow)
                ) {
                    val formattedDate = LocalDateTime.parse(it[cinema].fechaInicioPelicula, DateTimeFormatter.ISO_DATE_TIME).format(taskViewModel.dayTimeOutputFormaterES)
                    ShowCustomData("Nombre: ${it[cinema].nombrePelicula}")
                    ShowCustomData("Fecha: $formattedDate")
                    ShowCustomData("Cine: ${it[cinema].lugarPelicula}")
                }
            }
        }
    }
}
/**
 *
 */
@Composable
fun ShowMusicTasks(taskViewModel: TaskViewModel) {
    taskViewModel.availableMusicsList.data?.let {
        LazyColumn(modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 25.dp, vertical = 20.dp)) {
            items (it.count()) { music ->
                Card(colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 10.dp)//.border(5.dp, Color.Yellow)
                ) {
                    val formattedDate = LocalDateTime.parse(it[music].fechaInicioEvento, DateTimeFormatter.ISO_DATE_TIME).format(taskViewModel.dayTimeOutputFormaterES)
                    ShowCustomData("Nombre: ${it[music].nombreEvento}")
                    ShowCustomData("Dirección: ${it[music].lugarEvento}")
                    ShowCustomData("Fecha: $formattedDate")
                }
            }
        }
    }
}
/**
 *
 */
@Composable
fun ShowPaintingTasks(taskViewModel: TaskViewModel) {
    taskViewModel.availablePaintingList.data?.let {
        LazyColumn(modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 25.dp, vertical = 20.dp)) {
            items (it.count()) { painting ->
                Card(colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 10.dp)//.border(5.dp, Color.Yellow)
                ) {
                    val formattedDate = LocalDateTime.parse(it[painting].fechaInicioExposicionArte, DateTimeFormatter.ISO_DATE_TIME).format(taskViewModel.dayTimeOutputFormaterES)
                    ShowCustomData("Exposicion: ${it[painting].nombreExposicionArte}")
                    ShowCustomData("Direccion: ${it[painting].lugarExposicionArte}")
                    ShowCustomData("Fecha: $formattedDate")
                }
            }
        }
    }
}
/**
 *
 */
@Composable
fun ShowMedicamentTasks(taskViewModel: TaskViewModel) {
    taskViewModel.availableMedicamentsList.data?.let {
        LazyColumn(modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 25.dp, vertical = 20.dp)) {
            items (it.count()) { medicament ->
                Card(colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 10.dp)//.border(5.dp, Color.Yellow)
                ) {
                    val formattedDate = LocalDateTime.parse(it[medicament].fechaCaducidadMedicamento, DateTimeFormatter.ISO_DATE_TIME).format(taskViewModel.dayTimeOutputFormaterES)
                    ShowCustomData("Medicamento: ${it[medicament].nombreMedicamento}")
                    ShowCustomData("Cantidad: ${it[medicament].cantidadTotalCajaMedicamento}")
                    ShowCustomData("Fecha: $formattedDate")
                }
            }
        }
    }
}
/**
 *
 */
@Composable
fun ShowMedicalAppointmentTasks(taskViewModel: TaskViewModel) {
    taskViewModel.availableMedicalAppointmentsList.data?.let {
        LazyColumn(modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 25.dp, vertical = 20.dp)) {
            items (it.count()) { medicalAppointment ->
                Card(colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 10.dp)//.border(5.dp, Color.Yellow)
                ) {
                    val formattedDate = LocalDateTime.parse(it[medicalAppointment].fechaCitaMedica, DateTimeFormatter.ISO_DATE_TIME).format(taskViewModel.dayTimeOutputFormaterES)
                    ShowCustomData("Prueba: ${it[medicalAppointment].tipoPruebaCitaMedica}")
                    ShowCustomData("Lugar: ${it[medicalAppointment].lugarCitaMedica}")
                    ShowCustomData("Cita: $formattedDate}")
                }
            }
        }
    }
}
/**
 *
 */
@Composable
fun ShowFoodTasks(taskViewModel: TaskViewModel) {
    taskViewModel.availableFoodsList.data?.let {
        LazyColumn(modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 25.dp, vertical = 20.dp)) {
            items (it.count()) { food ->
                Card(colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 10.dp)//.border(5.dp, Color.Yellow)
                ) {
                    val formattedDate = LocalDateTime.parse(it[food].fechaReserva, DateTimeFormatter.ISO_DATE_TIME).format(taskViewModel.dayTimeOutputFormaterES)
                    ShowCustomData("Restaurante: ${it[food].nombreRestaurante}")
                    ShowCustomData("Dirección: ${it[food].direccionRestaurante}")
                    ShowCustomData("Reserva: $formattedDate")
                }
            }
        }
    }
}
/**
 *
 */
@Composable
fun ShowMeetingTasks(taskViewModel: TaskViewModel) {
    taskViewModel.availableMeetingsList.data?.let {
        LazyColumn(modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 25.dp, vertical = 20.dp)) {
            items (it.count()) { meeting ->
                Card(colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 10.dp)//.border(5.dp, Color.Yellow)
                ) {
                    val formattedDate = LocalDateTime.parse(it[meeting].fechaInicioReunion, DateTimeFormatter.ISO_DATE_TIME).format(taskViewModel.dayTimeOutputFormaterES)
                    ShowCustomData("Reunion: ${it[meeting].tituloReunion}")
                    ShowCustomData("Lugar: ${it[meeting].lugarReunion}")
                    ShowCustomData("Fecha: $formattedDate")
                }
            }
        }
    }
}
/**
 *
 */
@Composable
fun ShowTravelTasks(taskViewModel: TaskViewModel) {
    taskViewModel.availableTravelsList.data?.let {
        LazyColumn(modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 25.dp, vertical = 20.dp)) {
            items (it.count()) { travel ->
                Card(colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 10.dp)//.border(5.dp, Color.Yellow)
                ) {
                    val formattedDepartureDate = LocalDateTime.parse(it[travel].fechaSalidaViaje, DateTimeFormatter.ISO_DATE_TIME).format(taskViewModel.dayTimeOutputFormaterES)
                    val formattedArrivalDate = LocalDateTime.parse(it[travel].fechaRegresoViaje, DateTimeFormatter.ISO_DATE_TIME).format(taskViewModel.dayTimeOutputFormaterES)
                    ShowCustomData("Destino: ${it[travel].nombreDestinoViaje}")
                    ShowCustomData("Salida: $formattedDepartureDate")
                    ShowCustomData("Regreso: $formattedArrivalDate")
                }
            }
        }
    }
}
/**
 *
 */
@Composable
fun ShowSportTasks(taskViewModel: TaskViewModel) {
    taskViewModel.availableSportsList.data?.let {
        LazyColumn(modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 25.dp, vertical = 20.dp)) {
            items (it.count()) { sport ->
                Card(colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 10.dp)//.border(5.dp, Color.Yellow)
                ) {
                    val formattedDate = LocalDateTime.parse(it[sport].fechaInicioActividad, DateTimeFormatter.ISO_DATE_TIME).format(taskViewModel.dayTimeOutputFormaterES)
                    ShowCustomData("Actividad: ${it[sport].nombreActividad}")
                    ShowCustomData("Tiempo: ${it[sport].duracionActividadMinutos}")
                    ShowCustomData("Fecha: $formattedDate")
                }
            }
        }
    }
}
/**
 *
 */
@Composable
fun ShowWorkTasks(taskViewModel: TaskViewModel) {
    taskViewModel.availableWorksList.data?.let {
        LazyColumn(modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 25.dp, vertical = 20.dp)) {
            items (it.count()) { work ->
                Card(colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 10.dp)//.border(5.dp, Color.Yellow)
                ) {
                    val formattedInitDate = LocalDateTime.parse(it[work].fechaInicioTarea, DateTimeFormatter.ISO_DATE_TIME).format(taskViewModel.dayTimeOutputFormaterES)
                    val formattedFinishDate = LocalDateTime.parse(it[work].fechaEntregaTarea, DateTimeFormatter.ISO_DATE_TIME).format(taskViewModel.dayTimeOutputFormaterES)
                    ShowCustomData("Tarea: ${it[work].tituloTarea}")
                    ShowCustomData("Inicio: $formattedInitDate")
                    ShowCustomData("Entrega: $formattedFinishDate")
                }
            }
        }
    }
}