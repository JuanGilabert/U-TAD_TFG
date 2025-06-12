package com.cronosdev.taskmanagerapp.ui.screens.health.medicament

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
fun UpdateMedicamentScreen(navController: NavHostController, medicamentViewModel: MedicamentViewModel) {
    // Variable que controla el estado de la vista/screen.
    val uiState by medicamentViewModel.updateMedicamentScreenUiState.collectAsState()
    // Conjunto de acciones que se realizan al cargar la vista, en este caso obtener la lista de salas disponibles para reservar.
    LaunchedEffect(Unit) {
        medicamentViewModel.fetchUnavailableDates(Destinations.HEALTH_EDIT_MEDICAMENT_SCREEN_URL)
    }
    // Controlamos el estado de la vista.
    when (uiState) {
        is UiState.Loading -> ShowLoadingUiState()
        is UiState.Success -> ShowMedicamentScreenSuccessUiState(uiState as UiState.Success, navController, medicamentViewModel)
        is UiState.Error -> ShowMedicamentScreenErrorUiState(uiState as UiState.Error, medicamentViewModel)
        else -> {}
    }
}

/**
 *
 */
@Composable
fun ShowUpdateMedicamentScreen(navController: NavHostController, medicamentViewModel: MedicamentViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.bckmedicamentblack), contentDescription = null,
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
        )
        Column(modifier = Modifier.fillMaxSize().padding(10.dp)/*.border(width = 2.dp, color = Color.Red)*/,
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
            // Mostramos el titulo de la vista utilizando el componente correspondiente llamado 'ViewTitleComponent'.
            ViewTitleComponent(stringResource(R.string.editMedicamentScreenViewName), Color(0xFFFDFEFE))
            //
            ShowEditMedicamentScreenCards(medicamentViewModel, Modifier.weight(20F))
            // Mostramos el composable que muestra los botones de volver, guardar, y eliminar.
            ShowUpdateScreenButtons(onButtonClick = { userAction ->
                if (userAction == "back") navController.popBackStack(Destinations.HEALTH_MEDICAMENT_SCREEN_URL, false)
                if (userAction == "save") medicamentViewModel.putMedicament(Destinations.HEALTH_EDIT_MEDICAMENT_SCREEN_URL)
                if (userAction == "delete") medicamentViewModel.deleteMedicament(Destinations.HEALTH_EDIT_MEDICAMENT_SCREEN_URL)
            })
        }
    }
}
/**
 *
 */
@Composable
fun ShowEditMedicamentScreenCards(medicamentViewModel: MedicamentViewModel, modifier: Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.padding(15.dp, 10.dp)
        .verticalScroll(rememberScrollState())/*.border(width = 2.dp, color = Color.Yellow)*/) {
        // Mostramos el composable que recogera.
        ShowOutlinedTextFieldUpdateDataCard("update","Medicamento", "", medicamentViewModel.nombreMedicamento, onTextFieldValueChange = { medicamentViewModel.setNombreMedicamento(it) })
        // Mostramos el composable que recogera.
        ShowOutlinedTextFieldUpdateDataCard("update","Forma Administracion", "", medicamentViewModel.formaViaAdministracionMedicamento, onTextFieldValueChange = { medicamentViewModel.setFormaViaAdministracionMedicamento(it) })
        // Mostramos el composable que recogera.
        ShowOutlinedTextFieldUpdateDataCard("update","Tipo Administracion", "", medicamentViewModel.tipoViaAdministracionMedicamento, onTextFieldValueChange = { medicamentViewModel.setTipoViaAdministracionMedicamento(it) })
        // Mostramos el composable que recogera.
        ShowOutlinedTextFieldUpdateDataCard("update","Cantidad", "", medicamentViewModel.cantidadTotalCajaMedicamento.toString(), onTextFieldValueChange = { medicamentViewModel.setCantidadTotalCajaMedicamento(it.toIntOrNull() ?: 0) })
        // Mostramos el composable que recogera.
        ShowDatePickerComponentCard("update",medicamentViewModel, "Fecha de Caducidad", onInitDateChange2 = {
            val dateTime = Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDateTime()
            medicamentViewModel.setFechaCaducidadMedicamento(dateTime.format(medicamentViewModel.dayTimeFormaterEEUU))
        }, medicamentViewModel.fechaCaducidadMedicamento)
        // Mostramos el composable que recogera.
        ShowTimePickerComponentCard("update","Hora de Caducidad", onInitHourChange = { medicamentViewModel.setHoraCaducidadMedicamento(it) }, medicamentViewModel.fechaCaducidadMedicamento)
        // Mostramos el composable que recogera las notas de la pelicula.
        ShowOutlinedTextFieldUpdateDataCard("update","Notas", "", medicamentViewModel.notasMedicamento, onTextFieldValueChange = { medicamentViewModel.setNotasMedicamento(it) })
    }
}