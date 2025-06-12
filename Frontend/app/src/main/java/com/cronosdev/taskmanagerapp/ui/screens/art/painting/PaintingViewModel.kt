package com.cronosdev.taskmanagerapp.ui.screens.art.painting
//
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.cronosdev.taskmanagerapp.data.model.art.PaintingModel
import com.cronosdev.taskmanagerapp.data.model.courrutines.CoroutineCaller
import com.cronosdev.taskmanagerapp.data.repositories.art.PaintingRepository
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
class PaintingViewModel @Inject constructor(private val paintingRepository: PaintingRepository): ViewModel()  {
    private val _dayTimeFormaterEEUU = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dayTimeFormaterEEUU: DateTimeFormatter get() = _dayTimeFormaterEEUU
    // Formateador de fechas al castellano solo para la visualizacion de los datos.
    val dayTimeFormaterES = SimpleDateFormat("dd MMMM yyyy", Locale("es", "ES"))
    //
    private val _paintingScreenUiState = MutableStateFlow<UiState?>(null)
    val paintingScreenUiState: StateFlow<UiState?> = _paintingScreenUiState
    //
    private val _updatePaintingScreenUiState = MutableStateFlow<UiState?>(null)
    val updatePaintingScreenUiState: StateFlow<UiState?> = _updatePaintingScreenUiState
    //
    private val _createPaintingScreenUiState = MutableStateFlow<UiState?>(null)
    val createPaintingScreenUiState: StateFlow<UiState?> = _createPaintingScreenUiState
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
        if (screenCaller == Destinations.ART_PAINTING_SCREEN_URL)
            fetchPaintingScreenUIState(UiState.Error("throwableErrorType", screenCaller, methodCaller, errorMessage))
        if (screenCaller == Destinations.ART_CREATE_PAINTING_SCREEN_URL)
            fetchCreatePaintingScreenUIState(UiState.Error("throwableErrorType", screenCaller, methodCaller, errorMessage))
        if (screenCaller == Destinations.ART_UPDATE_PAINTING_SCREEN_URL)
            fetchEditPaintingScreenUIState(UiState.Error("throwableErrorType", screenCaller, methodCaller, errorMessage))
    }
    /** Funciones utilizadas para actualizar los estados de cada una de las UI que dependen de este viewModel.
     * @param state Nuevo estado de la UI.
     */
    private fun fetchPaintingScreenUIState(state: UiState) { _paintingScreenUiState.value = state }
    private fun fetchCreatePaintingScreenUIState(state: UiState) { _createPaintingScreenUiState.value = state }
    private fun fetchEditPaintingScreenUIState(state: UiState) { _updatePaintingScreenUiState.value = state }
    private var _responseMessage by mutableStateOf("")
    //// GETTERS & SETTERS
    private var _id by mutableStateOf("")
    val id: String get() = _id
    //
    private var _nombreExposicionArte by mutableStateOf("")
    val nombreExposicionArte: String get() = _nombreExposicionArte
    fun setNombreExposicionArte(newCinemaName: String) { _nombreExposicionArte = newCinemaName }
    //
    private var _descripcionExposicionArte by mutableStateOf("")
    val descripcionExposicionArte: String get() = _descripcionExposicionArte
    fun setDescripcionExposicionArte(newCinemaDescription: String) { _descripcionExposicionArte = newCinemaDescription }
    //
    private var _pintoresExposicionArte by mutableStateOf(listOf<String>())
    val pintoresExposicionArte: List<String> get() = _pintoresExposicionArte
    fun setPintoresExposicionArte(newData: List<String>) { _pintoresExposicionArte = newData }
    //
    private var _fechaInicioExposicionArte by mutableStateOf("Ninguna fecha seleccionada")
    val fechaInicioExposicionArte: String get() = _fechaInicioExposicionArte
    fun setFechaInicioExposicionArte(newDate: String) { _fechaInicioExposicionArte = newDate }
    var fechaInicioExposicionArteES by mutableStateOf("")
    //
    private var _horaInicioExposicionArte by mutableStateOf("Ninguna hora seleccionada")
    val horaInicioExposicionArte: String get() = _horaInicioExposicionArte
    fun setHoraInicioExposicionArte(newDate: String) { _horaInicioExposicionArte = newDate }
    //
    private var _fechaFinExposicionArte by mutableStateOf("Ninguna fecha seleccionada")
    val fechaFinExposicionArte: String get() = _fechaFinExposicionArte
    fun setFechaFinExposicionArte(newDate: String) { _fechaFinExposicionArte = newDate }
    var fechaFinExposicionArteES by mutableStateOf("")
    //
    private var _horaFinExposicionArte by mutableStateOf("Ninguna hora seleccionada")
    val horaFinExposicionArte: String get() = _horaFinExposicionArte
    fun setHoraFinExposicionArte(newDate: String) { _horaFinExposicionArte = newDate }
    //
    private var _lugarExposicionArte by mutableStateOf("")
    val lugarExposicionArte: String get() = _lugarExposicionArte
    fun setLugarExposicionArte(newLocation: String) { _lugarExposicionArte = newLocation }
    //
    private var _precioEntradaExposicionArte by mutableFloatStateOf(1F)
    val precioEntradaExposicionArte: Float get() = _precioEntradaExposicionArte
    fun setPrecioEntradaExposicionArte(newPrice: Float) { _precioEntradaExposicionArte = newPrice }
    //
    private var _notasExposicionArte by mutableStateOf("")
    val notasExposicionArte: String get() = _notasExposicionArte
    fun setNotasExposicionArte(newNotes: String) { _notasExposicionArte = newNotes }
    // Set all values
    fun setPaintingVariables() {
        setNombreExposicionArte(""); setDescripcionExposicionArte(""); setPintoresExposicionArte(emptyList());
        setFechaInicioExposicionArte("Ninguna fecha seleccionada"); fechaInicioExposicionArteES = "";
        setHoraInicioExposicionArte("Ninguna hora seleccionada"); setFechaFinExposicionArte("Ninguna fecha seleccionada");
        fechaFinExposicionArteES = ""; setHoraFinExposicionArte("Ninguna hora seleccionada");
        setLugarExposicionArte(""); setPrecioEntradaExposicionArte(1F); setNotasExposicionArte("");
    }
    // Fetch Painting Data To Update On Screen
    fun fetchSelectedPaintingData(paintingId: String) {
        _availablePaintingList.filterResultList { it._id == paintingId }.forEach {
            _id = it._id; _nombreExposicionArte = it.nombreExposicionArte;
            _descripcionExposicionArte = it.descripcionExposicionArte; _pintoresExposicionArte = it.pintoresExposicionArte;
            _fechaInicioExposicionArte = it.fechaInicioExposicionArte.substring(0, 10);
            _horaInicioExposicionArte = it.fechaInicioExposicionArte.substring(11, 19);
            fechaInicioExposicionArteES = dateTimeFormatterES(_fechaInicioExposicionArte).toString();
            _fechaFinExposicionArte = it.fechaFinExposicionArte.substring(0, 10);
            _horaFinExposicionArte = it.fechaFinExposicionArte.substring(11, 19);
            fechaFinExposicionArteES = dateTimeFormatterES(_fechaFinExposicionArte).toString();
            _lugarExposicionArte = it.lugarExposicionArte; _precioEntradaExposicionArte = it.precioEntradaExposicionArte;
            _notasExposicionArte = it.notasExposicionArte;
        }
    }
    // Painting Request
    private fun createPaintingRequest(): PaintingModel {
        return PaintingModel(_id = "", _nombreExposicionArte, _descripcionExposicionArte, _pintoresExposicionArte,
            "${_fechaInicioExposicionArte}T${_horaInicioExposicionArte}",
            "${_fechaFinExposicionArte}T${_horaFinExposicionArte}",
            _lugarExposicionArte, _precioEntradaExposicionArte, _notasExposicionArte
        )
    }
    //// METHODS OF THE VIEW MODEL
    // GET ALL
    private var _availablePaintingList by mutableStateOf(ApiResponseResultListModel<PaintingModel>(data = null, message = "", code = 0))
    val availablePaintingList: ApiResponseResultListModel<PaintingModel> get() = _availablePaintingList
    fun fetchAvailablePaintings(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchAvailablePaintings")) {
            fetchPaintingScreenUIState(UiState.Loading)
            paintingRepository.getAllPaintings().let { _availablePaintingList = it }
            _responseMessage = _availablePaintingList.message
            if (_availablePaintingList.code != 200) fetchPaintingScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "fetchAvailablePaintings", _responseMessage)
            )
            else {
                setPaintingVariables()
                fetchPaintingScreenUIState(
                    UiState.Success("screenInit", screenCallName, "fetchAvailablePaintings", _responseMessage)
                )
            }
        }
    }
    // GET UNAVAILABLE-DATES
    private var _unavailablePaintingDatesList by mutableStateOf(ApiResponseListModel<DatesModel>(data = null, message = "", code = 0))
    val unavailablePaintingDatesList: ApiResponseListModel<DatesModel> get() = _unavailablePaintingDatesList
    fun fetchUnavailableDates(screenCallName: String = "Unknown Screen Caller") {
        if (screenCallName == "artCreatePaintingScreen") {
            viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchUnavailableDates")) {
                fetchCreatePaintingScreenUIState(UiState.Loading)
                paintingRepository.getUnavailablePaintingsDates().let{ _unavailablePaintingDatesList = it }
                _responseMessage = _unavailablePaintingDatesList.message
                if (_unavailablePaintingDatesList.code != 200) fetchCreatePaintingScreenUIState(
                    UiState.Error("fetchDataErrorType", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
                else fetchCreatePaintingScreenUIState(
                    UiState.Success("screenInit", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
            }
        }
        if (screenCallName == "artEditPaintingScreen") {
            viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchUnavailableDates")) {
                fetchEditPaintingScreenUIState(UiState.Loading)
                paintingRepository.getUnavailablePaintingsDates().let{ _unavailablePaintingDatesList = it }
                _responseMessage = _unavailablePaintingDatesList.message
                if (_unavailablePaintingDatesList.code != 200) fetchEditPaintingScreenUIState(
                    UiState.Error("fetchDataErrorType", screenCallName, "fetchUnavailableDates",_responseMessage)
                )
                else fetchEditPaintingScreenUIState(
                    UiState.Success("screenInit", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
            }
        }
    }
    // POST
    private var _paintingToCreateResponse by mutableStateOf(ApiResponseListModel<GenericApiMessageResponse>(data = null, message = "", code = 0))
    fun createPainting(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller("createPaintingScreen", "createPainting")) {
            fetchCreatePaintingScreenUIState(UiState.Loading)
            paintingRepository.postPainting(createPaintingRequest()).let { _paintingToCreateResponse = it }
            _responseMessage = _paintingToCreateResponse.message
            if (_paintingToCreateResponse.code != 201) fetchCreatePaintingScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "createPainting",_responseMessage)
            )
            else {
                setPaintingVariables()
                fetchCreatePaintingScreenUIState(
                    UiState.Success("screenRunning", screenCallName, "createPainting", _responseMessage)
                )
            }
        }
    }
    // PUT
    private var _paintingToPutResponse by mutableStateOf(ApiResponseListModel<GenericApiMessageResponse>(data = null, message = "", code = 0))
    /** Actualiza una reserva existente y actualiza el estado de la UI.
     * Lanza una coroutine en el viewModelScope, asegurando la cancelación automática si el ViewModel es destruido evitando fugas de memoria o llamadas innecesarias.
     */
    fun putPainting(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller("editPaintingScreen", "putPainting")) {
            fetchEditPaintingScreenUIState(UiState.Loading)
            paintingRepository.putPainting(_id, createPaintingRequest()).let { _paintingToPutResponse = it }
            _responseMessage = _paintingToPutResponse.message
            if (_paintingToPutResponse.code != 200) fetchEditPaintingScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "putPainting", _responseMessage)
            )
            else {
                setPaintingVariables()
                fetchEditPaintingScreenUIState(
                    UiState.Success("screenRunning", screenCallName, "putPainting", _responseMessage)
                )
            }
        }
    }
    // DELETE
    private var _paintingToDeleteResponse by mutableStateOf(ApiResponseListModel<GenericApiMessageResponse>(data = null, message = "", code = 0))
    /** Elimina una reserva existente y actualiza el estado de la UI.
     * Lanza una coroutine en el viewModelScope, asegurando la cancelación automática si el ViewModel es destruido evitando fugas de memoria o llamadas innecesarias.
     */
    fun deletePainting(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller("editPaintingScreen", "deletePainting")) {
            fetchEditPaintingScreenUIState(UiState.Loading)
            paintingRepository.deletePainting(_id).let { _paintingToDeleteResponse = it }
            _responseMessage = _paintingToDeleteResponse.message
            if (_paintingToDeleteResponse.code != 204) fetchEditPaintingScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "deletePainting", _responseMessage)
            )
            else {
                setPaintingVariables()
                fetchEditPaintingScreenUIState(
                    UiState.Success("screenRunning", screenCallName, "deletePainting", _responseMessage)
                )
            }
        }
    }
    //// PAINTING SEARCHER
    private var _showSearchedPaintings by  mutableStateOf(false)
    val showSearchedPaintings: Boolean get() = _showSearchedPaintings
    fun setShowSearchedPaintings(value: Boolean) { _showSearchedPaintings = value }
    //
    private var _paintingNameToSearch by mutableStateOf("")
    private var _matchPaintingsList by mutableStateOf(ApiResponseResultListModel<PaintingModel>(data = null, message = "", code = 0))
    val matchPaintingsList: ApiResponseResultListModel<PaintingModel> get() = _matchPaintingsList
    /** Función que busca salas en base al nombre recibido por parametro.
     * @param nameToSearch Nombre de la sala recibido desde el buscador.
     */
    fun fetchPaintingToSearch(nameToSearch: String) {
        // Guardamos el nombre de la pelicula a buscar
        _paintingNameToSearch = nameToSearch
        // Guardamos los objetos de tipo '' para mostrar las salas que coincidan con el nombre de la sala a buscar(_roomToSearch).
        _matchPaintingsList = _availablePaintingList.filterResultList { it.nombreExposicionArte.contains(_paintingNameToSearch, ignoreCase = true) }
        // La lista solo se mostrara en caso de haber una sala que buscar que coincida con el patron indicado.
        _showSearchedPaintings = Regex("[a-zA-Z0-9áéíóúÁÉÍÓÚüÜñÑ -]+").containsMatchIn(_paintingNameToSearch)
    }
}