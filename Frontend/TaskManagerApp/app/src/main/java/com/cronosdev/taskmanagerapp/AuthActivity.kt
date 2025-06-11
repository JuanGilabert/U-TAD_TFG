package com.cronosdev.taskmanagerapp
//
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.cronosdev.taskmanagerapp.ui.navigation.AuthContentAppNavGraph
import com.cronosdev.taskmanagerapp.ui.theme.TaskManagerAppTheme
import dagger.hilt.android.AndroidEntryPoint
/** Clase `AuthActivity` que se encarga de mostrar la interfaz de inicio de sesión.
 *
 * Esta actividad utiliza Jetpack Compose para renderizar la interfaz gráfica y está
 * anotada con `@AndroidEntryPoint` para habilitar la inyección de dependencias con Hilt.
 * surface: Define la superficie principal de la pantalla.
 *
 * @author Juan Gilabert Lopez
 * @since 1.0
 */
@AndroidEntryPoint
class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskManagerAppTheme {
                AuthContent()
            }
        }
    }
}

@Composable
fun AuthContent() {
    val navController = rememberNavController()
    //
    Scaffold(
        topBar = {},
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                AuthContentAppNavGraph(navController)
            }
        }
    )
}