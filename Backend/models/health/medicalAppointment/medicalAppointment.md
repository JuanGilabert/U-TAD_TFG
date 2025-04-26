# Esquema para el modelo de datos de citas médicas en Art Health.
**@autor Juan Gilabert Lopez**
*@typedef {Object} HealthMedicalAppointmentSchema*
*@property {Date} fechaCitaMedica* Fecha de la cita médica (obligatorio).
*@property {string} horaCitaMedica* Hora de la cita médica (obligatorio).
*@property {string} servicioCitaMedica* Servicio solicitado en la cita médica (obligatorio).
*@property {string} tipoPruebaCitaMedica* Tipo de prueba médica solicitada (obligatorio).
*@property {string} lugarCitaMedica* Lugar donde se realiza la cita médica (obligatorio).
*@property {string} [notasCitaMedica]* Notas adicionales sobre la cita médica (opcional).
*@property {string} userId* Identificador del usuario que ha creado el recurso. (obligatorio).

# 🏥 Ficha de Cita Médica - Art Health
**Fecha de la cita:** 5 de junio de 2025  
**Hora de la cita:** 9:30  
**Servicio médico:** Traumatología y Cirugía Ortopédica  
**Tipo de prueba:** radiografía  
**Lugar de la cita:** Hospital Universitario Pta Hierro  
**Notas adicionales:** Radiografía (de huesos y articulaciones)