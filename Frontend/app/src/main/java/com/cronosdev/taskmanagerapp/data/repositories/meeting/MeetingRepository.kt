package com.cronosdev.taskmanagerapp.data.repositories.meeting

import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseListModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseResultListModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.DatesModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.GenericApiMessageResponse
import com.cronosdev.taskmanagerapp.data.model.meeting.MeetingModel
import com.cronosdev.taskmanagerapp.data.remote.RetrofitInstance
import com.cronosdev.taskmanagerapp.utils.apiDataExtractors.apiMessageExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MeetingRepository @Inject constructor() {
    suspend fun getAllMeetings(): ApiResponseResultListModel<MeetingModel> {
        var result= ApiResponseResultListModel<MeetingModel>(null,"",0)
        return withContext(Dispatchers.IO) {
            // Obtenemos los datos que nos devuelve la API sobre el ENDPOINT indicado
            val response = RetrofitInstance.utadApi.getAllMeetings()
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "getAllMeetings")
            result.code  = response.code()
            result
        }
    }
    suspend fun getMeetingById(meetingIdToSearch: Int?): ApiResponseListModel<MeetingModel> {
        var result= ApiResponseListModel<MeetingModel>(null,"",0)
        return withContext(Dispatchers.IO) {
            // Obtenemos los datos que nos devuelve la API sobre el ENDPOINT indicado
            val response = RetrofitInstance.utadApi.getMeetingById(meetingIdToSearch.toString())
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "getMeetingById")
            result.code  = response.code()
            result
        }
    }
    suspend fun getUnavailableMeetingsDates(): ApiResponseListModel<DatesModel> {
        var result= ApiResponseListModel<DatesModel>(null,"",0)
        return withContext(Dispatchers.IO) {
            // Obtenemos los datos que nos devuelve la API sobre el ENDPOINT indicado
            val response = RetrofitInstance.utadApi.getUnavailableMeetingDates()
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "getUnavailableMeetingsDates")
            result.code  = response.code()
            result
        }
    }
    suspend fun postMeeting(meetingToCreate: MeetingModel): ApiResponseListModel<GenericApiMessageResponse> {
        var result = ApiResponseListModel<GenericApiMessageResponse>(null,"",0)
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.utadApi.postMeeting(meetingToCreate)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "postMeeting")
            result.code  = response.code()
            result
        }
    }
    suspend fun putMeeting(meetingIdToPut: String, meetingToPut: MeetingModel): ApiResponseListModel<GenericApiMessageResponse> {
        var result= ApiResponseListModel<GenericApiMessageResponse>(null,"",0)
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.utadApi.putMeeting(meetingIdToPut, meetingToPut)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "putMeeting")
            result.code  = response.code()
            result
        }
    }
    suspend fun deleteMeeting(meetingIdToDelete: String): ApiResponseListModel<GenericApiMessageResponse> {
        var result = ApiResponseListModel<GenericApiMessageResponse>(null,"",0)
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.utadApi.deleteMeeting(meetingIdToDelete.toString())
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiMessageExtractor(response, "deleteMeeting")
            result.code  = response.code()
            result
        }
    }
}