package com.cronosdev.taskmanagerapp.ui.screens.art.painting
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
fun UpdatePaintingScreen(navController: NavHostController, paintingViewModel: PaintingViewModel) {
    // Variable que controla el estado de la vista/screen.
    val uiState by paintingViewModel.editPaintingScreenUiState.collectAsState()
    // Conjunto de acciones que se realizan al cargar la vista, en este caso obtener la lista de salas disponibles para reservar.
    LaunchedEffect(Unit) { paintingViewModel.fetchUnavailableDates(Destinations.ART_EDIT_PAINTING_SCREEN_URL) }
    // Controlamos el estado de la vista.
    when (uiState) {
        is UiState.Loading -> ShowLoadingUiState()
        is UiState.Success -> ShowPaintingScreenSuccessUiState(uiState as UiState.Success, navController, paintingViewModel)
        is UiState.Error -> ShowPaintingScreenErrorUiState(uiState as UiState.Error, paintingViewModel)
        else -> {}
    }
}

/**
 *
 */
@Composable
fun ShowUpdatePaintingScreen(navController: NavHostController, paintingViewModel: PaintingViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.bckcinemablack), contentDescription = null,
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
        )
        Column(modifier = Modifier.fillMaxSize().padding(10.dp)/*.border(width = 2.dp, color = Color.Red)*/,
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
            // Mostramos el titulo de la vista utilizando el componente correspondiente llamado 'ViewTitleComponent'.
            ViewTitleComponent(stringResource(R.string.editPaintingScreenViewName), Color(0xFFFDFEFE))
            //
            ShowEditPaintingScreenCards(paintingViewModel, Modifier.weight(20F))
            // Mostramos el composable que muestra los botones de volver, guardar, y eliminar.
            ShowUpdateScreenButtons(onButtonClick = { userAction ->
                if (userAction == "back") navController.popBackStack()
                if (userAction == "save") paintingViewModel.putPainting(Destinations.ART_EDIT_PAINTING_SCREEN_URL)
                if (userAction == "delete") paintingViewModel.deletePainting(Destinations.ART_EDIT_PAINTING_SCREEN_URL)
            })
        }
    }
}
/**
 *
 */
@Composable
fun ShowEditPaintingScreenCards(paintingViewModel: PaintingViewModel, modifier: Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.padding(15.dp, 10.dp)
        .verticalScroll(rememberScrollState())/*.border(width = 2.dp, color = Color.Yellow)*/) {
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard(
            "update",
            "Exposición",
            "",
            paintingViewModel.nombreExposicionArte,
            onTextFieldValueChange = { paintingViewModel.setNombreExposicionArte(it) }
        )
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard(
            "update",
            "Descripción",
            "",
            paintingViewModel.descripcionExposicionArte,
            onTextFieldValueChange = { paintingViewModel.setDescripcionExposicionArte(it) }
        )
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateMultipleDataCard(
            "update",
            "Pintores",
            "",
            paintingViewModel.pintoresExposicionArte,
            onTextFieldValueChange = { paintingViewModel.setPintoresExposicionArte(it) }
        )
        // Mostramos el composable que recogera la fecha seleccionada
        ShowDatePickerComponentCard("update",paintingViewModel, "Fecha de Inicio", onInitDateChange2 = {
            val dateTime = Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDateTime()
            paintingViewModel.setFechaInicioExposicionArte(dateTime.format(paintingViewModel.dayTimeFormaterEEUU)) }, paintingViewModel.fechaInicioExposicionArteES)
        // Mostramos el composable que recogera la hora.
        ShowTimePickerComponentCard(
            "update",
            "Hora de Inicio",
            onInitHourChange = { paintingViewModel.setHoraInicioExposicionArte(it) },
            paintingViewModel.horaInicioExposicionArte
        )
        // Mostramos el composable que recogera la fecha seleccionada
        ShowDatePickerComponentCard("update",paintingViewModel, "Fecha de Fin", onInitDateChange2 = {
            val dateTime = Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDateTime()
            paintingViewModel.setFechaFinExposicionArte(dateTime.format(paintingViewModel.dayTimeFormaterEEUU)) }, paintingViewModel.fechaFinExposicionArteES)
        // Mostramos el composable que recogera la hora.
        ShowTimePickerComponentCard(
            "update",
            "Hora de Fin",
            onInitHourChange = { paintingViewModel.setHoraFinExposicionArte(it) },
            paintingViewModel.horaFinExposicionArte
        )
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard(
            "update",
            "Dirección",
            "",
            paintingViewModel.lugarExposicionArte,
            onTextFieldValueChange = { paintingViewModel.setLugarExposicionArte(it) }
        )
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard(
            "update",
            "Precio",
            "",
            paintingViewModel.precioEntradaExposicionArte.toString(),
            onTextFieldValueChange = { paintingViewModel.setPrecioEntradaExposicionArte(it.toFloatOrNull() ?: 0F) }
        )
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard("update","Notas", "", paintingViewModel.notasExposicionArte, onTextFieldValueChange = { paintingViewModel.setNotasExposicionArte(it) })
    }
}