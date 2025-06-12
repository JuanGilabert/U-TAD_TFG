package com.cronosdev.taskmanagerapp.data.remote
//
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.GenericApiMessageResponse
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.DatesModel
import com.cronosdev.taskmanagerapp.data.model.art.CinemaModel
import com.cronosdev.taskmanagerapp.data.model.art.MusicModel
import com.cronosdev.taskmanagerapp.data.model.art.PaintingModel
import com.cronosdev.taskmanagerapp.data.model.auth.SigninRequestModel
import com.cronosdev.taskmanagerapp.data.model.auth.SignupRequestModel
import com.cronosdev.taskmanagerapp.data.model.auth.SigninResponseModel
import com.cronosdev.taskmanagerapp.data.model.auth.SignoutRequestModel
import com.cronosdev.taskmanagerapp.data.model.food.FoodModel
import com.cronosdev.taskmanagerapp.data.model.health.MedicalAppointmentModel
import com.cronosdev.taskmanagerapp.data.model.health.MedicamentModel
import com.cronosdev.taskmanagerapp.data.model.meeting.MeetingModel
import com.cronosdev.taskmanagerapp.data.model.sport.SportModel
import com.cronosdev.taskmanagerapp.data.model.travel.TravelModel
import com.cronosdev.taskmanagerapp.data.model.work.WorkModel
import retrofit2.Response
import retrofit2.http.GET;
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
/** Interfaz utilizada para la comunicacion con la api para asi obtener los datos de sus endpoints.
 *
 * @author Juan Gilabert Lopez
 * @see SignupRequestModel
 * @since 1.0
 */
interface TaskManagerAPIService {
    /**
     *
     */
    @POST("auth/signup")
    suspend fun signup (@Body signUpRequestModel: SignupRequestModel): Response<GenericApiMessageResponse>
    /** Inicia sesión en el sistema.
     * @param loginRequest Modelo que contiene las credenciales de acceso (email y password).
     * @return Una llamada que devuelve un modelo de respuesta con el token de acceso.
     */
    @POST("auth/signin")
    suspend fun signin(@Body loginRequest: SigninRequestModel): Response<SigninResponseModel>
    /** Cierra la sesión del usuario.
     * @return Una llamada que devuelve el modelo de respuesta de cierre de sesión.
     */
    @POST("auth/signout")
    suspend fun signout(@Body signoutRequest: SignoutRequestModel): Response<GenericApiMessageResponse>
    // CINEMA
    @GET("art/cinema")
    suspend fun getAllCinemas(): Response<List<CinemaModel>>
    //
    @GET("art/cinema/{id}")
    suspend fun getCinemaById(@Path("id") cinemaId: String?): Response<CinemaModel>
    //
    @GET("art/cinema/unavailable-dates")
    suspend fun getUnavailableCinemaDates(): Response<DatesModel>
    //
    @GET("art/cinema/unavailable-dates")
    suspend fun getAvailableCinemaDatesOnDayByDate(@Query("fechaInicioPelicula") startDate: String): Response<DatesModel>
    //
    @POST("art/cinema")
    suspend fun postCinema(@Body cinemaToCreate: CinemaModel): Response<GenericApiMessageResponse>
    //
    @PUT("art/cinema/{id}")
    suspend fun putCinema(@Path("id") cinemaId: String?, @Body cinemaModel: CinemaModel): Response<GenericApiMessageResponse>
    //
    @DELETE("art/cinema/{id}")
    suspend fun deleteCinema(@Path("id") cinemaId: String?): Response<GenericApiMessageResponse>
    // MUSIC
    @GET("art/music")
    suspend fun getAllMusics(): Response<List<MusicModel>>
    //
    @GET("art/music/{id}")
    suspend fun getMusicById(@Path("id") musicIdToSearch: String?): Response<MusicModel>
    //
    @GET("art/music/unavailable-dates")
    suspend fun getUnavailableMusicDates(): Response<DatesModel>
    //
    @POST("art/music")
    suspend fun postMusic(@Body musicModel: MusicModel): Response<GenericApiMessageResponse>
    //
    @PUT("art/music/{id}")
    suspend fun putMusic(@Path("id") musicId: String?, @Body musicToPut: MusicModel): Response<GenericApiMessageResponse>
    //
    @DELETE("art/music/{id}")
    suspend fun deleteMusic(@Path("id") cinemaId: String?): Response<GenericApiMessageResponse>
    //
    @GET("art/painting")
    suspend fun getAllPaintings(): Response<List<PaintingModel>>
    //
    @GET("art/painting/{id}")
    suspend fun getPaintingById(@Path("id") paintingIdToSearch: String?): Response<PaintingModel>
    //
    @GET("art/painting/unavailable-dates")
    suspend fun getUnavailablePaintingDates(): Response<DatesModel>
    //
    @POST("art/painting")
    suspend fun postPainting(@Body paintingToCreate: PaintingModel): Response<GenericApiMessageResponse>
    //
    @PUT("art/painting/{id}")
    suspend fun putPainting(@Path("id") paintingIdToPut: String?, @Body paintingToPut: PaintingModel): Response<GenericApiMessageResponse>
    //
    @DELETE("art/painting/{id}")
    suspend fun deletePainting(@Path("id") paintingIdToDelete: String?): Response<GenericApiMessageResponse>
    //
    @GET("food")
    suspend fun getAllFoods(): Response<List<FoodModel>>
    //
    @GET("food/{id}")
    suspend fun getFoodById(@Path("id") foodIdToSearch: String?): Response<FoodModel>
    //
    @GET("food/unavailable-dates")
    suspend fun getUnavailableFoodDates(): Response<DatesModel>
    //
    @POST("food")
    suspend fun postFood(@Body foodToCreate: FoodModel): Response<GenericApiMessageResponse>
    //
    @PUT("food/{id}")
    suspend fun putFood(@Path("id") foodIdToPut: String?, @Body foodToPut: FoodModel): Response<GenericApiMessageResponse>
    //
    @DELETE("food/{id}")
    suspend fun deleteFood(@Path("id") foodIdToDelete: String?): Response<GenericApiMessageResponse>
    //
    @GET("health/medical-appointment")
    suspend fun getAllMedicalAppointments(): Response<List<MedicalAppointmentModel>>
    //
    @GET("health/medical-appointment/{id}")
    suspend fun getMedicalAppointmentById(@Path("id") medicalAppointmentIdToSearch: String?): Response<MedicalAppointmentModel>
    //
    @GET("health/medical-appointment/unavailable-dates")
    suspend fun getUnavailableMedicalAppointmentDates(): Response<DatesModel>
    //
    @POST("health/medical-appointment")
    suspend fun postMedicalAppointment(@Body medicalAppointmentToCreate: MedicalAppointmentModel): Response<GenericApiMessageResponse>
    //
    @PUT("health/medical-appointment/{id}")
    suspend fun putMedicalAppointment(@Path("id") medicalAppointmentIdToPut: String?, @Body medicalAppointmentToPut: MedicalAppointmentModel): Response<GenericApiMessageResponse>
    //
    @DELETE("health/medical-appointment/{id}")
    suspend fun deleteMedicalAppointment(@Path("id") medicalAppointmentIdToDelete: String?): Response<GenericApiMessageResponse>
    //
    @GET("health/medicament")
    suspend fun getAllMedicaments(): Response<List<MedicamentModel>>
    //
    @GET("health/medicament/{id}")
    suspend fun getMedicamentById(@Path("id") medicamentIdToSearch: String): Response<MedicamentModel>
    //
    @GET("health/medicament/expiration-dates/")
    suspend fun getUnavailableMedicamentDates(): Response<DatesModel>
    //
    @GET("health/medicament/expiration-dates/")
    suspend fun getMedicamentExpirationDatesByDate(@Query("fechaCaducidadMedicamento") fechaCaducidadMedicamento: String): Response<DatesModel>
    //
    @POST("health/medicament")
    suspend fun postMedicament(@Body medicamentToCreate: MedicamentModel): Response<GenericApiMessageResponse>
    //
    @PUT("health/medicament/{id}")
    suspend fun putMedicament(@Path("id") medicamentIdToPut: String?, @Body medicamentToPut: MedicamentModel): Response<GenericApiMessageResponse>
    //
    @DELETE("health/medicament/{id}")
    suspend fun deleteMedicament(@Path("id") medicamentIdToDelete: String?): Response<GenericApiMessageResponse>
    //
    @GET("meeting")
    suspend fun getAllMeetings(): Response<List<MeetingModel>>
    //
    @GET("meeting/{id}")
    suspend fun getMeetingById(@Path("id") meetingIdToSearch: String?): Response<MeetingModel>
    //
    @GET("meeting/unavailable-dates")
    suspend fun getUnavailableMeetingDates(): Response<DatesModel>
    //
    @POST("meeting")
    suspend fun postMeeting(@Body meetingToCreate: MeetingModel): Response<GenericApiMessageResponse>
    //
    @PUT("meeting/{id}")
    suspend fun putMeeting(@Path("id") meetingIdToPut: String?, @Body meetingToPut: MeetingModel): Response<GenericApiMessageResponse>
    //
    @DELETE("meeting/{id}")
    suspend fun deleteMeeting(@Path("id") meetingIdToDelete: String?): Response<GenericApiMessageResponse>
    //
    @GET("sport")
    suspend fun getAllSports(): Response<List<SportModel>>
    //
    @GET("sport/{id}")
    suspend fun getSportById(@Path("id") sportIdToSearch: String?): Response<SportModel>
    //
    @GET("sport/unavailable-dates")
    suspend fun getUnavailableSportDates(): Response<DatesModel>
    //
    @POST("sport")
    suspend fun postSport(@Body sportToCreate: SportModel): Response<GenericApiMessageResponse>
    //
    @PUT("sport/{id}")
    suspend fun putSport(@Path("id") sportIdToPut: String?, @Body sportToPut: SportModel): Response<GenericApiMessageResponse>
    //
    @DELETE("sport/{id}")
    suspend fun deleteSport(@Path("id") sportIdToDelete: String?): Response<GenericApiMessageResponse>
    //
    @GET("travel/")
    suspend fun getAllTravels(): Response<List<TravelModel>>
    //
    @GET("travel/{id}")
    suspend fun getTravelById(@Path("id") travelIdToSearch: String?): Response<TravelModel>
    //
    @GET("travel/unavailable-dates")
    suspend fun getUnavailableTravelDates(): Response<DatesModel>
    //
    @POST("travel")
    suspend fun postTravel(@Body travelToCreate: TravelModel): Response<GenericApiMessageResponse>
    //
    @PUT("travel/{id}")
    suspend fun putTravel(@Path("id") travelIdToPut: String?, @Body travelToPut: TravelModel): Response<GenericApiMessageResponse>
    //
    @DELETE("travel/{id}")
    suspend fun deleteTravel(@Path("id") travelIdToDelete: String?): Response<GenericApiMessageResponse>
    //
    @GET("work")
    suspend fun getAllWorks(): Response<List<WorkModel>>
    //
    @GET("work/{id}")
    suspend fun getWorkById(@Path("id") workIdToSearch: String?): Response<WorkModel>
    //
    @GET("work/unavailable-dates")
    suspend fun getUnavailableWorkDates(): Response<DatesModel>
    //
    @POST("work")
    suspend fun postWork(@Body workToCreate: WorkModel): Response<GenericApiMessageResponse>
    //
    @PUT("work/{id}")
    suspend fun putWork(@Path("id") workIdToPut: String?, @Body workToPut: WorkModel): Response<GenericApiMessageResponse>
    //
    @DELETE("work/{id}")
    suspend fun deleteWork(@Path("id") workIdToDelete: String?): Response<GenericApiMessageResponse>
}