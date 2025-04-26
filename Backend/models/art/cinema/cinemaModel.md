# Esquema para el modelo de datos de películas en Art Cinema.
**@author Juan Gilabert Lopez**
*@typedef {Object} ArtCinemaSchema*
*@property {string} nombrePelicula* Nombre de la película (obligatorio y unico).
*@property {string} descripcionPelicula* Descripción de la película (obligatorio).
*@property {Array<string>} actoresPelicula* Lista de actores de la película (cada actor es obligatorio y unico).
*@property {Date} fechaInicioPelicula* Fecha de inicio de la película (obligatorio).
*@property {Date} horaInicioPelicula* Hora de inicio de la película (obligatorio).
*@property {number} duracionPeliculaMinutos* Duración de la pelicula en minutos (obligatorio).
*@property {string} lugarPelicula* Lugar donde se proyecta la película (obligatorio).
*@property {number} precioEntradaPelicula* Precio de la entrada para la película (obligatorio).
*@property {string} [notasPelicula]* Notas adicionales sobre la película (opcional).
*@property {string} userId* Identificador del usuario que ha creado el recurso. (obligatorio).

# 🎬 Ficha de Película - Art Cinema
**Nombre de la película:** Piratas del Caribe 5: La Venganza del capitán Salazar
**Descripcion:**
La amada Perla Negra del Capitán Jack sigue encerrada en una botella y su racha de mala suerte continúa.
Pero está a punto de enfrentarse a situaciones aún más penosas que le llevarán a recurrir a sus considerables artimañas para sobrevivir
ya que le persigue su enemigo más mortal, el fantasmal Capitán Salazar.
**Actores:**
- Jony Deep  
- Pedro Almodovar
**Fecha de inicio:** 20 de junio de 2025
**Hora de inicio:** 12:00
**Duración:** 120
**Lugar de proyección:**
Cinesa Equinoccio, Majadahonda, Comunidad de Madrid.
**Precio de la entrada:** 11,50 €
**Notas adicionales:**
Comprar entradas con antelación.