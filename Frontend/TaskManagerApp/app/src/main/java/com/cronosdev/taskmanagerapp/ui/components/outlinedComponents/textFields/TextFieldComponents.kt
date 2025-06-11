package com.cronosdev.taskmanagerapp.ui.components.outlinedComponents.textFields
//
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.StateFlow
//// TextField Components
/**
 *
 */
@Composable
fun ShowOutlinedTextFieldUpdateDataCard(
    textFieldColorTypeToShow: String,
    textFieldLabelToShow: String = "",
    textFieldPlaceholderToShow: String = "",
    textFieldValueToUpdate: String = "",
    onTextFieldValueChange:(String) -> Unit
) {
    var textFieldValue by remember { mutableStateOf(textFieldValueToUpdate) }
    var hasTextFieldValueChange by remember { mutableStateOf(false) }
    OutlinedTextField(value = textFieldValue,
        onValueChange = { textFieldValue = it; hasTextFieldValueChange = true },
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
                text = textFieldPlaceholderToShow,
                fontSize = 16.sp,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.W400,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = 0.25.sp
                )
        },
        /*leadingIcon = {
        },*/
        trailingIcon = {
            IconButton(onClick = { textFieldValue  = "" }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Borrar texto",
                    tint = Color(0xFFfdfefe).copy(alpha = 0.5f)
                )
            }
        },
        shape = RoundedCornerShape(12.dp), singleLine = true,
        colors = getOutlinedTextFieldColor(textFieldColorTypeToShow),
        modifier = Modifier.fillMaxWidth().padding(20.dp, 12.dp)//.shadow(1.dp, RoundedCornerShape(4.dp))
            .onFocusChanged { focusState ->
                if (!focusState.isFocused && hasTextFieldValueChange) {
                    onTextFieldValueChange(textFieldValue);
                    hasTextFieldValueChange = false;
                }
            }
    )
}

/** Composable utilizado para recoger listas de strings separadas por una coma o para editar listas de strings.
 *
 */
@Composable
fun ShowOutlinedTextFieldUpdateMultipleDataCard(
    textFieldColorTypeToShow: String,
    textFieldLabelToShow: String = "",
    textFieldPlaceholderToShow: String = "",
    textFieldListToEdit: List<String> = emptyList(),
    onTextFieldValueChange:(List<String>) ->Unit
) {
    var dataList by remember { mutableStateOf(textFieldListToEdit) }
    //var hasTextFieldValueChange by remember { mutableStateOf(false) }
    OutlinedTextField(
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
            Text(text = textFieldPlaceholderToShow,
                fontSize = 16.sp,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.W400,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = 0.25.sp
            )
        },
        textStyle = TextStyle(
            fontSize = 16.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.W400,
            fontFamily = FontFamily.SansSerif,
            letterSpacing = 0.25.sp
        ),
        shape = RoundedCornerShape(12.dp), singleLine = true, colors = getOutlinedTextFieldColor(textFieldColorTypeToShow),
        trailingIcon = {
            IconButton(onClick = { dataList  = emptyList(); onTextFieldValueChange(dataList) }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Borrar texto",
                    tint = Color(0xFFfdfefe).copy(alpha = 0.5f)
                )
            }
        },
        value = dataList.joinToString(", "), onValueChange = { dataList = it.split(", ").map { value -> value.trim() } },
        modifier = Modifier.fillMaxWidth().padding(20.dp, 12.dp)
            .onFocusChanged { focusState -> if (!focusState.isFocused) onTextFieldValueChange(dataList) }
    )
}
/* ANTIGUOS VALORES DEREFERENCIA
    focusedBorderColor = Color(0xFF00BCD4),
    unfocusedBorderColor = Color(0xFF888888),// Color de borde cuando no tiene el foco
    focusedLabelColor = Color(0xFFfdfefe),// Color del label cuando tiene el foco
    cursorColor = Color(0xFF00BCD4),// Color del cursor tenga o no el foco
    focusedTextColor = Color.White,// Color del texto cuando tiene el foco
    unfocusedTextColor = Color.White,// Color del texto cuando no tiene el foco
    focusedPlaceholderColor = Color.LightGray,// Color del placeholder cuando tiene el foco
    unfocusedPlaceholderColor = Color.Gray,// Color del placeholder cuando no tiene el foco
    focusedContainerColor = Color(0xFF1C1C1E),// Color del contenedor cuando tiene el foco
    unfocusedContainerColor = Color(0xFF1C1C1E) // Fondo oscuro para que luzca moderno
*/
@Composable
fun ShowDatePickerOutlinedTextFieldCard(textFieldLabelToShow: String, selectedDateTextToShow: StateFlow<String>, onOutlinedTextFieldTrailingIconButtonClick: () -> Unit) {
    //var selectedDateText by remember { mutableStateOf(selectedDateTextToShow) }
    val selectedDateText by selectedDateTextToShow.collectAsState()
    //
    OutlinedTextField(value = selectedDateText, onValueChange = { /*selectedDateText = it*/ },
        readOnly = true, label = { Text(text = textFieldLabelToShow) }, placeholder = { Text(text = selectedDateText) },
        shape = RoundedCornerShape(12.dp), singleLine = true,
        //colors = getCreateScreenOutlinedTextFieldColors(),
        trailingIcon = {
            IconButton(onClick = { onOutlinedTextFieldTrailingIconButtonClick() }) {
                Icon(imageVector = Icons.Filled.DateRange, contentDescription = "Seleccionar la hora", tint = Color.White)
            }
        },
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)
    )
}