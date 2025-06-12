package com.cronosdev.taskmanagerapp.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ShowAlertDialogComponent(alertDialogMessageToShow: String, onAlertDialogDismissRequest:(Boolean) -> Unit, onAlertDialogButtonClick:(String)-> Unit) {
    AlertDialog(
        //title = { Text(alertDialogMessageToShow) },
        text = { Text(alertDialogMessageToShow) },
        onDismissRequest = { onAlertDialogDismissRequest(false) },
        confirmButton = { TextButton(onClick = { onAlertDialogButtonClick("Si") }) { Text("Si") } },
        dismissButton = { TextButton(onClick = { onAlertDialogButtonClick("No") }) { Text("No") } }
    )
}