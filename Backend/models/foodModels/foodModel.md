# Esquema para el modelo de datos de reservas en restaurantes en Art Food.
**@autor Juan Gilabert Lopez**
*@typedef {Object} FoodSchema*
*@property {string} nombreRestaurante* Nombre del restaurante (obligatorio y único).
*@property {string} direccionRestaurante* Dirección del restaurante (obligatorio; debe incluir calle, ciudad, etc.).
*@property {string} tipoComida* Tipo de comida que ofrece el restaurante (obligatorio; ejemplo: francesa, italiana, etc.).
*@property {Date} fechaReserva* Fecha en la que se realiza la reserva (obligatorio).
*@property {string} horaReserva* Hora de la reserva (obligatorio).
*@property {number} asistentesReserva* Número de personas que asistirán a la reserva (obligatorio).
*@property {string} [notasReserva]* Notas adicionales sobre la reserva (opcional).
*@property {string} userId* Identificador del usuario que ha creado el recurso. (obligatorio).

# 🍽️ Ficha de Reserva - Art Food
**Nombre del restaurante:** Restaurante Bar La Cepa.  
**Dirección:** Calle Mayor, 45, Madrid, Comunidad de Madrid.  
**Tipo de comida:** Española  
**Fecha de reserva:** 1 de agosto de 2025  
**Hora de reserva:** 20:00  
**Número de asistentes:** 4  
**Notas adicionales:** Reservar mesa para 4 personas.