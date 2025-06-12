package com.cronosdev.taskmanagerapp.data.repositories.art

import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.GenericApiMessageResponse
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseListModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseResultListModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.DatesModel
import com.cronosdev.taskmanagerapp.data.model.art.CinemaModel
import com.cronosdev.taskmanagerapp.data.remote.RetrofitInstance
import com.cronosdev.taskmanagerapp.utils.apiDataExtractors.apiMessageExtractor
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
/**
 *
 */
class CinemaRepository @Inject constructor() {
    suspend fun getAllCinemas(): ApiResponseResultListModel<CinemaModel> {
        var result = ApiResponseResultListModel<CinemaModel>(null,"",0)
        return withContext(Dispatchers.IO) {
            // Obtenemos los datos que nos devuelve la API sobre el ENDPOINT indicado
            val response = RetrofitInstance.utadApi.getAllCinemas()
            /*if (response.isSuccessful) {
                val rawJson = response.body()?.toString()
                try {
                    // Intentamos convertir a lista
                    val type = object : TypeToken<List<CinemaModel>>() {}.type
                    val cinemaList: List<CinemaModel> = Gson().fromJson(rawJson ?: "{}", type)
                    // Si la conversion fue exitosa indica que si es la lista esperada.
                    result.data = response.body()
                } catch (e: Exception) {
                    // Implica que no es una lista, por lo tanto los datos no existen.
                    result.data = null
                }
            } else {
                result.data = null
            }*/
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "getAllCinemas")
            result.code  = response.code()
            result
        }
    }
    /*
    * if (response.isSuccessful) {
                val rawJson = response.body()?.toString()
                try {
                    // Intentamos convertir a lista
                    val type = object : TypeToken<List<CinemaModel>>() {}.type
                    val cinemaList: List<CinemaModel> = Gson().fromJson(rawJson, type)
                    // Si la conversion fue exitosa indica que si es la lista esperada.
                    result.data = cinemaList
                } catch (e: Exception) {
                    // Implica que no es una lista, por lo tanto los datos estan vacios y se obtiene el mensaje devuelto.
                    val json = JSONObject(rawJson ?: "{}")
                    result.data = emptyList()
                    result.message = json.optString("message", "Sin datos")
                }
            } else {
                result.message = apiMessageExtractor(response, "getAllCinemas")
            }*/
    suspend fun getCinemaById(cinemaId: String): ApiResponseListModel<CinemaModel> {
        var result= ApiResponseListModel<CinemaModel>(null,"",0)
        return withContext(Dispatchers.IO) {
            // Obtenemos los datos que nos devuelve la API sobre el ENDPOINT indicado
            val response = RetrofitInstance.utadApi.getCinemaById(cinemaId)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "getCinemaById")
            result.code  = response.code()
            // Devolvemos el resultado
            result
        }
    }
    suspend fun getUnavailableCinemasDates(): ApiResponseListModel<DatesModel> {
        var result= ApiResponseListModel<DatesModel>(null,"",0)
        return withContext(Dispatchers.IO) {
            // Obtenemos los datos que nos devuelve la API sobre el ENDPOINT indicado
            val response = RetrofitInstance.utadApi.getUnavailableCinemaDates()
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "getUnavailableCinemasDates")
            result.code  = response.code()
            // Devolvemos el resultado
            result
        }
    }
    suspend fun getAvailableDatesOnDayByDate(startDate: String): ApiResponseListModel<DatesModel> {
        var result = ApiResponseListModel<DatesModel>(null, "", 0)
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.utadApi.getAvailableCinemaDatesOnDayByDate(startDate)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "getAvailableDatesOnDayByDate")
            result.code  = response.code()
            // Devolvemos el resultado
            result
        }
    }
    suspend fun postCinema(cinemaToCreate: CinemaModel): ApiResponseListModel<GenericApiMessageResponse> {
        var result = ApiResponseListModel<GenericApiMessageResponse>(null,"",0)
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.utadApi.postCinema(cinemaToCreate)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "postCinema")
            result.code  = response.code()
            // Devolvemos el resultado
            result
        }
    }
    suspend fun putCinema(cinemaIdToPut: String, cinemaRequest: CinemaModel): ApiResponseListModel<GenericApiMessageResponse> {
        var result= ApiResponseListModel<GenericApiMessageResponse>(null,"",0)
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.utadApi.putCinema(cinemaIdToPut, cinemaRequest)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "putCinema")
            result.code  = response.code()
            // Devolvemos el resultado
            result
        }
    }
    /** Corrutina que elimina una reserva existente.
     * **Parameters;
     * @param cinemaIdToDelete El identificador único de la reserva a eliminar.
     * @return Un objeto `ApiResponseListModel<PostBookingModel>` indicando el resultado de la operación.
     * @throws IOException Si ocurre un error en la comunicación con la API.
     * @since 1.0
     */
    suspend fun deleteCinema(cinemaIdToDelete: String): ApiResponseListModel<GenericApiMessageResponse> {
        var result = ApiResponseListModel<GenericApiMessageResponse>(null,"",0)
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.utadApi.deleteCinema(cinemaIdToDelete)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "deleteCinema")
            result.code  = response.code()
            // Devolvemos el resultado
            result
        }
    }
}