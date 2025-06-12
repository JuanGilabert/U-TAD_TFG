package com.cronosdev.taskmanagerapp
//
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.ui.Modifier
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.cronosdev.taskmanagerapp.ui.theme.TaskManagerAppTheme
import com.cronosdev.taskmanagerapp.ui.components.TopBarComponent
import com.cronosdev.taskmanagerapp.ui.navigation.MainContentAppNavGraph
import com.cronosdev.taskmanagerapp.ui.navigation.DrawerContent
import kotlinx.coroutines.launch
import kotlinx.coroutines.*
//
import dagger.hilt.android.AndroidEntryPoint
//
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskManagerAppTheme {
                TaskManagerApp()
            }
        }
    }
}
/** Punto de entrada principal para la aplicación.
 * Gestiona un menú de navegación lateral utilizando [ModalNavigationDrawer] y una navegación basada en Jetpack Compose.
 *
 * **ModalNavigationDrawer;
 * Configuración del menú de navegación lateral modal.
 * @param drawerState Estado del menú lateral (abierto o cerrado).
 * @param drawerContent Contenido del menú lateral, incluyendo acciones y navegación.
 * @param content Contenido principal de la aplicación.
 */
@Composable
fun TaskManagerApp() {
    // Estado para controlar si el menú está abierto o cerrado
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    //
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            //if (drawerState.isOpen) {
            DrawerContent(
                onMenuItemClick = { route ->
                    scope.launch {
                        drawerState.close() // Cierra el menú al seleccionar un ítem
                        // Navega a la ruta seleccionada
                        navController.navigate(route) {
                            launchSingleTop = true // No duplica si ya está en la cima
                        }
                    }
                }
            )
            //}
        },
        content = {
            MainContent(
                navController,
                onMenuButtonClick = {
                    scope.launch { drawerState.open() }
                },
            )
        }
    )
}
/** MainContent representa el contenido principal de la aplicación,
 * combinando la barra superior (TopBarComponent) y la configuración de navegación (NavGraph).
 * Este composable organiza la estructura visual y funcionalidad principal
 * de la aplicación mediante el uso de Scaffold, que es un contenedor flexible
 * para gestionar el diseño y la jerarquía de la interfaz de usuario.
 *
 * **Estructura principal del código;
 * 1. **Scaffold:** Un componente de diseño que proporciona un marco básico con soporte
 *    para elementos comunes de la interfaz como barras superiores, inferiores y contenidos.
 *    En este caso, se utiliza para:
 *    - Mostrar una barra superior (TopBarComponent) que incluye un botón de menú.
 *    - Renderizar el contenido principal de la aplicación, que incluye el NavGraph.
 *
 * 2. **TopBarComponent:** Es un composable que representa la barra superior de la aplicación.
 *    Se pasa una acción `onMenuButtonClick` que será ejecutada al presionar el botón del menú.
 *    Esto permite manejar eventos relacionados con la interacción del usuario en la barra superior.
 *
 * 3. **NavGraph:** Este composable se encarga de configurar y renderizar las diferentes pantallas
 *    de la aplicación según la ruta activa en el `navController`.
 *    Se pasa el `appViewModel` para que las pantallas puedan acceder y gestionar datos compartidos.
 *
 * **Detalles técnicos;
 * - **`padding`:** El parámetro `padding` obtenido de Scaffold se utiliza para asegurar
 *   que el contenido principal respete los márgenes definidos por el sistema (como barras de estado).
 * - **`Box`:** Envuelve el NavGraph dentro de un contenedor que respeta el padding,
 *   garantizando que el contenido no se superponga a otros elementos del diseño.
 *
 * **Parámetros;
 * @author Juan Gilabert Lopez
 * @param navController Controlador de navegación encargado de gestionar las rutas y el estado de navegación. Es esencial para permitir el cambio de pantallas en la aplicación.
 * @param onMenuButtonClick Acción que se ejecuta cuando el usuario presiona el botón del menú en la barra superior (TopBarComponent).
 * @return
 * @sample MainContent()
 * @see Scaffold
 * @since 1.0
 */
@Composable
fun MainContent(navController: NavHostController, onMenuButtonClick: () -> Unit) {
    val snackbarHostState = remember { SnackbarHostState() }
    //
    Scaffold(
        topBar = {
            TopBarComponent(onMenuButtonClick = onMenuButtonClick)
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    Snackbar(
                        containerColor = Color.Red, // color personalizado
                        action = {
                            Button(onClick = { data.performAction() }) {
                                Text("Reintentar")
                            }
                        }
                    ) {
                        Text(data.visuals.message)
                    }
                }
            )
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)/*.wrapContentSize()*/) {
                MainContentAppNavGraph(navController)
            }
        }
    )
}