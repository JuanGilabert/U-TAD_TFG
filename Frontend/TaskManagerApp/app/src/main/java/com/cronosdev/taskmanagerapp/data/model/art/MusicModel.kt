package com.cronosdev.taskmanagerapp.data.model.art

data class MusicModel(
    val _id: String,
    val nombreEvento: String,
    val descripcionEvento: String,
    val artistasEvento: List<String>,
    val fechaInicioEvento: String,
    val fechaFinEvento: String,
    val lugarEvento: String,
    val precioEvento: Float,
    val notasEvento: String
)