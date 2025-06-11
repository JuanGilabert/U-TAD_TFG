package com.cronosdev.taskmanagerapp.ui.screens.art.music
//
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cronosdev.taskmanagerapp.R
import com.cronosdev.taskmanagerapp.data.model.art.MusicModel
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
/**
 *
 */
@Composable
fun MusicScreen(navController: NavHostController, musicViewModel: MusicViewModel) {
    // Variable que controla el estado de la vista/screen.
    val uiState by musicViewModel.musicScreenUiState.collectAsState()
    // Conjunto de acciones que se realizan al cargar la vista.
    LaunchedEffect(Unit) { musicViewModel.fetchAvailableMusics(Destinations.ART_MUSIC_SCREEN_URL) }
    // Estructura de la vista/pantalla principal de la aplicacion.
    when (uiState) {
        is UiState.Loading -> ShowLoadingUiState()
        is UiState.Success -> ShowMusicScreenSuccessUiState(uiState as UiState.Success, navController, musicViewModel)
        is UiState.Error -> ShowMusicScreenErrorUiState(uiState as UiState.Error, musicViewModel)
        else -> {}
    }
}
@Composable
fun ShowMusicScreenSuccessUiState(uiState: UiState.Success, navController: NavHostController, musicViewModel: MusicViewModel) {
    if ((uiState).successType == "screenInit") {
        if ((uiState).successScreenCallName == Destinations.ART_MUSIC_SCREEN_URL) {
            if ((uiState).successMethodName == "fetchAvailableMusics") ShowMusicScreen(navController, musicViewModel)
        }
        if ((uiState).successScreenCallName == Destinations.ART_CREATE_MUSIC_SCREEN_URL) {
            if ((uiState).successMethodName == "fetchUnavailableDates") ShowCreateMusicScreen(navController, musicViewModel)
        }
        if ((uiState).successScreenCallName == Destinations.ART_EDIT_MUSIC_SCREEN_URL) {
            if ((uiState).successMethodName == "fetchUnavailableDates") ShowUpdateMusicScreen(navController, musicViewModel)
        }
    }
    if ((uiState).successType == "screenRunning") {
        if ((uiState).successScreenCallName == Destinations.ART_CREATE_MUSIC_SCREEN_URL) {
            if ((uiState).successMethodName == "createMusic") {
                ShowAlertDialogComponent("¿ Desea crear otra tarea ?",
                    onAlertDialogDismissRequest = { musicViewModel.fetchUnavailableDates((uiState).successScreenCallName) },
                    onAlertDialogButtonClick = { buttonValue ->
                        if (buttonValue == "No") navController.popBackStack(Destinations.ART_MUSIC_SCREEN_URL, false)
                        if (buttonValue == "Si") musicViewModel.fetchUnavailableDates((uiState).successScreenCallName)
                    }
                )
            }
        }
        if ((uiState).successScreenCallName == Destinations.ART_EDIT_MUSIC_SCREEN_URL) {
            if ((uiState).successMethodName == "putMusic") navController.popBackStack(Destinations.ART_MUSIC_SCREEN_URL, false)
            if ((uiState).successMethodName == "deleteMusic") navController.popBackStack(Destinations.ART_MUSIC_SCREEN_URL, false)
        }
        //if ((uiState as UiState.Success).successMethodName == "fetchAvailableDatesOnDayByDate") INDICAR QUE SE MUESTRAN LAS TAREAS PARA LA FECHA INDICADA.
    }
}
@Composable
fun ShowMusicScreenErrorUiState(uiState: UiState.Error, musicViewModel: MusicViewModel) {
    ApiErrorSnackbar(message = (uiState).errorMessage, onApiErrorSnackBarButtonRetry = {
        if ((uiState).errorType == "throwableErrorType") {
            if ((uiState).errorScreenCallName == Destinations.ART_MUSIC_SCREEN_URL) {
                if ((uiState).errorMethodName == "fetchAvailableMusics") musicViewModel.fetchAvailableMusics((uiState).errorScreenCallName)
            }
            if ((uiState).errorScreenCallName == Destinations.ART_CREATE_MUSIC_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") musicViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "createMusic") musicViewModel.createMusic()
            }
            if ((uiState).errorScreenCallName == Destinations.ART_EDIT_MUSIC_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") musicViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "putMusic") musicViewModel.putMusic()
                if ((uiState).errorMethodName == "deleteMusic") musicViewModel.deleteMusic()
            }
        }
        if ((uiState).errorType == "fetchDataErrorType") {
            if ((uiState).errorScreenCallName == Destinations.ART_MUSIC_SCREEN_URL) {
                if ((uiState).errorMethodName == "fetchAvailableMusics") musicViewModel.fetchAvailableMusics((uiState).errorScreenCallName)
            }
            if ((uiState).errorScreenCallName == Destinations.ART_CREATE_MUSIC_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") musicViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "createMusic") musicViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
            }
            if ((uiState).errorScreenCallName == Destinations.ART_EDIT_MUSIC_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") musicViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "putMusic") musicViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "deleteMusic") musicViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
            }
        }
    })
    //if ((uiState).errorMethodName == "fetchAvailableTasksDatesByDate") cinemaViewModel.fetchAvailableTasksDatesByDate("artCreateCinemaScreen")
}
/**
 *
 */
@Composable
fun ShowMusicScreen(navController: NavHostController, musicViewModel: MusicViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.bckmusicwhite), contentDescription = null,
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
        )
        //.border(width = 2.dp, color = Color.Red)
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize().padding(10.dp)) {
            // Mostramos el titulo de la vista utilizando el componente correspondiente llamado 'ViewTitleComponent'.
            ViewTitleComponent(stringResource(R.string.musicScreenViewName), Color.Black)
            // Mostramos el busador de salas por nombre llamado 'TextFieldSearcherComponent'.
            ShowTextFieldSearcherComponent(
                onValueSearcherChange = { musicViewModel.fetchMusicToSearch(it) },
                onCreateButtonClick = { navController.navigate(Destinations.ART_CREATE_MUSIC_SCREEN_URL) }
            )
            //
            if (musicViewModel.showSearchedMusics) musicViewModel.matchMusicsList.data?.let { ShowMusicList(it,
                onCardClick = { id -> musicViewModel.fetchSelectedMusicData(id); navController.navigate(Destinations.ART_EDIT_MUSIC_SCREEN_URL) })
            }
            else musicViewModel.availableMusicsList.data?.let { ShowMusicList(it,
                onCardClick = { cinemaId -> musicViewModel.fetchSelectedMusicData(cinemaId); navController.navigate(Destinations.ART_EDIT_MUSIC_SCREEN_URL) })
            }
        }
    }
}
/**
 *
 */
@Composable
fun ShowMusicList(listToShow: List<MusicModel>, onCardClick: (String) -> Unit) {
    //.border(width = 5.dp, color = Color.Green)
    LazyColumn(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)) {
        items (listToShow.count()) { music -> ShowMusicData(listToShow, music, onCardClick = { id -> onCardClick(id) }) }
    }
}
/**
 *
 */
@Composable
fun ShowMusicData(listToShow: List<MusicModel>, music: Int, onCardClick: (String) -> Unit) {
    val outputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("es", "ES"))
    val formattedDate = LocalDateTime.parse(listToShow[music].fechaInicioEvento, DateTimeFormatter.ISO_DATE_TIME).format(outputFormatter)
    //.border(width = 5.dp, color = Color.Blue)
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.Black
        ), elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier = Modifier.fillMaxWidth().padding(30.dp, 10.dp).clickable(onClick = { onCardClick(listToShow[music]._id) })
    ) {
        ShowCustomData("Nombre: ${listToShow[music].nombreEvento}")
        ShowCustomData("Dirección: ${listToShow[music].lugarEvento}")
        ShowCustomData("Fecha: $formattedDate")
    }
}