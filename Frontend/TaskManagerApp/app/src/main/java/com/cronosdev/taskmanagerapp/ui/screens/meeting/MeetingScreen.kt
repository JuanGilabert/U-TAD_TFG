package com.cronosdev.taskmanagerapp.ui.screens.meeting

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
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
import com.cronosdev.taskmanagerapp.data.model.meeting.MeetingModel
import com.cronosdev.taskmanagerapp.ui.state.ShowLoadingUiState
import com.cronosdev.taskmanagerapp.ui.state.UiState
import com.cronosdev.taskmanagerapp.ui.components.ApiErrorSnackbar
import com.cronosdev.taskmanagerapp.ui.components.ShowAlertDialogComponent
import com.cronosdev.taskmanagerapp.ui.components.outlinedComponents.buttons.ShowCustomData
import com.cronosdev.taskmanagerapp.ui.components.viewSearchersComponents.ShowTextFieldSearcherComponent
import com.cronosdev.taskmanagerapp.ui.components.ViewTitleComponent
import com.cronosdev.taskmanagerapp.ui.navigation.Destinations
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun MeetingScreen (navController: NavHostController, meetingViewModel: MeetingViewModel) {
    // Variable que controla el estado de la vista/screen.
    val uiState by meetingViewModel.meetingScreenUiState.collectAsState()
    // Conjunto de acciones que se realizan al cargar la vista.
    LaunchedEffect(Unit) { meetingViewModel.fetchAvailableMeetings(Destinations.MEETING_SCREEN_URL) }
    // Estructura de la vista/pantalla principal de la aplicacion.
    when (uiState) {
        is UiState.Loading -> ShowLoadingUiState()
        is UiState.Success -> ShowMeetingScreenSuccessUiState(uiState as UiState.Success, navController, meetingViewModel)
        is UiState.Error -> ShowMeetingScreenErrorUiState(uiState as UiState.Error, meetingViewModel)
        else -> {}
    }
}
@Composable
fun ShowMeetingScreenSuccessUiState(uiState: UiState.Success, navController: NavHostController, meetingViewModel: MeetingViewModel) {
    if ((uiState).successType == "screenInit") {
        if ((uiState).successScreenCallName == Destinations.MEETING_SCREEN_URL) {
            if ((uiState).successMethodName == "fetchAvailableMeetings") ShowMeetingScreen(navController, meetingViewModel)
        }
        if ((uiState).successScreenCallName == Destinations.CREATE_MEETING_SCREEN_URL) {
            if ((uiState).successMethodName == "fetchUnavailableDates") ShowCreateMeetingScreen(navController, meetingViewModel)
        }
        if ((uiState).successScreenCallName == Destinations.EDIT_MEETING_SCREEN_URL) {
            if ((uiState).successMethodName == "fetchUnavailableDates") ShowUpdateMeetingScreen(navController, meetingViewModel)
        }
    }
    if ((uiState).successType == "screenRunning") {
        if ((uiState).successScreenCallName == Destinations.CREATE_MEETING_SCREEN_URL) {
            if ((uiState).successMethodName == "createMeeting") {
                ShowAlertDialogComponent("¿ Desea crear otra tarea ?",
                    onAlertDialogDismissRequest = { meetingViewModel.fetchUnavailableDates((uiState).successScreenCallName) },
                    onAlertDialogButtonClick = { buttonValue ->
                        if (buttonValue == "No") navController.popBackStack(Destinations.MEETING_SCREEN_URL, false)
                        if (buttonValue == "Si") meetingViewModel.fetchUnavailableDates((uiState).successScreenCallName)
                    }
                )
            }
        }
        if ((uiState).successScreenCallName == Destinations.EDIT_MEETING_SCREEN_URL) {
            if ((uiState).successMethodName == "putMeeting") navController.popBackStack(Destinations.MEETING_SCREEN_URL, false)
            if ((uiState).successMethodName == "deleteMeeting") navController.popBackStack(Destinations.MEETING_SCREEN_URL, false)
        }
        //if ((uiState as UiState.Success).successMethodName == "fetchAvailableDatesOnDayByDate") INDICAR QUE SE MUESTRAN LAS TAREAS PARA LA FECHA INDICADA.
    }
}
@Composable
fun ShowMeetingScreenErrorUiState(uiState: UiState.Error, meetingViewModel: MeetingViewModel) {
    ApiErrorSnackbar(message = (uiState).errorMessage, onApiErrorSnackBarButtonRetry = {
        if ((uiState).errorType == "throwableErrorType") {
            if ((uiState).errorScreenCallName == Destinations.MEETING_SCREEN_URL) {
                if ((uiState).errorMethodName == "fetchAvailableMeetings") meetingViewModel.fetchAvailableMeetings((uiState).errorScreenCallName)
            }
            if ((uiState).errorScreenCallName == Destinations.CREATE_MEETING_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") meetingViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "createMeeting") meetingViewModel.createMeeting()
            }
            if ((uiState).errorScreenCallName == Destinations.EDIT_MEETING_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") meetingViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "putMeeting") meetingViewModel.putMeeting()
                if ((uiState).errorMethodName == "deleteMeeting") meetingViewModel.deleteMeeting()
            }
        }
        if ((uiState).errorType == "fetchDataErrorType") {
            if ((uiState).errorScreenCallName == Destinations.MEETING_SCREEN_URL) {
                if ((uiState).errorMethodName == "fetchAvailableMeetings") meetingViewModel.fetchAvailableMeetings((uiState).errorScreenCallName)
            }
            if ((uiState).errorScreenCallName == Destinations.CREATE_MEETING_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") meetingViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "createMeeting") meetingViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
            }
            if ((uiState).errorScreenCallName == Destinations.EDIT_MEETING_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") meetingViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "putMeeting") meetingViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "deleteMeeting") meetingViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
            }
        }
    })
    //if ((uiState).errorMethodName == "fetchAvailableTasksDatesByDate") cinemaViewModel.fetchAvailableTasksDatesByDate("artCreateCinemaScreen")
}

/**
 *
 */
@Composable
fun ShowMeetingScreen(navController: NavHostController, meetingViewModel: MeetingViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.bckmeetingwhite), contentDescription = null,
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
        )
        //.border(width = 2.dp, color = Color.Red)
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize().padding(10.dp)) {
            // Mostramos el titulo de la vista utilizando el componente correspondiente llamado 'ViewTitleComponent'.
            ViewTitleComponent(stringResource(R.string.meetingScreenViewName), Color.Black)
            // Mostramos el busador de salas por nombre llamado 'TextFieldSearcherComponent'.
            ShowTextFieldSearcherComponent(
                onValueSearcherChange = { meetingViewModel.fetchPaintingToSearch(it) },
                onCreateButtonClick = { navController.navigate(Destinations.CREATE_MEETING_SCREEN_URL) }
            )
            // Mostramos la lista en funcion de la variable 'showSearchedCinemas', la cual indica si hay que mostrar la lista de citas disponibles o la lista de las salas que busca el usuario.
            if (meetingViewModel.showSearchedMeetings) meetingViewModel.matchMeetingsList.data?.let { ShowMeetingList(it,
                onCardClick = { id -> meetingViewModel.fetchSelectedMeetingData(id); navController.navigate(Destinations.EDIT_MEETING_SCREEN_URL) })
            }
            else meetingViewModel.availableMeetingList.data?.let { ShowMeetingList(it,
                onCardClick = { id -> meetingViewModel.fetchSelectedMeetingData(id); navController.navigate(Destinations.EDIT_MEETING_SCREEN_URL) })
            }
        }
    }
}
/**
 *
 */
@Composable
fun ShowMeetingList(listToShow: List<MeetingModel>, onCardClick: (String) -> Unit) {
    //.border(width = 5.dp, color = Color.Green)
    LazyColumn(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)) {
        items (listToShow.count()) { meeting -> ShowMeetingData(listToShow, meeting, onCardClick = { id -> onCardClick(id) }) }
    }
}
/**
 *
 */
@Composable
fun ShowMeetingData(listToShow: List<MeetingModel>, meeting: Int, onCardClick: (String) -> Unit) {
    val outputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("es", "ES"))
    val formattedDate = LocalDateTime.parse(listToShow[meeting].fechaInicioReunion, DateTimeFormatter.ISO_DATE_TIME).format(outputFormatter)
    //.border(width = 5.dp, color = Color.Blue)
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.Black
        ), elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier = Modifier.fillMaxWidth().padding(30.dp, 10.dp).clickable(onClick = { onCardClick(listToShow[meeting]._id) })
    ) {
        ShowCustomData("Reunion: ${listToShow[meeting].tituloReunion}")
        ShowCustomData("Lugar: ${listToShow[meeting].lugarReunion}")
        ShowCustomData("Fecha: $formattedDate")
    }
}