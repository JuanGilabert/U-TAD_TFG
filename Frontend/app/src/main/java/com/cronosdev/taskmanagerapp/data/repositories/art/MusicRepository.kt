package com.cronosdev.taskmanagerapp.data.repositories.art

import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.GenericApiMessageResponse
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseListModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseResultListModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.DatesModel
import com.cronosdev.taskmanagerapp.data.model.art.MusicModel
import com.cronosdev.taskmanagerapp.data.remote.RetrofitInstance
import com.cronosdev.taskmanagerapp.utils.apiDataExtractors.apiMessageExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MusicRepository @Inject constructor() {
    suspend fun getAllMusics(): ApiResponseResultListModel<MusicModel> {
        var result= ApiResponseResultListModel<MusicModel>(null,"",0)
        return withContext(Dispatchers.IO) {
            // Obtenemos los datos que nos devuelve la API sobre el ENDPOINT indicado
            val response = RetrofitInstance.utadApi.getAllMusics()
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "getAllMusics")
            result.code  = response.code()
            // Devolvemos el resultado
            result
        }
    }
    suspend fun getMusicById(musicIdToSearch: Int?): ApiResponseListModel<MusicModel> {
        var result= ApiResponseListModel<MusicModel>(null,"",0)
        return withContext(Dispatchers.IO) {
            // Obtenemos los datos que nos devuelve la API sobre el ENDPOINT indicado
            val response = RetrofitInstance.utadApi.getMusicById(musicIdToSearch.toString())
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "getMusicById")
            result.code  = response.code()
            // Devolvemos el resultado
            result
        }
    }
    suspend fun getUnavailableMusicsDates(): ApiResponseListModel<DatesModel> {
        var result= ApiResponseListModel<DatesModel>(null,"",0)
        return withContext(Dispatchers.IO) {
            // Obtenemos los datos que nos devuelve la API sobre el ENDPOINT indicado
            val response = RetrofitInstance.utadApi.getUnavailableMusicDates()
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "getUnavailableMusicsDates")
            result.code  = response.code()
            // Devolvemos el resultado
            result
        }
    }
    suspend fun postMusic(musicToCreate: MusicModel): ApiResponseListModel<GenericApiMessageResponse> {
        var result = ApiResponseListModel<GenericApiMessageResponse>(null,"",0)
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.utadApi.postMusic(musicToCreate)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "postMusic")
            result.code  = response.code()
            // Devolvemos el resultado
            result
        }
    }
    suspend fun putMusic(cinemaIdToPut: String, musicToPut: MusicModel): ApiResponseListModel<GenericApiMessageResponse> {
        var result= ApiResponseListModel<GenericApiMessageResponse>(null,"",0)
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.utadApi.putMusic(cinemaIdToPut, musicToPut)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "putMusic")
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
    suspend fun deleteMusic(cinemaIdToDelete: String): ApiResponseListModel<GenericApiMessageResponse> {
        var result = ApiResponseListModel<GenericApiMessageResponse>(null,"",0)
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.utadApi.deleteMusic(cinemaIdToDelete)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "deleteMusic")
            result.code  = response.code()
            // Devolvemos el resultado
            result
        }
    }
}