package com.cronosdev.taskmanagerapp.ui.screens.sport
//
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.cronosdev.taskmanagerapp.data.model.sport.SportModel
import com.cronosdev.taskmanagerapp.data.repositories.sport.SportRepository
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

/**
 *
 */
@HiltViewModel
class SportViewModel @Inject constructor(private val sportRepository: SportRepository): ViewModel()  {
    private val _dayTimeFormaterEEUU = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dayTimeFormaterEEUU: DateTimeFormatter get() = _dayTimeFormaterEEUU
    // Formateador de fechas al castellano solo para la visualizacion de los datos.
    val dayTimeFormaterES = SimpleDateFormat("dd MMMM yyyy", Locale("es", "ES"))
    //
    private val _sportScreenUiState = MutableStateFlow<UiState?>(null)
    val sportScreenUiState: StateFlow<UiState?> = _sportScreenUiState
    //
    private val _createSportScreenUiState = MutableStateFlow<UiState?>(null)
    val createSportScreenUiState: StateFlow<UiState?> = _createSportScreenUiState
    //
    private val _updateSportScreenUiState = MutableStateFlow<UiState?>(null)
    val updateSportScreenUiState: StateFlow<UiState?> = _updateSportScreenUiState
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
        if (screenCaller == Destinations.SPORT_SCREEN_URL) fetchSportScreenUIState(UiState.Error("throwableErrorType", screenCaller, methodCaller, errorMessage))
        if (screenCaller == Destinations.CREATE_SPORT_SCREEN_URL) fetchCreateSportScreenUIState(
            UiState.Error("throwableErrorType", screenCaller, methodCaller, errorMessage))
        if (screenCaller == Destinations.EDIT_SPORT_SCREEN_URL) fetchEditSportScreenUIState(UiState.Error("throwableErrorType", screenCaller, methodCaller, errorMessage))
    }
    /** Funciones utilizadas para actualizar los estados de cada una de las UI que dependen de este viewModel.
     * @param state Nuevo estado de la UI.
     */
    private fun fetchSportScreenUIState(state: UiState) { _sportScreenUiState.value = state }
    private fun fetchCreateSportScreenUIState(state: UiState) { _createSportScreenUiState.value = state }
    private fun fetchEditSportScreenUIState(state: UiState) { _updateSportScreenUiState.value = state }
    private var _responseMessage by mutableStateOf("")
    //// GETTERS & SETTERS
    private var _id by mutableStateOf("")
    val id: String get() = _id
    //
    private var _nombreActividad by mutableStateOf("")
    val nombreActividad: String get() = _nombreActividad
    fun setNombreActividad(newActivityName: String) { _nombreActividad = newActivityName }
    //
    private var _fechaInicioActividad by mutableStateOf("Ninguna fecha seleccionada")
    val fechaInicioActividad: String get() = _fechaInicioActividad
    fun setFechaInicioActividad(newDate: String) { _fechaInicioActividad = newDate }
    var fechaInicioActividadES by mutableStateOf("")
    //
    private var _horaInicioActividad by mutableStateOf("Ninguna hora seleccionada")
    val horaInicioActividad: String get() = _horaInicioActividad
    fun setHoraInicioActividad(newDate: String) { _horaInicioActividad = newDate }
    //
    private var _lugarActividad by mutableStateOf("")
    val lugarActividad: String get() = _lugarActividad
    fun setLugarActividad(newLocation: String) { _lugarActividad = newLocation }
    //
    private var _duracionActividadMinutos by mutableIntStateOf(1)
    val duracionActividadMinutos: Int get() = _duracionActividadMinutos
    fun setDuracionActividadMinutos(newDuration: Int) { _duracionActividadMinutos = newDuration }
    //
    private var _asistentesActividad by mutableStateOf(listOf<String>())
    val asistentesActividad: List<String> get() = _asistentesActividad
    fun setAsistentesActividad(newData: List<String>) { _asistentesActividad = newData }
    //
    private var _notasActividad by mutableStateOf("")
    val notasActividad: String get() = _notasActividad
    fun setNotasActividad(newNotes: String) { _notasActividad = newNotes }
    // Set all values
    fun setSportVariables() {_id = "";
        setNombreActividad(""); setFechaInicioActividad("Ninguna fecha seleccionada");
        fechaInicioActividadES = ""; setHoraInicioActividad("Ninguna hora seleccionada"); setLugarActividad("");
        setDuracionActividadMinutos(1); setAsistentesActividad(emptyList()); setNotasActividad("");
    }
    // Fetch Sport Data To Update On Screen
    fun fetchSelectedSportData(sportId: String) {
        _availableSportList.filterResultList { it._id == sportId }.forEach {
            _id = it._id; _nombreActividad = it.nombreActividad;
            _fechaInicioActividad = it.fechaInicioActividad.substring(0, 10);
            _horaInicioActividad = it.fechaInicioActividad.substring(11, 19);
            fechaInicioActividadES = dateTimeFormatterES(_fechaInicioActividad).toString();
            _lugarActividad = it.lugarActividad; _duracionActividadMinutos = it.duracionActividadMinutos;
            _asistentesActividad = it.asistentesActividad; _notasActividad = it.notasActividad;
        }
    }
    // Sport Request
    private fun createSportRequest(): SportModel {
        return SportModel(_id = "", _nombreActividad, "${_fechaInicioActividad}T${_horaInicioActividad}",
            _lugarActividad, _duracionActividadMinutos, _asistentesActividad, _notasActividad
        )
    }
    ////
    // GET ALL
    private var _availableSportList by mutableStateOf(ApiResponseResultListModel<SportModel>(data = null, message = "", code = 0))
    val availableSportList: ApiResponseResultListModel<SportModel> get() = _availableSportList
    fun fetchAvailableSports(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchAvailableSports")) {
            fetchSportScreenUIState(UiState.Loading)
            sportRepository.getAllSports().let { _availableSportList = it }
            _responseMessage = _availableSportList.message
            if (_availableSportList.code != 200) fetchSportScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "fetchAvailableSports", _responseMessage)
            )
            else {
                setSportVariables()
                fetchSportScreenUIState(
                    UiState.Success("screenInit", screenCallName, "fetchAvailableSports", _responseMessage)
                )
            }
        }
    }
    // GET UNAVAILABLE-DATES
    private var _unavailableSportDatesList by mutableStateOf(ApiResponseListModel<DatesModel>(data = null, message = "", code = 0))
    val unavailableSportDatesList: ApiResponseListModel<DatesModel> get() = _unavailableSportDatesList
    fun fetchUnavailableDates(screenCallName: String = "Unknown Screen Caller") {
        if (screenCallName == "createSportScreen") {
            viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchUnavailableDates")) {
                fetchCreateSportScreenUIState(UiState.Loading)
                sportRepository.getUnavailableSportsDates().let{ _unavailableSportDatesList = it }
                _responseMessage = _unavailableSportDatesList.message
                if (_unavailableSportDatesList.code != 200) fetchCreateSportScreenUIState(
                    UiState.Error("fetchDataErrorType", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
                else fetchCreateSportScreenUIState(
                    UiState.Success("screenInit", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
            }
        }
        if (screenCallName == "editSportScreen") {
            viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchUnavailableDates")) {
                fetchEditSportScreenUIState(UiState.Loading)
                sportRepository.getUnavailableSportsDates().let{ _unavailableSportDatesList = it }
                _responseMessage = _unavailableSportDatesList.message
                if (_unavailableSportDatesList.code != 200) fetchEditSportScreenUIState(
                    UiState.Error("fetchDataErrorType", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
                else fetchEditSportScreenUIState(
                    UiState.Success("screenInit", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
            }
        }
    }
    // POST
    private var _sportToCreateResponse by mutableStateOf(ApiResponseListModel<GenericApiMessageResponse>(data = null, message = "", code = 0))
    fun createSport(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller("createSportScreen", "createSport")) {
            fetchCreateSportScreenUIState(UiState.Loading)
            sportRepository.postSport(createSportRequest()).let { _sportToCreateResponse = it }
            _responseMessage = _sportToCreateResponse.message
            if (_sportToCreateResponse.code != 201) fetchCreateSportScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "createSport", _responseMessage)
            )
            else {
                setSportVariables()
                fetchCreateSportScreenUIState(
                    UiState.Success("screenRunning", screenCallName, "createSport", _responseMessage)
                )
            }
        }
    }
    // PUT
    private var _sportToPutResponse by mutableStateOf(ApiResponseListModel<GenericApiMessageResponse>(data = null, message = "", code = 0))
    /** Actualiza una reserva existente y actualiza el estado de la UI.
     * Lanza una coroutine en el viewModelScope, asegurando la cancelación automática si el ViewModel es destruido evitando fugas de memoria o llamadas innecesarias.
     */
    fun putSport(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller("editSportScreen", "putSport")) {
            fetchEditSportScreenUIState(UiState.Loading)
            sportRepository.putSport(_id, createSportRequest()).let { _sportToPutResponse = it }
            _responseMessage = _sportToPutResponse.message
            if (_sportToPutResponse.code != 200) fetchEditSportScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "putSport", _responseMessage)
            )
            else {
                setSportVariables()
                fetchEditSportScreenUIState(
                    UiState.Success("screenRunning", screenCallName, "putSport", _responseMessage)
                )
            }
        }
    }
    // DELETE
    private var _sportToDeleteResponse by mutableStateOf(ApiResponseListModel<GenericApiMessageResponse>(data = null, message = "", code = 0))
    /** Elimina una reserva existente y actualiza el estado de la UI.
     * Lanza una coroutine en el viewModelScope, asegurando la cancelación automática si el ViewModel es destruido evitando fugas de memoria o llamadas innecesarias.
     */
    fun deleteSport(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller("editSportScreen", "deleteSport")) {
            fetchEditSportScreenUIState(UiState.Loading)
            sportRepository.deleteSport(_id).let { _sportToDeleteResponse = it }
            _responseMessage = _sportToDeleteResponse.message
            if (_sportToDeleteResponse.code != 204) fetchEditSportScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "deleteSport", _responseMessage)
            )
            else {
                setSportVariables()
                fetchEditSportScreenUIState(
                    UiState.Success("screenRunning", screenCallName, "deleteSport", _responseMessage)
                )
            }
        }
    }
    //// SPORT SEARCHER
    private var _showSearchedSports by  mutableStateOf(false)
    val showSearchedSports: Boolean get() = _showSearchedSports
    fun setShowSearchedSports(value: Boolean) { _showSearchedSports = value }
    //
    private var _sportNameToSearch by mutableStateOf("")
    private var _matchSportsList by mutableStateOf(ApiResponseResultListModel<SportModel>(data = null, message = "", code = 0))
    val matchSportsList: ApiResponseResultListModel<SportModel> get() = _matchSportsList
    /** Función que busca salas en base al nombre recibido por parametro.
     * @param nameToSearch Nombre de la sala recibido desde el buscador.
     */
    fun fetchSportToSearch(nameToSearch: String) {
        // Guardamos el nombre de la pelicula a buscar
        _sportNameToSearch = nameToSearch
        // Guardamos los objetos de tipo '' para mostrar las salas que coincidan con el nombre de la sala a buscar(_roomToSearch).
        _matchSportsList = _availableSportList.filterResultList { it.nombreActividad.contains(_sportNameToSearch, ignoreCase = true) }
        // La lista solo se mostrara en caso de haber una sala que buscar que coincida con el patron indicado.
        _showSearchedSports = Regex("[a-zA-Z0-9áéíóúÁÉÍÓÚüÜñÑ -]+").containsMatchIn(_sportNameToSearch)
    }
}