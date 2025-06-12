package com.cronosdev.taskmanagerapp.ui.screens.health.medicalAppointment


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseListModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseResultListModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseResultListModel.Companion.filterResultList
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseResultListModel.Companion.forEach
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.DatesModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.GenericApiMessageResponse
import com.cronosdev.taskmanagerapp.data.model.courrutines.CoroutineCaller
import com.cronosdev.taskmanagerapp.data.model.health.MedicalAppointmentModel
import com.cronosdev.taskmanagerapp.data.repositories.health.MedicalAppointmentRepository
import com.cronosdev.taskmanagerapp.ui.state.UiState
import com.cronosdev.taskmanagerapp.ui.navigation.Destinations
import com.cronosdev.taskmanagerapp.utils.dateTimeFormatterES
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONException
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.net.ssl.SSLHandshakeException

/**
 *
 */
@HiltViewModel
class MedicalAppointmentViewModel @Inject constructor(private val medicalAppointmentRepository: MedicalAppointmentRepository): ViewModel()  {
    private val _dayTimeFormaterEEUU = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dayTimeFormaterEEUU: DateTimeFormatter get() = _dayTimeFormaterEEUU
    // Formateador de fechas al castellano solo para la visualizacion de los datos.
    val dayTimeFormaterES = SimpleDateFormat("dd MMMM yyyy", Locale("es", "ES"))
    //
    private val _medicalAppointmentScreenUiState = MutableStateFlow<UiState?>(null)
    val medicalAppointmentScreenUiState: StateFlow<UiState?> = _medicalAppointmentScreenUiState
    //
    private val _updateMedicalAppointmentScreenUiState = MutableStateFlow<UiState?>(null)
    val updateMedicalAppointmentScreenUiState: StateFlow<UiState?> = _updateMedicalAppointmentScreenUiState
    //
    private val _createMedicalAppointmentScreenUiState = MutableStateFlow<UiState?>(null)
    val createMedicalAppointmentScreenUiState: StateFlow<UiState?> = _createMedicalAppointmentScreenUiState
    //
    private val _exceptionHandler = CoroutineExceptionHandler { context, throwable ->
        val screenCaller = context[CoroutineCaller]?.screenCallName ?: "Screen desconocida"
        val methodCaller = context[CoroutineCaller]?.methodCallName ?: "Metodo desconocido"
        val errorMessage = when (throwable) {
            is SocketTimeoutException -> "Tiempo de espera agotado"
            is UnknownHostException -> "No se pudo conectar al servidor. Verifica tu conexión"
            is ConnectException -> "No se pudo establecer la conexión con el servidor"
            is SSLHandshakeException -> "Error de certificado SSL"
            is IOException -> "Error de red: ${throwable.message}"
            is HttpException -> "Error del servidor: ${throwable.code()}"
            // --- Errores de serialización / parsing ---
            is JsonSyntaxException -> "Sintaxis incorrecta en JSON" // Gson
            is JsonParseException -> "Error al parsear la respuesta JSON" // Gson (com.google.gson)
            is JSONException -> "Error de estructura en JSON" // org.json (Android nativo)
            else -> "Error inesperado: ${throwable.localizedMessage ?: "Error desconocido. Peligro"} "
        }
        // Actualizamos el estado de la UI indicando que el estado es 'error' al haber ocurrido una excepcion.
        if (screenCaller == Destinations.HEALTH_MEDICAL_APPOINTMENT_SCREEN_URL)
            fetchMedicalAppointmentScreenUIState(UiState.Error("throwableErrorType", screenCaller, methodCaller, errorMessage))
        if (screenCaller == Destinations.HEALTH_CREATE_MEDICAL_APPOINTMENT_SCREEN_URL)
            fetchCreateMedicalAppointmentScreenUIState(UiState.Error("throwableErrorType", screenCaller, methodCaller, errorMessage))
        if (screenCaller == Destinations.HEALTH_EDIT_MEDICAL_APPOINTMENT_SCREEN_URL)
            fetchEditMedicalAppointmentScreenUIState(UiState.Error("throwableErrorType", screenCaller, methodCaller, errorMessage))
    }
    /** Funciones utilizadas para actualizar los estados de cada una de las UI que dependen de este viewModel.
     * @param state Nuevo estado de la UI.
     */
    private fun fetchMedicalAppointmentScreenUIState(state: UiState) { _medicalAppointmentScreenUiState.value = state }
    private fun fetchCreateMedicalAppointmentScreenUIState(state: UiState) { _createMedicalAppointmentScreenUiState.value = state }
    private fun fetchEditMedicalAppointmentScreenUIState(state: UiState) { _updateMedicalAppointmentScreenUiState.value = state }
    private var _responseMessage by mutableStateOf("")
    //// GETTERS & SETTERS
    private var _id by mutableStateOf("")
    val id: String get() = _id
    //
    private var _fechaCitaMedica by mutableStateOf("Ninguna fecha seleccionada")
    val fechaCitaMedica: String get() = _fechaCitaMedica
    fun setFechaCitaMedica(newDate: String) { _fechaCitaMedica = newDate }
    var fechaCitaMedicaES by mutableStateOf("")
    //
    private var _horaCitaMedica by mutableStateOf("Ninguna hora seleccionada")
    val horaCitaMedica: String get() = _horaCitaMedica
    fun setHoraCitaMedica(newDate: String) { _horaCitaMedica = newDate }
    //
    private var _servicioCitaMedica by mutableStateOf("")
    val servicioCitaMedica: String get() = _servicioCitaMedica
    fun setServicioCitaMedica(newService: String) { _servicioCitaMedica = newService }
    //
    private var _tipoPruebaCitaMedica by mutableStateOf("")
    val tipoPruebaCitaMedica: String get() = _tipoPruebaCitaMedica
    fun setTipoPruebaCitaMedica(newType: String) { _tipoPruebaCitaMedica = newType }
    //
    private var _lugarCitaMedica by mutableStateOf("")
    val lugarCitaMedica: String get() = _lugarCitaMedica
    fun setLugarCitaMedica(newLocation: String) { _lugarCitaMedica = newLocation }
    //
    private var _notasCitaMedica by mutableStateOf("")
    val notasCitaMedica: String get() = _notasCitaMedica
    fun setNotasCitaMedica(newNotes: String) { _notasCitaMedica = newNotes }
    // Set all values
    fun setMedicalAppointmentVariables() {_id = ""
        setFechaCitaMedica("Ninguna fecha seleccionada"); fechaCitaMedicaES = "";
        setHoraCitaMedica("Ninguna hora seleccionada"); setServicioCitaMedica("");
        setTipoPruebaCitaMedica(""); setLugarCitaMedica(""); setNotasCitaMedica("");
    }
    // Fetch Med-Appointment Data To Update On Screen
    fun fetchSelectedMedicalAppointmentData(appointmentId: String) {
        _availableMedicalAppointmentList.filterResultList { it._id == appointmentId }.forEach {
            _id = it._id; _fechaCitaMedica = it.fechaCitaMedica.substring(0, 10);
            _horaCitaMedica = it.fechaCitaMedica.substring(11, 19);
            fechaCitaMedicaES = dateTimeFormatterES(_fechaCitaMedica).toString();
            _servicioCitaMedica = it.servicioCitaMedica; _tipoPruebaCitaMedica = it.tipoPruebaCitaMedica;
            _lugarCitaMedica = it.lugarCitaMedica; _notasCitaMedica = it.notasCitaMedica;
        }
    }
    // Med-Appointment Request
    private fun createMedicalAppointmentRequest(): MedicalAppointmentModel {
        return MedicalAppointmentModel(_id = "", "${_fechaCitaMedica}T${_horaCitaMedica}", _servicioCitaMedica, _tipoPruebaCitaMedica,
            _lugarCitaMedica, _notasCitaMedica
        )
    }
    //// METHODS OF THE VIEW MODEL
    // GET ALL
    private var _availableMedicalAppointmentList by mutableStateOf(ApiResponseResultListModel<MedicalAppointmentModel>(data = null, message = "", code = 0))
    val availableMedicalAppointmentList: ApiResponseResultListModel<MedicalAppointmentModel> get() = _availableMedicalAppointmentList
    fun fetchAvailableMedicalAppointments(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchAvailableMedicalAppointments")) {
            fetchMedicalAppointmentScreenUIState(UiState.Loading)
            medicalAppointmentRepository.getAllMedicalAppointments().let { _availableMedicalAppointmentList = it }
            _responseMessage = _availableMedicalAppointmentList.message
            if (_availableMedicalAppointmentList.code != 200) fetchMedicalAppointmentScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "fetchAvailableMedicalAppointments", _responseMessage)
            )
            else {
                setMedicalAppointmentVariables()
                fetchMedicalAppointmentScreenUIState(
                    UiState.Success("screenInit", screenCallName, "fetchAvailableMedicalAppointments", _responseMessage)
                )
            }
        }
    }
    // GET UNAVAILABLE-DATES
    private var _unavailableMedicalAppointmentDatesList by mutableStateOf(ApiResponseListModel<DatesModel>(data = null, message = "", code = 0))
    val unavailableMedicalAppointmentDatesList: ApiResponseListModel<DatesModel> get() = _unavailableMedicalAppointmentDatesList
    fun fetchUnavailableDates(screenCallName: String = "Unknown Screen Caller") {
        if (screenCallName == "healthCreateMedicalAppointmentScreen") {
            viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchUnavailableDates")) {
                fetchCreateMedicalAppointmentScreenUIState(UiState.Loading)
                medicalAppointmentRepository.getUnavailableMedicalAppointmentsDates().let{ _unavailableMedicalAppointmentDatesList = it }
                _responseMessage = _unavailableMedicalAppointmentDatesList.message
                if (_unavailableMedicalAppointmentDatesList.code != 200) fetchCreateMedicalAppointmentScreenUIState(
                    UiState.Error("fetchDataErrorType", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
                else fetchCreateMedicalAppointmentScreenUIState(
                    UiState.Success("screenInit", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
            }
        }
        if (screenCallName == "healthEditMedicalAppointmentScreen") {
            viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchUnavailableDates")) {
                fetchEditMedicalAppointmentScreenUIState(UiState.Loading)
                medicalAppointmentRepository.getUnavailableMedicalAppointmentsDates().let{ _unavailableMedicalAppointmentDatesList = it }
                _responseMessage = _unavailableMedicalAppointmentDatesList.message
                if (_unavailableMedicalAppointmentDatesList.code != 200) fetchEditMedicalAppointmentScreenUIState(
                    UiState.Error("fetchDataErrorType", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
                else fetchEditMedicalAppointmentScreenUIState(
                    UiState.Success("screenInit", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
            }
        }
    }
    // POST
    private var _medicalAppointmentToCreateResponse by mutableStateOf(ApiResponseListModel<GenericApiMessageResponse>(data = null, message = "", code = 0))
    fun createMedicalAppointment(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller("createMedicalAppointmentScreen", "createMedicalAppointment")) {
            fetchCreateMedicalAppointmentScreenUIState(UiState.Loading)
            medicalAppointmentRepository.postMedicalAppointment(createMedicalAppointmentRequest()).let { _medicalAppointmentToCreateResponse = it }
            _responseMessage = _medicalAppointmentToCreateResponse.message
            if (_medicalAppointmentToCreateResponse.code != 201)
                fetchCreateMedicalAppointmentScreenUIState(
                    UiState.Error("fetchDataErrorType", screenCallName, "createMedicalAppointment", _responseMessage)
                )
            else {
                setMedicalAppointmentVariables()
                fetchCreateMedicalAppointmentScreenUIState(
                    UiState.Success("screenRunning", screenCallName, "createMedicalAppointment", _responseMessage)
                )
            }
        }
    }
    // PUT
    private var _medicalAppointmentToPutResponse by mutableStateOf(ApiResponseListModel<GenericApiMessageResponse>(data = null, message = "", code = 0))
    /** Actualiza una reserva existente y actualiza el estado de la UI.
     * Lanza una coroutine en el viewModelScope, asegurando la cancelación automática si el ViewModel es destruido evitando fugas de memoria o llamadas innecesarias.
     */
    fun putMedicalAppointment(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller("editMedicalAppointmentScreen", "putMedicalAppointment")) {
            fetchEditMedicalAppointmentScreenUIState(UiState.Loading)
            medicalAppointmentRepository.putMedicalAppointment(_id, createMedicalAppointmentRequest()).let { _medicalAppointmentToPutResponse = it }
            _responseMessage = _medicalAppointmentToPutResponse.message
            if (_medicalAppointmentToPutResponse.code != 200)
                fetchEditMedicalAppointmentScreenUIState(
                    UiState.Error("fetchDataErrorType", screenCallName, "putMedicalAppointment", _responseMessage)
                )
            else {
                setMedicalAppointmentVariables()
                fetchEditMedicalAppointmentScreenUIState(
                    UiState.Success("screenRunning", screenCallName, "putMedicalAppointment", _responseMessage)
                )
            }
        }
    }
    // DELETE
    private var _medicalAppointmentToDeleteResponse by mutableStateOf(ApiResponseListModel<GenericApiMessageResponse>(data = null, message = "", code = 0))
    /** Elimina una reserva existente y actualiza el estado de la UI.
     * Lanza una coroutine en el viewModelScope, asegurando la cancelación automática si el ViewModel es destruido evitando fugas de memoria o llamadas innecesarias.
     */
    fun deleteMedicalAppointment(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller("editMedicalAppointmentScreen", "deleteMedicalAppointment")) {
            fetchEditMedicalAppointmentScreenUIState(UiState.Loading)
            medicalAppointmentRepository.deleteMedicalAppointment(_id).let { _medicalAppointmentToDeleteResponse = it }
            _responseMessage = _medicalAppointmentToDeleteResponse.message
            if (_medicalAppointmentToDeleteResponse.code != 204)
                fetchEditMedicalAppointmentScreenUIState(
                    UiState.Error("fetchDataErrorType", screenCallName, "deleteMedicalAppointment", _responseMessage)
                )
            else {
                setMedicalAppointmentVariables()
                fetchEditMedicalAppointmentScreenUIState(
                    UiState.Success("screenRunning", screenCallName, "deleteMedicalAppointment", _responseMessage)
                )
            }
        }
    }
    //// MEDICAL APPOINTMENT SEARCHER
    private var _showSearchedMedicalAppointments by  mutableStateOf(false)
    val showSearchedMedicalAppointments: Boolean get() = _showSearchedMedicalAppointments
    fun setShowSearchedMedicalAppointments(value: Boolean) { _showSearchedMedicalAppointments = value }
    //
    private var _medicalAppointmentNameToSearch by mutableStateOf("")
    private var _matchMedicalAppointmentsList by mutableStateOf(ApiResponseResultListModel<MedicalAppointmentModel>(data = null, message = "", code = 0))
    val matchMedicalAppointmentsList: ApiResponseResultListModel<MedicalAppointmentModel> get() = _matchMedicalAppointmentsList
    fun fetchMedicalAppointmentToSearch(nameToSearch: String) {
        // Guardamos el nombre de la pelicula a buscar
        _medicalAppointmentNameToSearch = nameToSearch
        // Guardamos los objetos de tipo '' para mostrar las salas que coincidan con el nombre de la sala a buscar(_roomToSearch).
        _matchMedicalAppointmentsList = _availableMedicalAppointmentList.filterResultList { it.tipoPruebaCitaMedica.contains(_medicalAppointmentNameToSearch, ignoreCase = true) }
        // La lista solo se mostrara en caso de haber una sala que buscar que coincida con el patron indicado.
        _showSearchedMedicalAppointments = Regex("[a-zA-Z0-9áéíóúÁÉÍÓÚüÜñÑ -]+").containsMatchIn(_medicalAppointmentNameToSearch)
    }
}