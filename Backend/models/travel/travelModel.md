# Esquema para el modelo de datos de viajes en Art Travel.
**@autor Juan Gilabert Lopez**
*@typedef {Object} TravelSchema*
*@property {string} nombreDestinoViaje* Nombre del destino del viaje (obligatorio y único).
*@property {Date} fechaSalidaViaje* Fecha de salida del viaje (obligatorio).
*@property {Date} horaSalidaViaje* Hora de salida del viaje (obligatorio).
*@property {Date} fechaRegresoViaje* Fecha de regreso del viaje (obligatorio).
*@property {Date} horaRegresoViaje* Hora de regreso del viaje (obligatorio).
*@property {string} transporteViaje* Medio de transporte utilizado para el viaje (obligatorio).
*@property {string} lugarSalidaViaje* Lugar desde donde inicia el viaje (obligatorio).
*@property {string} lugarDestinoViaje* Lugar de destino del viaje (obligatorio).
*@property {Array<string>} acompañantesViaje* Lista de acompañantes en el viaje (opcional).
*@property {string} [notasViaje]* Notas adicionales sobre el viaje (opcional).
*@property {string} userId* Identificador del usuario que ha creado el recurso. (obligatorio).

# 🌍 Ficha de Viaje - Art Travel
**Destino del viaje:** París  
**Fecha de salida:** 1 de julio de 2025  
**Hora de salida:** 08:00  
**Fecha de regreso:** 10 de julio de 2025  
**Hora de regreso:** 17:00  
**Transporte:** Avión  
**Lugar de salida:** Aeropuerto Madrid Barajas Adolfo Suárez  
**Lugar de destino:** Estación de París  
**Acompañantes:**  
- Juan Perez  
- Ana Gomez  
**Notas adicionales:** Verificar visa y hacer check-in online.