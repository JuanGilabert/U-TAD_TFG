package com.cronosdev.taskmanagerapp.ui.components
//
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import com.cronosdev.taskmanagerapp.MainActivity

/** Funcion composable utilizada para mostrar el mensaje de error recibido por parametro
 * cuyo valor es el mensaje que la api nos devuelve en caso de haber un error con un endpoint.
 *
 * **Parameters;
 * @author Juan Gilabert Lopez
 * @param message Mensaje de error que la api nos envia y que nosostros enviamos al componente para que se muestre.
 * @param onApiErrorSnackBarButtonRetry Funcion de orden superior o callback utilizado para indicar que se ha de ejecutar una accion definida en aquella vista que invoque este componente.
 * @return No retorna un valor directo, ya que es un Composable.
 * @sample ApiErrorSnackbar()
 * @since 1.0
 */
@Composable
fun ApiErrorSnackbar(message: String, onApiErrorSnackBarButtonRetry: () -> Unit) {
    Snackbar(
        action = {
            Button(onClick = { onApiErrorSnackBarButtonRetry() }) {
                Text("Reintentar")
            }
        }
    ) { Text(message) }
}
/** Funcion composable utilizada para mostrar mensajes en la vista en funcion de lo que suceda en la misma. Es decir si hacer doLogOut es exitoso se mostrara un mensaje o si no fue exitoso se mostrara otro mensaje, el mensaje recibido por parametro llamado 'messageToShow'.
 *
 * **Parameters;
 * @param context Contexto que indica donde nos encontramos para saber donde crear/mostrar el toast
 * @param messageToShow Mensaje a mostrar por el composable
 * @return En este caso la funcion no es un composable pero aun asi sigue sin devolver nada, no es necesario que la funcion devuelva nada. No hay nada que devolver ya que es una funcion que solo muestra los mensajes que recibe por parametro.
 * @sample showToastMessage()
 * @see MainActivity
 * @since 1.0
 */
fun showToastMessage(context: Context, messageToShow: String) { Toast.makeText(context, messageToShow, Toast.LENGTH_SHORT).show() }