package com.cronosdev.taskmanagerapp.data.repositories.sport

import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseListModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseResultListModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.DatesModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.GenericApiMessageResponse
import com.cronosdev.taskmanagerapp.data.model.sport.SportModel
import com.cronosdev.taskmanagerapp.data.remote.RetrofitInstance
import com.cronosdev.taskmanagerapp.utils.apiErrorMessageExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SportRepository @Inject constructor() {
    suspend fun getAllSports(): ApiResponseResultListModel<SportModel> {
        var result= ApiResponseResultListModel<SportModel>(null,"",0)
        return withContext(Dispatchers.IO) {
            // Obtenemos los datos que nos devuelve la API sobre el ENDPOINT indicado
            val response = RetrofitInstance.utadApi.getAllSports()
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiErrorMessageExtractor(response, "getAllSports")
            result.code  = response.code()
            result
        }
    }
    suspend fun getSportById(sportIdToSearch: Int?): ApiResponseListModel<SportModel> {
        var result= ApiResponseListModel<SportModel>(null,"",0)
        return withContext(Dispatchers.IO) {
            // Obtenemos los datos que nos devuelve la API sobre el ENDPOINT indicado
            val response = RetrofitInstance.utadApi.getSportById(sportIdToSearch.toString())
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiErrorMessageExtractor(response, "getSportById")
            result.code  = response.code()
            result
        }
    }
    suspend fun getUnavailableSportsDates(): ApiResponseListModel<DatesModel> {
        var result= ApiResponseListModel<DatesModel>(null,"",0)
        return withContext(Dispatchers.IO) {
            // Obtenemos los datos que nos devuelve la API sobre el ENDPOINT indicado
            val response = RetrofitInstance.utadApi.getUnavailableSportDates()
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiErrorMessageExtractor(response, "getUnavailableSportsDates")
            result.code  = response.code()
            result
        }
    }
    suspend fun postSport(sportToCreate: SportModel): ApiResponseListModel<GenericApiMessageResponse> {
        var result = ApiResponseListModel<GenericApiMessageResponse>(null,"",0)
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.utadApi.postSport(sportToCreate)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiErrorMessageExtractor(response, "postSport")
            result.code  = response.code()
            result
        }
    }
    suspend fun putSport(sportIdToPut: String, sportToPut: SportModel): ApiResponseListModel<GenericApiMessageResponse> {
        var result= ApiResponseListModel<GenericApiMessageResponse>(null,"",0)
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.utadApi.putSport(sportIdToPut, sportToPut)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiErrorMessageExtractor(response, "putSport")
            result.code  = response.code()
            result
        }
    }
    suspend fun deleteSport(sportIdToDelete: String): ApiResponseListModel<GenericApiMessageResponse> {
        var result = ApiResponseListModel<GenericApiMessageResponse>(null,"",0)
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.utadApi.deleteSport(sportIdToDelete)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiErrorMessageExtractor(response, "deleteSport")
            result.code  = response.code()
            result
        }
    }
}