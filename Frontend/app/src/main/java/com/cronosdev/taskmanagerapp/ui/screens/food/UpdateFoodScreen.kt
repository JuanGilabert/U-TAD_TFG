package com.cronosdev.taskmanagerapp.ui.screens.food
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
import java.time.Instant
import java.time.ZoneOffset

/**
 *
 */
@Composable
fun UpdateFoodScreen(navController: NavHostController, foodViewModel: FoodViewModel) {
    // Variable que controla el estado de la vista/screen.
    val uiState by foodViewModel.updateFoodScreenUiState.collectAsState()
    // Conjunto de acciones que se realizan al cargar la vista, en este caso obtener la lista de salas disponibles para reservar.
    LaunchedEffect(Unit) { foodViewModel.fetchUnavailableDates(Destinations.UPDATE_FOOD_SCREEN_URL) }
    // Controlamos el estado de la vista.
    when (uiState) {
        is UiState.Loading -> ShowLoadingUiState()
        is UiState.Success -> ShowFoodScreenSuccessUiState(uiState as UiState.Success, navController, foodViewModel)
        is UiState.Error -> ShowFoodScreenErrorUiState(uiState as UiState.Error, foodViewModel)
        else -> {}
    }
}

/**
 *
 */
@Composable
fun ShowUpdateFoodScreen(navController: NavHostController, foodViewModel: FoodViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.bckfoodblack), contentDescription = null,
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
        )
        Column(modifier = Modifier.fillMaxSize().padding(10.dp)/*.border(width = 2.dp, color = Color.Red)*/,
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
            // Mostramos el titulo de la vista utilizando el componente correspondiente llamado 'ViewTitleComponent'.
            ViewTitleComponent(stringResource(R.string.editFoodScreenViewName), Color(0xFFFDFEFE))
            //
            ShowEditFoodScreenCards(foodViewModel, Modifier.weight(20F))
            // Mostramos el composable que muestra los botones de volver, guardar, y eliminar.
            ShowUpdateScreenButtons(onButtonClick = { userAction ->
                if (userAction == "back") navController.popBackStack(Destinations.FOOD_SCREEN_URL, false)
                if (userAction == "save") foodViewModel.putFood(Destinations.UPDATE_FOOD_SCREEN_URL)
                if (userAction == "delete") foodViewModel.deleteFood(Destinations.UPDATE_FOOD_SCREEN_URL)
            })
        }
    }
}
/**
 *
 */
@Composable
fun ShowEditFoodScreenCards(foodViewModel: FoodViewModel, modifier: Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.padding(15.dp, 10.dp)
        .verticalScroll(rememberScrollState())/*.border(width = 2.dp, color = Color.Yellow)*/) {
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard("update","Restaurante", "", foodViewModel.nombreRestaurante, onTextFieldValueChange = { foodViewModel.setNombreRestaurante(it) })
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard("update","Dirección", "", foodViewModel.direccionRestaurante, onTextFieldValueChange = { foodViewModel.setDireccionRestaurante(it) })
        // Mostramos el composable que recogera
        ShowOutlinedTextFieldUpdateDataCard("update","Comida", "", foodViewModel.tipoComida, onTextFieldValueChange = { foodViewModel.setTipoComida(it) })
        // Mostramos el composable que recogera la fecha seleccionada o la fecha nueva que quiera elegir el usuario.
        ShowDatePickerComponentCard("update", foodViewModel, "Fecha de la Reserva", onInitDateChange2 = {
            val dateTime = Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDateTime()
            foodViewModel.setFechaReserva(dateTime.format(foodViewModel.dayTimeFormaterEEUU))
        }, foodViewModel.fechaReserva)
        // Mostramos el composable que recogera la hora de inicio.
        ShowTimePickerComponentCard("update","Hora de la Reserva", onInitHourChange = { foodViewModel.setHoraReserva(it) }, foodViewModel.horaReserva)
        // Mostramos el composable que recogera la duracion de la pelicula en minutos.
        ShowOutlinedTextFieldUpdateDataCard("update","Asistentes", "", foodViewModel.asistentesReserva.toString(), onTextFieldValueChange = { foodViewModel.setAsistentesReserva(it.toIntOrNull() ?: 0) })
        // Mostramos el composable que recogera las notas.
        ShowOutlinedTextFieldUpdateDataCard("update","Notas", "", foodViewModel.notasReserva, onTextFieldValueChange = { foodViewModel.setNotasReserva(it) })
    }
}