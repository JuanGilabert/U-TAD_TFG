@file:OptIn(ExperimentalMaterial3Api::class)
package com.cronosdev.taskmanagerapp.ui.components.timePickerComponent
//
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
//
import com.cronosdev.taskmanagerapp.ui.components.outlinedComponents.textFields.getOutlinedTextFieldColor
/**
 *
 */
@Composable
fun ShowTimePickerComponentCard(outlinedTextFieldColorTypeToShow: String, textFieldLabelToShow: String, onInitHourChange: (String) -> Unit, newHourToSHow: String) {
    val context = LocalContext.current
    var expandedEntrada by remember { mutableStateOf(false) }
    val hourEntrada by remember { mutableIntStateOf(0) }
    val minuteEntrada by remember { mutableIntStateOf(0) }
    var nuevaHoraEntrada by remember { mutableStateOf(newHourToSHow) }
    //
    OutlinedTextField(value = nuevaHoraEntrada, onValueChange = { nuevaHoraEntrada = it }, readOnly = true,
        label = { Text(text = textFieldLabelToShow) }, /*placeholder = { Text(text = newHourToSHow) },*/ shape = RoundedCornerShape(12.dp), singleLine = true,
        colors = getOutlinedTextFieldColor(outlinedTextFieldColorTypeToShow),
        trailingIcon = {
            IconButton(onClick = { expandedEntrada = true }) {
                Icon(
                    imageVector = Icons.Filled.AccessTime,
                    contentDescription = "Seleccionar la fecha",
                    tint = Color.White
                )
            }
        },
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)
            /*.onFocusChanged { focusState ->
                if (!focusState.isFocused) {
                    onInitHourChange(nuevaHoraEntrada)
                    //expandedEntrada = false
                }
            }*/
    )
    // Mostrar el TimePicker para la hora
    if (expandedEntrada) {
        TimePickerDialog(
            context,
            { _, selectedHour: Int, selectedMinute: Int ->
                nuevaHoraEntrada = String.format("%02d:%02d:00", selectedHour, selectedMinute)// 18:30:00
                expandedEntrada = false // Reseteamos el valor para cerrarlo.
                onInitHourChange(nuevaHoraEntrada)
            },
            hourEntrada,
            minuteEntrada,
            true
        ).show()
        /*Dialog(onDismissRequest = { expandedEntrada = false }, properties = DialogProperties(dismissOnBackPress = true)) {
            Box(modifier = Modifier.size(500.dp).clip(RoundedCornerShape(30.dp)).background(Color.White).padding(10.dp)) {
                TimePicker(state = rememberTimePickerState(0, 0, true), layoutType = TimePickerLayoutType.Vertical,
                    modifier = Modifier.padding(20.dp),
                    colors = TimePickerColors(
                        clockDialColor = Color.LightGray,
                        selectorColor = Color.Black,
                        containerColor = Color.Green,
                        periodSelectorBorderColor = Color.Red,
                        // ClockDial
                        clockDialSelectedContentColor = Color.Black,
                        clockDialUnselectedContentColor = Color.Black,
                        // PeriodSelector Container
                        periodSelectorSelectedContainerColor = Color.Yellow,
                        periodSelectorUnselectedContainerColor = Color.Yellow,
                        // PeriodSelector Content
                        periodSelectorSelectedContentColor = Color.Green,
                        periodSelectorUnselectedContentColor = Color.Green,
                        // TimeSelector Container
                        timeSelectorSelectedContainerColor = Color.LightGray,
                        timeSelectorUnselectedContainerColor = Color.LightGray,
                        // TimeSelector Content
                        timeSelectorSelectedContentColor = Color.Black,
                        timeSelectorUnselectedContentColor = Color.Black
                    )
                )
            }
        }*/
    }
}

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowTuPutaMadre() {
    var showTimePicker by remember { mutableStateOf(false) }
    val showingPicker = remember { mutableStateOf(true) }
    val state = rememberTimePickerState()
    val formatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    val snackState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    TimePickerDialog(
        title = if (showingPicker.value) "Select Time " else "Enter Time",
        onCancel = { showTimePicker = false },
        onConfirm = {
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, state.hour)
            cal.set(Calendar.MINUTE, state.minute)
            cal.isLenient = false
            snackScope.launch {
                snackState.showSnackbar(
                    "Entered time: ${formatter.format(cal.time)}"
                )
            }
            showTimePicker = false
        },
        toggle = {}
    ) {
        /*if (showingPicker.value && configuration.screenHeightDp > 400) TimePicker(state = state) else */TimeInput(state = state)
    }
}
@Composable
fun ToggleButton(showingPicker: MutableState<Boolean>) {
    IconButton(onClick = { showingPicker.value = !showingPicker.value }) {
        val icon = if (showingPicker.value) Icons.Outlined.Keyboard else Icons.Outlined.Schedule
        Icon(icon, contentDescription = if (showingPicker.value) "Cambiar a texto" else "Cambiar a reloj")
    }
}
*/