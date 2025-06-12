package com.cronosdev.taskmanagerapp.ui.components.outlinedComponents.buttons
//
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cronosdev.taskmanagerapp.R
////
@Composable
fun ShowCustomData(textToShow: String) {
    Text(text = textToShow, modifier = Modifier.padding(4.dp),
        fontSize = 14.sp,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.W400,
        fontFamily = FontFamily.SansSerif,
        letterSpacing = 0.5.sp
    )
}
//// BUTTONS
/**
 *
 */
@Composable
fun ShowCreateScreenButtons(outlinedButtonColorTypeToShowByScreenType: String = "create", onButtonClick: (String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(15.dp, 15.dp)/*.border(width = 2.dp, color = Color.Blue)*/,
        horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
        ShowOutlinedBackButton(
            onBackButtonClick = { onBackButtonClickValue ->  onButtonClick(onBackButtonClickValue) },
            outlinedButtonColorTypeToShowByScreenType
        )
        ShowOutlinedCreateButton(
            onCreateButtonClick = { onCreateButtonClickValue ->  onButtonClick(onCreateButtonClickValue) },
            outlinedButtonColorTypeToShowByScreenType
        )
    }
}
/**
 *
 */
@Composable
fun ShowUpdateScreenButtons(outlinedButtonColorTypeToShowByScreenType: String = "update", onButtonClick: (String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly) {
        ShowOutlinedBackButton(
            onBackButtonClick = { onBackButtonClickValue ->  onButtonClick(onBackButtonClickValue) },
            outlinedButtonColorTypeToShowByScreenType
        )
        ShowOutlinedUpdateButton(
            onUpdateButtonClick = { onButtonClickValue ->  onButtonClick(onButtonClickValue) },
            outlinedButtonColorTypeToShowByScreenType
        )
        ShowOutlinedDeleteButton(
            onDeleteButtonClick = { onButtonClickValue ->  onButtonClick(onButtonClickValue) },
            outlinedButtonColorTypeToShowByScreenType
        )
    }
}
//// GLOBAL BUTTONS
/**
 *
 */
@Composable
fun ShowTextFieldSearcherOutlinedButtonComponent(onOutlinedButtonClick:() -> Unit) {
    OutlinedButton(onClick = { onOutlinedButtonClick() }, modifier = Modifier.padding(5.dp),
        shape = RoundedCornerShape(20.dp), border = BorderStroke(1.dp, Color(0xFF1F3A5F)),
        colors = getOutlinedButtonColor("read", "back"),
    ) {
        Text(
            text = stringResource(R.string.createButtonTitle),
            fontSize = 14.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.W400,
            fontFamily = FontFamily.SansSerif,
            letterSpacing = 0.5.sp
        )
    }
}
/**
 *
 */
@Composable
private fun ShowOutlinedBackButton(onBackButtonClick:(String) -> Unit, colorTypeToShowByScreenType: String) {
    val backButtonValue = stringResource(R.string.backButtonValue)
    OutlinedButton(onClick = { onBackButtonClick(backButtonValue) }, modifier = Modifier.padding(10.dp),
        shape = RoundedCornerShape(22.dp),
        colors = getOutlinedButtonColor(colorTypeToShowByScreenType, backButtonValue),
        border = BorderStroke(1.dp, Color(0xFF1F3A5F)),
    ) {
        Text(
            text = stringResource(R.string.backButtonTitle),
            fontSize = 14.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.W400,
            fontFamily = FontFamily.SansSerif,
            letterSpacing = 0.5.sp
        )
    }
}
/**
 *
 */
@Composable
private fun ShowOutlinedCreateButton(onCreateButtonClick:(String) -> Unit, colorTypeToShowByScreenType: String) {
    val createButtonValue = stringResource(R.string.createButtonValue)
    //
    OutlinedButton(onClick = { onCreateButtonClick(createButtonValue) }, modifier = Modifier.padding(10.dp),
        shape = RoundedCornerShape(22.dp),
        colors = getOutlinedButtonColor(colorTypeToShowByScreenType, createButtonValue),
        border = BorderStroke(1.dp, Color(0xFF1B4E20)),
    ) {
        Text(
            text = stringResource(R.string.createButtonTitle),
            fontSize = 14.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.W400,
            fontFamily = FontFamily.SansSerif,
            letterSpacing = 0.5.sp
        )
    }
}
/**
 *
 */
@Composable
private fun ShowOutlinedUpdateButton(onUpdateButtonClick:(String) -> Unit, colorTypeToShowByScreenType: String) {
    val updateButtonValue = stringResource(R.string.updateButtonValue)
    //
    OutlinedButton(onClick = { onUpdateButtonClick(updateButtonValue) }, modifier = Modifier.padding(10.dp),
        shape = RoundedCornerShape(22.dp),
        colors = getOutlinedButtonColor(colorTypeToShowByScreenType, updateButtonValue),
        border = BorderStroke(1.dp, Color(0xFF1B4E20)),
    ) {
        Text(
            text = stringResource(R.string.updateButtonTitle),
            fontSize = 14.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.W400,
            fontFamily = FontFamily.SansSerif,
            letterSpacing = 0.5.sp
        )
    }
}
/**
 *
 */
@Composable
private fun ShowOutlinedDeleteButton(onDeleteButtonClick:(String) -> Unit, colorTypeToShowByScreenType: String) {
    val deleteButtonValue = stringResource(R.string.deleteButtonValue)
    //
    OutlinedButton(onClick = { onDeleteButtonClick(deleteButtonValue) }, modifier = Modifier.padding(10.dp),
        shape = RoundedCornerShape(22.dp),
        colors = getOutlinedButtonColor(colorTypeToShowByScreenType, deleteButtonValue),
        border = BorderStroke(1.dp, Color(0xFF6D1616)),
    ) {
        Text(
            text = stringResource(R.string.deleteButtonTitle),
            fontSize = 14.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.W400,
            fontFamily = FontFamily.SansSerif,
            letterSpacing = 0.5.sp
        )
    }
}
//// PALETAS
// Rojos & Rosas & Carnes & algun blanco casi blanco como 0xFFFFEBEE(!= 0xFFFFFFFF blanco puro)
val redPalette = listOf(
    /* Los colores aqui se escriben de izquierda a derecha, pero al mostrarse el orden de colores
    lo hacen de derecha a izquierda. Por tanto el color que esta escrito mas a la izquierda se vera
    mas a la derecha(de forma descendente). */
    Color(0xFFFFEBEE), Color(0xFFFFCDD2), Color(0xFFEF9A9A), Color(0xFFE57373),
    Color(0xFFEF5350), Color(0xFFF44336), Color(0xFFE53935), Color(0xFFD32F2F),
    Color(0xFFC62828), Color(0xFFB71C1C), Color(0xFFFF8A80), Color(0xFFFF5252),
    Color(0xFFFF1744), Color(0xFFD50000), Color(0xFFB22222), Color(0xFFCD5C5C),
    Color(0xFFF08080), Color(0xFFE9967A), Color(0xFFFA8072), Color(0xFFFF6347),
    0xFFFF4500, 0xFFDC143C, 0xFF8B0000, 0xFFA52A2A, 0xFFB22222,
    0xFF800000, 0xFF912222, 0xFFA12828, 0xFFB53030, 0xFFC04040,
    0xFFD05050, 0xFFE06060, 0xFFF07070, 0xFFFF8080, 0xFFFF9999,
    0xFFFFB3B3, 0xFFFFCCCC, 0xFFFFE5E5, 0xFFFFF5F5, 0xFFFFFAFA
)
// Verdes & Verdez Azulados
val greenPalette = listOf(
    Color(0xFFE8F5E9), Color(0xFFC8E6C9), Color(0xFFA5D6A7), Color(0xFF81C784),
    Color(0xFF66BB6A), Color(0xFF4CAF50), Color(0xFF43A047), Color(0xFF388E3C),
    Color(0xFF2E7D32), Color(0xFF1B5E20), Color(0xFFB9F6CA), Color(0xFF69F0AE),
    Color(0xFF00E676), Color(0xFF00C853), Color(0xFF2ECC71), Color(0xFF27AE60),
    Color(0xFF1ABC9C), Color(0xFF16A085), Color(0xFF00BFA5), Color(0xFF26A69A),
    0xFF43B581, 0xFF2FAD68, 0xFF20824E, 0xFF145A32, 0xFF0B3D28,
    0xFF006400, 0xFF007F5C, 0xFF00996B, 0xFF00B377, 0xFF00CC88,
    0xFF00E699, 0xFF00FFA5, 0xFF33FFB2, 0xFF66FFBF, 0xFF99FFCC,
    0xFFCCFFDD, 0xFFE0FFF0, 0xFFF0FFF5, 0xFFF8FFFA, 0xFFFFFFFF
)
// Azules & algun blanco casi blanco como 0xFFFFEBEE(!= 0xFFFFFFFF blanco puro)
val bluePalette = listOf(// 6, 15, 18
    Color(0xFFE3F2FD), Color(0xFFBBDEFB), Color(0xFF90CAF9), Color(0xFF64B5F6),
    Color(0xFF42A5F5), Color(0xFF2196F3), Color(0xFF1E88E5), Color(0xFF1976D2),
    Color(0xFF1565C0), Color(0xFF0D47A1), Color(0xFF82B1FF), Color(0xFF448AFF),
    Color(0xFF2979FF), Color(0xFF2962FF), Color(0xFF1E90FF), Color(0xFF4682B4),
    Color(0xFF5DADE2), Color(0xFF3498DB), Color(0xFF2E86C1), Color(0xFF2874A6),
    Color(0xFF21618C), Color(0xFF1B4F72), Color(0xFF154360), Color(0xFF0E3453),
    Color(0xFF001F3F), Color(0xFF0066CC), Color(0xFF007FFF), Color(0xFF3399FF),
    Color(0xFF66B2FF), Color(0xFF99CCFF), Color(0xFFCCE5FF), Color(0xFFE0F0FF),
    Color(0xFFF0F8FF), Color(0xFFF5FAFF), Color(0xFFFAFCFF), Color(0xFFFFFFFF),
    Color(0xFFD6EAF8), Color(0xFFB3D7F3), Color(0xFFAED6F1), Color(0xFFA9CCE3)
)
// Amarillos
val yellowPalette = listOf(
    Color(0xFFFFFDE7), Color(0xFFFFF9C4), Color(0xFFFFF59D), Color(0xFFFFF176),
    Color(0xFFFFEE58), Color(0xFFFFEB3B), Color(0xFFFDD835), Color(0xFFFBC02D),
    Color(0xFFF9A825), Color(0xFFF57F17), Color(0xFFFFFF8D), Color(0xFFFFFF00),
    Color(0xFFFFEA00), Color(0xFFFFD600), Color(0xFFFFC107), Color(0xFFFFB300),
    Color(0xFFFFA000), Color(0xFFFF8F00), Color(0xFFFF6F00), Color(0xFFF57C00),
    Color(0xFFFFD54F), Color(0xFFFFCA28), Color(0xFFFFB300), Color(0xFFFFA000),
    Color(0xFFFF8F00), Color(0xFFFF6F00), Color(0xFFFFE082), Color(0xFFFFF176),
    Color(0xFFFFF8E1), Color(0xFFFFECB3), Color(0xFFFFE0B2), Color(0xFFFFF3E0),
    0xFFFFF8DC, 0xFFFFFACD, 0xFFFFFDD0, 0xFFFFFFE0,
    0xFFFFFFF0, 0xFFFFFFFF, 0xFFFFF9E5, 0xFFFFF6CC
)
// Morados / Purpuras
val purplePalette = listOf(
    0xFFF3E5F5, 0xFFE1BEE7, 0xFFCE93D8, 0xFFBA68C8, 0xFFAB47BC,
    0xFF9C27B0, 0xFF8E24AA, 0xFF7B1FA2, 0xFF6A1B9A, 0xFF4A148C,
    0xFFEA80FC, 0xFFE040FB, 0xFFD500F9, 0xFFAA00FF, 0xFF8E44AD,
    0xFF9B59B6, 0xFFAF7AC5, 0xFFBB8FCE, 0xFF8E6CAF, 0xFF6C3483,
    0xFF5B2C6F, 0xFF512E5F, 0xFF4A235A, 0xFF39194D, 0xFF2E003E,
    0xFFB388EB, 0xFFCA9BF7, 0xFFD7BDE2, 0xFFE8DAEF, 0xFFF3EAF4,
    0xFFF8F0FA, 0xFFF9F4FC, 0xFFFDF7FF, 0xFFFDF9FF, 0xFFFFFFFF,
    0xFFEEE1F3, 0xFFD6C3DE, 0xFFC5B2D6, 0xFFB09FC7, 0xFF9B8BB7
)
// Naranjas & Amarillos claros / blanco
val orangePalette = listOf(
    Color(0xFFFFF3E0), Color(0xFFFFE0B2), Color(0xFFFFCC80), Color(0xFFFFB74D),
    Color(0xFFFFA726), Color(0xFFFF9800), Color(0xFFFB8C00), Color(0xFFF57C00),
    Color(0xFFEF6C00), Color(0xFFE65100), Color(0xFFFFD180), Color(0xFFFFAB40),
    Color(0xFFFF9100),Color(0xFFFF6D00), Color(0xFFFF7043), Color(0xFFFF5722),
    Color(0xFFF4511E), Color(0xFFE64A19), Color(0xFFD84315), Color(0xFFBF360C),
    Color(0xFFFF9E80), Color(0xFFFF6E40), Color(0xFFFF3D00), Color(0xFFDD2C00),
    Color(0xFFFF8C42), Color(0xFFFFA64D), Color(0xFFFFB37A), Color(0xFFFFC299),
    0xFFFFD1B3, 0xFFFFE0CC, 0xFFFFF0E6, 0xFFFFF8F0,
    0xFFFFFBF5, 0xFFFFFFFF, 0xFFFFF5EC, 0xFFFFE5D0,
    0xFFFFD6B0, 0xFFFFC690, 0xFFFFB570, 0xFFFFA64F
)
// Grises y Neutros
val grayPalette = listOf(
    Color(0xFFFAFAFA), 0xFFF5F5F5, 0xFFEEEEEE, 0xFFE0E0E0,
    Color(0xFFBDBDBD), Color(0xFF9E9E9E), Color(0xFF757575), Color(0xFF616161),
    Color(0xFF424242), Color(0xFF212121), Color(0xFF1C1C1C), Color(0xFF2C2C2C),
    Color(0xFF3C3C3C), Color(0xFF4C4C4C), Color(0xFF5C5C5C), Color(0xFF6C6C6C),
    Color(0xFF7C7C7C), Color(0xFF8C8C8C), Color(0xFF9C9C9C), Color(0xFFACACAC),
    Color(0xFFBCBCBC), Color(0xFFCCCCCC), Color(0xFFDDDDDD), Color(0xFFEDEDED),
    Color(0xFFF4F4F4), Color(0xFFF8F8F8), Color(0xFFFCFCFC), Color(0xFFFFFFFF),
    Color(0xFFEFEFEF), Color(0xFFE5E5E5), Color(0xFFDADADA), Color(0xFFCFCFCF),
    0xFFC4C4C4, 0xFFB9B9B9, 0xFFAEAEAE, 0xFFA3A3A3,
    0xFF989898, 0xFF8D8D8D, 0xFF828282, 0xFF777777
)