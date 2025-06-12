package com.cronosdev.taskmanagerapp.data.model.health
data class MedicalAppointmentModel(
    val _id: String,
    val fechaCitaMedica: String,
    val servicioCitaMedica: String,
    val tipoPruebaCitaMedica: String,
    val lugarCitaMedica: String,
    val notasCitaMedica : String,
)