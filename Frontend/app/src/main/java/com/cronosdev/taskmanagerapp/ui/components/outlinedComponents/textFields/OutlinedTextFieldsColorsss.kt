package com.cronosdev.taskmanagerapp.ui.components.outlinedComponents.textFields

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

//// TEXTFIELD COLORS
@Composable/* ESTO ES PUBLICO SIEMPRE, EL RESTO NO */
fun getOutlinedTextFieldColor(screenCallType: String): TextFieldColors =
    when (screenCallType) {
        "create" -> getCreateScreenOutlinedTextFieldColor()
        "update" -> getUpdateScreenOutlinedTextFieldColor()
        "signup" -> getSignupScreenOutlinedTextFieldColor()
        "signin" -> getSigninScreenOutlinedTextFieldColor()
        // inicializar con un valor las variables outlinedTextFieldColorTypeToShow de los ShowOutlinedTextField
        else -> OutlinedTextFieldDefaults.colors()
    }
/** REVISAR EL COLOR DE FONDO DE LOS PLACEHOLDER CUANDO ESTAN CON O SIN FOCO
 *
 */
@Composable
private fun getCreateScreenOutlinedTextFieldColor(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        // Color del Borde OK
        focusedBorderColor = Color(0xFF4285F5),
        unfocusedBorderColor = Color(0xFF4285F5),
        // Color del Label OK
        focusedLabelColor = Color(0xFFfdfefe),
        unfocusedLabelColor = Color(0xFFfdfefe),
        // Color del Cursor OK
        cursorColor = Color(0xFFFFFFFF),
        // Color del Text OK
        focusedTextColor = Color(0xFFFFFFFF),
        unfocusedTextColor = Color(0xFFFFFFFF),
        // Color del Placeholder OK
        focusedPlaceholderColor = Color(0xFFFFFFFF),
        unfocusedPlaceholderColor = Color.LightGray,
        // Color del Container OK
        focusedContainerColor = Color(0xFF42A5F5),
        unfocusedContainerColor = Color(0xFF42A5F5)
    )
}
/** REVISAR EL COLOR DE FONDO DE LOS PLACEHOLDER CUANDO ESTAN CON O SIN FOCO (MaterialTheme.colorScheme.onPrimary)
 *
 */
@Composable
private fun getUpdateScreenOutlinedTextFieldColor(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        // Color del Borde (0xFFecf0f1 blanco grisaceo) OK
        focusedBorderColor = Color.LightGray,
        unfocusedBorderColor = Color.LightGray,
        // Color del Label (0xFFfdfefe blanco puro se supone) OK
        focusedLabelColor = Color(0xFFfdfefe),
        unfocusedLabelColor = Color(0xFFfdfefe),
        // Color del Cursor (0xFFfdfefe blanco puro se supone) OK
        cursorColor = Color(0xFFfdfefe),
        // Color del Text OK
        focusedTextColor = Color(0xFFfdfefe),
        unfocusedTextColor = Color.LightGray,
        // Color del Placeholder OK
        focusedPlaceholderColor = Color.LightGray,
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
        // Color del Container
        focusedContainerColor = Color(0xFF1C1C1E),// Color del contenedor cuando tiene el foco
        unfocusedContainerColor = Color(0xFF1C1C1E) // Fondo oscuro para que luzca moderno
    )
}
/** REVISAR EL COLOR DE FONDO DE LOS PLACEHOLDER CUANDO ESTAN CON O SIN FOCO
 *
 */
@Composable
private fun getSignupScreenOutlinedTextFieldColor(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        /* Color del Borde
        focusedBorderColor = Color(0xFF333333),
        unfocusedBorderColor = Color(0xFFAAAAAA),
        // Color del Label REV
        focusedLabelColor = Color(0xFF333333),
        unfocusedLabelColor = Color(0xFF333333),//0xFFfdfefe
        // Color del Cursor OK
        cursorColor = Color(0xFF000000),
        // Color del Text OK
        focusedTextColor = Color(0xFF000000),
        unfocusedTextColor = Color(0xFF000000),
        // Color del Placeholder
        focusedPlaceholderColor = Color(0xFF000000),
        unfocusedPlaceholderColor = Color.LightGray,
        // Color del Container
        focusedContainerColor = Color(0xFFF9F9F9), //Color.Transparent
        unfocusedContainerColor = Color(0xFFF9F9F9)
        */
        // Color del Borde
        focusedBorderColor = Color(0xFF000000),
        unfocusedBorderColor = Color(0xFFAAAAAA),
        // Color del Label REV
        focusedLabelColor = Color(0xFF000000),
        unfocusedLabelColor = Color(0xFF333333),//0xFFfdfefe
        // Color del Cursor OK
        cursorColor = Color(0xFF333333),
        // Color del Text OK
        focusedTextColor = Color(0xFF333333),
        unfocusedTextColor = Color(0xFF333333),
        // Color del Placeholder
        focusedPlaceholderColor = Color(0xFF333333),
        unfocusedPlaceholderColor = Color.LightGray,
        // Color del Container
        focusedContainerColor = Color(0xFFF9F9F9), //Color.Transparent
        unfocusedContainerColor = Color(0xFFF9F9F9)
    )
}
/** REVISAR EL COLOR DE FONDO DE LOS PLACEHOLDER CUANDO ESTAN CON O SIN FOCO
 *
 */
@Composable
private fun getSigninScreenOutlinedTextFieldColor(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
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
    )
}