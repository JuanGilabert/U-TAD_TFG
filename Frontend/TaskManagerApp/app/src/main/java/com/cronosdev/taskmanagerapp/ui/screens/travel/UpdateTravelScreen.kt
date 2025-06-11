package com.cronosdev.taskmanagerapp.ui.screens.travel

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

@Composable
fun UpdateTravelScreen(navController: NavHostController, travelViewModel: TravelViewModel) {
    // Variable que controla el estado de la vista/screen.
    val uiState by travelViewModel.editTravelScreenUiState.collectAsState()
    // Conjunto de acciones que se realizan al cargar la vista, en este caso obtener la lista de salas disponibles para reservar.
    LaunchedEffect(Unit) { travelViewModel.fetchUnavailableDates(Destinations.EDIT_TRAVEL_SCREEN_URL) }
    // Controlamos el estado de la vista.
    when (uiState) {
        is UiState.Loading -> ShowLoadingUiState()
        is UiState.Success -> ShowTravelScreenSuccessUiState(uiState as UiState.Success, navController, travelViewModel)
        is UiState.Error -> ShowTravelScreenErrorUiState(uiState as UiState.Error, travelViewModel)
        else -> {}
    }
}

/**
 *
 */
@Composable
fun ShowUpdateTravelScreen(navController: NavHostController, travelViewModel: TravelViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.bcktravelblack), contentDescription = null,
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
        )
        Column(modifier = Modifier.fillMaxSize().padding(10.dp)/*.border(width = 2.dp, color = Color.Red)*/,
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
            // Mostramos el titulo de la vista utilizando el componente correspondiente llamado 'ViewTitleComponent'.
            ViewTitleComponent(stringResource(R.string.editTravelScreenViewName), Color(0xFFFDFEFE))
            //
            ShowEditTravelScreenCards(travelViewModel, Modifier.weight(20F))
            // Mostramos el composable que muestra los botones de volver, guardar, y eliminar.
            ShowUpdateScreenButtons(onButtonClick = { userAction ->
                if (userAction == "back") navController.popBackStack()
                if (userAction == "save") travelViewModel.putTravel(Destinations.EDIT_TRAVEL_SCREEN_URL)
                if (userAction == "delete") travelViewModel.deleteTravel(Destinations.EDIT_TRAVEL_SCREEN_URL)
            })
        }
    }
}
/**
 *
 */
@Composable
fun ShowEditTravelScreenCards(travelViewModel: TravelViewModel, modifier: Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.padding(15.dp, 10.dp)
        .verticalScroll(rememberScrollState())/*.border(width = 2.dp, color = Color.Yellow)*/) {
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard("update","Viaje", travelViewModel.nombreDestinoViaje, "", onTextFieldValueChange = { travelViewModel.setNombreDestinoViaje(it) })
        // Mostramos el composable que recogera
        ShowDatePickerComponentCard("update",travelViewModel, "Fecha de Salida", onInitDateChange2 = {
            val dateTime = Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDateTime()
            travelViewModel.setFechaSalidaViaje(dateTime.format(travelViewModel.dayTimeFormaterEEUU))
        }, travelViewModel.fechaSalidaViaje)
        // Mostramos el composable que recogera
        ShowTimePickerComponentCard("update","Hora de Salida", onInitHourChange = { travelViewModel.setHoraSalidaViaje(it) }, travelViewModel.fechaSalidaViaje)
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard("update","Salida", travelViewModel.lugarSalidaViaje, "", onTextFieldValueChange = { travelViewModel.setLugarSalidaViaje(it) })
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard("update","Destino", travelViewModel.lugarDestinoViaje, "", onTextFieldValueChange = { travelViewModel.setLugarDestinoViaje(it) })
        // Mostramos el composable que recogera
        ShowDatePickerComponentCard("update",travelViewModel, "Fecha de Regreso", onInitDateChange2 = {
            val dateTime = Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDateTime()
            travelViewModel.setFechaRegresoViaje(dateTime.format(travelViewModel.dayTimeFormaterEEUU))
        }, travelViewModel.fechaRegresoViaje)
        // Mostramos el composable que recogera
        ShowTimePickerComponentCard("update","Hora de Regreso", onInitHourChange = { travelViewModel.setHoraRegresoViaje(it) }, travelViewModel.fechaRegresoViaje)
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard("update","Trasporte", "", travelViewModel.transporteViaje, onTextFieldValueChange = { travelViewModel.setTransporteViaje(it) })
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateMultipleDataCard("update","Acompañantes", "", travelViewModel.acompañantesViaje, onTextFieldValueChange = { travelViewModel.setAcompañantesViaje(it) })
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard("update","Notas", "", travelViewModel.notasViaje, onTextFieldValueChange = { travelViewModel.setNotasViaje(it) })
    }
}