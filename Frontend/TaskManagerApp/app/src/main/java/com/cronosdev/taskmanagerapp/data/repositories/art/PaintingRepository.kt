package com.cronosdev.taskmanagerapp.data.repositories.art

import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.GenericApiMessageResponse
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseListModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseResultListModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.DatesModel
import com.cronosdev.taskmanagerapp.data.model.art.PaintingModel
import com.cronosdev.taskmanagerapp.data.remote.RetrofitInstance
import com.cronosdev.taskmanagerapp.utils.apiErrorMessageExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PaintingRepository @Inject constructor() {
    suspend fun getAllPaintings(): ApiResponseResultListModel<PaintingModel> {
        var result= ApiResponseResultListModel<PaintingModel>(null,"",0)
        return withContext(Dispatchers.IO) {
            // Obtenemos los datos que nos devuelve la API sobre el ENDPOINT indicado
            val response = RetrofitInstance.utadApi.getAllPaintings()
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiErrorMessageExtractor(response, "getAllPaintings")
            result.code  = response.code()
            result
        }
    }
    suspend fun getPaintingById(paintingIdToSearch: Int?): ApiResponseListModel<PaintingModel> {
        var result= ApiResponseListModel<PaintingModel>(null,"",0)
        return withContext(Dispatchers.IO) {
            // Obtenemos los datos que nos devuelve la API sobre el ENDPOINT indicado
            val response = RetrofitInstance.utadApi.getPaintingById(paintingIdToSearch.toString())
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiErrorMessageExtractor(response, "getPaintingById")
            result.code  = response.code()
            result
        }
    }
    suspend fun getUnavailablePaintingsDates(): ApiResponseListModel<DatesModel> {
        var result= ApiResponseListModel<DatesModel>(null,"",0)
        return withContext(Dispatchers.IO) {
            // Obtenemos los datos que nos devuelve la API sobre el ENDPOINT indicado
            val response = RetrofitInstance.utadApi.getUnavailablePaintingDates()
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiErrorMessageExtractor(response, "getUnavailablePaintingsDates")
            result.code  = response.code()
            result
        }
    }
    suspend fun postPainting(paintingToCreate: PaintingModel): ApiResponseListModel<GenericApiMessageResponse> {
        var result = ApiResponseListModel<GenericApiMessageResponse>(null,"",0)
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.utadApi.postPainting(paintingToCreate)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiErrorMessageExtractor(response, "postPainting")
            result.code  = response.code()
            result
        }
    }
    suspend fun putPainting(paintingIdToPut: String, paintingToPut: PaintingModel): ApiResponseListModel<GenericApiMessageResponse> {
        var result= ApiResponseListModel<GenericApiMessageResponse>(null,"",0)
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.utadApi.putPainting(paintingIdToPut, paintingToPut)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiErrorMessageExtractor(response, "putPainting")
            result.code  = response.code()
            result
        }
    }
    suspend fun deletePainting(paintingIdToDelete: String): ApiResponseListModel<GenericApiMessageResponse> {
        var result = ApiResponseListModel<GenericApiMessageResponse>(null,"",0)
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.utadApi.deletePainting(paintingIdToDelete)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiErrorMessageExtractor(response, "deletePainting")
            result.code  = response.code()
            result
        }
    }
}