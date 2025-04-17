# Esquema para el modelo de datos de exposiciones de arte en Art Painting.
**@author Juan Gilabert Lopez**
*@typedef {Object} ArtPaintingSchema*
*@property {string} nombreExposicionArte* Nombre de la exposición de arte (obligatorio y único).
*@property {string} descripcionExposicionArte* Descripción de la exposición de arte (obligatorio).
*@property {Array<string>} pintoresExposicionArte* Lista de pintores que participan en la exposición
(cada pintor es obligatorio y único).
*@property {Date} fechaInicioExposicionArte* Fecha de inicio de la exposición (obligatorio).
*@property {string} horaInicioExposicionArte* Hora de inicio de la exposición (obligatorio).
*@property {string} lugarExposicionArte* Lugar donde se lleva a cabo la exposición (obligatorio).
*@property {number} precioEntradaExposicionArte* Precio de la entrada para la exposición (obligatorio).
*@property {string} [notasExposicionArte]* Notas adicionales sobre la exposición de arte (opcional).
*@property {string} userId* Identificador del usuario que ha creado el recurso. (obligatorio).

# 🖼️ Ficha de Exposición de Arte - Art Painting
**Nombre de la exposición:** Un paseo botánico por el Prado  
**Descripción:**  
El itinerario recorre un amplio abanico temporal, desde la escultura romana hasta comienzos del siglo XVIII, para descubrir cómo en la pintura, la representación de flores y plantas nos puede hablar de la simbología mitológica, religiosa, nobiliaria o costumbrista para transmitir a la escena cualidades que les son propias.  
**Pintores:**  
- Tiziano  
- Rubens  
- Velazquez  
**Fecha de inicio:** 20 de junio de 2025  
**Hora de inicio:** 10:00  
**Lugar de la exposición:**  
Museo del Prado  
**Precio de la entrada:** 19,50 €  
**Notas adicionales:**  
Comprar entradas con antelación.