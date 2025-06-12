package com.cronosdev.taskmanagerapp.data.repositories.work

import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseListModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseResultListModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.DatesModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.GenericApiMessageResponse
import com.cronosdev.taskmanagerapp.data.model.work.WorkModel
import com.cronosdev.taskmanagerapp.data.remote.RetrofitInstance
import com.cronosdev.taskmanagerapp.utils.apiDataExtractors.apiMessageExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WorkRepository @Inject constructor() {
    suspend fun getAllWorks(): ApiResponseResultListModel<WorkModel> {
        var result= ApiResponseResultListModel<WorkModel>(null,"",0)
        return withContext(Dispatchers.IO) {
            // Obtenemos los datos que nos devuelve la API sobre el ENDPOINT indicado
            val response = RetrofitInstance.utadApi.getAllWorks()
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "getAllWorks")
            result.code  = response.code()
            result
        }
    }
    suspend fun getWorkById(workIdToSearch: String): ApiResponseListModel<WorkModel> {
        var result= ApiResponseListModel<WorkModel>(null,"",0)
        return withContext(Dispatchers.IO) {
            // Obtenemos los datos que nos devuelve la API sobre el ENDPOINT indicado
            val response = RetrofitInstance.utadApi.getWorkById(workIdToSearch.toString())
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "getWorkById")
            result.code  = response.code()
            result
        }
    }
    suspend fun getUnavailableWorksDates(): ApiResponseListModel<DatesModel> {
        var result= ApiResponseListModel<DatesModel>(null,"",0)
        return withContext(Dispatchers.IO) {
            // Obtenemos los datos que nos devuelve la API sobre el ENDPOINT indicado
            val response = RetrofitInstance.utadApi.getUnavailableWorkDates()
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "getUnavailableWorksDates")
            result.code  = response.code()
            result
        }
    }
    suspend fun postWork(workToCreate: WorkModel): ApiResponseListModel<GenericApiMessageResponse> {
        var result = ApiResponseListModel<GenericApiMessageResponse>(null,"",0)
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.utadApi.postWork(workToCreate)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "postWork")
            result.code  = response.code()
            result
        }
    }
    suspend fun putWork(workIdToPut: String, workToPut: WorkModel): ApiResponseListModel<GenericApiMessageResponse> {
        var result= ApiResponseListModel<GenericApiMessageResponse>(null,"",0)
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.utadApi.putWork(workIdToPut.toString(), workToPut)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "putWork")
            result.code  = response.code()
            result
        }
    }
    /** Corrutina que elimina una reserva existente.
     * **Parameters;
     * @param musicIdToDelete El identificador único de la reserva a eliminar.
     * @return Un objeto `ApiResponseListModel<PostBookingModel>` indicando el resultado de la operación.
     * @throws IOException Si ocurre un error en la comunicación con la API.
     * @since 1.0
     */
    suspend fun deleteWork(workIdToDelete: String): ApiResponseListModel<GenericApiMessageResponse> {
        var result = ApiResponseListModel<GenericApiMessageResponse>(null,"",0)
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.utadApi.deleteWork(workIdToDelete.toString())
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "deleteWork")
            result.code  = response.code()
            result
        }
    }
}