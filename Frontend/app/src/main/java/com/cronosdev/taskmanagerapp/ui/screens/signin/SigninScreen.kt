package com.cronosdev.taskmanagerapp.ui.screens.signin

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
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
    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo difuminada. Usa una imagen oscura de fondo
        Image(painter = painterResource(id = R.drawable.bcksigninblue), contentDescription = null,
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
        )
        //.border(width = 2.dp, color = Color.Red)
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(20.dp)) {
            /* Logo
            Image(painter = painterResource(id = R.drawable.logotaskmanager1),
                contentDescription = "Logo", modifier = Modifier.size(120.dp)//.border(width = 2.dp, color = Color.Green)
            */
            Spacer(modifier = Modifier.height(16.dp).background(color = Color.Blue))
            // TITLE
            Text(text = "Task Manager", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White,
                //modifier = Modifier.border(width = 2.dp, color = Color.Green)
            )
            Spacer(modifier = Modifier.height(40.dp).border(width = 5.dp, color = Color.Blue))
            // EMAIL
            ShowOutlinedTextFieldUpdateDataCard(
                "signin",
                "Email",
                "Introduzca el email",
                ""
            ) { authViewModel.setUserEmail(it) }
            Spacer(modifier = Modifier.height(20.dp).border(width = 5.dp, color = Color.Blue))
            // PASSWORD
            ShowOutlinedUserPasswordTextField("Contraseña", onOutlinedTextFieldValueChange = { authViewModel.setUserPassword(it) })
            Spacer(modifier = Modifier.height(40.dp).border(width = 5.dp, color = Color.Blue))
            // SIGNIN BUTTON
            ShowLinearGradientSigninButton(onLinearGradientLoginButtonClick = { authViewModel.signin(Destinations.SIGNIN_SCREEN_URL) })
            // CREATE ACCOUNT BUTTON
            Spacer(modifier = Modifier.height(35.dp).border(width = 5.dp, color = Color.Blue))
            ShowCreateAccountTextButton(onTextButtonClick = { navController.navigate(Destinations.SIGNUP_SCREEN_URL) })
            // FORGET PASSWORD
            //TextButton(onClick = { /* lógica de recuperación */ }) { Text("¿Olvidaste tu contraseña?", color = Color.White) }
            //
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
/**
 *
 */
@Composable
fun ShowOutlinedUserPasswordTextField(textFieldLabelToShow: String, onOutlinedTextFieldValueChange: (String) -> Unit) {
    //
    var passwordVisible by remember { mutableStateOf(false) }
    var userPassword by remember { mutableStateOf("") }
    //
    OutlinedTextField(value = userPassword, onValueChange = { userPassword = it },
        textStyle = TextStyle(
            fontSize = 16.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.W400,
            fontFamily = FontFamily.SansSerif,
            letterSpacing = 0.25.sp
        ),
        label = {
            Text(
                text = textFieldLabelToShow,
                fontSize = 14.sp,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.W400,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = 0.25.sp
            )
        },
        placeholder = {
            Text(
                text = "Introduzca la contraseña",
                fontSize = 16.sp,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.W400,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = 0.25.sp,
                color = Color(0xFF94A3B8)
            )
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), shape = RoundedCornerShape(12.dp),
        //leadingIcon = { Icon(imageVector = Icons.Default.Password, contentDescription = null, tint = Color.Black) },
        modifier = Modifier.fillMaxWidth().padding(20.dp, 12.dp)
            .onFocusChanged { focusState -> if (!focusState.isFocused) { onOutlinedTextFieldValueChange(userPassword); } },
        colors = OutlinedTextFieldDefaults.colors(
            // Color del Borde (0xFFecf0f1 blanco grisaceo) OK
            focusedBorderColor = Color(0xFF60A5FA),
            unfocusedBorderColor = Color(0xFF334155),
            // Color del Label REV
            focusedLabelColor = Color(0xFFfdfefe),
            unfocusedLabelColor = Color(0xFFfdfefe),
            // Color del Cursor OK
            cursorColor = Color(0xFFFFFFFF),
            // Color del Text OK
            focusedTextColor = Color(0xFFFFFFFF),
            unfocusedTextColor = Color(0xFFFFFFFF),
            // Color del Placeholder
            focusedPlaceholderColor = Color(0xFF94A3B8),
            unfocusedPlaceholderColor = Color(0xFF94A3B8),
            // Color del Container
            focusedContainerColor = Color(0xFF1E293B),
            unfocusedContainerColor = Color(0xFF1E293B)
        ),
        trailingIcon = {
            val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
            val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, contentDescription = description, tint = Color(0xFFCBD5E1))
            }
        },
    )
}
/**
 *
 */
@Composable
fun ShowLinearGradientSigninButton(onLinearGradientLoginButtonClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    //
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth().padding(20.dp, 12.dp)
        .height(50.dp).shadow(8.dp, RoundedCornerShape(16.dp)) //0xFF2563EB, 0xFF1E3A8A
        .background(brush = Brush.horizontalGradient(colors = listOf(Color(0xFF1D4ED8), Color(0xFF172554))), shape = RoundedCornerShape(16.dp))
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
    TextButton(onClick = { onTextButtonClick() }) { Text("Crear cuenta", color = Color.White) }
}