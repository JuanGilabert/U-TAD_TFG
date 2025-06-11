package com.cronosdev.taskmanagerapp.ui.components
//
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cronosdev.taskmanagerapp.R

/** Funcion composable que representa la parte superior de la aplicación.
 * Esta parte contiene un ícono de menú que ejecuta una acción cuando es clicado.
 *
 * **Parameters;
 * @author Juan Gilabert Lopez
 * @param onMenuButtonClick Función lambda que se ejecuta cuando se presiona el ícono del menú. Esta función es utilizada para abrir el menú o realizar alguna acción personalizada.
 * @return No retorna un valor directo, ya que es un Composable.
 * @sample TopBarComponent()
 * @since 1.0
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarComponent(onMenuButtonClick: ()->Unit) {
    TopAppBar(
        title = { Text("") },
        navigationIcon = {
            IconButton(onClick = onMenuButtonClick) { Icon(imageVector = Icons.Default.Menu, contentDescription = "Abrir Menú", tint = Color.Black) }
        },
        actions = {
            // Agregamos una imagen de banner en la parte superior, al lado del menu
            Box(modifier = Modifier.fillMaxWidth()) {
                // Imagen del banner o cabecera de la aplicacion
                Image(painter = painterResource(id = R.drawable.bannertaskmanager2),
                    contentDescription = "Banner superior", modifier = Modifier.fillMaxWidth().height(100.dp).align(Alignment.Center)
                )
            }
        },
        colors = TopAppBarColors(containerColor = Color.White,
            scrolledContainerColor = Color.Yellow, navigationIconContentColor = Color.Green,
            titleContentColor = Color.Red, actionIconContentColor = Color.Blue
        )
    )
}