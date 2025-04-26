# Esquema para el modelo de datos de eventos musicales en Art Music.
**@author Juan Gilabert Lopez**
*@typedef {Object} ArtMusicSchema*
*@property {string} nombreEvento* Nombre del evento musical (obligatorio y único).
*@property {string} descripcionEvento* Descripción del evento musical (obligatorio).
*@property {Array<string>} artistasEvento* Lista de artistas que participan en el evento (cada artista es obligatorio y único).
*@property {Date} fechaInicioEvento* Fecha de inicio del evento (obligatorio).
*@property {Date} fechaFinEvento* Fecha de finalización del evento (obligatorio).
*@property {string} horaInicioEvento* Hora de inicio del evento (obligatorio).
*@property {string} lugarEvento* Lugar donde se lleva a cabo el evento (obligatorio).
*@property {number} precioEvento* Precio de la entrada para el evento (obligatorio).
*@property {string} [notasEvento]* Notas adicionales sobre el evento (opcional).
*@property {string} userId* Identificador del usuario que ha creado el recurso. (obligatorio).

# 🎵 Ficha de Evento Musical - Art Music
**Nombre del evento:** Concierto Cocacola Music Experience  
**Descripción:**  
Concierto de música pop con artistas nacionales e internacionales.  
**Artistas:**  
- Juan Magan  
- Coldplay  
**Fecha de inicio:** 20 de junio de 2025  
**Fecha de finalización:** 20 de junio de 2025  
**Hora de inicio:** 12:00  
**Lugar del evento:**  
Auditorio Nacional  
**Precio de la entrada:** 150,00 €  
**Notas adicionales:**  
Comprar entradas con antelación.