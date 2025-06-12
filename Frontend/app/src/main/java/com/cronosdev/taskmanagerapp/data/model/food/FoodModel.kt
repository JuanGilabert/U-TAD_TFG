package com.cronosdev.taskmanagerapp.data.model.food

data class FoodModel (
    val _id: String,
    val nombreRestaurante: String,
    val direccionRestaurante: String,
    val tipoComida: String,
    val fechaReserva: String,
    val asistentesReserva: Int,
    val notasReserva: String,
)