package com.cronosdev.taskmanagerapp.data.model.health

data class MedicamentModel(
    val _id: String,
    val nombreMedicamento: String,
    val viaAdministracionMedicamento: ViaAdministracionMedicamentoModel,
    val cantidadTotalCajaMedicamento: Int,
    val fechaCaducidadMedicamento: String,
    val notasMedicamento: String
)