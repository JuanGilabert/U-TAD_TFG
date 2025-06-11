package com.cronosdev.taskmanagerapp.ui.state

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
/** Clase sellada que representa el estado de la UI en una aplicación.
 *
 * @author Juan Gilabert Lopez
 * @since 1.0
 */
sealed class UiState {
    /** Representa el estado de carga de la UI. */
    data object Loading : UiState()
    /** Representa un estado exitoso con datos genéricos.
     * // screenInit: Indica que es para el inicio de una vista.
     *         // screenRunning: Indica que es un metodo que ocurre cuando la vista ya esta mostrando datos, osea se ha iniciado.
     * @property errorType Mensaje de error que describe el problema.
     * @property errorScreenCallName Nombre de la Screen que invoca el metodo del modelo para solicitar datos al mismo.
     * @property errorMethodName Nombre de la funcion del ViewModel que solicita y obtiene del repositorio la respuesta.
     * @property errorType Indica el mensaje. En este caso es el mensaje devuelto por la api cuando el repositorio realiza la accion solicitada por el modelo.
     */
    data class Success(
        val successType: String = "Tipo desconocido",
        val successScreenCallName: String = "Screen desconocida",
        val successMethodName: String = "Metodo desconocido",
        val successMessage: String = "Mensaje desconocido",
    ) : UiState()

    /** Representa un estado de error con un mensaje descriptivo.
     * @property errorType Mensaje de error que describe el problema.
     * @property errorScreenCallName Nombre de la Screen que invoca el metodo del modelo para solicitar datos al mismo.
     * @property errorMethodName Nombre de la funcion del ViewModel que solicita y obtiene del repositorio la respuesta.
     * @property errorType Indica el mensaje. En este caso es el mensaje devuelto por la api cuando el repositorio realiza la accion solicitada por el modelo.
     */
    data class Error(
        val errorType: String = "Tipo desconocido",
        val errorScreenCallName: String = "Screen desconocida",
        val errorMethodName: String = "Metodo desconocido",
        val errorMessage: String = "Mensaje desconocido"
    ) : UiState()
}
/**/
@Composable
fun ShowLoadingUiState() {
    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()
    ) { CircularProgressIndicator(modifier = Modifier.padding(20.dp)) }
}