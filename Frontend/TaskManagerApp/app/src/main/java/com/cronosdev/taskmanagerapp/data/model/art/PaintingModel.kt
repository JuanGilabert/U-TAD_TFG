package com.cronosdev.taskmanagerapp.data.model.art

data class PaintingModel(
    val _id: String,
    val nombreExposicionArte: String,
    val descripcionExposicionArte: String,
    val pintoresExposicionArte: List<String>,
    val fechaInicioExposicionArte: String,
    val fechaFinExposicionArte: String,
    val lugarExposicionArte: String,
    val precioEntradaExposicionArte: Float,
    val notasExposicionArte: String
)