# Esquema para el modelo de datos de tareas de trabajo en Art Work.
**@autor Juan Gilabert Lopez**
*@typedef {Object} WorkSchema*
*@property {string} tituloTarea* Título único de la tarea (obligatorio y único).
*@property {string} descripcionTarea* Descripción detallada de la tarea (obligatorio).
*@property {Date} fechaInicioTarea* Fecha de inicio de la tarea (obligatorio).
*@property {Date} horaInicioTarea* Hora de inicio de la tarea (obligatorio).
*@property {Date} fechaEntregaTarea* Fecha límite de entrega de la tarea (obligatorio).
*@property {Date} horaEntregaTarea* Hora límite de entrega de la tarea (obligatorio).
*@property {string} prioridadTarea* Nivel de prioridad de la tarea (obligatorio).
*@property {string} [notasTarea]* Notas adicionales sobre la tarea (opcional).
*@property {string} userId* Identificador del usuario que ha creado el recurso. (obligatorio).

# 📝 Ficha de Tarea - Art Work
**Título de la tarea:** Informe mensual  
**Descripción:** Redacción y envío del informe mensual de ventas.  
**Fecha de inicio:** 10 de marzo de 2025  
**Hora de inicio:** 18:00  
**Fecha de entrega:** 10 de marzo de 2025  
**Hora de entrega:** 15:00  
**Prioridad:** Alta  
**Notas adicionales:** Revisar los datos con el equipo financiero.