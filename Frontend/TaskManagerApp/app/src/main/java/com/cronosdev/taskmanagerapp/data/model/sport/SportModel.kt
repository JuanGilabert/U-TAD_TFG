package com.cronosdev.taskmanagerapp.data.model.sport

data class SportModel(
    val _id: String,
    val nombreActividad: String,
    val fechaInicioActividad: String,
    val lugarActividad: String,
    val duracionActividadMinutos: Int,
    val asistentesActividad: List<String>,
    val notasActividad: String
)