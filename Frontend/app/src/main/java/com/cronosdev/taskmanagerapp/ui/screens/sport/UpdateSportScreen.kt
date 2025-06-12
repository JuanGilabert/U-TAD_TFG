package com.cronosdev.taskmanagerapp.ui.screens.sport

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
import com.cronosdev.taskmanagerapp.ui.screens.art.cinema.CinemaViewModel
import java.time.Instant
import java.time.ZoneOffset

@Composable
fun UpdateSportScreen(navController: NavHostController, sportViewModel: SportViewModel) {
    // Variable que controla el estado de la vista/screen.
    val uiState by sportViewModel.updateSportScreenUiState.collectAsState()
    // Conjunto de acciones que se realizan al cargar la vista, en este caso obtener la lista de salas disponibles para reservar.
    LaunchedEffect(Unit) { sportViewModel.fetchUnavailableDates(Destinations.EDIT_SPORT_SCREEN_URL) }
    // Controlamos el estado de la vista.
    when (uiState) {
        is UiState.Loading -> ShowLoadingUiState()
        is UiState.Success -> ShowSportScreenSuccessUiState(uiState as UiState.Success, navController, sportViewModel)
        is UiState.Error -> ShowSportScreenErrorUiState(uiState as UiState.Error, sportViewModel)
        else -> {}
    }
}

/** Funcion composable utilizada para mostrar un cuadro de diálogo para editar o eliminar una reserva.
 *
 * **Parameters;
 * @param sportViewModel ViewModel con la lógica de negocio.
 * @return No retorna un valor directo, ya que es un Composable.
 * @sample ShowUpdateSportScreen()
 * @see CinemaViewModel
 * @since 1.0
 */
@Composable
fun ShowUpdateSportScreen(navController: NavHostController, sportViewModel: SportViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.bcksportblack), contentDescription = null,
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
        )
        Column(modifier = Modifier.fillMaxSize().padding(10.dp)/*.border(width = 2.dp, color = Color.Red)*/,
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
            // Mostramos el titulo de la vista utilizando el componente correspondiente llamado 'ViewTitleComponent'.
            ViewTitleComponent(stringResource(R.string.editSportScreenViewName), Color(0xFFFDFEFE))
            //
            ShowEditSportScreenCards(sportViewModel, Modifier.weight(20F))
            // Mostramos el composable que muestra los botones de volver, guardar, y eliminar.
            ShowUpdateScreenButtons(onButtonClick = { userAction ->
                if (userAction == "back") navController.popBackStack(Destinations.SPORT_SCREEN_URL, false)
                if (userAction == "save") sportViewModel.putSport(Destinations.EDIT_SPORT_SCREEN_URL)
                if (userAction == "delete") sportViewModel.deleteSport(Destinations.EDIT_SPORT_SCREEN_URL)
            })
        }
    }
}
/**
 *
 */
@Composable
fun ShowEditSportScreenCards(sportViewModel: SportViewModel, modifier: Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.padding(15.dp, 10.dp)
        .verticalScroll(rememberScrollState())/*.border(width = 2.dp, color = Color.Yellow)*/) {
        // Mostramos el composable que recogera.
        ShowOutlinedTextFieldUpdateDataCard("update","Actividad", "", sportViewModel.nombreActividad, onTextFieldValueChange = { sportViewModel.setNombreActividad(it) })
        // Mostramos el composable que recogera.
        ShowDatePickerComponentCard("update",sportViewModel, "Fecha de la Actividad", onInitDateChange2 = {
            val dateTime = Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDateTime()
            sportViewModel.setFechaInicioActividad(dateTime.format(sportViewModel.dayTimeFormaterEEUU))
        }, sportViewModel.fechaInicioActividad)
        // Mostramos el composable que recogera.
        ShowTimePickerComponentCard("update","Hora de la Actiidad", onInitHourChange = { sportViewModel.setHoraInicioActividad(it) }, sportViewModel.horaInicioActividad)
        // Mostramos el composable que recogera.
        ShowOutlinedTextFieldUpdateDataCard("update","Lugar", "", sportViewModel.lugarActividad, onTextFieldValueChange = { sportViewModel.setLugarActividad(it) })
        // Mostramos el composable que recogera.
        ShowOutlinedTextFieldUpdateDataCard("update","Duración", "", sportViewModel.duracionActividadMinutos.toString(), onTextFieldValueChange = { sportViewModel.setDuracionActividadMinutos(it.toIntOrNull() ?: 0) })
        // Mostramos el composable que recogera.
        ShowOutlinedTextFieldUpdateMultipleDataCard("update","Asistentes", "", sportViewModel.asistentesActividad, onTextFieldValueChange = { sportViewModel.setAsistentesActividad(it) })
        // Mostramos el composable que recogera.
        ShowOutlinedTextFieldUpdateDataCard("update","Notas", "", sportViewModel.notasActividad, onTextFieldValueChange = { sportViewModel.setNotasActividad(it) })
    }
}