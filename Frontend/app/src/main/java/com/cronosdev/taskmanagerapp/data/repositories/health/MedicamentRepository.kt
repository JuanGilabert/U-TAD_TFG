package com.cronosdev.taskmanagerapp.data.repositories.health
//
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseListModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseResultListModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.DatesModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.GenericApiMessageResponse
import com.cronosdev.taskmanagerapp.data.model.health.MedicamentModel
import com.cronosdev.taskmanagerapp.data.remote.RetrofitInstance
import com.cronosdev.taskmanagerapp.utils.apiDataExtractors.apiMessageExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
/**
 *
 */
class MedicamentRepository @Inject constructor() {
    suspend fun getAllMedicaments(): ApiResponseResultListModel<MedicamentModel> {
        var result= ApiResponseResultListModel<MedicamentModel>(null,"",0)
        return withContext(Dispatchers.IO) {
            // Obtenemos los datos que nos devuelve la API sobre el ENDPOINT indicado
            val response = RetrofitInstance.utadApi.getAllMedicaments()
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "getAllMedicaments")
            result.code  = response.code()
            result
        }
    }
    suspend fun getMedicamentById(medicamentIdToSearch: String): ApiResponseListModel<MedicamentModel> {
        var result= ApiResponseListModel<MedicamentModel>(null,"",0)
        return withContext(Dispatchers.IO) {
            // Obtenemos los datos que nos devuelve la API sobre el ENDPOINT indicado
            val response = RetrofitInstance.utadApi.getMedicamentById(medicamentIdToSearch)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "getMedicamentById")
            result.code  = response.code()
            result
        }
    }
    suspend fun getUnavailableMedicamentsDates(): ApiResponseListModel<DatesModel> {
        var result= ApiResponseListModel<DatesModel>(null,"",0)
        return withContext(Dispatchers.IO) {
            // Obtenemos los datos que nos devuelve la API sobre el ENDPOINT indicado
            val response = RetrofitInstance.utadApi.getUnavailableMedicamentDates()
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "getUnavailableMedicamentsDates")
            result.code  = response.code()
            // Devolvemos el resultado
            result
        }
    }
    suspend fun getMedicamentExpirationDatesByDate(fechaCaducidadMedicamento: String): ApiResponseListModel<DatesModel> {
        var result= ApiResponseListModel<DatesModel>(null,"",0)
        return withContext(Dispatchers.IO) {
            // Obtenemos los datos que nos devuelve la API sobre el ENDPOINT indicado
            val response = RetrofitInstance.utadApi.getMedicamentExpirationDatesByDate(fechaCaducidadMedicamento)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "getMedicamentExpirationDatesByDate")
            result.code  = response.code()
            result
        }
    }
    suspend fun postMedicament(medicamentToCreate: MedicamentModel): ApiResponseListModel<GenericApiMessageResponse> {
        var result = ApiResponseListModel<GenericApiMessageResponse>(null,"",0)
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.utadApi.postMedicament(medicamentToCreate)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "postMedicament")
            result.code  = response.code()
            result
        }
    }
    suspend fun putMedicament(medicamentIdToPut: String, medicamentToPut: MedicamentModel): ApiResponseListModel<GenericApiMessageResponse> {
        var result= ApiResponseListModel<GenericApiMessageResponse>(null,"",0)
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.utadApi.putMedicament(medicamentIdToPut, medicamentToPut)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "putMedicament")
            result.code  = response.code()
            result
        }
    }
    suspend fun deleteMedicament(medicamentIdToDelete: String): ApiResponseListModel<GenericApiMessageResponse> {
        var result = ApiResponseListModel<GenericApiMessageResponse>(null,"",0)
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.utadApi.deleteMedicament(medicamentIdToDelete)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "deleteMedicament")
            result.code  = response.code()
            result
        }
    }
}