package com.cronosdev.taskmanagerapp.ui.screens.travel
//
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
import com.cronosdev.taskmanagerapp.data.model.travel.TravelModel
import com.cronosdev.taskmanagerapp.data.repositories.travel.TravelRepository
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
class TravelViewModel @Inject constructor(private val travelRepository: TravelRepository): ViewModel()  {
    private val _dayTimeFormaterEEUU = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dayTimeFormaterEEUU: DateTimeFormatter get() = _dayTimeFormaterEEUU
    // Formateador de fechas al castellano solo para la visualizacion de los datos.
    val dayTimeFormaterES = SimpleDateFormat("dd MMMM yyyy", Locale("es", "ES"))
    //
    private val _travelScreenUiState = MutableStateFlow<UiState?>(null)
    val travelScreenUiState: StateFlow<UiState?> = _travelScreenUiState
    //
    private val _createTravelScreenUiState = MutableStateFlow<UiState?>(null)
    val createTravelScreenUiState: StateFlow<UiState?> = _createTravelScreenUiState
    //
    private val _updateTravelScreenUiState = MutableStateFlow<UiState?>(null)
    val updateTravelScreenUiState: StateFlow<UiState?> = _updateTravelScreenUiState
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
        if (screenCaller == Destinations.TRAVEL_SCREEN_URL) fetchTravelScreenUIState(UiState.Error("throwableErrorType", screenCaller, methodCaller, errorMessage))
        if (screenCaller == Destinations.CREATE_TRAVEL_SCREEN_URL) fetchCreateTravelScreenUIState(
            UiState.Error("throwableErrorType", screenCaller, methodCaller, errorMessage))
        if (screenCaller == Destinations.EDIT_TRAVEL_SCREEN_URL) fetchEditTravelScreenUIState(
            UiState.Error("throwableErrorType", screenCaller, methodCaller, errorMessage))
    }
    /** Funciones utilizadas para actualizar los estados de cada una de las UI que dependen de este viewModel.
     * @param state Nuevo estado de la UI.
     */
    private fun fetchTravelScreenUIState(state: UiState) { _travelScreenUiState.value = state }
    private fun fetchCreateTravelScreenUIState(state: UiState) { _createTravelScreenUiState.value = state }
    private fun fetchEditTravelScreenUIState(state: UiState) { _updateTravelScreenUiState.value = state }
    private var _responseMessage by mutableStateOf("")
    //// GETTERS & SETTERS
    private var _id by mutableStateOf("")
    val id: String get() = _id
    //
    private var _nombreDestinoViaje by mutableStateOf("")
    val nombreDestinoViaje: String get() = _nombreDestinoViaje
    fun setNombreDestinoViaje(newDestinationName: String) { _nombreDestinoViaje = newDestinationName }
    //
    private var _fechaSalidaViaje by mutableStateOf("Ninguna fecha seleccionada")
    val fechaSalidaViaje: String get() = _fechaSalidaViaje
    fun setFechaSalidaViaje(newDate: String) { _fechaSalidaViaje = newDate }
    var fechaSalidaViajeES by mutableStateOf("")
    //
    private var _horaSalidaViaje by mutableStateOf("Ninguna hora seleccionada")
    val horaSalidaViaje: String get() = _horaSalidaViaje
    fun setHoraSalidaViaje(newDate: String) { _horaSalidaViaje = newDate }
    //
    private var _lugarSalidaViaje by mutableStateOf("")
    val lugarSalidaViaje: String get() = _lugarSalidaViaje
    fun setLugarSalidaViaje(newLocation: String) { _lugarSalidaViaje = newLocation }
    //
    private var _lugarDestinoViaje by mutableStateOf("")
    val lugarDestinoViaje: String get() = _lugarDestinoViaje
    fun setLugarDestinoViaje(newLocation: String) { _lugarDestinoViaje = newLocation }
    //
    private var _fechaRegresoViaje by mutableStateOf("Ninguna fecha seleccionada")
    val fechaRegresoViaje: String get() = _fechaRegresoViaje
    fun setFechaRegresoViaje(newDate: String) { _fechaRegresoViaje = newDate }
    var fechaRegresoViajeES by mutableStateOf("")
    //
    private var _horaRegresoViaje by mutableStateOf("Ninguna hora seleccionada")
    val horaRegresoViaje: String get() = _horaRegresoViaje
    fun setHoraRegresoViaje(newDate: String) { _horaRegresoViaje = newDate }
    //
    private var _transporteViaje by mutableStateOf("")
    val transporteViaje: String get() = _transporteViaje
    fun setTransporteViaje(newTravelTransport: String) { _transporteViaje = newTravelTransport }
    //
    private var _acompañantesViaje by mutableStateOf(listOf<String>())
    val acompañantesViaje: List<String> get() = _acompañantesViaje
    fun setAcompañantesViaje(newData: List<String>) { _acompañantesViaje = newData }
    //
    private var _notasViaje by mutableStateOf("")
    val notasViaje: String get() = _notasViaje
    fun setNotasViaje(newNotes: String) { _notasViaje = newNotes }
    // Set all values
    fun setTravelVariables() { _id= ""; setNombreDestinoViaje("");
        setFechaSalidaViaje("Ninguna fecha seleccionada"); fechaSalidaViajeES = "";
        setHoraSalidaViaje("Ninguna hora seleccionada"); setLugarSalidaViaje("");
        setLugarDestinoViaje(""); setFechaRegresoViaje("Ninguna fecha seleccionada");
        fechaRegresoViajeES = ""; setHoraRegresoViaje("Ninguna hora seleccionada");
        setTransporteViaje(""); setAcompañantesViaje(emptyList()); setNotasViaje("");
    }
    // Fetch Travel Data To Update On Screen
    fun fetchSelectedTravelData(travelId: String) {
        _availableTravelList.filterResultList { it._id == travelId }.forEach {
            _id = it._id; _nombreDestinoViaje = it.nombreDestinoViaje;
            _fechaSalidaViaje = it.fechaSalidaViaje.substring(0, 10);
            _horaSalidaViaje = it.fechaSalidaViaje.substring(11, 19);
            fechaSalidaViajeES = dateTimeFormatterES(_fechaSalidaViaje).toString();
            _lugarSalidaViaje = it.lugarSalidaViaje; _lugarDestinoViaje = it.lugarDestinoViaje;
            _fechaRegresoViaje = it.fechaRegresoViaje.substring(0, 10);
            _horaRegresoViaje = it.fechaRegresoViaje.substring(11, 19);
            fechaRegresoViajeES = dateTimeFormatterES(_fechaRegresoViaje).toString();
            _transporteViaje = it.transporteViaje; _acompañantesViaje = it.acompañantesViaje;
            _notasViaje = it.notasViaje;
        }
    }
    // Travel Request
    private fun createTravelRequest(): TravelModel {
        return TravelModel(_id = "", _nombreDestinoViaje,
            "${_fechaSalidaViaje}T${_horaSalidaViaje}",
            _lugarSalidaViaje, _lugarDestinoViaje,
            "${_fechaRegresoViaje}T${_horaRegresoViaje}",
            _transporteViaje, _acompañantesViaje, _notasViaje
        )
    }
    ////
    // GET ALL
    private var _availableTravelList by mutableStateOf(ApiResponseResultListModel<TravelModel>(data = null, message = "", code = 0))
    val availableTravelList: ApiResponseResultListModel<TravelModel> get() = _availableTravelList
    fun fetchAvailableTravels(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchAvailableTravels")) {
            fetchTravelScreenUIState(UiState.Loading)
            travelRepository.getAllTravels().let { _availableTravelList = it }
            _responseMessage = _availableTravelList.message
            if (_availableTravelList.code != 200) fetchTravelScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "fetchAvailableTravels", _responseMessage)
            )
            else {
                setTravelVariables()
                fetchTravelScreenUIState(
                    UiState.Success("screenInit", screenCallName, "fetchAvailableTravels", _responseMessage)
                )
            }
        }
    }
    //// GET UNAVAILABLE-DATES
    private var _unavailableTravelDatesList by mutableStateOf(ApiResponseListModel<DatesModel>(data = null, message = "", code = 0))
    val unavailableTravelDatesList: ApiResponseListModel<DatesModel> get() = _unavailableTravelDatesList
    fun fetchUnavailableDates(screenCallName: String = "Unknown Screen Caller") {
        if (screenCallName == "createTravelScreen") {
            viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchUnavailableDates")) {
                fetchCreateTravelScreenUIState(UiState.Loading)
                travelRepository.getUnavailableTravelsDates().let{ _unavailableTravelDatesList = it }
                _responseMessage = _unavailableTravelDatesList.message
                if (_unavailableTravelDatesList.code != 200) fetchCreateTravelScreenUIState(
                    UiState.Error("fetchDataErrorType", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
                else fetchCreateTravelScreenUIState(
                    UiState.Success("screenInit", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
            }
        }
        if (screenCallName == "editTravelScreen") {
            viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchUnavailableDates")) {
                fetchEditTravelScreenUIState(UiState.Loading)
                travelRepository.getUnavailableTravelsDates().let{ _unavailableTravelDatesList = it }
                _responseMessage = _unavailableTravelDatesList.message
                if (_unavailableTravelDatesList.code != 200) fetchEditTravelScreenUIState(
                    UiState.Error("fetchDataErrorType", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
                else fetchEditTravelScreenUIState(
                    UiState.Success("screenInit", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
            }
        }
    }
    // POST
    private var _travelToCreateResponse by mutableStateOf(ApiResponseListModel<GenericApiMessageResponse>(data = null, message = "", code = 0))
    fun createTravel(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller("createTravelScreen", "createTravel")) {
            fetchCreateTravelScreenUIState(UiState.Loading)
            travelRepository.postTravel(createTravelRequest()).let { _travelToCreateResponse = it }
            _responseMessage = _travelToCreateResponse.message
            if (_travelToCreateResponse.code != 201) fetchCreateTravelScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "createTravel", _responseMessage)
            )
            else {
                setTravelVariables()
                fetchCreateTravelScreenUIState(
                    UiState.Success("screenRunning", screenCallName, "createTravel", _responseMessage)
                )
            }
        }
    }
    // PUT
    private var _travelToPutResponse by mutableStateOf(ApiResponseListModel<GenericApiMessageResponse>(data = null, message = "", code = 0))
    /** Actualiza una reserva existente y actualiza el estado de la UI.
     * Lanza una coroutine en el viewModelScope, asegurando la cancelación automática si el ViewModel es destruido evitando fugas de memoria o llamadas innecesarias.
     */
    fun putTravel(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "putTravel")) {
            fetchEditTravelScreenUIState(UiState.Loading)
            travelRepository.putTravel(_id, createTravelRequest()).let { _travelToPutResponse = it }
            _responseMessage = _travelToPutResponse.message
            if (_travelToPutResponse.code != 200) fetchEditTravelScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "putTravel", _responseMessage)
            )
            else {
                setTravelVariables()
                fetchEditTravelScreenUIState(
                    UiState.Success("screenRunning", screenCallName, "putTravel", _responseMessage)
                )
            }
        }
    }
    // DELETE
    private var _travelToDeleteResponse by mutableStateOf(ApiResponseListModel<GenericApiMessageResponse>(data = null, message = "", code = 0))
    /** Elimina una reserva existente y actualiza el estado de la UI.
     * Lanza una coroutine en el viewModelScope, asegurando la cancelación automática si el ViewModel es destruido evitando fugas de memoria o llamadas innecesarias.
     */
    fun deleteTravel(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "deleteTravel")) {
            fetchEditTravelScreenUIState(UiState.Loading)
            travelRepository.deleteTravel(_id).let { _travelToDeleteResponse = it }
            _responseMessage = _travelToDeleteResponse.message
            if (_travelToDeleteResponse.code != 204) fetchEditTravelScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "deleteTravel", _responseMessage)
            )
            else {
                setTravelVariables()
                fetchEditTravelScreenUIState(
                    UiState.Success("screenRunning", screenCallName, "deleteTravel", _responseMessage)
                )
            }
        }
    }
    //// TRAVEL SEARCHER
    private var _showSearchedTravels by  mutableStateOf(false)
    val showSearchedTravels: Boolean get() = _showSearchedTravels
    fun setShowSearchedTravels(value: Boolean) { _showSearchedTravels = value }
    //
    private var _travelNameToSearch by mutableStateOf("")
    private var _matchTravelsList by mutableStateOf(ApiResponseResultListModel<TravelModel>(data = null, message = "", code = 0))
    val matchTravelsList: ApiResponseResultListModel<TravelModel> get() = _matchTravelsList
    /** Función que busca salas en base al nombre recibido por parametro.
     * @param nameToSearch Nombre de la sala recibido desde el buscador.
     */
    fun fetchTravelToSearch(nameToSearch: String) {
        // Guardamos el nombre de la pelicula a buscar
        _travelNameToSearch = nameToSearch
        // Guardamos los objetos de tipo '' para mostrar las salas que coincidan con el nombre de la sala a buscar(_roomToSearch).
        _matchTravelsList = _availableTravelList.filterResultList { it.nombreDestinoViaje.contains(_travelNameToSearch, ignoreCase = true) }
        // La lista solo se mostrara en caso de haber una sala que buscar que coincida con el patron indicado.
        _showSearchedTravels = Regex("[a-zA-Z0-9áéíóúÁÉÍÓÚüÜñÑ -]+").containsMatchIn(_travelNameToSearch)
    }
}