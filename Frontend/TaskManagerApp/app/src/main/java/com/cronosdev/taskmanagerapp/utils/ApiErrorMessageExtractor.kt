package com.cronosdev.taskmanagerapp.utils
//
import org.json.JSONObject
import retrofit2.Response
/** Extrae el campo "message" del JSON de error
 * ESTO USA JSON NATIVO EN VEZ DE USAR Gson Converter de: Gson (com.google.gson)
 * @param
 * @since 1.0
 *
 */
fun <T> apiErrorMessageExtractor(response: Response<T>, methodCallName: String = ""): String =
    if (response.isSuccessful) {
        // Intentamos extraer mensaje del body (puede que el body sea solo texto plano o un JSON con "message")
        try {
            val bodyString = response.body()?.toString()
            if (!bodyString.isNullOrBlank()) bodyString
            else methodCallName
        }catch (e: Exception) { "Error procesando body exitoso: $e" }
    } else {
        //val errorString = response.errorBody()?.string()
        //try { JSONObject(errorString!!).optString("message", "Error desconocido") }
        //catch (e: Exception) { "ApiErrorMessageExtractorException. Datos obtenidos: $errorString .ErrorStackTrace: $e" }
        //Intentamos extraer mensaje del errorBody, que normalmente es un JSON
        //
        val errorString = response.errorBody()?.string()
        try {
            val json = JSONObject(errorString ?: "")
            json.optString("message", "Error desconocido")
        } catch (e: Exception) {
            "ApiErrorMessageExtractorException. Datos obtenidos: $errorString . ErrorStackTrace: $e"
        }
    }


/*if (response.isSuccessful) response.message()
    else {
        val errorString = response.errorBody()?.string()
        try { JSONObject(errorString!!).optString("message", "Error desconocido") } catch (e: Exception) { "ApiErrorMessageExtractorException. Datos obtenidos: $errorString .ErrorStackTrace: $e" }
    }*/