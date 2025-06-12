package com.cronosdev.taskmanagerapp.ui.components.outlinedComponents.buttons

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.cronosdev.taskmanagerapp.R

//// BUTTONS COLORS
/**
 *
 */
@Composable // UTILIZAR ESTO PARA LA MEJORA DEL BOTON DEL TextFieldSercherComponent de las vistas
fun getOutlinedButtonColor(colorTypeByScreenType: String, colorTypeByButtonType: String = "buttonDefaults"): ButtonColors =
    when (colorTypeByScreenType) {
        "create" -> getCreateScreenOutlinedButtonColor(colorTypeByButtonType)
        "update" -> getUpdateScreenOutlinedButtonColor(colorTypeByButtonType)
        // esto esta habilitado para el TextFieldSercherComponent.
        else -> getReadScreenOutlinedButtonColor(colorTypeByButtonType)
    }
/**
 *
 */
@Composable
private fun getReadScreenOutlinedButtonColor(buttonType: String): ButtonColors {
    return ButtonDefaults.outlinedButtonColors(
        containerColor = Color(0xFF1F3A5F),
        contentColor = Color.LightGray,
        disabledContainerColor = Color.Gray,
        disabledContentColor = Color.DarkGray
    )
}
/**
 *
 */
@Composable
private fun getCreateScreenOutlinedButtonColor(buttonType: String): ButtonColors {
    val backButtonValue = stringResource(R.string.backButtonValue)
    val createButtonValue = stringResource(R.string.createButtonValue)
    // Devolvemos el color en funcion del tipo de vista y tipo de boton.
    return when (buttonType) {
        backButtonValue -> ButtonDefaults.outlinedButtonColors(
            containerColor = Color(0xFF1F3A5F),
            contentColor = Color.LightGray,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.DarkGray
        )
        createButtonValue -> ButtonDefaults.outlinedButtonColors(
            // 0xFF1db954 || 0xFF1aa64c || 0xFF168d42 || 0xFF2E7D32 || 0xFF1B5E20
            containerColor = Color(0xFF1B4E20),
            contentColor = Color.White,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.DarkGray
        )
        else -> ButtonDefaults.buttonColors()
    }
}
/**
 *
 */
@Composable
private fun getUpdateScreenOutlinedButtonColor(buttonType: String): ButtonColors {
    val backButtonValue = stringResource(R.string.backButtonValue)
    val updateButtonValue = stringResource(R.string.updateButtonValue)
    val deleteButtonValue = stringResource(R.string.deleteButtonValue)
    // Devolvemos el color en funcion del tipo de vista y tipo de boton.
    return when (buttonType) {
        backButtonValue -> ButtonDefaults.outlinedButtonColors(
            containerColor = Color(0xFF1F3A5F),
            contentColor = Color.LightGray,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.DarkGray
        )
        updateButtonValue -> ButtonDefaults.outlinedButtonColors(
            // 0xFF1db954 || 0xFF1aa64c || 0xFF168d42 || 0xFF2E7D32 || 0xFF1B5E20
            containerColor = Color(0xFF1B4E20),
            contentColor = Color.LightGray,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.DarkGray
        )
        deleteButtonValue -> ButtonDefaults.outlinedButtonColors(
            containerColor = Color(0xFF6D1616),//0xFFB91D1D || 0xFFA61A1A || 0xFF6D1616
            contentColor = Color.LightGray,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.DarkGray
        )
        else -> ButtonDefaults.buttonColors()
    }
}