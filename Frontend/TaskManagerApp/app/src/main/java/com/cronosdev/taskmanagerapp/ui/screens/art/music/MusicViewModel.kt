package com.cronosdev.taskmanagerapp.ui.screens.art.music
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
import com.cronosdev.taskmanagerapp.data.model.art.MusicModel
import com.cronosdev.taskmanagerapp.data.model.courrutines.CoroutineCaller
import com.cronosdev.taskmanagerapp.data.repositories.art.MusicRepository
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
class MusicViewModel @Inject constructor(private val musicRepository: MusicRepository): ViewModel()  {
    private val _dayTimeFormaterEEUU = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dayTimeFormaterEEUU: DateTimeFormatter get() = _dayTimeFormaterEEUU
    // Formateador de fechas al castellano solo para la visualizacion de los datos.
    val dayTimeFormaterES = SimpleDateFormat("dd MMMM yyyy", Locale("es", "ES"))
    //
    private val _musicScreenUiState = MutableStateFlow<UiState?>(null)
    val musicScreenUiState: StateFlow<UiState?> = _musicScreenUiState
    //
    private val _editMusicScreenUiState = MutableStateFlow<UiState?>(null)
    val editMusicScreenUiState: StateFlow<UiState?> = _editMusicScreenUiState
    //
    private val _createMusicScreenUiState = MutableStateFlow<UiState?>(null)
    val createMusicScreenUiState: StateFlow<UiState?> = _createMusicScreenUiState
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
        if (screenCaller == Destinations.ART_MUSIC_SCREEN_URL)
            fetchMusicScreenUIState(UiState.Error("throwableErrorType", screenCaller, methodCaller, errorMessage))
        if (screenCaller == Destinations.ART_CREATE_MUSIC_SCREEN_URL)
            fetchCreateMusicScreenUIState(UiState.Error("throwableErrorType", screenCaller, methodCaller, errorMessage))
        if (screenCaller == Destinations.ART_EDIT_MUSIC_SCREEN_URL)
            fetchEditMusicScreenUIState(UiState.Error("throwableErrorType", screenCaller, methodCaller, errorMessage))
    }
    /** Funciones utilizadas para actualizar los estados de cada una de las UI que dependen de este viewModel.
     * @param state Nuevo estado de la UI.
     */
    private fun fetchMusicScreenUIState(state: UiState) { _musicScreenUiState.value = state }
    private fun fetchEditMusicScreenUIState(state: UiState) { _editMusicScreenUiState.value = state }
    private fun fetchCreateMusicScreenUIState(state: UiState) { _createMusicScreenUiState.value = state }
    private var _responseMessage by mutableStateOf("")
    //// GETTERS & SETTERS
    private var _id by mutableStateOf("")
    val id: String get() = _id
    //
    private var _nombreEvento by mutableStateOf("")
    val nombreEvento: String get() = _nombreEvento
    fun setNombreEvento(newEventName: String) { _nombreEvento = newEventName }
    //
    private var _descripcionEvento by mutableStateOf("")
    val descripcionEvento: String get() = _descripcionEvento
    fun setDescripcionEvento(newEventDescription: String) { _descripcionEvento = newEventDescription }
    //
    private var _artistasEvento by mutableStateOf(listOf<String>())
    val artistasEvento: List<String> get() = _artistasEvento
    fun setArtistasEvento(newData: List<String>) { _artistasEvento = newData }
    // FECHA INICIO
    private var _fechaInicioEvento by mutableStateOf("Ninguna fecha seleccionada")
    val fechaInicioEvento: String get() = _fechaInicioEvento
    fun setFechaInicioEvento(newStartDate: String) { _fechaInicioEvento = newStartDate }
    var fechaInicioEventoES by mutableStateOf("")
    // HORA INICIO
    private var _horaInicioEvento by mutableStateOf("Ninguna hora seleccionada")
    val horaInicioPelicula: String get() = _horaInicioEvento
    fun setHoraInicioEvento(newHourDate: String) { _horaInicioEvento = newHourDate }

    // FECHA FIN
    private var _fechaFinEvento by mutableStateOf("Ninguna hora seleccionada")
    val fechaFinEvento: String get() = _fechaFinEvento
    fun setFechaFinEvento(newEndDate: String) { _fechaFinEvento = newEndDate }

    private var _fechaFinEventoES by mutableStateOf("Ninguna hora seleccionada")
    val fechaFinEventoES: String get()  = _fechaFinEventoES
    fun setFechaFinEventoES() { _fechaFinEventoES = dateTimeFormatterES(_fechaFinEvento).toString(); }
    // HORA FIN
    private var _horaFinEvento by mutableStateOf("Ninguna hora seleccionada")
    val horaFinEvento: String get() = _horaFinEvento
    fun setHoraFinEvento(newHourDate: String) { _horaFinEvento = newHourDate }


    private val _showFechaFinDatePicker = MutableStateFlow(false)
    val showFechaFinDatePicker: StateFlow<Boolean> = _showFechaFinDatePicker
    fun setShowFechaFinDatePicker(value: Boolean) { _showFechaFinDatePicker.value = value }


    //
    private var _lugarEvento by mutableStateOf("")
    val lugarEvento: String get() = _lugarEvento
    fun setLugarEvento(newLocation: String) { _lugarEvento = newLocation }
    //
    private var _precioEvento by mutableFloatStateOf(1F)
    val precioEvento: Float get() = _precioEvento
    fun setPrecioEntradaEvento(newPrice: Float) { _precioEvento = newPrice }
    //
    private var _notasEvento by mutableStateOf("")
    val notasEvento: String get() = _notasEvento
    fun setNotasEvento(newNotes: String) { _notasEvento = newNotes }
    //
    private fun _setMusicVariables() {_id = "";
        setNombreEvento(""); setDescripcionEvento(""); setArtistasEvento(emptyList());
        setFechaInicioEvento("Ninguna fecha seleccionada"); fechaInicioEventoES = ""; setHoraInicioEvento("Ninguna hora seleccionada");
        setFechaFinEvento("Ninguna fecha seleccionada"); _fechaFinEventoES = ""; setHoraFinEvento("Ninguna hora seleccionada");
        setLugarEvento(""); setPrecioEntradaEvento(1F); setNotasEvento("");
    }
    /** Funcion encargada de obtener los datos de la reserva seleecionada para editar los datos de la misma o para eliminarla en el componente/vista ¿¿??
     * @param musicId ID de la reserva.
     */
    fun fetchSelectedMusicData(musicId: String) {
        _availableMusicsList.filterResultList { it._id == musicId }.forEach {
            _id = it._id; _nombreEvento = it.nombreEvento;
            _descripcionEvento = it.descripcionEvento; _artistasEvento = it.artistasEvento
            _fechaInicioEvento = it.fechaInicioEvento.substring(0, 10);
            _horaInicioEvento = it.fechaInicioEvento.substring(11, 19);
            fechaInicioEventoES = dateTimeFormatterES(_fechaInicioEvento).toString();
            _fechaFinEvento = it.fechaFinEvento.substring(0, 10);
            _horaFinEvento = it.fechaFinEvento.substring(11, 19);
            _fechaFinEventoES = dateTimeFormatterES(_fechaFinEvento).toString();
            _lugarEvento = it.lugarEvento; _precioEvento = it.precioEvento;
            _notasEvento = it.notasEvento;
        }
    }
    //
    private fun createMusicRequest(): MusicModel {
        return MusicModel(_id = "", _nombreEvento, _descripcionEvento, _artistasEvento,
            "${_fechaInicioEvento}T${_horaInicioEvento}",
            "${_fechaFinEvento}T${_horaFinEvento}",
            _lugarEvento, _precioEvento, _notasEvento
        )
    }
    //// METHODS OF THE VIEW MODEL
    // GET ALL
    private var _availableMusicsList by mutableStateOf(ApiResponseResultListModel<MusicModel>(data = null, message = "", code = 0))
    val availableMusicsList: ApiResponseResultListModel<MusicModel> get() = _availableMusicsList
    fun fetchAvailableMusics(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchAvailableMusics")) {
            fetchMusicScreenUIState(UiState.Loading)
            musicRepository.getAllMusics().let { _availableMusicsList = it }
            _responseMessage = _availableMusicsList.message
            if (_availableMusicsList.code != 200) fetchMusicScreenUIState(UiState.Error("fetchDataErrorType", screenCallName, "fetchAvailableMusics", _responseMessage))
            else {
                _setMusicVariables()
                fetchMusicScreenUIState(
                    UiState.Success("screenInit", screenCallName, "fetchAvailableMusics", _responseMessage)
                )
            }
        }
    }
    // GET UNAVAILABLE-DATES
    private var _unavailableMusicDatesList by mutableStateOf(ApiResponseListModel<DatesModel>(data = null, message = "", code = 0))
    val unavailableMusicDatesList: ApiResponseListModel<DatesModel> get() = _unavailableMusicDatesList
    fun fetchUnavailableDates(screenCallName: String = "Unknown Screen Caller") {
        if (screenCallName == "artCreateMusicScreen") {
            viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchUnavailableDates")) {
                fetchCreateMusicScreenUIState(UiState.Loading)
                musicRepository.getUnavailableMusicsDates().let{ _unavailableMusicDatesList = it }
                _responseMessage = _unavailableMusicDatesList.message
                if (_unavailableMusicDatesList.code != 200) fetchCreateMusicScreenUIState(
                    UiState.Error("fetchDataErrorType", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
                else fetchCreateMusicScreenUIState(
                    UiState.Success("screenInit", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
            }
        }
        if (screenCallName == "artEditMusicScreen") {
            viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchUnavailableDates")) {
                fetchEditMusicScreenUIState(UiState.Loading)
                musicRepository.getUnavailableMusicsDates().let{ _unavailableMusicDatesList = it }
                _responseMessage = _unavailableMusicDatesList.message
                if (_unavailableMusicDatesList.code != 200) fetchEditMusicScreenUIState(
                    UiState.Error("fetchDataErrorType", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
                else fetchEditMusicScreenUIState(
                    UiState.Success("screenInit", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
            }
        }
    }
    // POST
    private var _musicToCreateResponse by mutableStateOf(ApiResponseListModel<GenericApiMessageResponse>(data = null, message = "", code = 0))
    /** Crea una nueva cita y actualiza el estado de la UI.
     * Lanza una coroutine en el viewModelScope, asegurando la cancelación automática si el ViewModel es destruido evitando fugas de memoria o llamadas innecesarias.
     */
    fun createMusic(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler) {
            fetchCreateMusicScreenUIState(UiState.Loading)
            musicRepository.postMusic(createMusicRequest()).let { _musicToCreateResponse = it }
            _responseMessage = _musicToCreateResponse.message
            if (_musicToCreateResponse.code != 201) fetchCreateMusicScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "createMusic", _responseMessage)
            )
            else {
                _setMusicVariables()
                fetchCreateMusicScreenUIState(
                    UiState.Success("screenRunning", screenCallName, "createMusic", _responseMessage)
                )
            }
        }
    }
    // PUT
    private var _musicToPutResponse by mutableStateOf(ApiResponseListModel<GenericApiMessageResponse>(data = null, message = "", code = 0))
    /** Actualiza una reserva existente y actualiza el estado de la UI.
     * Lanza una coroutine en el viewModelScope, asegurando la cancelación automática si el ViewModel es destruido evitando fugas de memoria o llamadas innecesarias.
     */
    fun putMusic(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler) {
            fetchEditMusicScreenUIState(UiState.Loading)
            musicRepository.putMusic(_id, createMusicRequest()).let { _musicToPutResponse = it }
            _responseMessage = _musicToPutResponse.message
            if (_musicToPutResponse.code != 200) fetchEditMusicScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "putMusic", _responseMessage)
            )
            else {
                _setMusicVariables()
                fetchEditMusicScreenUIState(
                    UiState.Success("screenRunning", screenCallName, "putMusic", _responseMessage)
                )
            }
        }
    }
    // DELETE
    private var _musicToDelete by mutableStateOf(ApiResponseListModel<GenericApiMessageResponse>(data = null, message = "", code = 0))
    /** Elimina una reserva existente y actualiza el estado de la UI.
     * Lanza una coroutine en el viewModelScope, asegurando la cancelación automática si el ViewModel es destruido evitando fugas de memoria o llamadas innecesarias.
     */
    fun deleteMusic(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler) {
            fetchEditMusicScreenUIState(UiState.Loading)
            musicRepository.deleteMusic(_id).let { _musicToDelete = it }
            _responseMessage = _musicToDelete.message
            if (_musicToDelete.code != 204) fetchEditMusicScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "deleteMusic", _responseMessage)
            )
            else {
                _setMusicVariables()
                fetchEditMusicScreenUIState(
                    UiState.Success("screenRunning", screenCallName, "deleteMusic", _responseMessage)
                )
            }
        }
    }
    //// MUSIC SEARCHER
    private var _showSearchedMusics by  mutableStateOf(false)
    val showSearchedMusics: Boolean get() = _showSearchedMusics
    private var _musicNameToSearch by mutableStateOf("")
    private var _matchMusicsList by mutableStateOf(ApiResponseResultListModel<MusicModel>(data = null, message = "", code = 0))
    val matchMusicsList: ApiResponseResultListModel<MusicModel> get() = _matchMusicsList
    /** Función que busca salas en base al nombre recibido por parametro.
     * @param cinemaNameToSearch Nombre de la sala recibido desde el buscador.
     */
    fun fetchMusicToSearch(cinemaNameToSearch: String) {
        // Guardamos el nombre de la pelicula a buscar
        _musicNameToSearch = cinemaNameToSearch
        // Guardamos los objetos de tipo '' para mostrar las salas que coincidan con el nombre de la sala a buscar(_roomToSearch).
        _matchMusicsList = _availableMusicsList.filterResultList { it.nombreEvento.contains(_musicNameToSearch, ignoreCase = true) }
        // La lista solo se mostrara en caso de haber una sala que buscar que coincida con el patron indicado.
        _showSearchedMusics = Regex("[a-zA-Z0-9áéíóúÁÉÍÓÚüÜñÑ -]+").containsMatchIn(_musicNameToSearch)
    }
}