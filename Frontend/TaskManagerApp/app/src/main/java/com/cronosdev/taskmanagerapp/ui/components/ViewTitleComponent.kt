package com.cronosdev.taskmanagerapp.ui.components
//
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
/** Funcion composable utilizada para mostrar los distintos titulos de las vistas de la aplicacion recibidos por parametro.
 * Normalmente se recibira un reource que indicara el titulo a mostrar en la vista cuando este composable es invocado.
 * **Parameters;
 * @author Juan Gilabert Lopez
 * @param titleView Titulo de la vista a mostrar.
 * @return No retorna un valor directo, ya que es un Composable.
 * @sample ViewTitleComponent()
 * @since 1.0
 */
@Composable
fun ViewTitleComponent(titleView: String, viewTitleColor: Color) {
    //.border(width = 2.dp, color = Color.Green)
    Text(text = titleView, modifier = Modifier.padding(10.dp),
        viewTitleColor,
        fontSize = 30.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.W500,
        fontFamily = FontFamily.Serif,
        letterSpacing = 0.4.sp
    )
}