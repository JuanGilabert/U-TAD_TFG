package com.cronosdev.taskmanagerapp.ui.screens.work

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
import com.cronosdev.taskmanagerapp.data.model.work.WorkModel
import com.cronosdev.taskmanagerapp.data.repositories.work.WorkRepository
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
class WorkViewModel @Inject constructor(private val workRepository: WorkRepository): ViewModel()  {
    private val _dayTimeFormaterEEUU = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dayTimeFormaterEEUU: DateTimeFormatter get() = _dayTimeFormaterEEUU
    // Formateador de fechas al castellano solo para la visualizacion de los datos.
    val dayTimeFormaterES = SimpleDateFormat("dd MMMM yyyy", Locale("es", "ES"))
    //
    private val _workScreenUiState = MutableStateFlow<UiState?>(null)
    val workScreenUiState: StateFlow<UiState?> = _workScreenUiState
    //
    private val _editWorkScreenUiState = MutableStateFlow<UiState?>(null)
    val editWorkScreenUiState: StateFlow<UiState?> = _editWorkScreenUiState
    //
    private val _createWorkScreenUiState = MutableStateFlow<UiState?>(null)
    val createWorkScreenUiState: StateFlow<UiState?> = _createWorkScreenUiState
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
        if (screenCaller == Destinations.WORK_SCREEN_URL) fetchWorkScreenUIState(UiState.Error("throwableErrorType", screenCaller, methodCaller, errorMessage))
        if (screenCaller == Destinations.CREATE_WORK_SCREEN_URL) fetchCreateWorkScreenUIState(
            UiState.Error("throwableErrorType", screenCaller, methodCaller, errorMessage))
        if (screenCaller == Destinations.EDIT_WORK_SCREEN_URL) fetchEditWorkScreenUIState(UiState.Error("throwableErrorType", screenCaller, methodCaller, errorMessage))
    }
    /** Funciones utilizadas para actualizar los estados de cada una de las UI que dependen de este viewModel.
     * @param state Nuevo estado de la UI.
     */
    private fun fetchWorkScreenUIState(state: UiState) { _workScreenUiState.value = state }
    private fun fetchCreateWorkScreenUIState(state: UiState) { _createWorkScreenUiState.value = state }
    private fun fetchEditWorkScreenUIState(state: UiState) { _editWorkScreenUiState.value = state }
    private var _responseMessage by mutableStateOf("")
    //// GETTERS & SETTERS
    private var _id by mutableStateOf("")
    val id: String get() = _id
    //
    private var _tituloTarea by mutableStateOf("")
    val tituloTarea: String get() = _tituloTarea
    fun setTituloTarea(newTitle: String) { _tituloTarea = newTitle }
    //
    private var _descripcionTarea by mutableStateOf("")
    val descripcionTarea: String get() = _descripcionTarea
    fun setDescripcionTarea(newDescription: String) { _descripcionTarea = newDescription }
    //
    private var _fechaInicioTarea by mutableStateOf("Ninguna fecha seleccionada")
    val fechaInicioTarea: String get() = _fechaInicioTarea
    fun setFechaInicioTarea(newDate: String) { _fechaInicioTarea = newDate }
    var fechaInicioTareaES by mutableStateOf("")
    //
    private var _horaInicioTarea by mutableStateOf("Ninguna hora seleccionada")
    val horaInicioTarea: String get() = _horaInicioTarea
    fun setHoraInicioTarea(newDate: String) { _horaInicioTarea = newDate }
    //
    private var _fechaEntregaTarea by mutableStateOf("Ninguna fecha seleccionada")
    val fechaEntregaTarea: String get() = _fechaEntregaTarea
    fun setFechaEntregaTarea(newDate: String) { _fechaEntregaTarea = newDate }
    var fechaEntregaTareaES by mutableStateOf("")
    //
    private var _horaEntregaTarea by mutableStateOf("Ninguna hora seleccionada")
    val horaEntregaTarea: String get() = _horaEntregaTarea
    fun setHoraEntregaTarea(newDate: String) { _horaEntregaTarea = newDate }
    //
    private var _organizadorTarea by mutableStateOf("")
    val organizadorTarea: String get() = _organizadorTarea
    fun setOrganizadorTarea(newOrganizator: String) { _organizadorTarea = newOrganizator }
    //
    private var _prioridadTarea by mutableStateOf("")
    val prioridadTarea: String get() = _prioridadTarea
    fun setPrioridadTarea(newPriority: String) { _prioridadTarea = newPriority }
    //
    private var _notasTarea by mutableStateOf("")
    val notasTarea: String get() = _notasTarea
    fun setNotasTarea(newNotes: String) { _notasTarea = newNotes }
    //
    private fun _setWorkVariables() { _id = ""; setTituloTarea(""); setDescripcionTarea("");
        setFechaInicioTarea("Ninguna fecha seleccionada"); fechaInicioTareaES = "";
        setHoraInicioTarea("Ninguna hora seleccionada"); setFechaEntregaTarea("Ninguna fecha seleccionada");
        fechaEntregaTareaES = ""; setHoraEntregaTarea("Ninguna hora seleccionada");
        setOrganizadorTarea(""); setPrioridadTarea(""); setNotasTarea("");
    }
    /** Funcion encargada de obtener los datos de la reserva seleecionada para editar los datos de la misma o para eliminarla en el componente/vista 'editCinemaScreen'.
     * @param workId ID de la reserva.
     */
    fun fetchSelectedPaintingData(workId: String) {
        _availableWorkList.filterResultList { it._id == workId }.forEach {
            _id = it._id; _tituloTarea = it.tituloTarea; _descripcionTarea = it.descripcionTarea;
            _fechaInicioTarea = it.fechaInicioTarea.substring(0, 10);
            _horaInicioTarea = it.fechaInicioTarea.substring(11, 19);
            fechaInicioTareaES = dateTimeFormatterES(_fechaInicioTarea).toString();
            _fechaEntregaTarea = it.fechaEntregaTarea.substring(0, 10);
            _horaEntregaTarea = it.fechaEntregaTarea.substring(11, 19);
            fechaEntregaTareaES = dateTimeFormatterES(_fechaEntregaTarea).toString();
            _organizadorTarea = it.organizadorTarea; _prioridadTarea = it.prioridadTarea;
            _notasTarea = it.notasTarea;
        }
    }
    //
    private fun createWorkRequest(): WorkModel {
        return WorkModel(_id = "", _tituloTarea, _descripcionTarea,
            "${_fechaInicioTarea}T${_horaInicioTarea}",
            "${_fechaEntregaTarea}T${_horaEntregaTarea}",
            _organizadorTarea, _prioridadTarea, _notasTarea
        )
    }
    ////
    // GET ALL
    private var _availableWorkList by mutableStateOf(ApiResponseResultListModel<WorkModel>(data = null, message = "", code = 0))
    val availableWorkList: ApiResponseResultListModel<WorkModel> get() = _availableWorkList
    fun fetchAvailableWorks(screenCallName: String = "Unknown Screen Caller", methodName: String = "") {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchAvailableWorks")) {
            fetchWorkScreenUIState(UiState.Loading)
            workRepository.getAllWorks().let { _availableWorkList = it }
            _responseMessage = _availableWorkList.message
            if (_availableWorkList.code != 200) fetchWorkScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "fetchAvailableWorks", _responseMessage)
            )
            else {
                _setWorkVariables()
                fetchWorkScreenUIState(
                    UiState.Success("screenInit", screenCallName, "fetchAvailableWorks", _responseMessage)
                )
            }
        }
    }
    // GET UNAVAILABLE-DATES
    private var _unavailableWorkDatesList by mutableStateOf(ApiResponseListModel<DatesModel>(data = null, message = "", code = 0))
    val unavailableWorkDatesList: ApiResponseListModel<DatesModel> get() = _unavailableWorkDatesList
    fun fetchUnavailableDates(screenCallName: String = "Unknown Screen Caller", methodName: String = "") {
        if (screenCallName == "createWorkScreen") {
            viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchUnavailableDates")) {
                fetchCreateWorkScreenUIState(UiState.Loading)
                workRepository.getUnavailableWorksDates().let{ _unavailableWorkDatesList = it }
                _responseMessage = _unavailableWorkDatesList.message
                if (_unavailableWorkDatesList.code != 200) fetchCreateWorkScreenUIState(
                    UiState.Error("fetchDataErrorType", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
                else fetchCreateWorkScreenUIState(
                    UiState.Success("screenInit", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
            }
        }
        if (screenCallName == "editWorkScreen") {
            viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchUnavailableDates")) {
                fetchEditWorkScreenUIState(UiState.Loading)
                workRepository.getUnavailableWorksDates().let{ _unavailableWorkDatesList = it }
                _responseMessage = _unavailableWorkDatesList.message
                if (_unavailableWorkDatesList.code != 200) fetchEditWorkScreenUIState(
                    UiState.Error("fetchDataErrorType", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
                else fetchEditWorkScreenUIState(
                    UiState.Success("screenInit", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
            }
        }
    }
    // POST
    private var _workToCreateResponse by mutableStateOf(ApiResponseListModel<GenericApiMessageResponse>(data = null, message = "", code = 0))
    fun createWork(screenCallName: String = "Unknown Screen Caller", methodName: String = "createWork") {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, methodName)) {
            fetchCreateWorkScreenUIState(UiState.Loading)
            workRepository.postWork(createWorkRequest()).let { _workToCreateResponse = it }
            _responseMessage = _workToCreateResponse.message
            if (_workToCreateResponse.code != 201) fetchCreateWorkScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, methodName, _responseMessage)
            )
            else {
                _setWorkVariables()
                fetchCreateWorkScreenUIState(
                    UiState.Success("screenRunning", screenCallName, methodName, _responseMessage)
                )
            }
        }
    }
    // PUT
    private var _workToPutResponse by mutableStateOf(ApiResponseListModel<GenericApiMessageResponse>(data = null, message = "", code = 0))
    /** Actualiza una reserva existente y actualiza el estado de la UI.
     * Lanza una coroutine en el viewModelScope, asegurando la cancelación automática si el ViewModel es destruido evitando fugas de memoria o llamadas innecesarias.
     */
    fun putWork(screenCallName: String = "Unknown Screen Caller", methodName: String = "putWork") {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, methodName)) {
            fetchEditWorkScreenUIState(UiState.Loading)
            workRepository.putWork(_id, createWorkRequest()).let { _workToPutResponse = it }
            _responseMessage = _workToPutResponse.message
            if (_workToPutResponse.code != 200) fetchEditWorkScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, methodName, _responseMessage)
            )
            else {
                _setWorkVariables()
                fetchEditWorkScreenUIState(
                    UiState.Success("screenRunning", screenCallName, methodName, _responseMessage)
                )
            }
        }
    }
    // DELETE
    private var _workToDeleteResponse by mutableStateOf(ApiResponseListModel<GenericApiMessageResponse>(data = null, message = "", code = 0))
    /** Elimina una reserva existente y actualiza el estado de la UI.
     * Lanza una coroutine en el viewModelScope, asegurando la cancelación automática si el ViewModel es destruido evitando fugas de memoria o llamadas innecesarias.
     */
    fun deleteWork(screenCallName: String = "Unknown Screen Caller", methodName: String = "deleteWork") {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, methodName)) {
            fetchEditWorkScreenUIState(UiState.Loading)
            workRepository.deleteWork(_id).let { _workToDeleteResponse = it }
            _responseMessage = _workToDeleteResponse.message
            if (_workToDeleteResponse.code != 204) fetchEditWorkScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, methodName, _responseMessage)
            )
            else {
                _setWorkVariables()
                fetchEditWorkScreenUIState(
                    UiState.Success("screenRunning", screenCallName, methodName, _responseMessage)
                )
            }
        }
    }
    //// WORK SEARCHER
    private var _showSearchedWorks by  mutableStateOf(false)
    val showSearchedWorks: Boolean get() = _showSearchedWorks
    private var _workNameToSearch by mutableStateOf("")
    private var _matchWorksList by mutableStateOf(ApiResponseResultListModel<WorkModel>(data = null, message = "", code = 0))
    val matchWorksList: ApiResponseResultListModel<WorkModel> get() = _matchWorksList
    /** Función que busca salas en base al nombre recibido por parametro.
     * @param nameToSearch Nombre de la sala recibido desde el buscador.
     */
    fun fetchPaintingToSearch(nameToSearch: String) {
        // Guardamos el nombre de la pelicula a buscar
        _workNameToSearch = nameToSearch
        // Guardamos los objetos de tipo '' para mostrar las salas que coincidan con el nombre de la sala a buscar(_roomToSearch).
        _matchWorksList = _availableWorkList.filterResultList { it.tituloTarea.contains(_workNameToSearch, ignoreCase = true) }
        // La lista solo se mostrara en caso de haber una sala que buscar que coincida con el patron indicado.
        _showSearchedWorks = Regex("[a-zA-Z0-9áéíóúÁÉÍÓÚüÜñÑ -]+").containsMatchIn(_workNameToSearch)
    }
}