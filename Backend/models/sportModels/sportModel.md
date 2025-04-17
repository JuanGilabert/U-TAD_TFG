# Esquema para el modelo de datos de actividades deportivas en Art Sport.
**@autor Juan Gilabert Lopez**
*@typedef {Object} SportSchema*
*@property {string} nombreActividad* Nombre de la actividad deportiva (obligatorio y único).
*@property {Date} fechaInicioActividad* Fecha en la que se llevará a cabo la actividad (obligatorio).
*@property {Date} horaInicioActividad* Hora en la que comienza la actividad (obligatorio).
*@property {string} lugarActividad* Lugar donde se realizará la actividad (obligatorio).
*@property {number} duracionActividadMinutos* Duración de la actividad en minutos (obligatorio).
*@property {string} [notasActividad]* Notas adicionales sobre la actividad (opcional).
*@property {string} userId* Identificador del usuario que ha creado el recurso. (obligatorio).

# ⚽ Ficha de Actividad Deportiva - Art Sport
**Nombre de la actividad:** Fútbol  
**Fecha de la actividad:** 15 de marzo de 2025  
**Hora de inicio:** 17:00  
**Lugar de la actividad:** Parque Central  
**Duración de la actividad:** 90 minutos  
**Notas adicionales:** Llevar botella de agua.