package com.cronosdev.taskmanagerapp.ui.screens.meeting

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseListModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseResultListModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseResultListModel.Companion.filterResultList
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseResultListModel.Companion.forEach
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.DatesModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.GenericApiMessageResponse
import com.cronosdev.taskmanagerapp.data.model.courrutines.CoroutineCaller
import com.cronosdev.taskmanagerapp.data.model.meeting.MeetingModel
import com.cronosdev.taskmanagerapp.data.repositories.meeting.MeetingRepository
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
import javax.inject.Inject
import javax.net.ssl.SSLHandshakeException

//
/**
 *
 */
@HiltViewModel
class MeetingViewModel @Inject constructor(private val meetingRepository: MeetingRepository): ViewModel()  {
    private val _dayTimeFormaterEEUU = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dayTimeFormaterEEUU: DateTimeFormatter get() = _dayTimeFormaterEEUU
    // Formateador de fechas al castellano solo para la visualizacion de los datos.
    val dayTimeFormaterES = SimpleDateFormat("dd MMMM yyyy", Locale("es", "ES"))
    //
    private val _meetingScreenUiState = MutableStateFlow<UiState?>(null)
    val meetingScreenUiState: StateFlow<UiState?> = _meetingScreenUiState
    //
    private val _createMeetingScreenUiState = MutableStateFlow<UiState?>(null)
    val createMeetingScreenUiState: StateFlow<UiState?> = _createMeetingScreenUiState
    //
    private val _editMeetingScreenUiState = MutableStateFlow<UiState?>(null)
    val editMeetingScreenUiState: StateFlow<UiState?> = _editMeetingScreenUiState
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
        if (screenCaller == Destinations.MEETING_SCREEN_URL) fetchMeetingScreenUIState(UiState.Error("throwableErrorType", screenCaller, methodCaller, errorMessage))
        if (screenCaller == Destinations.CREATE_MEETING_SCREEN_URL) fetchCreateMeetingScreenUIState(
            UiState.Error("throwableErrorType", screenCaller, methodCaller, errorMessage))
        if (screenCaller == Destinations.EDIT_MEETING_SCREEN_URL) fetchEditMeetingScreenUIState(
            UiState.Error("throwableErrorType", screenCaller, methodCaller, errorMessage))
    }
    /** Funciones utilizadas para actualizar los estados de cada una de las UI que dependen de este viewModel.
     * @param state Nuevo estado de la UI.
     */
    private fun fetchMeetingScreenUIState(state: UiState) { _meetingScreenUiState.value = state }
    private fun fetchCreateMeetingScreenUIState(state: UiState) { _createMeetingScreenUiState.value = state }
    private fun fetchEditMeetingScreenUIState(state: UiState) { _editMeetingScreenUiState.value = state }
    private var _responseMessage by mutableStateOf("")
    //// GETTERS & SETTERS
    private var _id by mutableStateOf("")
    val id: String get() = _id
    //
    private var _tituloReunion by mutableStateOf("")
    val tituloReunion: String get() = _tituloReunion
    fun setTituloReunion(newMeetingName: String) { _tituloReunion = newMeetingName }
    //
    private var _tipoReunion by mutableStateOf("")
    val tipoReunion: String get() = _tipoReunion
    fun setTipoReunion(newMeetingtype: String) { _tipoReunion = newMeetingtype }
    //
    private var _asistentesReunion by mutableStateOf(listOf<String>())
    val asistentesReunion: List<String> get() = _asistentesReunion
    fun setAsistentesReunion(newData: List<String>) { _asistentesReunion = newData }
    //
    private var _fechaInicioReunion by mutableStateOf("Ninguna fecha seleccionada")
    val fechaInicioReunion: String get() = _fechaInicioReunion
    fun setFechaInicioReunion(newDate: String) { _fechaInicioReunion = newDate }
    var fechaInicioReunionES by mutableStateOf("")
    //
    private var _horaInicioReunion by mutableStateOf("Ninguna hora seleccionada")
    val horaInicioReunion: String get() = _horaInicioReunion
    fun setHoraInicioReunion(newDate: String) { _horaInicioReunion = newDate }
    //
    private var _fechaFinReunion by mutableStateOf("Ninguna fecha seleccionada")
    val fechaFinReunion: String get() = _fechaFinReunion
    fun setFechaFinReunion(newDate: String) { _fechaFinReunion = newDate }
    var fechaFinReunionES by mutableStateOf("")
    //
    private var _horaFinReunion by mutableStateOf("Ninguna hora seleccionada")
    val horaFinReunion: String get() = _horaFinReunion
    fun setHoraFinReunion(newDate: String) { _horaFinReunion = newDate }
    //
    private var _lugarReunion by mutableStateOf("")
    val lugarReunion: String get() = _lugarReunion
    fun setLugarReunion(newLocation: String) { _lugarReunion = newLocation }
    //
    private var _organizadorReunion by mutableStateOf("")
    val organizadorReunion: String get() = _organizadorReunion
    fun setOrganizadorReunion(newOrganizator: String) { _organizadorReunion = newOrganizator }
    //
    private var _notasReunion by mutableStateOf("")
    val notasReunion: String get() = _notasReunion
    fun setNotasReunion(newNotes: String) { _notasReunion = newNotes }
    //
    private fun _setMeetingVariables() {_id = "";
        setTituloReunion(""); setTipoReunion(""); setAsistentesReunion(emptyList());
        setFechaInicioReunion("Ninguna fecha seleccionada"); fechaInicioReunionES = "";
        setHoraInicioReunion("Ninguna hora seleccionada"); setFechaFinReunion("Ninguna fecha seleccionada");
        fechaFinReunionES = ""; setHoraFinReunion("Ninguna hora seleccionada");
        setLugarReunion(""); setOrganizadorReunion(""); setNotasReunion("");
    }
    /** Funcion encargada de obtener los datos de la reserva seleecionada para editar los datos de la misma o para eliminarla en el componente/vista 'editCinemaScreen'.
     * @param meetingId ID de la
     */
    fun fetchSelectedMeetingData(meetingId: String) {
        _availableMeetingList.filterResultList { it._id == meetingId }.forEach {
            _id = it._id; _tituloReunion = it.tituloReunion;
            _tipoReunion = it.tipoReunion; _asistentesReunion = it.asistentesReunion;
            _fechaInicioReunion = it.fechaInicioReunion.substring(0, 10);
            _horaInicioReunion = it.fechaInicioReunion.substring(11, 19)
            fechaInicioReunionES = dateTimeFormatterES(_fechaInicioReunion).toString();
            _fechaFinReunion = it.fechaFinReunion.substring(0, 10);
            _horaFinReunion = it.fechaFinReunion.substring(11, 19)
            fechaFinReunionES = dateTimeFormatterES(_fechaFinReunion).toString();
            _lugarReunion = it.lugarReunion; _organizadorReunion = it.organizadorReunion;
            _notasReunion = it.notasReunion;
        }
    }
    //
    private fun createMeetingRequest(): MeetingModel {
        return MeetingModel(_id = "", _tituloReunion, _tipoReunion, _organizadorReunion, _asistentesReunion,
            "${_fechaInicioReunion}T${_horaInicioReunion}",
            "${_fechaFinReunion}T${_horaFinReunion}", _lugarReunion, _notasReunion
        )
    }
    ////
    // GET ALL
    private var _availableMeetingList by mutableStateOf(ApiResponseResultListModel<MeetingModel>(data = null, message = "", code = 0))
    val availableMeetingList: ApiResponseResultListModel<MeetingModel> get() = _availableMeetingList
    fun fetchAvailableMeetings(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchAvailableMeetings")) {
            fetchMeetingScreenUIState(UiState.Loading)
            meetingRepository.getAllMeetings().let { _availableMeetingList = it }
            _responseMessage = _availableMeetingList.message
            if (_availableMeetingList.code != 200) fetchMeetingScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "fetchAvailableMeetings", _responseMessage)
            )
            else {
                _setMeetingVariables()
                fetchMeetingScreenUIState(
                    UiState.Success("screenInit", screenCallName, "fetchAvailableMeetings", _responseMessage)
                )
            }
        }
    }
    // GET UNAVAILABLE-DATES
    private var _unavailableMeetingDatesList by mutableStateOf(ApiResponseListModel<DatesModel>(data = null, message = "", code = 0))
    val unavailableMeetingDatesList: ApiResponseListModel<DatesModel> get() = _unavailableMeetingDatesList
    fun fetchUnavailableDates(screenCallName: String = "Unknown Screen Caller") {
        if (screenCallName == "createMeetingScreen") {
            viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchUnavailableDates")) {
                fetchCreateMeetingScreenUIState(UiState.Loading)
                meetingRepository.getUnavailableMeetingsDates().let{ _unavailableMeetingDatesList = it }
                _responseMessage = _unavailableMeetingDatesList.message
                if (_unavailableMeetingDatesList.code != 200) fetchCreateMeetingScreenUIState(
                    UiState.Error("fetchDataErrorType", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
                else fetchCreateMeetingScreenUIState(
                    UiState.Success("screenInit", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
            }
        }
        if (screenCallName == "editMeetingScreen") {
            viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchUnavailableDates")) {
                fetchEditMeetingScreenUIState(UiState.Loading)
                meetingRepository.getUnavailableMeetingsDates().let{ _unavailableMeetingDatesList = it }
                _responseMessage = _unavailableMeetingDatesList.message
                if (_unavailableMeetingDatesList.code != 200) fetchEditMeetingScreenUIState(
                    UiState.Error("fetchDataErrorType", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
                else fetchEditMeetingScreenUIState(
                    UiState.Success("screenInit", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
            }
        }
    }
    // POST
    private var _meetingToCreateResponse by mutableStateOf(ApiResponseListModel<GenericApiMessageResponse>(data = null, message = "", code = 0))
    fun createMeeting(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller("createMeetingScreen", "createMeeting")) {
            fetchCreateMeetingScreenUIState(UiState.Loading)
            meetingRepository.postMeeting(createMeetingRequest()).let { _meetingToCreateResponse = it }
            _responseMessage = _meetingToCreateResponse.message
            if (_meetingToCreateResponse.code != 201) fetchCreateMeetingScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "createMeeting", _responseMessage)
            )
            else {
                _setMeetingVariables()
                fetchCreateMeetingScreenUIState(
                    UiState.Success("screenRunning", screenCallName, "createMeeting", _responseMessage)
                )
            }
        }
    }
    // PUT
    private var _meetingToPutResponse by mutableStateOf(ApiResponseListModel<GenericApiMessageResponse>(data = null, message = "", code = 0))
    /** Actualiza una reserva existente y actualiza el estado de la UI.
     * Lanza una coroutine en el viewModelScope, asegurando la cancelación automática si el ViewModel es destruido evitando fugas de memoria o llamadas innecesarias.
     */
    fun putMeeting(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller("editMeetingScreen", "putMeeting")) {
            fetchEditMeetingScreenUIState(UiState.Loading)
            meetingRepository.putMeeting(_id, createMeetingRequest()).let { _meetingToPutResponse = it }
            _responseMessage = _meetingToPutResponse.message
            if (_meetingToPutResponse.code != 200) fetchEditMeetingScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "putMeeting", _responseMessage)
            )
            else {
                _setMeetingVariables()
                fetchEditMeetingScreenUIState(
                    UiState.Success("screenRunning", screenCallName, "putMeeting", _responseMessage)
                )
            }
        }
    }
    // DELETE
    private var _meetingToDeleteResponse by mutableStateOf(ApiResponseListModel<GenericApiMessageResponse>(data = null, message = "", code = 0))
    /** Elimina una reserva existente y actualiza el estado de la UI.
     * Lanza una coroutine en el viewModelScope, asegurando la cancelación automática si el ViewModel es destruido evitando fugas de memoria o llamadas innecesarias.
     */
    fun deleteMeeting(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller("editMeetingScreen", "deleteMeeting")) {
            fetchEditMeetingScreenUIState(UiState.Loading)
            meetingRepository.deleteMeeting(_id).let { _meetingToDeleteResponse = it }
            _responseMessage = _meetingToDeleteResponse.message
            if (_meetingToDeleteResponse.code != 204) fetchEditMeetingScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "deleteMeeting", _responseMessage)
            )
            else {
                _setMeetingVariables()
                fetchEditMeetingScreenUIState(
                    UiState.Success("screenRunning", screenCallName, "deleteMeeting", _responseMessage)
                )
            }
        }
    }
    //// MEETING SEARCHER
    private var _showSearchedMeetings by  mutableStateOf(false)
    val showSearchedMeetings: Boolean get() = _showSearchedMeetings
    private var _meetingNameToSearch by mutableStateOf("")
    private var _matchMeetingsList by mutableStateOf(ApiResponseResultListModel<MeetingModel>(data = null, message = "", code = 0))
    val matchMeetingsList: ApiResponseResultListModel<MeetingModel> get() = _matchMeetingsList
    /** Función que busca salas en base al nombre recibido por parametro.
     * @param nameToSearch Nombre de la sala recibido desde el buscador.
     */
    fun fetchPaintingToSearch(nameToSearch: String) {
        // Guardamos el nombre de la pelicula a buscar
        _meetingNameToSearch = nameToSearch
        // Guardamos los objetos de tipo '' para mostrar las salas que coincidan con el nombre de la sala a buscar(_roomToSearch).
        _matchMeetingsList = _availableMeetingList.filterResultList { it.tituloReunion.contains(_meetingNameToSearch, ignoreCase = true) }
        // La lista solo se mostrara en caso de haber una sala que buscar que coincida con el patron indicado.
        _showSearchedMeetings = Regex("[a-zA-Z0-9áéíóúÁÉÍÓÚüÜñÑ -]+").containsMatchIn(_meetingNameToSearch)
    }
}