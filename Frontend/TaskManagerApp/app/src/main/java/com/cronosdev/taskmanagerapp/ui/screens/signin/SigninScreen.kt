package com.cronosdev.taskmanagerapp.ui.screens.signin

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cronosdev.taskmanagerapp.MainActivity
import com.cronosdev.taskmanagerapp.R
import com.cronosdev.taskmanagerapp.ui.components.outlinedComponents.textFields.ShowOutlinedTextFieldUpdateDataCard
import com.cronosdev.taskmanagerapp.ui.components.showToastMessage
import com.cronosdev.taskmanagerapp.ui.state.ShowLoadingUiState
import com.cronosdev.taskmanagerapp.ui.state.UiState
import com.cronosdev.taskmanagerapp.ui.navigation.Destinations
import com.cronosdev.taskmanagerapp.ui.state.error.AuthScreensErrors.ShowErrorForSigninScreen
import com.cronosdev.taskmanagerapp.ui.state.success.AuthScreensSuccess.ShowSuccessForSigninScreen
/**
 *
 */
@Composable
fun SigninScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    // Variables para el control del cambio Entre Activities y para el estado de la UI.
    val uiState by authViewModel.signinScreenUiState.collectAsState()
    val context = LocalContext.current
    val intent = Intent(context, MainActivity::class.java)
    // Conjunto de acciones que se realizan al cargar la vista.
    LaunchedEffect(Unit) {
        authViewModel.fetchSigninScreenUIState(
            UiState.Success("screenInit", Destinations.SIGNIN_SCREEN_URL, "startSigning", "")
        )
    }
    // Estructura de la vista/pantalla principal de la aplicacion.
    when (uiState) {
        is UiState.Loading -> ShowLoadingUiState()
        is UiState.Success -> ShowSuccessForSigninScreen(
            uiState as UiState.Success, onScreenSuccessEvent = {
                if ( it == "startSigning") ShowSigninScreen(navController, authViewModel)
                if ( it == "signin") {
                    showToastMessage(context, (uiState as UiState.Success).successMessage)
                    // Iniciamos el MainActivity y finalizamos el activity actual.(AuthActivity)
                    context.startActivity(intent)
                    (context as? ComponentActivity)?.finish()
                }
            }
        )
        is UiState.Error -> ShowErrorForSigninScreen(
            uiState as UiState.Error, onScreenErrorEvent = { errorEvent ->
                if (errorEvent == "throwableErrorType") authViewModel.signin(Destinations.SIGNIN_SCREEN_URL)
                if (errorEvent == "fetchDataErrorType") authViewModel.fetchSigninScreenUIState(
                    UiState.Success("screenInit", Destinations.SIGNIN_SCREEN_URL, "startSigning", "")
                )
            }
        )
        else -> {}
    }
}
/**
 *
 */
@Composable
fun ShowSigninScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Imagen de fondo difuminada. Usa una imagen oscura de fondo
        Image(painter = painterResource(id = R.drawable.loginbackground), contentDescription = null,
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize().alpha(0.35f)
        )
        //.border(width = 2.dp, color = Color.Red)
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(20.dp).border(width = 2.dp, color = Color.Red)) {
            // Logo
            Image(painter = painterResource(id = R.drawable.logotaskmanager1),
                contentDescription = "Logo", modifier = Modifier.size(120.dp).border(width = 2.dp, color = Color.Green)
            )
            Spacer(modifier = Modifier.height(16.dp).background(color = Color.Blue))
            Text(text = "Task Manager", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White,
                modifier = Modifier.border(width = 2.dp, color = Color.Green)
            )
            Spacer(modifier = Modifier.height(40.dp).border(width = 5.dp, color = Color.Blue))
            // EMAIL
            ShowOutlinedTextFieldUpdateDataCard(
                "signup",
                "Email",
                "Introduzca el email de usuario",
                ""
            ) { authViewModel.setUserEmail(it) }
            Spacer(modifier = Modifier.height(20.dp).border(width = 5.dp, color = Color.Blue))
            // PASSWORD
            ShowOutlinedUserPasswordTextField(authViewModel.userPassword, onOutlinedTextFieldValueChange = { authViewModel.setUserPassword(it) })
            Spacer(modifier = Modifier.height(40.dp).border(width = 5.dp, color = Color.Blue))
            // LOGIN BUTTON
            ShowLinearGradientSigninButton(onLinearGradientLoginButtonClick = { authViewModel.signin(Destinations.SIGNIN_SCREEN_URL) })
            // CREATE ACCOUNT
            Spacer(modifier = Modifier.height(35.dp).border(width = 5.dp, color = Color.Blue))
            ShowCreateAccountTextButton(onTextButtonClick = { navController.navigate(Destinations.SIGNUP_SCREEN_URL) })
            // FORGET PASSWORD
            TextButton(onClick = { /* lógica de recuperación */ }) { Text("¿Olvidaste tu contraseña?", color = Color.White) }
            //
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
/**
 *
 */
@Composable
fun ShowOutlinedUserValueTextField(outlinedTextFieldValueToShow: String, onOutlinedTextFieldValueChange:(String) -> Unit) {
    var textFieldValue by remember { mutableStateOf(outlinedTextFieldValueToShow) }
    OutlinedTextField(value = textFieldValue, onValueChange = { textFieldValue = it },
        label = { Text("Email", color = Color.White) },
        shape = RoundedCornerShape(12.dp),colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Black,
            unfocusedIndicatorColor = Color.Black, focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black, cursorColor = Color.Black
        ),
        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = Color.Black) },
        trailingIcon = {
            IconButton(onClick = { textFieldValue  = "" }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Limpiar texto",
                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
        }, modifier = Modifier.fillMaxWidth().background(Color(0x66FFFFFF), RoundedCornerShape(12.dp)).border(width = 2.dp, color = Color.Green)
            .onFocusChanged { focusState -> if (!focusState.isFocused) { onOutlinedTextFieldValueChange(textFieldValue); } }
    )
}
/**
 *
 */
@Composable
fun ShowOutlinedUserPasswordTextField(outlinedTextFieldValuetoShow: String, onOutlinedTextFieldValueChange: (String) -> Unit) {
    //
    var passwordVisible by remember { mutableStateOf(false) }
    var userPassword by remember { mutableStateOf(outlinedTextFieldValuetoShow) }
    //
    OutlinedTextField(value = userPassword, onValueChange = { userPassword = it }, label = { Text("Contraseña", color = Color.White) },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        leadingIcon = { Icon(imageVector = Icons.Default.Password, contentDescription = null, tint = Color.Black) },
        shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().background(
            Color(0x66FFFFFF), RoundedCornerShape(12.dp)
        ).border(width = 2.dp, color = Color.Green).onFocusChanged { focusState -> if (!focusState.isFocused) { onOutlinedTextFieldValueChange(userPassword); } },
        colors = TextFieldDefaults.colors(focusedIndicatorColor = Color.Black,
            unfocusedIndicatorColor = Color.Black, focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black, cursorColor = Color.Black
        ),
        trailingIcon = {
            val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
            val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
            IconButton(onClick = { passwordVisible = !passwordVisible }) { Icon(imageVector = image, contentDescription = description) }
        },
    )
}
/**
 *
 */
@Composable
fun ShowLinearGradientSigninButton(onLinearGradientLoginButtonClick: () -> Unit) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth().height(50.dp).shadow(8.dp, RoundedCornerShape(16.dp))
        .background(brush = Brush.horizontalGradient(colors = listOf(Color(0xFF2196F3), Color(0xFF0D47A1))), shape = RoundedCornerShape(16.dp))
        .clickable { onLinearGradientLoginButtonClick() },
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.AutoMirrored.Filled.Login, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Iniciar sesión", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
/**
 *
 */
@Composable
fun ShowCreateAccountTextButton(onTextButtonClick: () -> Unit) {
    TextButton(onClick = { onTextButtonClick() }, modifier = Modifier.border(width = 2.dp, color = Color.Green)) { Text("Crear cuenta", color = Color.White) }
}