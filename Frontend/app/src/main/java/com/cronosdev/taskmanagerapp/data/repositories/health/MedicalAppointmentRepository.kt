package com.cronosdev.taskmanagerapp.data.repositories.health

import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseListModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseResultListModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.DatesModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.GenericApiMessageResponse
import com.cronosdev.taskmanagerapp.data.model.health.MedicalAppointmentModel
import com.cronosdev.taskmanagerapp.data.remote.RetrofitInstance
import com.cronosdev.taskmanagerapp.utils.apiDataExtractors.apiMessageExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class MedicalAppointmentRepository @Inject constructor() {
    suspend fun getAllMedicalAppointments(): ApiResponseResultListModel<MedicalAppointmentModel> {
        var result= ApiResponseResultListModel<MedicalAppointmentModel>(null,"",0)
        return withContext(Dispatchers.IO) {
            // Obtenemos los datos que nos devuelve la API sobre el ENDPOINT indicado
            val response = RetrofitInstance.utadApi.getAllMedicalAppointments()
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "getAllMedicalAppointments")
            result.code  = response.code()
            result
        }
    }
    suspend fun getMedicalAppointmentById(medicalAppointmentIdToSearch: String): ApiResponseListModel<MedicalAppointmentModel> {
        var result= ApiResponseListModel<MedicalAppointmentModel>(null,"",0)
        return withContext(Dispatchers.IO) {
            // Obtenemos los datos que nos devuelve la API sobre el ENDPOINT indicado
            val response = RetrofitInstance.utadApi.getMedicalAppointmentById(medicalAppointmentIdToSearch)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "getMedicalAppointmentById")
            result.code  = response.code()
            result
        }
    }
    suspend fun getUnavailableMedicalAppointmentsDates(): ApiResponseListModel<DatesModel> {
        var result= ApiResponseListModel<DatesModel>(null,"",0)
        return withContext(Dispatchers.IO) {
            // Obtenemos los datos que nos devuelve la API sobre el ENDPOINT indicado
            val response = RetrofitInstance.utadApi.getUnavailableMedicalAppointmentDates()
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "getUnavailableMedicalAppointmentsDates")
            result.code  = response.code()
            result
        }
    }
    suspend fun postMedicalAppointment(medicalAppointmentToCreate: MedicalAppointmentModel): ApiResponseListModel<GenericApiMessageResponse> {
        var result = ApiResponseListModel<GenericApiMessageResponse>(null,"",0)
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.utadApi.postMedicalAppointment(medicalAppointmentToCreate)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "postMedicalAppointment")
            result.code  = response.code()
            result
        }
    }
    suspend fun putMedicalAppointment(medicalAppointmentIdToPut: String, medicalAppointmentToPut: MedicalAppointmentModel): ApiResponseListModel<GenericApiMessageResponse> {
        var result= ApiResponseListModel<GenericApiMessageResponse>(null,"",0)
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.utadApi.putMedicalAppointment(medicalAppointmentIdToPut, medicalAppointmentToPut)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "putMedicalAppointment")
            result.code  = response.code()
            result
        }
    }
    suspend fun deleteMedicalAppointment(medicalAppointmentIdToDelete: String): ApiResponseListModel<GenericApiMessageResponse> {
        var result = ApiResponseListModel<GenericApiMessageResponse>(null,"",0)
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.utadApi.deleteMedicalAppointment(medicalAppointmentIdToDelete)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "deleteMedicalAppointment")
            result.code  = response.code()
            result
        }
    }
}