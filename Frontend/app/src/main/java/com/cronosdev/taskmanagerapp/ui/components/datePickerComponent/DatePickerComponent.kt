package com.cronosdev.taskmanagerapp.ui.components.datePickerComponent
//
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.cronosdev.taskmanagerapp.ui.screens.art.cinema.CinemaViewModel
//import com.google.protobuf.Internal.BooleanList REVISAR DE DONDE SALE ESTO(ULTIMA VEZ MODIFICADO CreateCinemaScreen)
//
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneOffset
import java.util.Locale
import java.time.LocalDate
//
/** Funcion composable utilizada para mostrar el calendrio con las fechas de reserva disponibles y no disponibles de una sala en concreto.
 *
 * **Parameters;
 * @return No retorna un    valor directo, ya que es un Composable.
 * @sample DatePickerComponent()
 * @see CinemaViewModel
 * @since 1.0
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerComponent(datesToDisableOnUi: List<String>, onInitDateChange:(Long) -> Unit, onDatePickerButtonClick:(Boolean)-> Unit) {
    //
    var selectedDateByUser by remember{ mutableStateOf("") }
    // Variable que controla cuando hay que mostrar las reservas que tiene una sala en concreto para una fecha en concreto.
    var showTasksOnDay: Boolean? by remember { mutableStateOf(false) }
    // Formateador de fechas.
    var dateFormatter = remember { DatePickerDefaults.dateFormatter() }
    // Variable para almacenar el estado del picker donde indicamos inicialmente que las fechas anteriores a la actual o los fines de semana quedan deshabilitadas por defecto
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = null,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                // Guardamos la fecha actual en UTC
                val currentDateUTC = LocalDate.now(ZoneOffset.UTC)
                // Convertimos utcTimeMillis a LocalDate
                val dateToCheck = Instant.ofEpochMilli(utcTimeMillis).atZone(ZoneOffset.UTC).toLocalDate()
                // Si hay una lista de fechas para deshabilitar pues se deshabilitan,
                // pero si la lista esta vacia entonces no hay que iterar por la lista de fechas al estar vacia.
                if (datesToDisableOnUi.isNotEmpty()) {
                    //  Lista de fechas no disponibles en formato String (YYYY-MM-DD) cenvertidas a fechas LocalDate.
                    val unavailableLocalDates = datesToDisableOnUi.map{ Instant.parse(it).atZone(ZoneOffset.UTC).toLocalDate() }
                    // Devolvemos true para indicar que la fecha puede ser elegida solo cuando la fecha es igual aldia actual, cuando la fecha es despues de la fecha actual, cuando la fecha no es sabado ni domingo y cuando la fecha
                    return dateToCheck.isEqual(currentDateUTC) || ( dateToCheck.isAfter(currentDateUTC) && !unavailableLocalDates.contains(dateToCheck))
                }
                // Devolvemos true para indicar que la fecha puede ser elegida solo cuando la fecha es igual al dia actual, cuando la fecha es despues de la fecha actual, cuando la fecha no es sabado ni domingo y cuando la fecha
                return dateToCheck.isEqual(currentDateUTC) || ( dateToCheck.isAfter(currentDateUTC))
            }
            override fun isSelectableYear(year: Int): Boolean {
                // Guardamos el año actual en UTC
                val currentYear = LocalDate.now(ZoneOffset.UTC).year
                // Permitimos solo el año actual o posteriores
                return year >= currentYear
            }
        }
    )
    // Conjunto de acciones que se ejecutan cuando cada vez que se selecciona una fecha en el datePicker
    LaunchedEffect(datePickerState.selectedDateMillis) {
        // Guardamos la fecha seleccionada por el usuario.
        datePickerState.selectedDateMillis?.let {
            //selectedDateByUser = it.toString()
            onInitDateChange(it)
        }
        // Mostramos las reservas del dia seleccionado estableciendo la variable que controla cuando mostrarlo.
        showTasksOnDay = true
    }
    // Composable 'DatePicker' utilizado para mostrar un calendario.
    Dialog(onDismissRequest = { onDatePickerButtonClick(false) }, properties = DialogProperties(dismissOnBackPress = true)) {
        Box(modifier = Modifier.size(550.dp).clip(RoundedCornerShape(30.dp)).background(Color.White).padding(5.dp)) {
            /*Column {
            }*/
            DatePicker(state = datePickerState, modifier = Modifier.fillMaxSize(), dateFormatter = dateFormatter, headline = { CustomDatePickerHeadline(datePickerState) },
                title = { Text("Disponibilidad de la sala", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(start = 24.dp, end = 12.dp, top = 16.dp)) },
                // Controla si se muestra o no el botón que permite alternar entre los modos de visualización (Picker y Text). El valor por defecto es true.
                showModeToggle = false,
                colors = DatePickerDefaults.colors(
                    //
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    headlineContentColor = Color.Black,
                    subheadContentColor = Color.Unspecified,
                    navigationContentColor = Color.Black,
                    // ALL YEARS
                    yearContentColor = Color.Black,
                    currentYearContentColor = Color.Unspecified, // Año actual del sistema
                    disabledYearContentColor = Color.Unspecified,

                    selectedYearContainerColor = Color.Black, // Año seleccionado por el usuario
                    selectedYearContentColor = Color.White,

                    disabledSelectedYearContainerColor = Color.Red,
                    disabledSelectedYearContentColor = Color.Red,
                    // ALL WEEKS
                    weekdayContentColor = Color.Black,
                    // ALL DAYS
                    dayContentColor = Color.Black,

                    selectedDayContainerColor = Color.Black,
                    selectedDayContentColor = Color.White,
                    disabledDayContentColor = Color.Unspecified,

                    disabledSelectedDayContentColor = Color.Unspecified,
                    disabledSelectedDayContainerColor = Color.Unspecified,

                    dayInSelectionRangeContainerColor = Color.Unspecified,
                    dayInSelectionRangeContentColor = Color.Unspecified,
                    // TODAY
                    todayDateBorderColor = Color.Unspecified,
                    todayContentColor = Color.Unspecified,
                    dividerColor = Color.Unspecified,
                    //dateTextFieldColors = ColorScheme.defaultOutlinedTextFieldColors
                )
            )
        }
    }
    // Mostramos el dialog que contiene las tareas el dia elegido en el calendario por el usuario.
    //if (showTasksOnDay == true) BookingsDatesOnDayByRoomComponent(onDismiss = { dismiss -> showTasksOnDay = dismiss }, selectedDateByUser, availableDatesOnDayByDate)
}
/** Funcion composable utilizada para mostrar de forma personalizada la parte superior de un DatePicker(headLine).
 * el metodo llamara a una funcion para realizar el formateo de fechas para mostrarlas en el headLine del DatePicker.
 * Gracias al remember del estado del DatePicker cada vez que se cambia la fecha se formatea al idioma indicado mediante la funcion 'formatDate'
 *
 * **Parameters;
 * @param pickerState: Este parametro sirve para poder trabajar con el estado del DatePicker, en este caso es usado para formatear la fecha al idioma Español.
 * @return No retorna un valor directo, ya que es un Composable.
 * @sample CustomDatePickerHeadline()
 * @see DatePickerState
 * @since 1.0
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePickerHeadline(pickerState: DatePickerState) {
    val formattedDate = pickerState.selectedDateMillis?.let {
        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale("es", "ES"))
        formatter.format(it)
    } ?: "Ninguna fecha seleccionada"
    Text(text = formattedDate, style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 24.dp, end = 12.dp, bottom = 12.dp)
    )
}