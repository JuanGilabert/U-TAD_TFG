package com.cronosdev.taskmanagerapp.ui.screens.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.cronosdev.taskmanagerapp.ui.components.outlinedComponents.buttons.ShowCreateScreenButtons
import com.cronosdev.taskmanagerapp.ui.components.outlinedComponents.textFields.ShowOutlinedTextFieldUpdateDataCard
import com.cronosdev.taskmanagerapp.ui.components.ViewTitleComponent
import com.cronosdev.taskmanagerapp.ui.navigation.Destinations
import com.cronosdev.taskmanagerapp.ui.screens.signin.AuthViewModel
import com.cronosdev.taskmanagerapp.ui.state.ShowLoadingUiState
import com.cronosdev.taskmanagerapp.ui.state.UiState
import com.cronosdev.taskmanagerapp.ui.state.error.AuthScreensErrors.ShowErrorForSignupScreen
import com.cronosdev.taskmanagerapp.ui.state.success.AuthScreensSuccess.ShowSuccessForSignupScreen

@Composable
fun SignupScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    // Variables para el control del cambio Entre Activities y para el estado de la UI.
    val uiState by authViewModel.signupScreenUiState.collectAsState()
    // Conjunto de acciones que se realizan al cargar la vista.
    LaunchedEffect(Unit) {
        authViewModel.fetchSignupScreenUIState(
            UiState.Success("screenInit", Destinations.SIGNUP_SCREEN_URL, "startSigningUp", "")
        )
    }
    // Estructura de la vista/pantalla principal de la aplicacion.
    when (uiState) {
        is UiState.Loading -> ShowLoadingUiState()
        is UiState.Success -> ShowSuccessForSignupScreen(
            uiState as UiState.Success, onScreenSuccessEvent = {
                if ( it == "startSigningUp") ShowSignupScreen(navController, authViewModel)
                if ( it == "signup") navController.popBackStack(Destinations.SIGNIN_SCREEN_URL, false)
            }
        )
        is UiState.Error -> ShowErrorForSignupScreen(
            uiState as UiState.Error, onScreenErrorEvent = { errorEvent ->
                if (errorEvent == "throwableErrorType") authViewModel.signup(Destinations.SIGNUP_SCREEN_URL)
                if (errorEvent == "fetchDataErrorType") authViewModel.fetchSignupScreenUIState(
                    UiState.Success("screenInit", Destinations.SIGNUP_SCREEN_URL, "startSigningUp", "")
                )
            }
        )
        else -> {}
    }
}

@Composable
fun ShowSignupScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.bcksignupwhite), contentDescription = null,
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
        )
        //.border(width = 2.dp, color = Color.Red) AL SER LA VISTA DE CREATE USER TENDRA 10.dp MAS DE PADDING QUE EL RESTO DE VISTAS DE TIPO CREAR
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize().padding(20.dp)) {
            // Mostramos el titulo de la vista utilizando el componente correspondiente llamado 'ViewTitleComponent'.
            ViewTitleComponent(stringResource(R.string.signupScreenViewName), Color.Black)
            //
            ShowSignupScreenCards(authViewModel, Modifier.weight(20F))
            // SIGNUP BUTTON
            ShowCreateScreenButtons { userAction ->
                if (userAction == "back") navController.popBackStack(Destinations.SIGNIN_SCREEN_URL, false)
                if (userAction == "create") authViewModel.signup(Destinations.SIGNUP_SCREEN_URL)
            }
        }
    }
}
/**
 *
 */
@Composable
fun ShowSignupScreenCards(authViewModel: AuthViewModel, modifier: Modifier) {
    //.border(width = 2.dp, color = Color.Blue)
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(15.dp, 10.dp)) {
        Spacer(modifier = Modifier.height(235.dp))
        // NAME
        ShowOutlinedTextFieldUpdateDataCard(
            "signup",
            "Nombre",
            "Introduzca el nombre de usuario",
            ""
        ) { authViewModel.setUserName(it) }
        Spacer(modifier = Modifier.height(40.dp))//25
        // EMAIL
        ShowOutlinedTextFieldUpdateDataCard(
            "signup",
            "Email",
            "Introduzca el email de usuario",
            ""
        ) { authViewModel.setUserEmail(it) }
        Spacer(modifier = Modifier.height(40.dp))//25
        // PASSWORD
        ShowOutlinedTextFieldUpdateDataCard(
            "signup",
            "Contraseña",
            "Introduzca la contraseña de usuario",
            ""
        ) { authViewModel.setUserPassword(it) }
        Spacer(modifier = Modifier.height(40.dp))
        Spacer(modifier = Modifier.height(210.dp))
    }
}