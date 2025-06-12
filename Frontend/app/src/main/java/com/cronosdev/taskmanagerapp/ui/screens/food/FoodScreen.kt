package com.cronosdev.taskmanagerapp.ui.screens.food

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
import com.cronosdev.taskmanagerapp.data.model.food.FoodModel
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
fun FoodScreen (navController: NavHostController, foodViewModel: FoodViewModel) {
    // Variable que controla el estado de la vista/screen.
    val uiState by foodViewModel.foodScreenUiState.collectAsState()
    // Conjunto de acciones que se realizan al cargar la vista.
    LaunchedEffect(Unit) {
        foodViewModel.setFoodVariables()
        foodViewModel.setShowSearchedFoods(false)
        foodViewModel.fetchAvailableFoods(Destinations.FOOD_SCREEN_URL)
    }
    // Estructura de la vista/pantalla principal de la aplicacion.
    when (uiState) {
        is UiState.Loading -> ShowLoadingUiState()
        is UiState.Success -> ShowFoodScreenSuccessUiState(uiState as UiState.Success, navController, foodViewModel)
        is UiState.Error -> ShowFoodScreenErrorUiState(uiState as UiState.Error, foodViewModel)
        else -> {}
    }
}
@Composable
fun ShowFoodScreenSuccessUiState(uiState: UiState.Success, navController: NavHostController, foodViewModel: FoodViewModel) {
    if ((uiState).successType == "screenInit") {
        if ((uiState).successScreenCallName == Destinations.FOOD_SCREEN_URL) {
            if ((uiState).successMethodName == "fetchAvailableFoods") ShowFoodScreen(navController, foodViewModel)
        }
        if ((uiState).successScreenCallName == Destinations.CREATE_FOOD_SCREEN_URL) {
            if ((uiState).successMethodName == "fetchUnavailableDates") ShowCreateFoodScreen(navController, foodViewModel)
        }
        if ((uiState).successScreenCallName == Destinations.UPDATE_FOOD_SCREEN_URL) {
            if ((uiState).successMethodName == "fetchUnavailableDates") ShowUpdateFoodScreen(navController, foodViewModel)
        }
    }
    if ((uiState).successType == "screenRunning") {
        //if ((uiState).successScreenCallName == Destinations.FOOD_SCREEN_URL) {}
        if ((uiState).successScreenCallName == Destinations.CREATE_FOOD_SCREEN_URL) {
            if ((uiState).successMethodName == "createFood") {
                ShowAlertDialogComponent("¿ Desea crear otra tarea ?",
                    onAlertDialogDismissRequest = { foodViewModel.fetchUnavailableDates((uiState).successScreenCallName) },
                    onAlertDialogButtonClick = { buttonValue ->
                        if (buttonValue == "No") navController.popBackStack(Destinations.FOOD_SCREEN_URL, false)
                        if (buttonValue == "Si") foodViewModel.fetchUnavailableDates((uiState).successScreenCallName)
                    }
                )
            }
        }
        if ((uiState).successScreenCallName == Destinations.UPDATE_FOOD_SCREEN_URL) {
            if ((uiState).successMethodName == "putFood") navController.popBackStack(Destinations.FOOD_SCREEN_URL, false)
            if ((uiState).successMethodName == "deleteFood") navController.popBackStack(Destinations.FOOD_SCREEN_URL, false)
        }
        //if ((uiState as UiState.Success).successMethodName == "fetchAvailableDatesOnDayByDate") INDICAR QUE SE MUESTRAN LAS TAREAS PARA LA FECHA INDICADA.
    }
}
@Composable
fun ShowFoodScreenErrorUiState(uiState: UiState.Error, foodViewModel: FoodViewModel) {
    ApiErrorSnackbar(message = (uiState).errorMessage, onApiErrorSnackBarButtonRetry = {
        if ((uiState).errorType == "throwableErrorType") {
            if ((uiState).errorScreenCallName == Destinations.FOOD_SCREEN_URL) {
                if ((uiState).errorMethodName == "fetchAvailableFoods") foodViewModel.fetchAvailableFoods((uiState).errorScreenCallName)
            }
            if ((uiState).errorScreenCallName == Destinations.CREATE_FOOD_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") foodViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "createFood") foodViewModel.createFood()
            }
            if ((uiState).errorScreenCallName == Destinations.UPDATE_FOOD_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") foodViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "putFood") foodViewModel.putFood()
                if ((uiState).errorMethodName == "deleteFood") foodViewModel.deleteFood()
            }
        }
        if ((uiState).errorType == "fetchDataErrorType") {
            if ((uiState).errorScreenCallName == Destinations.FOOD_SCREEN_URL) {
                if ((uiState).errorMethodName == "fetchAvailableFoods") foodViewModel.fetchAvailableFoods((uiState).errorScreenCallName)
            }
            if ((uiState).errorScreenCallName == Destinations.CREATE_FOOD_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") foodViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "createFood") foodViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
            }
            if ((uiState).errorScreenCallName == Destinations.UPDATE_FOOD_SCREEN_URL) {
                if ((uiState ).errorMethodName == "fetchUnavailableDates") foodViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "putFood") foodViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
                if ((uiState).errorMethodName == "deleteFood") foodViewModel.fetchUnavailableDates((uiState).errorScreenCallName)
            }
        }
    })
    //if ((uiState).errorMethodName == "fetchAvailableTasksDatesByDate") cinemaViewModel.fetchAvailableTasksDatesByDate("artCreateCinemaScreen")
}

/**
 *
 */
@Composable
fun ShowFoodScreen(navController: NavHostController, foodViewModel: FoodViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.bckfoodwhite), contentDescription = null,
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
        )
        //.border(width = 2.dp, color = Color.Red)
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize().padding(10.dp)) {
            // Mostramos el titulo de la vista utilizando el componente correspondiente llamado 'ViewTitleComponent'.
            ViewTitleComponent(stringResource(R.string.foodScreenViewName), Color.Black)
            // Mostramos el busador de salas por nombre llamado 'TextFieldSearcherComponent'.
            ShowTextFieldSearcherComponent(
                onValueSearcherChange = { foodViewModel.fetchFoodToSearch(it) },
                onCreateButtonClick = { navController.navigate(Destinations.CREATE_FOOD_SCREEN_URL) }
            )
            if (foodViewModel.showSearchedFoods) foodViewModel.matchFoodsList.data?.let { ShowPaintingList(it,
                onCardClick = { id -> foodViewModel.fetchSelectedPaintingData(id); navController.navigate(Destinations.UPDATE_FOOD_SCREEN_URL) })
            }
            else foodViewModel.availableFoodsList.data?.let { ShowPaintingList(it,
                onCardClick = { id -> foodViewModel.fetchSelectedPaintingData(id); navController.navigate(Destinations.UPDATE_FOOD_SCREEN_URL) })
            }
        }
    }
}
/**
 *
 */
@Composable
fun ShowPaintingList(listToShow: List<FoodModel>, onCardClick: (String) -> Unit) {
    //.border(width = 5.dp, color = Color.Green)
    LazyColumn(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)) {
        items (listToShow.count()) { food -> ShowPaintingData(listToShow, food, onCardClick = { id -> onCardClick(id) }) }
    }
}
/**
 *
 */
@Composable
fun ShowPaintingData(listToShow: List<FoodModel>, food: Int, onCardClick: (String) -> Unit) {
    val outputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("es", "ES"))
    val formattedDate = LocalDateTime.parse(listToShow[food].fechaReserva, DateTimeFormatter.ISO_DATE_TIME).format(outputFormatter)
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.Black
        ), elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier = Modifier.fillMaxWidth().padding(30.dp, 10.dp).clickable(onClick = { onCardClick(listToShow[food]._id) })
    ) {
        ShowCustomData("Restaurante: ${listToShow[food].nombreRestaurante}")
        ShowCustomData("Dirección: ${listToShow[food].direccionRestaurante}")
        ShowCustomData("Reserva: $formattedDate")
    }
}