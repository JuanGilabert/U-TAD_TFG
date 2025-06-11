package com.cronosdev.taskmanagerapp.data.model.travel

data class TravelModel(
    val _id: String,
    val nombreDestinoViaje: String,
    val fechaSalidaViaje: String,
    val lugarSalidaViaje: String,
    val lugarDestinoViaje: String,
    val fechaRegresoViaje: String,
    val transporteViaje: String,
    val acompañantesViaje: List<String>,
    val notasViaje: String
)