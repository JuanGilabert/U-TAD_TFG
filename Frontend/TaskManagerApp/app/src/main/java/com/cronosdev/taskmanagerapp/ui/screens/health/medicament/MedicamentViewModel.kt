package com.cronosdev.taskmanagerapp.ui.screens.health.medicament
//
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.cronosdev.taskmanagerapp.data.model.health.MedicamentModel
import com.cronosdev.taskmanagerapp.data.model.health.ViaAdministracionMedicamentoModel
import com.cronosdev.taskmanagerapp.data.repositories.health.MedicamentRepository
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
class MedicamentViewModel @Inject constructor(private val medicamentRepository: MedicamentRepository): ViewModel()  {
    private val _dayTimeFormaterEEUU = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dayTimeFormaterEEUU: DateTimeFormatter get() = _dayTimeFormaterEEUU
    // Formateador de fechas al castellano solo para la visualizacion de los datos.
    val dayTimeFormaterES = SimpleDateFormat("dd MMMM yyyy", Locale("es", "ES"))
    //
    private val _medicamentScreenUiState = MutableStateFlow<UiState?>(null)
    val medicamentScreenUiState: StateFlow<UiState?> = _medicamentScreenUiState
    //
    private val _editMedicamentScreenUiState = MutableStateFlow<UiState?>(null)
    val editMedicamentScreenUiState: StateFlow<UiState?> = _editMedicamentScreenUiState
    //
    private val _createMedicamentScreenUiState = MutableStateFlow<UiState?>(null)
    val createMedicamentScreenUiState: StateFlow<UiState?> = _createMedicamentScreenUiState
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
        if (screenCaller == Destinations.HEALTH_MEDICAMENT_SCREEN_URL)
            fetchMedicamentScreenUIState(UiState.Error("throwableErrorType", screenCaller, methodCaller, errorMessage))
        if (screenCaller == Destinations.HEALTH_CREATE_MEDICAMENT_SCREEN_URL)
            fetchCreateMedicamentScreenUIState(UiState.Error("throwableErrorType", screenCaller, methodCaller, errorMessage))
        if (screenCaller == Destinations.HEALTH_EDIT_MEDICAMENT_SCREEN_URL)
            fetchEditMedicamentScreenUIState(UiState.Error("throwableErrorType", screenCaller, methodCaller, errorMessage))
    }
    /** Funciones utilizadas para actualizar los estados de cada una de las UI que dependen de este viewModel.
     * @param state Nuevo estado de la UI.
     */
    private fun fetchMedicamentScreenUIState(state: UiState) { _medicamentScreenUiState.value = state }
    private fun fetchEditMedicamentScreenUIState(state: UiState) { _editMedicamentScreenUiState.value = state }
    private fun fetchCreateMedicamentScreenUIState(state: UiState) { _createMedicamentScreenUiState.value = state }
    private var _responseMessage by mutableStateOf("")
    //// GETTERS & SETTERS
    private var _id by mutableStateOf("")
    val id: String get() = _id
    //
    private var _nombreMedicamento by mutableStateOf("")
    val nombreMedicamento: String get() = _nombreMedicamento
    fun setNombreMedicamento(newMedicamentName: String) { _nombreMedicamento = newMedicamentName }
    //
    private var _formaViaAdministracionMedicamento by mutableStateOf("")
    val formaViaAdministracionMedicamento: String get() = _formaViaAdministracionMedicamento
    fun setFormaViaAdministracionMedicamento(newData: String) { _formaViaAdministracionMedicamento = newData }

    private var _tipoViaAdministracionMedicamento by mutableStateOf("")
    val tipoViaAdministracionMedicamento: String get() = _tipoViaAdministracionMedicamento
    fun setTipoViaAdministracionMedicamento(newData: String) { _tipoViaAdministracionMedicamento = newData }
    //
    private var _cantidadTotalCajaMedicamento by mutableIntStateOf(1)
    val cantidadTotalCajaMedicamento: Int get() = _cantidadTotalCajaMedicamento
    fun setCantidadTotalCajaMedicamento(newQuantity: Int) { _cantidadTotalCajaMedicamento = newQuantity }
    //
    private var _fechaCaducidadMedicamento by mutableStateOf("Ninguna fecha seleccionada")
    val fechaCaducidadMedicamento: String get() = _fechaCaducidadMedicamento
    fun setFechaCaducidadMedicamento(newStartDate: String) {
        _fechaCaducidadMedicamento = newStartDate
        //fetchMedicamentExpirationDatesByDate("healthCreateMedicamentScreen")
    }
    var fechaCaducidadMedicamentoES by mutableStateOf("")
    //
    private var _horaCaducidadMedicamento by mutableStateOf("Ninguna hora seleccionada")
    val horaCaducidadMedicamento: String get() = _horaCaducidadMedicamento
    fun setHoraCaducidadMedicamento(newHourDate: String) { _horaCaducidadMedicamento = newHourDate }
    //
    private var _notasMedicamento by mutableStateOf("")
    val notasMedicamento: String get() = _notasMedicamento
    fun setNotasMedicamento(newNotes: String) { _notasMedicamento = newNotes }
    //
    private fun _setMedicamentVariables() {_id = "";
        setNombreMedicamento(""); setFormaViaAdministracionMedicamento("");
        setTipoViaAdministracionMedicamento(""); setCantidadTotalCajaMedicamento(1);
        setFechaCaducidadMedicamento("Ninguna fecha seleccionada"); fechaCaducidadMedicamentoES = "";
        setHoraCaducidadMedicamento("Ninguna hora seleccionada"); setNotasMedicamento("")
    }
    //
    fun fetchSelectedMedicamentData(medicametnId: String) {
        _availableMedicamentsList.filterResultList { it._id == medicametnId }.forEach {
            _id = it._id; _nombreMedicamento = it.nombreMedicamento;
            _formaViaAdministracionMedicamento = it.viaAdministracionMedicamento.forma;
            _tipoViaAdministracionMedicamento = it.viaAdministracionMedicamento.tipo;
            _fechaCaducidadMedicamento = it.fechaCaducidadMedicamento.substring(0, 10);
            _horaCaducidadMedicamento = it.fechaCaducidadMedicamento.substring(11, 19);
            fechaCaducidadMedicamentoES = dateTimeFormatterES(_fechaCaducidadMedicamento).toString();
            _cantidadTotalCajaMedicamento = it.cantidadTotalCajaMedicamento; _notasMedicamento = it.notasMedicamento;
        }
    }
    //
    private fun createMedicamentRequest(): MedicamentModel {
        return MedicamentModel(_id = "", _nombreMedicamento, ViaAdministracionMedicamentoModel(_formaViaAdministracionMedicamento,_tipoViaAdministracionMedicamento),
            _cantidadTotalCajaMedicamento, "${_fechaCaducidadMedicamento}T${_horaCaducidadMedicamento}", _notasMedicamento
        )
    }
    //// METHODS OF THE VIEW MODEL
    // GET ALL
    private var _availableMedicamentsList by mutableStateOf(ApiResponseResultListModel<MedicamentModel>(data = null, message = "", code = 0))
    val availableMedicamentsList: ApiResponseResultListModel<MedicamentModel> get() = _availableMedicamentsList
    fun fetchAvailableMedicaments(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchAvailableMedicaments")) {
            fetchMedicamentScreenUIState(UiState.Loading)
            medicamentRepository.getAllMedicaments().let { _availableMedicamentsList = it }
            _responseMessage = _availableMedicamentsList.message
            if (_availableMedicamentsList.code != 200)
                fetchMedicamentScreenUIState(
                    UiState.Error("fetchDataErrorType", screenCallName, "fetchAvailableMedicaments", _responseMessage)
                )
            else {
                _setMedicamentVariables()
                fetchMedicamentScreenUIState(
                    UiState.Success("screenInit", screenCallName, "fetchAvailableMedicaments", _responseMessage)
                )
            }
        }
    }
    //// GET UNAVAILABLE-DATES
    private var _unavailableMedicamentDatesList by mutableStateOf(ApiResponseListModel<DatesModel>(data = null, message = "", code = 0))
    val unavailableMedicamentDatesList: ApiResponseListModel<DatesModel> get() = _unavailableMedicamentDatesList
    fun fetchUnavailableDates(screenCallName: String = "Unknown Screen Caller") {
        if (screenCallName == "healthCreateMedicamentScreen") {
            viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchUnavailableDates")) {
                fetchCreateMedicamentScreenUIState(UiState.Loading)
                medicamentRepository.getUnavailableMedicamentsDates().let{ _unavailableMedicamentDatesList = it }
                _responseMessage = _unavailableMedicamentDatesList.message
                if (_unavailableMedicamentDatesList.code != 200) fetchCreateMedicamentScreenUIState(
                    UiState.Error("fetchDataErrorType", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
                else fetchCreateMedicamentScreenUIState(
                    UiState.Success("screenInit", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
            }
        }
        if (screenCallName == "healthEditMedicamentScreen") {
            viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchUnavailableDates")) {
                fetchEditMedicamentScreenUIState(UiState.Loading)
                medicamentRepository.getUnavailableMedicamentsDates().let{ _unavailableMedicamentDatesList = it }
                _responseMessage = _unavailableMedicamentDatesList.message
                if (_unavailableMedicamentDatesList.code != 200) fetchEditMedicamentScreenUIState(
                    UiState.Error("fetchDataErrorType", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
                else fetchEditMedicamentScreenUIState(
                    UiState.Success("screenInit", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
            }
        }
    }
    // GET
    private var _medicamentExpirationDatesList by mutableStateOf(ApiResponseListModel<DatesModel>(data = null, message = "", code = 0))
    val medicamentExpirationDatesList: ApiResponseListModel<DatesModel> get() = _medicamentExpirationDatesList
    fun fetchMedicamentExpirationDatesByDate(screenCallName: String) {
        var responseMessage: String
        if (screenCallName == "healthCreateMedicamentScreen") {
            viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchMedicamentExpirationDatesByDate")) {
                fetchCreateMedicamentScreenUIState(UiState.Loading)
                medicamentRepository.getMedicamentExpirationDatesByDate(_fechaCaducidadMedicamento).let{ _medicamentExpirationDatesList = it }
                responseMessage = _medicamentExpirationDatesList.message
                if (_medicamentExpirationDatesList.code != 200) fetchCreateMedicamentScreenUIState(
                    UiState.Error(responseMessage, "fetchMedicamentExpirationDatesByDate"))
                else fetchCreateMedicamentScreenUIState(UiState.Success("screenRunning", responseMessage, "fetchMedicamentExpirationDatesByDate"))
            }
        }
        if (screenCallName == "healthEditMedicamentScreen") {
            viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchMedicamentExpirationDatesByDate")) {
                fetchEditMedicamentScreenUIState(UiState.Loading)
                medicamentRepository.getMedicamentExpirationDatesByDate(_fechaCaducidadMedicamento).let{ _medicamentExpirationDatesList = it }
                responseMessage = _medicamentExpirationDatesList.message
                if (_medicamentExpirationDatesList.code != 200) fetchEditMedicamentScreenUIState(
                    UiState.Error(responseMessage, "fetchMedicamentExpirationDatesByDate"))
                else fetchEditMedicamentScreenUIState(UiState.Success("screenRunning", responseMessage, "fetchMedicamentExpirationDatesByDate"))
            }
        }
    }
    // POST
    private var _medicamentToCreateResponse by mutableStateOf(ApiResponseListModel<GenericApiMessageResponse>(data = null, message = "", code = 0))
    /** Crea una nueva cita y actualiza el estado de la UI.
     * Lanza una coroutine en el viewModelScope, asegurando la cancelación automática si el ViewModel es destruido evitando fugas de memoria o llamadas innecesarias.
     */
    fun createMedicament(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler) {
            fetchCreateMedicamentScreenUIState(UiState.Loading)
            medicamentRepository.postMedicament(createMedicamentRequest()).let { _medicamentToCreateResponse = it }
            _responseMessage = _medicamentToCreateResponse.message
            if (_medicamentToCreateResponse.code != 201) fetchCreateMedicamentScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "createMedicament", _responseMessage)
            )
            else {
                _setMedicamentVariables()
                fetchCreateMedicamentScreenUIState(
                    UiState.Success("screenRunning", screenCallName, "createMedicament", _responseMessage)
                )
            }
        }
    }
    // PUT
    private var _medicamentToPutResponse by mutableStateOf(ApiResponseListModel<GenericApiMessageResponse>(data = null, message = "", code = 0))
    /** Actualiza una reserva existente y actualiza el estado de la UI.
     * Lanza una coroutine en el viewModelScope, asegurando la cancelación automática si el ViewModel es destruido evitando fugas de memoria o llamadas innecesarias.
     */
    fun putMedicament(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler) {
            fetchEditMedicamentScreenUIState(UiState.Loading)
            medicamentRepository.putMedicament(_id, createMedicamentRequest()).let { _medicamentToPutResponse = it }
            _responseMessage = _medicamentToPutResponse.message
            if (_medicamentToPutResponse.code != 200) fetchEditMedicamentScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "putMedicament", _responseMessage)
            )
            else {
                _setMedicamentVariables()
                fetchEditMedicamentScreenUIState(
                    UiState.Success("screenRunning", screenCallName, "putMedicament", _responseMessage)
                )
            }
        }
    }
    // DELETE
    private var _musicToDeleteResponse by mutableStateOf(ApiResponseListModel<GenericApiMessageResponse>(data = null, message = "", code = 0))
    /** Elimina una reserva existente y actualiza el estado de la UI.
     * Lanza una coroutine en el viewModelScope, asegurando la cancelación automática si el ViewModel es destruido evitando fugas de memoria o llamadas innecesarias.
     */
    fun deleteMedicament(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler) {
            fetchEditMedicamentScreenUIState(UiState.Loading)
            medicamentRepository.deleteMedicament(_id).let { _musicToDeleteResponse = it }
            _responseMessage = _musicToDeleteResponse.message
            if (_musicToDeleteResponse.code != 204) fetchEditMedicamentScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "deleteMedicament", _responseMessage)
            )
            else {
                _setMedicamentVariables()
                fetchEditMedicamentScreenUIState(
                    UiState.Success("screenRunning", screenCallName, "deleteMedicament", _responseMessage)
                )
            }
        }
    }
    //// MEDICAMENT  SEARCHER
    private var _showSearchedMedicaments by  mutableStateOf(false)
    val showSearchedMedicaments: Boolean get() = _showSearchedMedicaments
    private var _medicamentNameToSearch by mutableStateOf("")
    private var _matchMedicamentsList by mutableStateOf(ApiResponseResultListModel<MedicamentModel>(data = null, message = "", code = 0))
    val matchMedicamentsList: ApiResponseResultListModel<MedicamentModel> get() = _matchMedicamentsList
    /** Función que busca salas en base al nombre recibido por parametro.
     * @param cinemaNameToSearch Nombre de la sala recibido desde el buscador.
     */
    fun fetchMedicamentToSearch(cinemaNameToSearch: String) {
        // Guardamos el nombre de la pelicula a buscar
        _medicamentNameToSearch = cinemaNameToSearch
        // Guardamos los objetos de tipo '' para mostrar las salas que coincidan con el nombre de la sala a buscar(_roomToSearch).
        _matchMedicamentsList = _availableMedicamentsList.filterResultList { it.nombreMedicamento.contains(_medicamentNameToSearch, ignoreCase = true) }
        // La lista solo se mostrara en caso de haber una sala que buscar que coincida con el patron indicado.
        _showSearchedMedicaments = Regex("[a-zA-Z0-9áéíóúÁÉÍÓÚüÜñÑ -]+").containsMatchIn(_medicamentNameToSearch)
    }
}