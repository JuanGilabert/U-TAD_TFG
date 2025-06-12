package com.cronosdev.taskmanagerapp.data.repositories.travel

import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseListModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseResultListModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.DatesModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.GenericApiMessageResponse
import com.cronosdev.taskmanagerapp.data.model.travel.TravelModel
import com.cronosdev.taskmanagerapp.data.remote.RetrofitInstance
import com.cronosdev.taskmanagerapp.utils.apiDataExtractors.apiMessageExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TravelRepository @Inject constructor() {
    suspend fun getAllTravels(): ApiResponseResultListModel<TravelModel> {
        var result= ApiResponseResultListModel<TravelModel>(null,"",0)
        return withContext(Dispatchers.IO) {
            // Obtenemos los datos que nos devuelve la API sobre el ENDPOINT indicado
            val response = RetrofitInstance.utadApi.getAllTravels()
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "getAllTravels")
            result.code  = response.code()
            result
        }
    }
    suspend fun getTravelById(travelIdToSearch: String): ApiResponseListModel<TravelModel> {
        var result= ApiResponseListModel<TravelModel>(null,"",0)
        return withContext(Dispatchers.IO) {
            // Obtenemos los datos que nos devuelve la API sobre el ENDPOINT indicado
            val response = RetrofitInstance.utadApi.getTravelById(travelIdToSearch)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "getTravelById")
            result.code  = response.code()
            result
        }
    }
    suspend fun getUnavailableTravelsDates(): ApiResponseListModel<DatesModel> {
        var result= ApiResponseListModel<DatesModel>(null,"",0)
        return withContext(Dispatchers.IO) {
            // Obtenemos los datos que nos devuelve la API sobre el ENDPOINT indicado
            val response = RetrofitInstance.utadApi.getUnavailableTravelDates()
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "getUnavailableTravelsDates")
            result.code  = response.code()
            result
        }
    }
    suspend fun postTravel(travelToCreate: TravelModel): ApiResponseListModel<GenericApiMessageResponse> {
        var result = ApiResponseListModel<GenericApiMessageResponse>(null,"",0)
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.utadApi.postTravel(travelToCreate)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "postTravel")
            result.code  = response.code()
            result
        }
    }
    suspend fun putTravel(travelIdToPut: String, travelToPut: TravelModel): ApiResponseListModel<GenericApiMessageResponse> {
        var result= ApiResponseListModel<GenericApiMessageResponse>(null,"",0)
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.utadApi.putTravel(travelIdToPut, travelToPut)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "putTravel")
            result.code  = response.code()
            result
        }
    }
    suspend fun deleteTravel(travelIdToDelete: String): ApiResponseListModel<GenericApiMessageResponse> {
        var result = ApiResponseListModel<GenericApiMessageResponse>(null,"",0)
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.utadApi.deleteTravel(travelIdToDelete)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "deleteTravel")
            result.code  = response.code()
            result
        }
    }
}