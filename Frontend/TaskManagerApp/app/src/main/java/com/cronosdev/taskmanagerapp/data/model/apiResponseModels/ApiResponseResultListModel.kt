package com.cronosdev.taskmanagerapp.data.model.apiResponseModels
//
/** Modelo genérico de respuesta de API que contiene una lista de resultados.
 * Esta clase encapsula la estructura de la respuesta con los datos obtenidos de la API.
 *
 * @author Juan Gilabert Lopez
 * @param T Tipo genérico de los datos almacenados en la lista.
 * @property data Lista de elementos del tipo especificado, que puede ser nula.
 * @property message Mensaje de respuesta proporcionado por la API.
 * @property code Código de estado de la respuesta de la API.
 * @since 1.0
 */
data class ApiResponseResultListModel<T> (var data: List<T>?, var message: String, var code: Int) {
    companion object {
        /** Filtra la lista de resultados en base a un criterio dado.
         * @param predicate Función que define el criterio de filtrado.
         * @return Un nuevo `ApiResponseResultListModel` con los elementos que cumplen el criterio.
         */
        fun <T> ApiResponseResultListModel<T>.filterResultList(predicate: (T) -> Boolean): ApiResponseResultListModel<T> {
            val filteredData = this.data?.let { list ->
                val result = mutableListOf<T>()
                for (item in list) {
                    if (predicate(item)) {
                        result.add(item)
                    }
                }
                result
            }
            return ApiResponseResultListModel(data = filteredData, message = this.message, code = this.code)
        }
        /** Verifica si la lista de resultados está vacía o es nula.
         * @return `true` si la lista es nula o está vacía, `false` en caso contrario.
         */
        fun <T> ApiResponseResultListModel<T>.isResultListNullOrEmpty(): Boolean { return this.data == null || this.data!!.isEmpty() }
        /** Aplica una acción a cada elemento de la lista.
         * @param action Función que se ejecuta sobre cada elemento de la lista.
         */
        fun <T> ApiResponseResultListModel<T>.forEach(action: (T) -> Unit) { this.data?.forEach(action) }
        /** Obtiene el tamaño de la lista de resultados.
         * @return El número de elementos en la lista, o 0 si la lista es nula.
         */
        fun <T> ApiResponseResultListModel<T>.size(): Int { return this.data?.size ?: 0 }
    }
}