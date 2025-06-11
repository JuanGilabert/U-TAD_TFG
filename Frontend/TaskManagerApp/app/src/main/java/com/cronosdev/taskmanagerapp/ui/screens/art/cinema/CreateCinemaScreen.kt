package com.cronosdev.taskmanagerapp.ui.screens.art.cinema
//
import androidx.compose.foundation.Image
import java.time.Instant
import java.time.ZoneOffset
import java.util.Date
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.cronosdev.taskmanagerapp.R
import com.cronosdev.taskmanagerapp.ui.state.ShowLoadingUiState
import com.cronosdev.taskmanagerapp.ui.state.UiState
import com.cronosdev.taskmanagerapp.ui.components.outlinedComponents.buttons.ShowCreateScreenButtons
import com.cronosdev.taskmanagerapp.ui.components.ViewTitleComponent
import com.cronosdev.taskmanagerapp.ui.components.outlinedComponents.textFields.ShowOutlinedTextFieldUpdateDataCard
import com.cronosdev.taskmanagerapp.ui.components.outlinedComponents.textFields.ShowOutlinedTextFieldUpdateMultipleDataCard
import com.cronosdev.taskmanagerapp.ui.components.datePickerComponent.DatePickerComponent
import com.cronosdev.taskmanagerapp.ui.components.outlinedComponents.textFields.getOutlinedTextFieldColor
import com.cronosdev.taskmanagerapp.ui.components.timePickerComponent.ShowTimePickerComponentCard
import com.cronosdev.taskmanagerapp.ui.navigation.Destinations
import com.cronosdev.taskmanagerapp.ui.state.error.CinemaScreenErrors.ShowErrorForArtCreateCinemaScreen

/**
 * 
 */
@Composable
fun CreateCinemaScreen(navController: NavHostController, cinemaViewModel: CinemaViewModel) {
    // Obtenemos el estado de la vista.
    val uiState by cinemaViewModel.createCinemaScreenUiState.collectAsState()
    // Conjunto de acciones que se realizan al cargar la vista, en este caso obtener la lista de salas disponibles para reservar.
    LaunchedEffect(Unit) { cinemaViewModel.fetchUnavailableDates(Destinations.ART_CREATE_CINEMA_SCREEN_URL) }
    // Controlamos el estado de la vista.
    when (uiState) {
        is UiState.Loading -> ShowLoadingUiState()
        is UiState.Success -> ShowCinemaScreenSuccessUiState(uiState as UiState.Success, navController, cinemaViewModel)
        is UiState.Error -> ShowErrorForArtCreateCinemaScreen(uiState as UiState.Error, cinemaViewModel)
        else -> {}
    }
}
/**
 *
 */
@Composable
fun ShowCreateCinemaScreen(navController: NavHostController, cinemaViewModel: CinemaViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.bckcinemablack), contentDescription = null,
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
        )
        Column(modifier = Modifier.fillMaxSize().padding(10.dp)/*.border(width = 2.dp, color = Color.Red)*/,
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
            // Mostramos el titulo de la vista utilizando el componente correspondiente llamado 'ViewTitleComponent'.
            ViewTitleComponent(stringResource(R.string.createCinemaScreenViewName), Color(0xFFFDFEFE))
            //
            ShowCreateCinemaScreenCards(cinemaViewModel, Modifier.weight(20F))
            // Mostramos el composable que muestra el boton de cancelar/volver y el boton encargado de guardar la reserva y redirigir al usuario a la vista 'artCinema' en calquiera de ambos casos.
            ShowCreateScreenButtons() { userAction ->
                // Volvemos a la vista anterior que en este caso es la indicada
                // eliminando la actual del stack y manteniendo la anterior sin eliminarla tambien .popBackStack("artCinemaScreen", false)
                if (userAction == "back") navController.popBackStack(Destinations.ART_CINEMA_SCREEN_URL, false)
                if (userAction == "create") cinemaViewModel.createCinema(Destinations.ART_CREATE_CINEMA_SCREEN_URL)
            }
        }
    }
}
/**
 *
 */
@Composable
fun ShowCreateCinemaScreenCards(cinemaViewModel: CinemaViewModel, modifier: Modifier) {
    //.border(width = 2.dp, color = Color.Yellow)
    Column(modifier = modifier.padding(15.dp, 10.dp).verticalScroll(rememberScrollState())) {
        // Mostramos el nombre de la pellicula.
        ShowOutlinedTextFieldUpdateDataCard(
            "create",
            "Nombre",
            "Escriba el nombre de la pelicula ...",
            ""
        ) { cinemaViewModel.setNombrePelicula(it) }
        // Mostramos la descripcion de la pellicula.
        ShowOutlinedTextFieldUpdateDataCard("create","Sinopsis", "Escriba la descripcion ...", "", onTextFieldValueChange = { cinemaViewModel.setDescripcionPelicula(it) })
        // Mostramos el composable que recogera los actores de la pelicula.
        ShowOutlinedTextFieldUpdateMultipleDataCard("create","Actores", "Indique los actores separados por  ', ' ", onTextFieldValueChange = { cinemaViewModel.setActoresPelicula(it) })
        // Mostramos el composable que recogera la fecha seleccionada o la nueva fecha que elija el usuario.
        ShowDatePickerComponentCard("create",cinemaViewModel, "Fecha de Inicio", onInitDateChange2 = {
            val dateTime = Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDateTime() // dateTime = 2025-05-28T:00:00
            cinemaViewModel.setFechaInicioPelicula(dateTime.format(cinemaViewModel.dayTimeFormaterEEUU)) /* Formated date into ViewModel: 2025-05-28*/ },
            cinemaViewModel.fechaInicioPelicula
        )
        // Mostramos el composable que recogera la hora de inicio.
        ShowTimePickerComponentCard("create","Hora de Inicio", onInitHourChange = { cinemaViewModel.setHoraInicioPelicula(it) }, cinemaViewModel.horaInicioPelicula)
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard("create","Duracion", "Escriba la duracion en minutos ...", "", onTextFieldValueChange = { cinemaViewModel.setDuracionPeliculaMinutos(it.toIntOrNull() ?: 1) })
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard("create","Dirección:", "Escriba el lugar ...", "", onTextFieldValueChange = { cinemaViewModel.setLugarPelicula(it) })
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard("create","Precio", "Escriba el precio ...", "", onTextFieldValueChange = { cinemaViewModel.setPrecioEntradaPelicula(it.toFloatOrNull() ?: 0F) })
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard("create","Notas", "Escriba las notas ...", "", onTextFieldValueChange = { cinemaViewModel.setNotasPelicula(it) })
    }
}
/**
 *
 */
@Composable
fun ShowDatePickerComponentCard(outlinedTextFieldColorTypeToShow: String, cinemaViewModel: CinemaViewModel, textFieldLabelToShow: String, onInitDateChange2:(Long)-> Unit, selectedDateTextToShow: String) {
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
        cinemaViewModel.unavailableCinemaDatesList.data?.dates?.let {
            DatePickerComponent(it, onInitDateChange= { startDate ->
                selectedDateText = cinemaViewModel.dayTimeFormaterES.format(Date(startDate))
                onInitDateChange2(startDate)
                showDatePickerComponent = false
                // Cargamos las fechas del dia seleccionado por el usuario.
                //cinemaViewModel.fetchAvailableTasksDatesByDate(screenCallName)
                }, onDatePickerButtonClick={ value -> showDatePickerComponent = value }/*, cinemaViewModel.availableCinemaTasksDatesByDateList*/
            )
        }
    }
}

/* ANTIGUO CON fetchAvailableTasksDatesByDate
// Mostramos el composable que recogera la fecha seleccionada o la nueva fecha que elija el usuario.
        ShowDatePickerComponentCard(cinemaViewModel, "artCreateCinemaScreen", onInitDateChange2 = {
            val dateTime = Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDateTime() // dateTime = 2025-05-28T:00:00
            cinemaViewModel.setFechaInicioPelicula(dateTime.format(cinemaViewModel.dayTimeFormaterEEUU)) /* Formated date into ViewModel: 2025-05-28*/ },
            cinemaViewModel.fechaInicioPelicula"Fecha de Inicio"
        )


@Composable
fun ShowDatePickerComponentCard(cinemaViewModel: CinemaViewModel, screenCallName: String, onInitDateChange2:(Long)-> Unit, selectedDateTextToShow: String) {
    var showDatePickerComponent by remember { mutableStateOf(false) }
    var selectedDateText by remember { mutableStateOf(selectedDateTextToShow) }
    //
    OutlinedTextField(value = selectedDateText, onValueChange = { selectedDateText = it },
        readOnly = true, label = { Text(text = "Fecha de Inicio") }, //placeholder = { Text(text = newHourToSHow) },
        shape = RoundedCornerShape(12.dp), singleLine = true,
        colors = getCreateScreenOutlinedTextFieldColors(),
        trailingIcon = {
            IconButton(onClick = { showDatePickerComponent = true }) {
                Icon(imageVector = Icons.Filled.DateRange, contentDescription = "Seleccionar la hora", tint = Color.White)
            }
        },
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)
    )
    // Dialog para mostrar el DatePciker comun de la aplicacion llamado 'DatePickerComponent'.
    if (showDatePickerComponent) {
        cinemaViewModel.unavailableCinemaDatesList.data?.dates?.let {
            ShowDatePickerComponent(it, onInitDateChange= { startDate ->
                selectedDateText = cinemaViewModel.dayTimeFormaterES.format(Date(startDate))
                onInitDateChange2(startDate)
                showDatePickerComponent = false
                // Cargamos las fechas del dia seleccionado por el usuario.
                //cinemaViewModel.fetchAvailableTasksDatesByDate(screenCallName)
                }, onDatePickerButtonClick={ value -> showDatePickerComponent = value }/*, cinemaViewModel.availableCinemaTasksDatesByDateList*/
            )
        }
    }
}
*/