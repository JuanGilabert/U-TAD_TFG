package com.cronosdev.taskmanagerapp.data.model.art

data class CinemaModel(
    val _id: String,
    val nombrePelicula: String,
    val descripcionPelicula: String,
    val actoresPelicula: List<String>,
    val fechaInicioPelicula: String,
    val duracionPeliculaMinutos: Int,
    val lugarPelicula: String,
    val precioEntradaPelicula: Float,
    val notasPelicula: String
)