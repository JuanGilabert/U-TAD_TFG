package com.cronosdev.taskmanagerapp.data.repositories.food

import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseListModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseResultListModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.DatesModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.GenericApiMessageResponse
import com.cronosdev.taskmanagerapp.data.model.food.FoodModel
import com.cronosdev.taskmanagerapp.data.remote.RetrofitInstance
import com.cronosdev.taskmanagerapp.utils.apiErrorMessageExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FoodRepository @Inject constructor() {
    suspend fun getAllFoods(): ApiResponseResultListModel<FoodModel> {
        var result= ApiResponseResultListModel<FoodModel>(null,"",0)
        return withContext(Dispatchers.IO) {
            // Obtenemos los datos que nos devuelve la API sobre el ENDPOINT indicado
            val response = RetrofitInstance.utadApi.getAllFoods()
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiErrorMessageExtractor(response, "getAllFoods")
            result.code  = response.code()
            result
        }
    }
    suspend fun getFoodById(foodIdToSearch: String): ApiResponseListModel<FoodModel> {
        var result= ApiResponseListModel<FoodModel>(null,"",0)
        return withContext(Dispatchers.IO) {
            // Obtenemos los datos que nos devuelve la API sobre el ENDPOINT indicado
            val response = RetrofitInstance.utadApi.getFoodById(foodIdToSearch.toString())
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiErrorMessageExtractor(response, "getFoodById")
            result.code  = response.code()
            result
        }
    }
    suspend fun getUnavailableFoodsDates(): ApiResponseListModel<DatesModel> {
        var result= ApiResponseListModel<DatesModel>(null,"",0)
        return withContext(Dispatchers.IO) {
            // Obtenemos los datos que nos devuelve la API sobre el ENDPOINT indicado
            val response = RetrofitInstance.utadApi.getUnavailableFoodDates()
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiErrorMessageExtractor(response, "getUnavailableFoodsDates")
            result.code  = response.code()
            result
        }
    }
    suspend fun postFood(foodToCreate: FoodModel): ApiResponseListModel<GenericApiMessageResponse> {
        var result = ApiResponseListModel<GenericApiMessageResponse>(null,"",0)
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.utadApi.postFood(foodToCreate)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiErrorMessageExtractor(response, "postFood")
            result.code  = response.code()
            result
        }
    }
    suspend fun putFood(foodIdToPut: String, foodToPut: FoodModel): ApiResponseListModel<GenericApiMessageResponse> {
        var result= ApiResponseListModel<GenericApiMessageResponse>(null,"",0)
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.utadApi.putFood(foodIdToPut, foodToPut)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiErrorMessageExtractor(response, "putFood")
            result.code  = response.code()
            result
        }
    }
    suspend fun deleteFood(foodIdToDelete: String): ApiResponseListModel<GenericApiMessageResponse> {
        var result = ApiResponseListModel<GenericApiMessageResponse>(null,"",0)
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.utadApi.deleteFood(foodIdToDelete)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiErrorMessageExtractor(response, "deleteFood")
            result.code  = response.code()
            result
        }
    }
}