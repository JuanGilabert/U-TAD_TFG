package com.cronosdev.taskmanagerapp.ui.screens.art.cinema
//
import androidx.compose.foundation.Image
import java.time.Instant
import java.time.ZoneOffset
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
import com.cronosdev.taskmanagerapp.ui.state.error.CinemaScreenErrors.ShowErrorForArtUpdateCinemaScreen

/**
 *
 */
@Composable
fun EditCinemaScreen(navController: NavHostController, cinemaViewModel: CinemaViewModel) {
    // Variable que controla el estado de la vista/screen.
    val uiState by cinemaViewModel.editCinemaScreenUiState.collectAsState()
    // Conjunto de acciones que se realizan al cargar la vista, en este caso obtener la lista de salas disponibles para reservar.
    LaunchedEffect(Unit) {
        cinemaViewModel.fetchUnavailableDates(Destinations.ART_UPDATE_CINEMA_SCREEN_URL)
        // Obtenemos las tareas en la fecha seleccionada y por defecto se envia la fecha del modelo.
        //cinemaViewModel.fetchAvailableDatesOnDayByDate("editCinemaScreen")
    }
    // Controlamos el estado de la vista.
    when (uiState) {
        is UiState.Loading -> ShowLoadingUiState()
        is UiState.Success -> ShowCinemaScreenSuccessUiState(uiState as UiState.Success, navController, cinemaViewModel)
        is UiState.Error -> ShowErrorForArtUpdateCinemaScreen(uiState as UiState.Error, cinemaViewModel)
        else -> {}
    }
}
////
/**
 *
 */
@Composable
fun ShowEditCinemaScreen(navController: NavHostController, cinemaViewModel: CinemaViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.bckcinemablack), contentDescription = null,
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
        )
        Column(modifier = Modifier.fillMaxSize().padding(10.dp)/*.border(width = 2.dp, color = Color.Red)*/,
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
            // Mostramos el titulo de la vista utilizando el componente correspondiente llamado 'ViewTitleComponent'.
            ViewTitleComponent(stringResource(R.string.editCinemaScreenViewName), Color(0xFFFDFEFE))
            //
            ShowEditCinemaScreenCards(cinemaViewModel, Modifier.weight(20F))
            // Mostramos el composable que muestra los botones de volver, guardar, y eliminar.
            ShowUpdateScreenButtons(onButtonClick = { userAction ->
                if (userAction == "back") navController.popBackStack(Destinations.ART_CINEMA_SCREEN_URL, false)
                if (userAction == "save") cinemaViewModel.putCinema(Destinations.ART_UPDATE_CINEMA_SCREEN_URL)
                if (userAction == "delete") cinemaViewModel.deleteCinema(Destinations.ART_UPDATE_CINEMA_SCREEN_URL)
            })
        }
    }
}
/**
 *
 */
@Composable
fun ShowEditCinemaScreenCards(cinemaViewModel: CinemaViewModel, modifier: Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.padding(15.dp, 10.dp)
        .verticalScroll(rememberScrollState())/*.border(width = 2.dp, color = Color.Yellow)*/) {
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard("update","Pelicula", "", cinemaViewModel.nombrePelicula, onTextFieldValueChange = { cinemaViewModel.setNombrePelicula(it) })
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard("update","Sinopsis", "", cinemaViewModel.descripcionPelicula, onTextFieldValueChange = { cinemaViewModel.setDescripcionPelicula(it) })
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateMultipleDataCard("update","Actores", "", cinemaViewModel.actoresPelicula, onTextFieldValueChange = { cinemaViewModel.setActoresPelicula(it) })
        // Mostramos el composable que recogera
        ShowDatePickerComponentCard("update", cinemaViewModel, "Fecha de Inicio", onInitDateChange2 = {
            val dateTime = Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDateTime()
            cinemaViewModel.setFechaInicioPelicula(dateTime.format(cinemaViewModel.dayTimeFormaterEEUU)) }, cinemaViewModel.fechaInicioPeliculaES
        )
        // Mostramos el composable que recogera
        ShowTimePickerComponentCard("update","Hora de Inicio", onInitHourChange = { cinemaViewModel.setHoraInicioPelicula(it) }, cinemaViewModel.horaInicioPelicula)
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard("update","Duracion", "", cinemaViewModel.duracionPeliculaMinutos.toString(), onTextFieldValueChange = { cinemaViewModel.setDuracionPeliculaMinutos(it.toIntOrNull() ?: 0) })
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard("update","Dirección:", "", cinemaViewModel.lugarPelicula, onTextFieldValueChange = { cinemaViewModel.setLugarPelicula(it) })
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard("update","Precio", "", cinemaViewModel.precioEntradaPelicula.toString(), onTextFieldValueChange = { cinemaViewModel.setPrecioEntradaPelicula(it.toFloatOrNull() ?: 0F) })
        // Mostramos el composable que recogera las notas.
        ShowOutlinedTextFieldUpdateDataCard("update","Notas", "", cinemaViewModel.notasPelicula, onTextFieldValueChange = { cinemaViewModel.setNotasPelicula(it) })
    }
}