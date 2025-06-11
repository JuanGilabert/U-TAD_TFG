package com.cronosdev.taskmanagerapp.data.model.meeting

data class MeetingModel(
    val _id: String,
    val tituloReunion: String,
    val tipoReunion: String,
    val organizadorReunion: String,
    val asistentesReunion: List<String>,
    val fechaInicioReunion: String,
    val fechaFinReunion: String,
    val lugarReunion: String,
    val notasReunion: String
)