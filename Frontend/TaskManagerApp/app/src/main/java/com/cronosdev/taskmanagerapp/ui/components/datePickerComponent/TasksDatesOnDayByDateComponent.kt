package com.cronosdev.taskmanagerapp.ui.components.datePickerComponent
//
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.util.Locale
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseListModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseListModel.Companion.size
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.DatesModel
import java.text.SimpleDateFormat
/** Funcion composable utilizada para mostrar un dialog con las fechas de reserva para la sala elegida en la fecha elegida.
 *
 * @author Juan Gilabert Lopez
 * @param onDismiss Funcion de orden superior o callback que se ejecuta cuando
 * @param selectedDateByUser
 * @param availableDatesOnDayByDate
 * @return No retorna un valor directo, ya que es un Composable.
 * @sample BookingsDatesOnDayByRoomComponent()
 * @see ApiResponseListModel
 * @since 1.0
 */
@Composable
fun BookingsDatesOnDayByRoomComponent(onDismiss: (Boolean) -> Unit, selectedDateByUser: String, availableDatesOnDayByDate: ApiResponseListModel<DatesModel>) {
    Dialog(onDismissRequest = { onDismiss(false) }, properties = DialogProperties(dismissOnBackPress = true)) {
        Box(modifier = Modifier.size(550.dp).clip(RoundedCornerShape(30.dp)).background(Color.White).padding(5.dp)) {
            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 10.dp))/*.border(5.dp, Color.Yellow)*/ {
                // Encabezado con el día numerico y semanal de la fecha elegida.
                ShowDialogHeader(selectedDateByUser)
                // Linea divisora horizontal
                HorizontalDivider(modifier = Modifier.padding(top=10.dp, bottom = 20.dp), thickness = 1.dp)
                // Lista de reservas
                ShowBody(availableDatesOnDayByDate)
            }
        }
    }
}
/** Funcion composable utilizada para mostrar la cabecera del dialog que muestra la lista de reservas existentes.
 *
 * @param headerDateToShow String que contiene la fecha a mostrar.
 * @return No retorna un valor directo, ya que es un Composable.
 * @sample ShowDialogHeader()
 * @since 1.0
 */
@Composable
fun ShowDialogHeader(headerDateToShow: String) {
    if (headerDateToShow .isNotBlank()) {
        Log.d("fetchRoomDateToBooking", "Fecha del modelo $headerDateToShow")
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("es")).parse(headerDateToShow)
        Log.d("fetchRoomDateToBooking", "Fecha del modelo formateada $date")
        date?.let {
            val formattedDay = SimpleDateFormat("dd", Locale("es")).format(it)
            val formattedWeekDay = SimpleDateFormat("EEEE", Locale("es")).format(it)
            Log.d("fetchRoomDateToBooking", "Fecha dia del mes $formattedDay")
            Log.d("fetchRoomDateToBooking", "Fecha dia de la semana $formattedWeekDay")
            Row {
                Text(text = formattedDay, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = formattedWeekDay, style = MaterialTheme.typography.headlineSmall)
            }
        }
    }
}
/** Funcion composable utilizada para
 *
 * @param availableDatesOnDayByDate ViewModel que gestiona la selección de fechas.
 * @return No retorna un valor directo, ya que es un Composable.
 * @sample ShowBody()
 * @see DatesModel
 * @since 1.0
 */
@Composable
fun ShowBody(availableDatesOnDayByDate: ApiResponseListModel<DatesModel>) {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("es"))
    val outputFormat = SimpleDateFormat("HH:mm", Locale("es"))
    // En caso de que haya reservas en el dia seleccionado para la sala seleccionada mostramos las reservas
    if (!availableDatesOnDayByDate.data?.dates.isNullOrEmpty()) {
        LazyColumn(modifier = Modifier.size(400.dp)) {
            availableDatesOnDayByDate.size()?.let {
                items(it) { task ->
                    Card(colors = CardDefaults.cardColors(Color.White), //elevation = CardDefaults.cardElevation(4.dp),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(10.dp)) {
                            Icon(imageVector = Icons.Default.Event, // Usamos el ícono de evento
                                contentDescription = "Icono Reserva", tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.padding(8.dp))
                            val startDate = inputFormat.parse(availableDatesOnDayByDate.data!!.dates[task])
                            startDate?.let { date -> outputFormat.format(date) }?.let { date -> ShowDateOfTask(date) }
                        }
                    }
                }
            }
        }
    } else Text(text = "No hay tareas para este día.", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(vertical = 4.dp))
}
/** Funcion composable utilizada para mostrar las la horas de la reservas de la fecha elegida.
 *
 * @param taskDate String que contiene la hora de la reserva de la fecha elegida.
 * @return No retorna un valor directo, ya que es un Composable.
 * @sample ShowDateOfTask()
 * @since 1.0
 */
@Composable
fun ShowDateOfTask(taskDate: String) { Text(text = taskDate, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodyMedium) }