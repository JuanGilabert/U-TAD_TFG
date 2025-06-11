package com.cronosdev.taskmanagerapp.data.model.work

data class WorkModel(
    val _id: String,
    val tituloTarea: String,
    val descripcionTarea: String,
    val fechaInicioTarea: String,
    val fechaEntregaTarea: String,
    val organizadorTarea: String,
    val prioridadTarea: String,
    val notasTarea: String
)