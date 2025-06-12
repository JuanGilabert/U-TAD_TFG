package com.cronosdev.taskmanagerapp.utils

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun dateTimeFormatterES(fechaStr: String): String? {
    /* Funcion que recibe una fecha string americana (2025-01-19) y la convierte en una fecha al castellano(19 enero 2025) */
    val parser = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    // Formateador de fechas al castellano solo para la visualizacion de los datos.
    val dayTimeFormaterES = SimpleDateFormat("dd MMMM yyyy", Locale("es", "ES"))
    return parser.parse(fechaStr)?.let { dayTimeFormaterES.format(it) }
}
/**
 *
 */
fun longToFechaEEUU(millis: Long): String {
    val dateTime = Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDateTime()
    val dayTimeFormaterEEUU = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return dateTime.format(dayTimeFormaterEEUU)
}
/**
 *
 */
fun fechaEEUUToLong(fecha: String): Long {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    return formatter.parse(fecha)?.time ?: throw IllegalArgumentException("Formato inválido")
}
/**
 *
 */
fun longToFechaCastellano(millis: Long): String {
    val date = Date(millis)
    val formatter = SimpleDateFormat("dd MMMM yyyy", Locale("es", "ES"))
    return formatter.format(date)
}
/**
 *
 */
fun fechaCastellanoToLong(fecha: String): Long {
    val formatter = SimpleDateFormat("dd MMMM yyyy", Locale("es", "ES"))
    return formatter.parse(fecha)?.time ?: throw IllegalArgumentException("Formato inválido")
}