package com.cronosdev.taskmanagerapp.data.model.apiResponseModels
//
/** Modelo genérico de respuesta de API que contiene un único objeto de datos.
 * Esta clase encapsula la estructura de la respuesta con los datos obtenidos de la API.
 *
 * @author Juan Gilabert Lopez
 * @param T Tipo genérico del dato almacenado.
 * @property data Objeto de tipo T que puede ser nulo.
 * @property message Mensaje de respuesta proporcionado por la API.
 * @property code Código de estado de la respuesta de la API.
 * @since 1.0
 */
data class ApiResponseListModel<T> (
    var data: T?,
    var message: String,
    var code: Int
) {
    companion object {
        /** Verifica si el objeto `data` es nulo o está vacío.
         * @return `true` si `data` es nulo o está vacío, `false` en caso contrario.
         */
        fun <T> ApiResponseListModel<T>.isNullOrEmpty(): Boolean{
            return when (val value = this.data) {
                null -> true // Si data es null, devolvemos true
                is Collection<*> -> value.isEmpty() // Si data es una Collection<*>, usamos isEmpty()
                is String -> value.isEmpty() // Si data es un String, usamos isEmpty()
                else -> false // Si data es de otro tipo, asumimos que no tiene un concepto de "vacío" y devolvemos false.
            }
        }
        /** Verifica si un elemento está contenido en `data`, asumiendo que `data` es una lista.
         * @param element Elemento a verificar dentro de la lista.
         * @return `true` si el elemento está contenido en `data`, `false` en caso contrario.
         */
        fun <T> ApiResponseListModel<List<T>>.contains(element: T): Boolean {
            return this.data?.contains(element) == true
        }
        /** Obtiene el tamaño de `data` si es una colección o un string.
         * @return Tamaño de `data`, o `null` si no se puede determinar.
         */
        fun <T> ApiResponseListModel<T>.size(): Int? {
            return when (val value = this.data) {
                is Collection<*> -> value.size
                is String -> value.length
                else -> null
            }
        }
    }
}
