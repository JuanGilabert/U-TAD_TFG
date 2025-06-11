package com.cronosdev.taskmanagerapp.ui.components.viewSearchersComponents
//
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cronosdev.taskmanagerapp.R
import com.cronosdev.taskmanagerapp.ui.components.outlinedComponents.buttons.ShowTextFieldSearcherOutlinedButtonComponent
import com.cronosdev.taskmanagerapp.ui.components.outlinedComponents.buttons.getOutlinedButtonColor

////
/**
 *
 */
@Composable
fun ShowTextFieldSearcherComponent(onValueSearcherChange:(String) -> Unit, onCreateButtonClick:() -> Unit) {
    //.border(width = 5.dp, color = Color.Yellow)
    Row(horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(10.dp)) {
        TextFieldSearcherComponent(onTextFieldValueChange = { valueToSearch -> onValueSearcherChange(valueToSearch) })
        ShowTextFieldSearcherOutlinedButtonComponent(onOutlinedButtonClick = { onCreateButtonClick() })
    }
}
/**
 *
 */
@Composable
fun TextFieldSearcherComponent(onTextFieldValueChange: (String) -> Unit){
    var textToSearch by remember { mutableStateOf("") }
    //
    OutlinedTextField(value = textToSearch, onValueChange = { textToSearch = it; onTextFieldValueChange(textToSearch) },
        modifier = Modifier.padding(8.dp),
        textStyle = TextStyle(
            fontSize = 16.sp,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.W300,
            fontFamily = FontFamily.SansSerif,
            letterSpacing = 0.25.sp
        ),
        placeholder = {
            Text(text = "Escriba para buscar ...", style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Escriba para buscar ...",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingIcon = {
            IconButton(onClick = { textToSearch  = ""; onTextFieldValueChange(textToSearch) }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Limpiar texto",
                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
        },
        singleLine = true, shape = MaterialTheme.shapes.large,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent, // Fondo transparente
            unfocusedContainerColor = Color.Transparent, // Fondo transparente
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
            focusedIndicatorColor = MaterialTheme.colorScheme.primary, // Color del borde al enfocar
            unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f), // Color del borde sin enfocar
            cursorColor = MaterialTheme.colorScheme.primary // Color del cursor
        )
    )
}