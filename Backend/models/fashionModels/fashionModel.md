# Esquema para el modelo de datos de prendas de moda en Fashion.
**@autor Juan Gilabert Lopez**
*@typedef {Object} FashionSchema*
*@property {string} nombrePrenda* Nombre de la prenda de moda (obligatorio y único).
*@property {string} tipoPrenda* Tipo de prenda (ej. camiseta, pantalón, etc.) (obligatorio).
*@property {string} generoPrenda* Género de la prenda (obligatorio).
*@property {string} tallaPrenda* Talla de la prenda (obligatorio).
*@property {string} colorPrenda* Color de la prenda (obligatorio).
*@property {string} materialPrenda* Material de la prenda (obligatorio).
*@property {number} precioPrenda* Precio de la prenda (obligatorio).
*@property {string} marcaPrenda* Marca de la prenda (obligatorio).
*@property {string} [stockPrenda]* Cantidad de stock disponible de la prenda (opcional).
*@property {string} [notasPrenda]* Notas adicionales sobre la prenda (opcional).
*@property {string} userId* Identificador del usuario que ha creado la prenda. (obligatorio).

# 🧥 Ficha de Prenda - Fashion
**Nombre de la prenda:** Camiseta básica  
**Tipo de prenda:** Camiseta  
**Género:** Unisex  
**Tallas disponibles:** S, M, L, XL  
**Colores disponibles:** Blanco, Negro, Azul  
**Material:** Algodón  
**Precio:** 19.99 EUR  
**Marca:** Marca Genérica  
**Stock disponible:** 150 unidades  
**Notas adicionales:** Ninguna.