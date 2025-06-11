package com.cronosdev.taskmanagerapp.ui.screens.art.cinema
//
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseListModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseResultListModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseResultListModel.Companion.filterResultList
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseResultListModel.Companion.forEach
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.GenericApiMessageResponse
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.DatesModel
import com.cronosdev.taskmanagerapp.data.model.art.CinemaModel
import com.cronosdev.taskmanagerapp.data.model.courrutines.CoroutineCaller
import com.cronosdev.taskmanagerapp.data.repositories.art.CinemaRepository
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

/** Instancia del constructor ViewModel que lmacena los datos que la interfaz de usuario necesita mostrar y se encarga de preparar y transformar datos antes de exponerlos a la vista.
 * @author Juan Gilabert Lopez
 * @constructor Crea una instancia de los repositorios necesarios para el modelo de la vista.
 * @param cinemaRepository Objeto de tipo 'CinemaRepository' el cual es una inyeccion de dependencias del propio repositorio.
 * Cualquier cambio en su valor sera notificado automáticamente a las vistas que lo están observando
 * para que las vistas puedan observar los cambios, pero no modificar el estado directamente.
 * @property _cinemaScreenUiState MutableStateFlow es una clase de Kotlin utilizada para mantener y observar un estado reactivo.
 * @property cinemaScreenUiState Estado de la vista gestionado mediante un StateFlow, una versión pública y de solo lectura de la variable [_cinemaScreenUiState]
 * @property
 * @property _exceptionHandler Manejo de excepciones para las coroutines.
 * @property _availableCinemasList: MutableStateOf de tipo 'RoomModel' que contiene la lista de salas disponibles para poder reservar.
 * @property _showSearchedCinemas: Boolean mutable cuyo contenido indica si hay que mostrar o no las salas buscadas por el usuario.
 * @property _cinemaNameToSearch: String mutable que contiene el nombre de ¿?que se pretende buscar.
 * @property _matchCinemasList: ApiResponseListModel mutable de tipo '' que contiene la lista de salas encontradas en base al nombre indicado en el buscador de salas por nombre.
 * @see CinemaRepository
 * @since 1.0
 * @throws _exceptionHandler En caso de fallo en la carga de datos.
 */
@HiltViewModel
class CinemaViewModel @Inject constructor(private val cinemaRepository: CinemaRepository): ViewModel()  {
    private val _dayTimeFormaterEEUU = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dayTimeFormaterEEUU: DateTimeFormatter get() = _dayTimeFormaterEEUU
    // Formateador de fechas al castellano solo para la visualizacion de los datos.
    val dayTimeFormaterES = SimpleDateFormat("dd MMMM yyyy", Locale("es", "ES"))
    //
    private val _cinemaScreenUiState = MutableStateFlow<UiState?>(null)
    val cinemaScreenUiState: StateFlow<UiState?> = _cinemaScreenUiState
    //
    private val _editCinemaScreenUiState = MutableStateFlow<UiState?>(null)
    val editCinemaScreenUiState: StateFlow<UiState?> = _editCinemaScreenUiState
    //
    private val _createCinemaScreenUiState = MutableStateFlow<UiState?>(null)
    val createCinemaScreenUiState: StateFlow<UiState?> = _createCinemaScreenUiState
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
        if (screenCaller == Destinations.ART_CINEMA_SCREEN_URL) fetchCinemaScreenUIState(UiState.Error("throwableErrorType", screenCaller, methodCaller, errorMessage))
        if (screenCaller == Destinations.ART_CREATE_CINEMA_SCREEN_URL) fetchCreateCinemaScreenUIState(
            UiState.Error("throwableErrorType", screenCaller, methodCaller, errorMessage))
        if (screenCaller == Destinations.ART_EDIT_CINEMA_SCREEN_URL) fetchEditCinemaScreenUIState(
            UiState.Error("throwableErrorType", screenCaller, methodCaller, errorMessage))
    }
    /** Funciones utilizadas para actualizar los estados de cada una de las UI que dependen de este viewModel.
     * @param state Nuevo estado de la UI.
     */
    private fun fetchCinemaScreenUIState(state: UiState) { _cinemaScreenUiState.value = state }
    private fun fetchEditCinemaScreenUIState(state: UiState) { _editCinemaScreenUiState.value = state }
    private fun fetchCreateCinemaScreenUIState(state: UiState) { _createCinemaScreenUiState.value = state }
    private var _responseMessage by mutableStateOf("")
    //// GETTERS & SETTERS
    private var _id by mutableStateOf("")
    val id: String get() = _id
    //
    private var _nombrePelicula by mutableStateOf("")
    val nombrePelicula: String get() = _nombrePelicula
    fun setNombrePelicula(newCinemaName: String) { _nombrePelicula = newCinemaName }
    //
    private var _descripcionPelicula by mutableStateOf("")
    val descripcionPelicula: String get() = _descripcionPelicula
    fun setDescripcionPelicula(newCinemaDescription: String) { _descripcionPelicula = newCinemaDescription }
    //
    private var _actoresPelicula by mutableStateOf(listOf<String>())
    val actoresPelicula: List<String> get() = _actoresPelicula
    fun setActoresPelicula(newData: List<String>) { _actoresPelicula = newData }
    //
    private var _fechaInicioPelicula by mutableStateOf("Ninguna fecha seleccionada")
    val fechaInicioPelicula: String get() = _fechaInicioPelicula
    fun setFechaInicioPelicula(newDate: String) { _fechaInicioPelicula = newDate }
    var fechaInicioPeliculaES by mutableStateOf("")
    //
    private var _horaInicioPelicula by mutableStateOf("Ninguna hora seleccionada")
    val horaInicioPelicula: String get() = _horaInicioPelicula
    fun setHoraInicioPelicula(newHourDate: String) { _horaInicioPelicula = newHourDate }
    //
    private var _duracionPeliculaMinutos by mutableIntStateOf(1)
    val duracionPeliculaMinutos: Int get() = _duracionPeliculaMinutos
    fun setDuracionPeliculaMinutos(newCinemaDuration: Int) { _duracionPeliculaMinutos = newCinemaDuration }
    //
    private var _lugarPelicula by mutableStateOf("")
    val lugarPelicula: String get() = _lugarPelicula
    fun setLugarPelicula(newLocation: String) { _lugarPelicula = newLocation }
    //
    private var _precioEntradaPelicula by mutableFloatStateOf(1F)
    val precioEntradaPelicula: Float get() = _precioEntradaPelicula
    fun setPrecioEntradaPelicula(newPrice: Float) { _precioEntradaPelicula = newPrice }
    //
    private var _notasPelicula by mutableStateOf("")
    val notasPelicula: String get() = _notasPelicula
    fun setNotasPelicula(newNotes: String) { _notasPelicula = newNotes }
    //
    private fun _setCinemaVariables() {_id = "";
        setNombrePelicula("");setDescripcionPelicula("");setActoresPelicula(emptyList());setFechaInicioPelicula("Ninguna fecha seleccionada");
        setHoraInicioPelicula("Ninguna hora seleccionada");setDuracionPeliculaMinutos(1);setLugarPelicula("");setPrecioEntradaPelicula(1F);
        setNotasPelicula("");
    }
    //
    fun fetchSelectedCinemaData(cinemaId: String) {
        _availableCinemasList.filterResultList { it._id == cinemaId }.forEach {
            _id = it._id; _nombrePelicula = it.nombrePelicula;
            _descripcionPelicula = it.descripcionPelicula; _actoresPelicula = it.actoresPelicula;
            _fechaInicioPelicula = it.fechaInicioPelicula.substring(0, 10)/*2025-01-01 sin T*/;
            _horaInicioPelicula = it.fechaInicioPelicula.substring(11, 19);/*18:30:00 sin .000Z*/
            fechaInicioPeliculaES = dateTimeFormatterES(_fechaInicioPelicula).toString();
            _duracionPeliculaMinutos = it.duracionPeliculaMinutos; _lugarPelicula = it.lugarPelicula
            _precioEntradaPelicula = it.precioEntradaPelicula; _notasPelicula = it.notasPelicula;
        }
    }
    //
    private fun createCinemaRequest(): CinemaModel {
        return CinemaModel(_id = "", _nombrePelicula, _descripcionPelicula, _actoresPelicula,
            "${_fechaInicioPelicula}T${_horaInicioPelicula}",
            _duracionPeliculaMinutos, _lugarPelicula, _precioEntradaPelicula,
            _notasPelicula
        )
    }
    //// METHODS OF THE VIEW MODEL
    // GET ALL
    private var _availableCinemasList by mutableStateOf(ApiResponseResultListModel<CinemaModel>(data = null, message = "", code = 0))
    val availableCinemasList: ApiResponseResultListModel<CinemaModel> get() = _availableCinemasList
    /** Funcion que carga en la variable '_availableCinemasList' los datos, es decir ¿¿?? disponibles y actualiza el estado de la vista a traves de la variable '_uiState'.
     * Lanza una coroutine en el viewModelScope, asegurando la cancelación automática si el ViewModel es destruido evitando fugas de memoria o llamadas innecesarias.
     */
    fun fetchAvailableCinemas(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchAvailableCinemas")) {
            fetchCinemaScreenUIState(UiState.Loading)
            cinemaRepository.getAllCinemas().let { _availableCinemasList = it }
            _responseMessage = _unavailableCinemaDatesList.message
            if (_availableCinemasList.code != 200) fetchCinemaScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "fetchAvailableCinemas", _responseMessage)
            )
            else {
                _setCinemaVariables()
                fetchCinemaScreenUIState(
                    UiState.Success("screenInit", screenCallName, "fetchAvailableCinemas", _responseMessage)
                )
            }
        }
    }
    //// GET UNAVAILABLE-DATES
    private var _unavailableCinemaDatesList by mutableStateOf(ApiResponseListModel<DatesModel>(data = null, message = "", code = 0))
    val unavailableCinemaDatesList: ApiResponseListModel<DatesModel> get() = _unavailableCinemaDatesList
    fun fetchUnavailableDates(screenCallName: String = "Unknown Screen Caller") {
        if (screenCallName == Destinations.ART_CREATE_CINEMA_SCREEN_URL) {
            viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchUnavailableDates")) {
                fetchCreateCinemaScreenUIState(UiState.Loading)
                cinemaRepository.getUnavailableCinemasDates().let{ _unavailableCinemaDatesList = it }
                _responseMessage = _unavailableCinemaDatesList.message
                if (_unavailableCinemaDatesList.code != 200) fetchCreateCinemaScreenUIState(
                    UiState.Error("fetchDataErrorType", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
                else fetchCreateCinemaScreenUIState(
                    UiState.Success("screenInit", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
            }
        }
        if (screenCallName == "artEditCinemaScreen") {
            viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchUnavailableDates")) {
                fetchEditCinemaScreenUIState(UiState.Loading)
                cinemaRepository.getUnavailableCinemasDates().let{ _unavailableCinemaDatesList = it }
                _responseMessage = _unavailableCinemaDatesList.message
                if (_unavailableCinemaDatesList.code != 200) fetchEditCinemaScreenUIState(
                    UiState.Error("fetchDataErrorType", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
                else fetchEditCinemaScreenUIState(
                    UiState.Success("screenInit", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
            }
        }
    }
    //// GET AVAILABLE-DATES-ON-DAY-BY-DATE
    private var _availableCinemaTasksDatesByDateList by mutableStateOf(ApiResponseListModel<DatesModel>(data = null, message = "", code = 0))
    val availableCinemaTasksDatesByDateList: ApiResponseListModel<DatesModel> get() = _availableCinemaTasksDatesByDateList
    fun fetchAvailableTasksDatesByDate(screenCallName: String = "Unknown Screen Caller") {
        var responseMessage: String
        if (screenCallName == "artCreateCinemaScreen") {
            viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchAvailableTasksDatesByDate")) {
                fetchCreateCinemaScreenUIState(UiState.Loading)
                cinemaRepository.getAvailableDatesOnDayByDate(_fechaInicioPelicula).let{ _availableCinemaTasksDatesByDateList = it }
                responseMessage = _availableCinemaTasksDatesByDateList.message
                if (_availableCinemaTasksDatesByDateList.code != 200) fetchCreateCinemaScreenUIState(
                    UiState.Error("fetchDataErrorType", responseMessage, "fetchAvailableTasksDatesByDate"))
                else fetchCreateCinemaScreenUIState(UiState.Success("screenRunning", _availableCinemaTasksDatesByDateList.message, "fetchAvailableTasksDatesByDate"))
            }
        }
        if (screenCallName == "artEditCinemaScreen") {
            viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchAvailableTasksDatesByDate")) {
                fetchEditCinemaScreenUIState(UiState.Loading)
                cinemaRepository.getAvailableDatesOnDayByDate(_fechaInicioPelicula).let{ _availableCinemaTasksDatesByDateList = it }
                responseMessage = _availableCinemaTasksDatesByDateList.message
                if (_availableCinemaTasksDatesByDateList.code != 200) fetchEditCinemaScreenUIState(
                    UiState.Error("fetchDataErrorType", responseMessage, "fetchAvailableTasksDatesByDate"))
                else fetchEditCinemaScreenUIState(UiState.Success("screenRunning", _availableCinemaTasksDatesByDateList.message, "fetchAvailableTasksDatesByDate"))
            }
        }
    }
    // POST
    private var _cinemaToCreateResponse by mutableStateOf(ApiResponseListModel<GenericApiMessageResponse>(data = null, message = "", code = 0))
    /** Crea una nuevo recurso y actualiza el estado de la UI.
     * Lanza una coroutine en el viewModelScope, asegurando la cancelación automática si el ViewModel es destruido evitando fugas de memoria o llamadas innecesarias.
     */
    fun createCinema(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "createCinema")) {
            fetchCreateCinemaScreenUIState(UiState.Loading)
            cinemaRepository.postCinema(createCinemaRequest()).let { _cinemaToCreateResponse = it }
            _responseMessage = _cinemaToCreateResponse.message
            if (_cinemaToCreateResponse.code != 201) fetchCreateCinemaScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "createCinema", _responseMessage)
            )
            else {
                _setCinemaVariables()
                fetchCreateCinemaScreenUIState(
                    UiState.Success("screenRunning", screenCallName, "createCinema", _responseMessage)
                )
            }
        }
    }
    // PUT
    private var _cinemaToPutResponse by mutableStateOf(ApiResponseListModel<GenericApiMessageResponse>(data = null, message = "", code = 0))
    /** Actualiza un recurso existente y actualiza el estado de la UI.
     * Lanza una coroutine en el viewModelScope, asegurando la cancelación automática si el ViewModel es destruido evitando fugas de memoria o llamadas innecesarias.
     */
    fun putCinema(screenCallName: String = "Unknown Screen Caller"){
        viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "putCinema")) {
            fetchEditCinemaScreenUIState(UiState.Loading)
            cinemaRepository.putCinema(_id, createCinemaRequest()).let { _cinemaToPutResponse = it }
            _responseMessage = _cinemaToPutResponse.message
            if (_cinemaToPutResponse.code != 200) fetchEditCinemaScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "putCinema", _responseMessage)
            )
            else {
                _setCinemaVariables()
                fetchEditCinemaScreenUIState(
                    UiState.Success("screenRunning", screenCallName, "putCinema", _responseMessage)
                )
            }
        }
    }
    // DELETE
    private var _cinemaToDelete by mutableStateOf(ApiResponseListModel<GenericApiMessageResponse>(data = null, message = "", code = 0))
    /** Elimina un recurso y actualiza el estado de la UI.
     * Lanza una coroutine en el viewModelScope, asegurando la cancelación automática si el ViewModel es destruido evitando fugas de memoria o llamadas innecesarias.
     */
    fun deleteCinema(screenCallName: String = "Unknown Screen Caller"){
        viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "deleteCinema")) {
            fetchEditCinemaScreenUIState(UiState.Loading)
            cinemaRepository.deleteCinema(_id).let { _cinemaToDelete = it }
            _responseMessage = _cinemaToDelete.message
            if (_cinemaToDelete.code != 204) fetchEditCinemaScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "deleteCinema", _responseMessage)
            )
            else {
                _setCinemaVariables()
                fetchEditCinemaScreenUIState(
                    UiState.Success("screenRunning", screenCallName, "deleteCinema", _responseMessage)
                )
            }
        }
    }
    //// CINEMA SEARCHER
    private var _showSearchedCinemas by  mutableStateOf(false)
    val showSearchedCinemas: Boolean get() = _showSearchedCinemas
    private var _cinemaNameToSearch by mutableStateOf("")
    private var _matchCinemasList by mutableStateOf(ApiResponseResultListModel<CinemaModel>(data = null, message = "", code = 0))
    val matchRoomList: ApiResponseResultListModel<CinemaModel> get() = _matchCinemasList
    /** Función que busca salas en base al nombre recibido por parametro.
     * @param cinemaNameToSearch Nombre de la sala recibido desde el buscador.
     */
    fun fetchCinemaToSearch(cinemaNameToSearch: String) {
        // Guardamos el nombre de la pelicula a buscar
        _cinemaNameToSearch = cinemaNameToSearch
        // Guardamos los objetos de tipo '' para mostrar las salas que coincidan con el nombre de la sala a buscar(_roomToSearch).
        _matchCinemasList = _availableCinemasList.filterResultList { it.nombrePelicula.contains(_cinemaNameToSearch, ignoreCase = true) }
        // La lista solo se mostrara en caso de haber una sala que buscar que coincida con el patron indicado.
        _showSearchedCinemas = Regex("[a-zA-Z0-9áéíóúÁÉÍÓÚüÜñÑ -]+").containsMatchIn(_cinemaNameToSearch)
    }
}