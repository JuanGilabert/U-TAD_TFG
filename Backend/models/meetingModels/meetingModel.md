# Esquema para el modelo de datos de reuniones en Art Meeting.
**@autor Juan Gilabert Lopez**
*@typedef {Object} MeetingSchema*
*@property {string} tituloReunion* Título de la reunión (obligatorio y único).
*@property {string} tipoReunion* Tipo de reunión (ej. conferencia, junta, entrevista) (obligatorio).
*@property {string} organizadorReunion* Nombre del organizador de la reunión (obligatorio).
*@property {Array<string>} asistentesReunion* Lista de participantes de la reunión (opcional).
*@property {Date} fechaInicioReunion* Fecha en la que se realizará la reunión (obligatorio).
*@property {string} horaReunion* Hora en la que se realizará la reunión (obligatorio).
*@property {string} lugarReunion* Lugar donde se llevará a cabo la reunión (obligatorio).
*@property {string} [notasReunion]* Notas adicionales sobre la reunión (opcional).
*@property {string} userId* Identificador del usuario que ha creado el recurso. (obligatorio).

# 📅 Ficha de Reunión - Art Meeting
**Título de la reunión:** Cita con Silvia  
**Tipo de reunión:** ej. conferencia, junta, entrevista  
**Organizador:** Juan Gilabert Lopez  
**Asistentes:**  
- Juan Perez  
- Ana Gomez  
**Fecha de la reunión:** 5 de marzo de 2025  
**Hora de la reunión:** 11:00  
**Lugar:** U-TAD  
**Notas adicionales:** Conversación sobre el proyecto integrador Salas-A2.