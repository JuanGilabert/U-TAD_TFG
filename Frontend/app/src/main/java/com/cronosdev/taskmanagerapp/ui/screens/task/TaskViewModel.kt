package com.cronosdev.taskmanagerapp.ui.screens.task
//
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseResultListModel
import com.cronosdev.taskmanagerapp.data.model.art.CinemaModel
import com.cronosdev.taskmanagerapp.data.model.art.MusicModel
import com.cronosdev.taskmanagerapp.data.model.art.PaintingModel
import com.cronosdev.taskmanagerapp.data.model.courrutines.CoroutineCaller
import com.cronosdev.taskmanagerapp.data.model.food.FoodModel
import com.cronosdev.taskmanagerapp.data.model.health.MedicalAppointmentModel
import com.cronosdev.taskmanagerapp.data.model.health.MedicamentModel
import com.cronosdev.taskmanagerapp.data.model.meeting.MeetingModel
import com.cronosdev.taskmanagerapp.data.model.sport.SportModel
import com.cronosdev.taskmanagerapp.data.model.travel.TravelModel
import com.cronosdev.taskmanagerapp.data.model.work.WorkModel
import com.cronosdev.taskmanagerapp.data.repositories.art.CinemaRepository
import com.cronosdev.taskmanagerapp.data.repositories.art.MusicRepository
import com.cronosdev.taskmanagerapp.data.repositories.art.PaintingRepository
import com.cronosdev.taskmanagerapp.data.repositories.food.FoodRepository
import com.cronosdev.taskmanagerapp.data.repositories.health.MedicalAppointmentRepository
import com.cronosdev.taskmanagerapp.data.repositories.health.MedicamentRepository
import com.cronosdev.taskmanagerapp.data.repositories.meeting.MeetingRepository
import com.cronosdev.taskmanagerapp.data.repositories.sport.SportRepository
import com.cronosdev.taskmanagerapp.data.repositories.travel.TravelRepository
import com.cronosdev.taskmanagerapp.data.repositories.work.WorkRepository
import com.cronosdev.taskmanagerapp.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
/**
 *
 */
@HiltViewModel
class TaskViewModel @Inject constructor(
    private val cinemaRepository: CinemaRepository, private val musicRepository: MusicRepository,
    private val paintingRepository: PaintingRepository, private val medicamentRepository: MedicamentRepository,
    private val medicalAppointmentRepository: MedicalAppointmentRepository, private val foodRepository: FoodRepository,
    private val meetingRepository: MeetingRepository, private val travelRepository: TravelRepository,
    private val sportRepository: SportRepository, private val workRepository: WorkRepository
): ViewModel()  {
    private val _dayTimeFormaterEEUU = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dayTimeFormaterEEUU: DateTimeFormatter get() = _dayTimeFormaterEEUU
    // Formateador de fechas al castellano solo para la visualizacion de los datos.
    val dayTimeOutputFormaterES: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("es", "ES"))
    //
    private val _allTaskScreenUiStateCinema = MutableStateFlow<UiState?>(null)
    val allTaskScreenUiStateCinema: StateFlow<UiState?> = _allTaskScreenUiStateCinema
    //
    private val _exceptionHandler = CoroutineExceptionHandler { context, throwable ->
        val screenCaller = context[CoroutineCaller]?.screenCallName ?: "Screen desconocida"
        val methodCaller = context[CoroutineCaller]?.methodCallName ?: "Metodo desconocido"
        val errorMessage = when (throwable) {
            is SocketTimeoutException -> "Tiempo de espera agotado"
            is IOException -> "Error de red: ${throwable.message}"
            is HttpException -> "Error del servidor: ${throwable.code()}"
            else -> "Error inesperado: ${throwable.localizedMessage}"
        }
        // Actualizamos el estado de la UI indicando que el estado es 'error' al haber ocurrido una excepcion.
        fetchAllTaskScreenUIState(UiState.Error(errorMessage, methodCaller))
    }
    /** Funciones utilizadas para actualizar los estados de cada una de las UI que dependen de este viewModel.
     * @param state Nuevo estado de la UI.
     */
    private fun fetchAllTaskScreenUIState(state: UiState) { _allTaskScreenUiStateCinema.value = state }
    //// GET ALL CINEMA TASKS
    private var _availableCinemasList by mutableStateOf(ApiResponseResultListModel<CinemaModel>(data = null, message = "", code = 0))
    val availableCinemasList: ApiResponseResultListModel<CinemaModel> get() = _availableCinemasList
    fun fetchAvailableCinemaTasks(screenCallName: String) {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchAvailableCinemaTasks")) {
            fetchAllTaskScreenUIState(UiState.Loading)
            cinemaRepository.getAllCinemas().let { _availableCinemasList = it }
            if (_availableCinemasList.code != 200) fetchAllTaskScreenUIState(UiState.Error(_availableCinemasList.message, "fetchAvailableCinemaTasks"))
            else fetchAllTaskScreenUIState(UiState.Success("screenInit", _availableCinemasList.message, "fetchAvailableCinemaTasks"))
        }
    }
    // GET ALL MUSIC TASKS
    private var _availableMusicsList by mutableStateOf(ApiResponseResultListModel<MusicModel>(data = null, message = "", code = 0))
    val availableMusicsList: ApiResponseResultListModel<MusicModel> get() = _availableMusicsList
    fun fetchAvailableMusicTasks(screenCallName: String) {
        fetchAllTaskScreenUIState(UiState.Loading)
        viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchAvailableMusicTasks")) {
            musicRepository.getAllMusics().let { _availableMusicsList = it }
        }
        if (_availableMusicsList.code != 200) fetchAllTaskScreenUIState(UiState.Error(_availableMusicsList.message, "fetchAvailableMusicTasks"))
        else fetchAllTaskScreenUIState(UiState.Success("screenInit", _availableMusicsList.message, "fetchAvailableMusicTasks"))
    }
    // GET ALL PAINTING TASKS
    private var _availablePaintingsList by mutableStateOf(ApiResponseResultListModel<PaintingModel>(data = null, message = "", code = 0))
    val availablePaintingsList: ApiResponseResultListModel<PaintingModel> get() = _availablePaintingsList
    fun fetchAvailablePaintingTasks(screenCallName: String) {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchAvailablePaintingTasks")) {
            fetchAllTaskScreenUIState(UiState.Loading)
            paintingRepository.getAllPaintings().let { _availablePaintingsList = it }
            if (_availablePaintingsList.code != 200) fetchAllTaskScreenUIState(UiState.Error(_availablePaintingsList.message, "fetchAvailablePaintingTasks"))
            else fetchAllTaskScreenUIState(UiState.Success("screenInit", _availablePaintingsList.message, "fetchAvailablePaintingTasks"))
        }
    }
    // GET ALL MEDICAMENT TASKS
    private var _availableMedicamentsList by mutableStateOf(ApiResponseResultListModel<MedicamentModel>(data = null, message = "", code = 0))
    val availableMedicamentsList: ApiResponseResultListModel<MedicamentModel> get() = _availableMedicamentsList
    fun fetchAvailableMedicamentTasks(screenCallName: String) {
        fetchAllTaskScreenUIState(UiState.Loading)
        viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchAvailableMedicamentTasks")) {
            medicamentRepository.getAllMedicaments().let { _availableMedicamentsList = it }
        }
        if (_availableMedicamentsList.code != 200)
            fetchAllTaskScreenUIState(UiState.Error(_availableMedicamentsList.message, "fetchAvailableMedicamentTasks"))
        else fetchAllTaskScreenUIState(UiState.Success("screenInit", _availableMedicamentsList.message, "fetchAvailableMedicamentTasks"))
    }
    // GET ALL MEDICAL APPOINTMENT TASKS
    private var _availableMedicalAppointmentsList by mutableStateOf(ApiResponseResultListModel<MedicalAppointmentModel>(data = null, message = "", code = 0))
    val availableMedicalAppointmentsList: ApiResponseResultListModel<MedicalAppointmentModel> get() = _availableMedicalAppointmentsList
    fun fetchAvailableMedicalAppointmentTasks(screenCallName: String) {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchAvailableMedicalAppointmentTasks")) {
            fetchAllTaskScreenUIState(UiState.Loading)
            medicalAppointmentRepository.getAllMedicalAppointments().let { _availableMedicalAppointmentsList = it }
            if (_availableMedicalAppointmentsList.code != 200)
                fetchAllTaskScreenUIState(UiState.Error(_availableMedicalAppointmentsList.message, "fetchAvailableMedicalAppointmentTasks"))
            else fetchAllTaskScreenUIState(UiState.Success("screenInit", _availableMedicalAppointmentsList.message, "fetchAvailableMedicalAppointmentTasks"))
        }
    }
    // GET ALL FOOD TASKS
    private var _availableFoodsList by mutableStateOf(ApiResponseResultListModel<FoodModel>(data = null, message = "", code = 0))
    val availableFoodsList: ApiResponseResultListModel<FoodModel> get() = _availableFoodsList
    fun fetchAvailableFoodTasks(screenCallName: String) {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchAvailableFoodTasks")) {
            fetchAllTaskScreenUIState(UiState.Loading)
            foodRepository.getAllFoods().let { _availableFoodsList = it }
            if (_availableFoodsList.code != 200) fetchAllTaskScreenUIState(UiState.Error(_availableFoodsList.message, "fetchAvailableFoodTasks"))
            else fetchAllTaskScreenUIState(UiState.Success("screenInit", _availableFoodsList.message, "fetchAvailableFoodTasks"))
        }
    }
    // GET ALL MEETING TASKS
    private var _availableMeetingsList by mutableStateOf(ApiResponseResultListModel<MeetingModel>(data = null, message = "", code = 0))
    val availableMeetingsList: ApiResponseResultListModel<MeetingModel> get() = _availableMeetingsList
    /** Funcion que carga en la variable '_availableCinemasList' los datos, es decir ¿¿?? disponibles y actualiza el estado de la vista a traves de la variable '_uiState'.
     * Lanza una coroutine en el viewModelScope, asegurando la cancelación automática si el ViewModel es destruido evitando fugas de memoria o llamadas innecesarias.
     */
    fun fetchAvailableMeetingTasks(screenCallName: String) {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchAvailableMeetingTasks")) {
            fetchAllTaskScreenUIState(UiState.Loading)
            meetingRepository.getAllMeetings().let { _availableMeetingsList = it }
            if (_availableMeetingsList.code != 200) fetchAllTaskScreenUIState(UiState.Error(_availableMeetingsList.message, "fetchAvailableMeetingTasks"))
            else fetchAllTaskScreenUIState(UiState.Success("screenInit", _availableMeetingsList.message, "fetchAvailableMeetingTasks"))
        }
    }
    // GET ALL TRAVEL TASKS
    private var _availableTravelsList by mutableStateOf(ApiResponseResultListModel<TravelModel>(data = null, message = "", code = 0))
    val availableTravelsList: ApiResponseResultListModel<TravelModel> get() = _availableTravelsList
    fun fetchAvailableTravelTasks(screenCallName: String) {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchAvailableTravelTasks")) {
            fetchAllTaskScreenUIState(UiState.Loading)
            travelRepository.getAllTravels().let { _availableTravelsList = it }
            if (_availableTravelsList.code != 200) fetchAllTaskScreenUIState(UiState.Error(_availableTravelsList.message, "fetchAvailableTravelTasks"))
            else fetchAllTaskScreenUIState(UiState.Success("screenInit", _availableTravelsList.message, "fetchAvailableTravelTasks"))
        }
    }
    // GET ALL SPORT TASKS
    private var _availableSportsList by mutableStateOf(ApiResponseResultListModel<SportModel>(data = null, message = "", code = 0))
    val availableSportsList: ApiResponseResultListModel<SportModel> get() = _availableSportsList
    fun fetchAvailableSportTasks(screenCallName: String) {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchAvailableSportTasks")) {
            fetchAllTaskScreenUIState(UiState.Loading)
            sportRepository.getAllSports().let { _availableSportsList = it }
            if (_availableSportsList.code != 200) fetchAllTaskScreenUIState(UiState.Error(_availableSportsList.message, "fetchAvailableSportTasks"))
            else fetchAllTaskScreenUIState(UiState.Success("screenInit", _availableSportsList.message, "fetchAvailableSportTasks"))
        }
    }
    // GET ALL WORK TASKS
    private var _availableWorksList by mutableStateOf(ApiResponseResultListModel<WorkModel>(data = null, message = "", code = 0))
    val availableWorksList: ApiResponseResultListModel<WorkModel> get() = _availableWorksList
    fun fetchAvailableWorkTasks(screenCallName: String) {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchAvailableWorkTasks")) {
            fetchAllTaskScreenUIState(UiState.Loading)
            workRepository.getAllWorks().let { _availableWorksList = it }
            if (_availableWorksList.code != 200) fetchAllTaskScreenUIState(UiState.Error(_availableWorksList.message, "fetchAvailableWorkTasks"))
            else fetchAllTaskScreenUIState(UiState.Success("screenInit", _availableWorksList.message, "fetchAvailableWorkTasks"))
        }
    }
}