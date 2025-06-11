package com.cronosdev.taskmanagerapp.ui.screens.art.music
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
fun UpdateMusicScreen(navController: NavHostController, musicViewModel: MusicViewModel) {
    // Variable que controla el estado de la vista/screen.
    val uiState by musicViewModel.editMusicScreenUiState.collectAsState()
    // Conjunto de acciones que se realizan al cargar la vista, en este caso obtener la lista de salas disponibles para reservar.
    LaunchedEffect(Unit) { musicViewModel.fetchUnavailableDates(Destinations.ART_EDIT_MUSIC_SCREEN_URL) }
    // Controlamos el estado de la vista.
    when (uiState) {
        is UiState.Loading -> ShowLoadingUiState()
        is UiState.Success -> ShowMusicScreenSuccessUiState(uiState as UiState.Success, navController, musicViewModel)
        is UiState.Error -> ShowMusicScreenErrorUiState(uiState as UiState.Error, musicViewModel)
        else -> {}
    }
}
/**
 *
 */
@Composable
fun ShowUpdateMusicScreen(navController: NavHostController, musicViewModel: MusicViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.bckmusicblack), contentDescription = null,
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
        )
        Column(modifier = Modifier.fillMaxSize().padding(10.dp)/*.border(width = 2.dp, color = Color.Red)*/,
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
            // Mostramos el titulo de la vista utilizando el componente correspondiente llamado 'ViewTitleComponent'.
            ViewTitleComponent(stringResource(R.string.editMusicScreenViewName), Color(0xFFFDFEFE))
            //
            ShowEditMusicScreenCards(musicViewModel, Modifier.weight(20F))
            // Mostramos el composable que muestra los botones de volver, guardar, y eliminar.
            ShowUpdateScreenButtons(onButtonClick = { userAction ->
                if (userAction == "back") navController.popBackStack()
                if (userAction == "save") musicViewModel.putMusic(Destinations.ART_EDIT_MUSIC_SCREEN_URL)
                if (userAction == "delete") musicViewModel.deleteMusic(Destinations.ART_EDIT_MUSIC_SCREEN_URL)
            })
        }
    }
}
/**
 *
 */
@Composable
fun ShowEditMusicScreenCards(musicViewModel: MusicViewModel, modifier: Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.padding(15.dp, 10.dp)
        .verticalScroll(rememberScrollState())/*.border(width = 2.dp, color = Color.Yellow)*/) {
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard(
            "update",
            "Evento",
            "",
            musicViewModel.nombreEvento,
            onTextFieldValueChange = { musicViewModel.setNombreEvento(it) }
        )
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard(
            "update",
            "Descripcion",
            "",
            musicViewModel.descripcionEvento,
            onTextFieldValueChange = { musicViewModel.setDescripcionEvento(it) }
        )
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateMultipleDataCard(
            "update",
            "Artistas",
            "",
            musicViewModel.artistasEvento,
            onTextFieldValueChange = { musicViewModel.setArtistasEvento(it) }
        )
        // Mostramos el composable que recogera la fecha
        ShowDatePickerComponentCard("update",musicViewModel, "Fecha de Inicio", onInitDateChange2 = {
            val dateTime = Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDateTime()
            musicViewModel.setFechaInicioEvento(dateTime.format(musicViewModel.dayTimeFormaterEEUU)) }, musicViewModel.fechaInicioEventoES
        )
        // Mostramos el composable que recogera
        ShowTimePickerComponentCard(
            "update",
            "Hora de Inicio",
            onInitHourChange = { musicViewModel.setHoraInicioEvento(it) },
            musicViewModel.horaInicioPelicula
        )
        // Mostramos el composable que recogera la fecha
        ShowDatePickerComponentCard("update",musicViewModel, "Fecha de Fin", onInitDateChange2 = {
            val dateTime = Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDateTime()
            musicViewModel.setFechaFinEvento(dateTime.format(musicViewModel.dayTimeFormaterEEUU)) }, musicViewModel.fechaFinEventoES
        )
        // Mostramos el composable que recogera
        ShowTimePickerComponentCard(
            "update",
            "Hora de Fin",
            onInitHourChange = { musicViewModel.setHoraFinEvento(it) },
            musicViewModel.horaFinEvento
        )
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard(
            "update",
            "Lugar",
            "",
            musicViewModel.lugarEvento,
            onTextFieldValueChange = { musicViewModel.setLugarEvento(it) }
        )
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard(
            "update",
            "Precio",
            "",
            musicViewModel.precioEvento.toString(),
            onTextFieldValueChange = { musicViewModel.setPrecioEntradaEvento(it.toFloatOrNull() ?: 0F) }
        )
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard(
            "update",
            "Notas",
            "",
            musicViewModel.notasEvento,
            onTextFieldValueChange = { musicViewModel.setNotasEvento(it) }
        )
    }
}