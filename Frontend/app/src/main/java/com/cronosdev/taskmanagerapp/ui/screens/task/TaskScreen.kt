package com.cronosdev.taskmanagerapp.ui.screens.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.cronosdev.taskmanagerapp.data.model.art.CinemaModel
import com.cronosdev.taskmanagerapp.data.model.art.MusicModel
import com.cronosdev.taskmanagerapp.data.model.art.PaintingModel
import com.cronosdev.taskmanagerapp.data.model.food.FoodModel
import com.cronosdev.taskmanagerapp.data.model.health.MedicalAppointmentModel
import com.cronosdev.taskmanagerapp.data.model.health.MedicamentModel
import com.cronosdev.taskmanagerapp.data.model.meeting.MeetingModel
import com.cronosdev.taskmanagerapp.data.model.sport.SportModel
import com.cronosdev.taskmanagerapp.data.model.travel.TravelModel
import com.cronosdev.taskmanagerapp.data.model.work.WorkModel
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
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            ViewTitleComponent(stringResource(R.string.taskScreenViewName), Color.Black)
        }
        taskViewModel.availableCinemasList.data?.let {
            items(it.count()) { cinema ->
                ShowCinemaTaskItem(it, cinema, taskViewModel)
            }
        }
        taskViewModel.availableMusicsList.data?.let {
            items(it.count()) { cinema ->
                ShowMusicTaskItem(it, cinema, taskViewModel)
            }
        }
        taskViewModel.availablePaintingsList.data?.let {
            items(it.count()) { cinema ->
                ShowPaintingTaskItem(it, cinema, taskViewModel)
            }
        }
        taskViewModel.availableMedicamentsList.data?.let {
            items(it.count()) { cinema ->
                ShowMedicamentTaskItem(it, cinema, taskViewModel)
            }
        }
        taskViewModel.availableMedicalAppointmentsList.data?.let {
            items(it.count()) { cinema ->
                ShowMedicalAppointmentTaskItem(it, cinema, taskViewModel)
            }
        }
        taskViewModel.availableFoodsList.data?.let {
            items(it.count()) { cinema ->
                ShowFoodTaskItem(it, cinema, taskViewModel)
            }
        }
        taskViewModel.availableMeetingsList.data?.let {
            items(it.count()) { cinema ->
                ShowMeetingTaskItem(it, cinema, taskViewModel)
            }
        }
        taskViewModel.availableTravelsList.data?.let {
            items(it.count()) { cinema ->
                ShowTravelTaskItem(it, cinema, taskViewModel)
            }
        }
        taskViewModel.availableSportsList.data?.let {
            items(it.count()) { cinema ->
                ShowSportTaskItem(it, cinema, taskViewModel)
            }
        }
        taskViewModel.availableWorksList.data?.let {
            items(it.count()) { cinema ->
                ShowWorkTaskItem(it, cinema, taskViewModel)
            }
        }
    }
}
/**
 *
 */
@Composable
fun ShowCinemaTaskItem(it: List<CinemaModel>, cinema: Int, taskViewModel: TaskViewModel) {
    val formattedDate = LocalDateTime.parse(it[cinema].fechaInicioPelicula, DateTimeFormatter.ISO_DATE_TIME).format(taskViewModel.dayTimeOutputFormaterES)
    Card(colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 10.dp)//.border(5.dp, Color.Yellow)
    ) {
        ShowCustomData("Nombre: ${it[cinema].nombrePelicula}")
        ShowCustomData("Fecha: $formattedDate")
        ShowCustomData("Cine: ${it[cinema].lugarPelicula}")
    }
}
/**
 *
 */
@Composable
fun ShowMusicTaskItem(it: List<MusicModel>, music: Int, taskViewModel: TaskViewModel) {
    val formattedDate = LocalDateTime.parse(it[music].fechaInicioEvento, DateTimeFormatter.ISO_DATE_TIME).format(taskViewModel.dayTimeOutputFormaterES)
    Card(colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 10.dp)//.border(5.dp, Color.Yellow)
    ) {
        ShowCustomData("Nombre: ${it[music].nombreEvento}")
        ShowCustomData("Dirección: ${it[music].lugarEvento}")
        ShowCustomData("Fecha: $formattedDate")
    }
}
/**
 *
 */
@Composable
fun ShowPaintingTaskItem(it: List<PaintingModel>, painting: Int, taskViewModel: TaskViewModel) {
    val formattedDate = LocalDateTime.parse(it[painting].fechaInicioExposicionArte, DateTimeFormatter.ISO_DATE_TIME).format(taskViewModel.dayTimeOutputFormaterES)
    Card(colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 10.dp)//.border(5.dp, Color.Yellow)
    ) {
        ShowCustomData("Exposicion: ${it[painting].nombreExposicionArte}")
        ShowCustomData("Direccion: ${it[painting].lugarExposicionArte}")
        ShowCustomData("Fecha: $formattedDate")
    }
}
/**
 *
 */
@Composable
fun ShowMedicamentTaskItem(it: List<MedicamentModel>, medicament: Int, taskViewModel: TaskViewModel) {
    val formattedDate = LocalDateTime.parse(it[medicament].fechaCaducidadMedicamento, DateTimeFormatter.ISO_DATE_TIME).format(taskViewModel.dayTimeOutputFormaterES)
    Card(colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 10.dp)//.border(5.dp, Color.Yellow)
    ) {
        ShowCustomData("Medicamento: ${it[medicament].nombreMedicamento}")
        ShowCustomData("Cantidad: ${it[medicament].cantidadTotalCajaMedicamento}")
        ShowCustomData("Fecha: $formattedDate")
    }
}
/**
 *
 */
@Composable
fun ShowMedicalAppointmentTaskItem(it: List<MedicalAppointmentModel>, medicalAppointment: Int, taskViewModel: TaskViewModel) {
    val formattedDate = LocalDateTime.parse(it[medicalAppointment].fechaCitaMedica, DateTimeFormatter.ISO_DATE_TIME).format(taskViewModel.dayTimeOutputFormaterES)
    Card(colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 10.dp)//.border(5.dp, Color.Yellow)
    ) {
        ShowCustomData("Prueba: ${it[medicalAppointment].tipoPruebaCitaMedica}")
        ShowCustomData("Lugar: ${it[medicalAppointment].lugarCitaMedica}")
        ShowCustomData("Cita: $formattedDate}")
    }
}
/**
 *
 */
@Composable
fun ShowFoodTaskItem(it: List<FoodModel>, food: Int, taskViewModel: TaskViewModel) {
    val formattedDate = LocalDateTime.parse(it[food].fechaReserva, DateTimeFormatter.ISO_DATE_TIME).format(taskViewModel.dayTimeOutputFormaterES)
    Card(colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 10.dp)//.border(5.dp, Color.Yellow)
    ) {
        ShowCustomData("Restaurante: ${it[food].nombreRestaurante}")
        ShowCustomData("Dirección: ${it[food].direccionRestaurante}")
        ShowCustomData("Reserva: $formattedDate")
    }
}
/**
 *
 */
@Composable
fun ShowMeetingTaskItem(it: List<MeetingModel>, meeting: Int, taskViewModel: TaskViewModel) {
    val formattedDate = LocalDateTime.parse(it[meeting].fechaInicioReunion, DateTimeFormatter.ISO_DATE_TIME).format(taskViewModel.dayTimeOutputFormaterES)
    Card(colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 10.dp)//.border(5.dp, Color.Yellow)
    ) {
        ShowCustomData("Reunion: ${it[meeting].tituloReunion}")
        ShowCustomData("Lugar: ${it[meeting].lugarReunion}")
        ShowCustomData("Fecha: $formattedDate")
    }
}
/**
 *
 */
@Composable
fun ShowTravelTaskItem(it: List<TravelModel>, travel: Int, taskViewModel: TaskViewModel) {
    val formattedDepartureDate = LocalDateTime.parse(it[travel].fechaSalidaViaje, DateTimeFormatter.ISO_DATE_TIME).format(taskViewModel.dayTimeOutputFormaterES)
    val formattedArrivalDate = LocalDateTime.parse(it[travel].fechaRegresoViaje, DateTimeFormatter.ISO_DATE_TIME).format(taskViewModel.dayTimeOutputFormaterES)
    Card(colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 10.dp)//.border(5.dp, Color.Yellow)
    ) {
        ShowCustomData("Destino: ${it[travel].nombreDestinoViaje}")
        ShowCustomData("Salida: $formattedDepartureDate")
        ShowCustomData("Regreso: $formattedArrivalDate")
    }
}
/**
 *
 */
@Composable
fun ShowSportTaskItem(it: List<SportModel>, sport: Int, taskViewModel: TaskViewModel) {
    val formattedDate = LocalDateTime.parse(it[sport].fechaInicioActividad, DateTimeFormatter.ISO_DATE_TIME).format(taskViewModel.dayTimeOutputFormaterES)
    Card(colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 10.dp)//.border(5.dp, Color.Yellow)
    ) {
        ShowCustomData("Actividad: ${it[sport].nombreActividad}")
        ShowCustomData("Tiempo: ${it[sport].duracionActividadMinutos}")
        ShowCustomData("Fecha: $formattedDate")
    }
}
/**
 *
 */
@Composable
fun ShowWorkTaskItem(it: List<WorkModel>, work: Int, taskViewModel: TaskViewModel) {
    val formattedInitDate = LocalDateTime.parse(it[work].fechaInicioTarea, DateTimeFormatter.ISO_DATE_TIME).format(taskViewModel.dayTimeOutputFormaterES)
    val formattedFinishDate = LocalDateTime.parse(it[work].fechaEntregaTarea, DateTimeFormatter.ISO_DATE_TIME).format(taskViewModel.dayTimeOutputFormaterES)
    Card(colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 10.dp)//.border(5.dp, Color.Yellow)
    ) {
        ShowCustomData("Tarea: ${it[work].tituloTarea}")
        ShowCustomData("Inicio: $formattedInitDate")
        ShowCustomData("Entrega: $formattedFinishDate")
    }
}