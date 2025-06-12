package com.cronosdev.taskmanagerapp.utils.apiDataExtractors
//
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseResultListModel
import com.cronosdev.taskmanagerapp.data.model.art.CinemaModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import retrofit2.Response
////
/**
 *
 */
/*fun <T> apiDataExtractor(response: Response<T>, methodCallName: String = ""): Any =
    if (response.isSuccessful) {
        val rawJson = response.body()?.toString()
        try {
            // Intentamos convertir a lista
            val type = object : TypeToken<List<CinemaModel>>() {}.type
            val cinemaList: List<CinemaModel> = Gson().fromJson(rawJson, type)
            // Si la conversion fue exitosa indica
            cinemaList
        } catch (e: Exception) {
            // No era una lista, intentamos leer el mensaje
            val json = JSONObject(rawJson ?: "{}")
            result.data = emptyList()
            result.message = json.optString("message", "Sin datos")
        }
    } else {
        result.message = apiErrorMessageExtractor(response, "getAllCinemas")
    }*/
/** Esta funcion sirve para extraer el mensaje devuelto por la api en cualquier peticion independientemente de si la respuesta es Successfull o no, siempre se extrae el mensaje devuelto.
 * ESTO USA JSON NATIVO EN VEZ DE USAR Gson Converter de: Gson (com.google.gson). REVISAR Y ACTUALIZAR.
 * @param
 * @since 1.0
 *
 */
fun <T> apiMessageExtractor(response: Response<T>, methodCallName: String = ""): String =
    if (response.isSuccessful) {
        // Intentamos extraer mensaje del body.
        try {
            val bodyString = response.body()?.toString()
            if (bodyString.isNullOrBlank()) {
                methodCallName
            } else {
                val json = JSONObject(bodyString ?: "{}")
                json.optString("message", "Error desconocidoSuccessfull")
            }
        } catch (e: Exception) { "ApiMessageExtractorException. Error procesando. Body exitoso: $e" }
    } else {
        //val errorString = response.errorBody()?.string()
        //try { JSONObject(errorString!!).optString("message", "Error desconocido") }
        //catch (e: Exception) { "ApiErrorMessageExtractorException. Datos obtenidos: $errorString .ErrorStackTrace: $e" }
        //Intentamos extraer mensaje del errorBody, que normalmente es un JSON
        //
        val errorString = response.errorBody()?.string()
        try {
            val json = JSONObject(errorString ?: "")
            json.optString("message", "Error desconocidoNotSuccessfull")
        } catch (e: Exception) {
            "ApMessageExtractorException. Datos obtenidos: $errorString . ErrorStackTrace: $e"
        }
    }

/*if (response.isSuccessful) {
    // Intentamos extraer mensaje del body.
    try {
        val bodyString = response.body()?.toString()
        if (!bodyString.isNullOrBlank()) bodyString
        else methodCallName
    } catch (e: Exception) { "Error procesando body exitoso: $e" }
}*/