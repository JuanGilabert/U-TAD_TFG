package com.cronosdev.taskmanagerapp.ui.navigation
//
import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cronosdev.taskmanagerapp.AuthActivity
import com.cronosdev.taskmanagerapp.R
import com.cronosdev.taskmanagerapp.ui.components.showToastMessage
import com.cronosdev.taskmanagerapp.ui.screens.signin.AuthViewModel

/** Funcion composable que representa el contenido de un cajón de navegación.
 * Este cajón actúa como un menú lateral que permite a los usuarios interactuar con diferentes
 * funcionalidades de la aplicación, como navegar entre pantallas o cerrar sesión.
 *
 * **Funcionamiento principal:**
 * - El cajón se organiza visualmente dentro de un `Box` que asegura que el contenido respete
 *   las dimensiones y el diseño especificado.
 * - Los elementos del menú se colocan dentro de una `Column`, con espaciado y estilo para
 *   garantizar una interfaz limpia y consistente.
 * - Se incluyen diferentes opciones como "Reservar Sala", "Mis Reservas", "Notificaciones y Alertas"
 *   y una opción para "Salir". Cada elemento del menú está vinculado a una acción específica
 *   mediante un callback proporcionado (`onMenuItemClick`).
 *
 * **Uso de variables clave:**
 * - **`context`:** Utiliza `LocalContext.current` para obtener el contexto actual de la composición.
 *   Esto es necesario para iniciar actividades o interactuar con los recursos del sistema Android.
 *   En este caso, se usa para crear un `Intent` que inicia la actividad `AuthActivity` cuando el
 *   usuario selecciona la opción "Salir".
 * - **`intent`:** Define un `Intent` que ini cia la actividad de inicio de sesión (`AuthActivity`).
 *   Esto permite que el usuario cierre sesión y regrese a la pantalla de inicio de sesión.
 *
 * **Parameters;
 * @author Juan Gilabert Lopez
 * @param onMenuItemClick  Funcion de orden superior o callback que se ejecuta cuando el usuariohace clic en alguno de los items del menu.
 * Se pasa un identificador (como una URL) para determinar la acción o pantalla a la que se debe navegar.
 * @param authViewModel ViewModel utilizado para la gestion del doLogOut.
 * @return No retorna un valor directo, ya que es un Composable.
 * @sample DrawerContent()
 * @see AuthViewModel
 * @since 1.0
 */
@Composable
fun DrawerContent(onMenuItemClick: (String) -> Unit, authViewModel: AuthViewModel = hiltViewModel()) {
    Box(/*contentAlignment = Alignment.TopStart,*/ modifier = Modifier.fillMaxHeight().widthIn(max = 300.dp)
        .background(MaterialTheme.colorScheme.onPrimary)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize().padding(14.dp)) {
            Text(text = "Menú", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(10.dp))
            ShowMenu(onMenuItemClick = { item -> onMenuItemClick(item) }, authViewModel)
        }
    }
}
/** Funcion composable utilizada para mostrar un menu lateral utilizado para la navegacion de la aplicacion entre las distintas vistas/pantallas del propio menu.
 * **Elementos del menú;
 * - **Consult Rooms:** Redirige al usuario a la vista/pantalla 'ConsultRoomsScreen' para
 * - **Room Booking:** Redirige a la pantalla 'RoomBookingsScreen' para
 * - **My Bookings:** Redirige a la pantalla 'MyBookingsScreen'
 * - **Salir:** Inicia la actividad `AuthActivity` para cerrar sesión y volver a la pantalla de inicio para volver a loguearse(iniciar sesion-doLogin)
 *
 * **Parameters;
 * @param onMenuItemClick Funcion de orden superior o callback que se ejecuta cuando el usuariohace clic en alguno de los items del menu.
 * @param authViewModel ViewModel utilizado para la gestion del doLogOut.
 * @return No retorna un valor directo, ya que es un Composable.
 * @sample ShowMenu()
 * @see AuthViewModel
 * @since 1.0
 */
@Composable
fun ShowMenu(onMenuItemClick: (String) -> Unit, authViewModel: AuthViewModel) {
    //
    val context = LocalContext.current
    val signOutResult by authViewModel.signoutResult.collectAsState()
    //
    LaunchedEffect(signOutResult) {
        if (signOutResult == true) {
            showToastMessage(context, authViewModel.logOutApiResponse.message)
            context.startActivity(Intent(context, AuthActivity::class.java))
            (context as? Activity)?.finish()
        }
        if (signOutResult == false) {
            showToastMessage(context, authViewModel.logOutApiResponse.message)
            onMenuItemClick(Destinations.ALL_TASKS_SCREEN_URL)
        }
    }
    // Item allTasksScreen
    ShowMenuItem(R.drawable.tasksdrawercontenticon, stringResource(R.string.taskScreenViewName), onMenuItemTextClick = { onMenuItemClick(Destinations.ALL_TASKS_SCREEN_URL)} )
    // Item cinemaScreen
    ShowMenuItem(R.drawable.artcinemadrawercontenticon, stringResource(R.string.cinemaScreenViewName), onMenuItemTextClick = { onMenuItemClick(Destinations.ART_CINEMA_SCREEN_URL) })
    // Item musicScreen
    ShowMenuItem(R.drawable.artmusicdrawercontenticon, stringResource(R.string.musicScreenViewName), onMenuItemTextClick = { onMenuItemClick(Destinations.ART_MUSIC_SCREEN_URL) })
    // Item paintingScreen
    ShowMenuItem(R.drawable.artpaintingdrawercontenticon, stringResource(R.string.paintingScreenViewName), onMenuItemTextClick = { onMenuItemClick(Destinations.ART_PAINTING_SCREEN_URL) })
    // Item foodScreen
    ShowMenuItem(R.drawable.fooddrawercontenticon, stringResource(R.string.foodScreenViewName), onMenuItemTextClick = { onMenuItemClick(Destinations.FOOD_SCREEN_URL) })
    // Item medicamentScreen
    ShowMenuItem(R.drawable.medicamentdrawercontenticon, stringResource(R.string.medicamentScreenViewName), onMenuItemTextClick = { onMenuItemClick(Destinations.HEALTH_MEDICAMENT_SCREEN_URL) })
    // Item medicalAppointmentScreen
    ShowMenuItem(R.drawable.medicalappointmentdrawercontenticon, stringResource(R.string.medicalAppointmentScreenViewName), onMenuItemTextClick = { onMenuItemClick(Destinations.HEALTH_MEDICAL_APPOINTMENT_SCREEN_URL) })
    // Item meetingScreen
    ShowMenuItem(R.drawable.meetingdrawercontenticon, stringResource(R.string.meetingScreenViewName), onMenuItemTextClick = { onMenuItemClick(Destinations.MEETING_SCREEN_URL) })
    // Item travelScreen
    ShowMenuItem(R.drawable.traveldrawercontenticon, stringResource(R.string.travelScreenViewName), onMenuItemTextClick = { onMenuItemClick(Destinations.TRAVEL_SCREEN_URL) })
    // Item sportScreen
    ShowMenuItem(R.drawable.sportdrawercontenticon, stringResource(R.string.sportScreenViewName), onMenuItemTextClick = { onMenuItemClick(Destinations.SPORT_SCREEN_URL) })
    // Item workScreen
    ShowMenuItem(R.drawable.workdrawercontenticon, stringResource(R.string.workScreenViewName), onMenuItemTextClick = { onMenuItemClick(Destinations.WORK_SCREEN_URL) })
    // Item Salir
    ShowMenuItem(R.drawable.iconsalir1, "Salir", onMenuItemTextClick = { authViewModel.signout() })
}
/** Funcion composable utilizado para mostrar cada uno de los distintos items del menu.
 *
 * **Parameters;
 * @param idIconToPaint Resource del icono a pintar/mostrar (R.drawable)
 * @param menuItemText Texto del item del menu
 * @param onMenuItemTextClick Funcion de orden superior o callback que se ejecuta cuando el usuariohace clic en alguno de los items del menu.
 * @return No retorna un valor directo, ya que es un Composable.
 * @sample ShowMenuItem()
 * @since 1.0
 */
@Composable
fun ShowMenuItem(idIconToPaint: Int, menuItemText: String, onMenuItemTextClick:() -> Unit) {
    //.border(width = 2.dp, Color.Red)
    Row (verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth()) {
        ShowMenuItemIcon(idIconToPaint)
        ShowMenuItemText(text = menuItemText) { onMenuItemTextClick() }
    }
}
/** Funcion composable utilizado para mostrar el icono que tiene cada uno de los items del menu.
 *
 * **Parameters;
 * @param idIconToPaint Resource del icono a pintar/mostrar (R.drawable)
 * @return No retorna un valor directo, ya que es un Composable.
 * @sample ShowMenuItemIcon()
 * @since 1.0
 */
@Composable
fun ShowMenuItemIcon(idIconToPaint: Int) {
    // Mantenemos el color original del ícono mediante 'tint = Color.Unspecified'. .border(width = 2.dp, Color.Green)
    Icon(painter = painterResource(id = idIconToPaint), contentDescription = "", tint = Color.Unspecified,
        modifier = Modifier.size(70.dp).padding(10.dp, 10.dp)
    )
}
/** Funcion composable utilizado para mostrar el texto que tiene cada uno de los items del menu.
 *
 * **Funcionamiento principal;
 * - El elemento del menú se implementa utilizando un `TextButton`, un botón estilizado que muestra texto y responde a las interacciones del usuario.
 * - El texto del botón se muestra con un tamaño de fuente de 18 sp, proporcionando una apariencia clara y legible para los usuarios.
 * - El `onClick` es una función lambda que define la acción que debe ejecutarse cuando el usuario selecciona este elemento. Esto lo hace altamente reutilizable para diferentes casos de uso en menús dinámicos.
 *
 * **Parameters;
 * @param text El texto que se mostrará en el elemento del menú. Define la etiqueta o descripción visible del elemento, por ejemplo, "Reservar Sala" o "Cerrar Sesión".
 * @param onMenuItemTextClick Función de orden superior o callback que se ejecuta cuando se hace clic en el elemento.
 * Esto permite asociar cada instancia de `ShowMenuItemText` con funcionalidades específicas, como navegar a una pantalla o realizar una operación como se hace en el composable que invoca esta funcion llamado 'ShowMenuItem'.
 * @return No retorna un valor directo, ya que es un Composable.
 * @sample ShowMenuItemText()
 * @since 1.0
 */
@Composable
fun ShowMenuItemText(text: String, onMenuItemTextClick: () -> Unit) {
    //.border(width = 4.dp, Color.Blue)
    TextButton(onClick = { onMenuItemTextClick() }, modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
            Text(text = text, fontSize = 18.sp)
        }
    }
}
/*
class AlertDialogState {
    private var visible by mutableStateOf(false)

    fun show() {
        visible = true
    }

    fun dismiss() {
        visible = false
    }
}

@Composable
fun rememberAlertDialogState(): AlertDialogState {
    return remember { AlertDialogState() }
}
*/