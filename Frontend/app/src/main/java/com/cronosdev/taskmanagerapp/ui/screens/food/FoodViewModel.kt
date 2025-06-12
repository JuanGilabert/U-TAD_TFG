package com.cronosdev.taskmanagerapp.ui.screens.food

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
import com.cronosdev.taskmanagerapp.data.model.food.FoodModel
import com.cronosdev.taskmanagerapp.data.repositories.food.FoodRepository
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
class FoodViewModel @Inject constructor(private val foodRepository: FoodRepository): ViewModel()  {
    private val _dayTimeFormaterEEUU = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dayTimeFormaterEEUU: DateTimeFormatter get() = _dayTimeFormaterEEUU
    // Formateador de fechas al castellano solo para la visualizacion de los datos.
    val dayTimeFormaterES = SimpleDateFormat("dd MMMM yyyy", Locale("es", "ES"))
    //
    private val _foodScreenUiState = MutableStateFlow<UiState?>(null)
    val foodScreenUiState: StateFlow<UiState?> = _foodScreenUiState
    //
    private val _updateFoodScreenUiState = MutableStateFlow<UiState?>(null)
    val updateFoodScreenUiState: StateFlow<UiState?> = _updateFoodScreenUiState
    //
    private val _createFoodScreenUiState = MutableStateFlow<UiState?>(null)
    val createFoodScreenUiState: StateFlow<UiState?> = _createFoodScreenUiState
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
        if (screenCaller == Destinations.FOOD_SCREEN_URL) fetchFoodScreenUIState(UiState.Error("throwableErrorType", screenCaller, methodCaller, errorMessage))
        if (screenCaller == Destinations.CREATE_FOOD_SCREEN_URL) fetchCreateFoodScreenUIState(
            UiState.Error("throwableErrorType", screenCaller, methodCaller, errorMessage))
        if (screenCaller == Destinations.UPDATE_FOOD_SCREEN_URL) fetchEditFoodScreenUIState(UiState.Error("throwableErrorType", screenCaller, methodCaller, errorMessage))
    }
    /** Funciones utilizadas para actualizar los estados de cada una de las UI que dependen de este viewModel.
     * @param state Nuevo estado de la UI.
     */
    private fun fetchFoodScreenUIState(state: UiState) { _foodScreenUiState.value = state }
    private fun fetchCreateFoodScreenUIState(state: UiState) { _createFoodScreenUiState.value = state }
    private fun fetchEditFoodScreenUIState(state: UiState) { _updateFoodScreenUiState.value = state }
    private var _responseMessage by mutableStateOf("")
    //// GETTERS & SETTERS
    private var _id by mutableStateOf("")
    val id: String get() = _id
    //
    private var _nombreRestaurante by mutableStateOf("")
    val nombreRestaurante: String get() = _nombreRestaurante
    fun setNombreRestaurante(newRestaurantName: String) { _nombreRestaurante = newRestaurantName }
    //
    private var _direccionRestaurante by mutableStateOf("")
    val direccionRestaurante: String get() = _direccionRestaurante
    fun setDireccionRestaurante(newRestaurantDirection: String) { _direccionRestaurante = newRestaurantDirection }
    //
    private var _tipoComida by mutableStateOf("")
    val tipoComida: String get() = _tipoComida
    fun setTipoComida(newFoodType: String) { _tipoComida = newFoodType }
    //
    private var _fechaReserva by mutableStateOf("Ninguna fecha seleccionada")
    val fechaReserva: String get() = _fechaReserva
    fun setFechaReserva(newDate: String) { _fechaReserva = newDate }
    var fechaReservaES by mutableStateOf("")
    //
    private var _horaReserva by mutableStateOf("Ninguna hora seleccionada")
    val horaReserva: String get() = _horaReserva
    fun setHoraReserva(newHour: String) { _horaReserva = newHour }
    //
    private var _asistentesReserva by mutableIntStateOf(1)
    val asistentesReserva: Int get() = _asistentesReserva
    fun setAsistentesReserva(newAssistants: Int) { _asistentesReserva = newAssistants }
    //
    private var _notasReserva by mutableStateOf("")
    val notasReserva: String get() = _notasReserva
    fun setNotasReserva(newNotes: String) { _notasReserva = newNotes }
    // Set all values
    fun setFoodVariables() {_id = ""
        setNombreRestaurante(""); setDireccionRestaurante(""); setTipoComida("");
        setFechaReserva("Ninguna fecha seleccionada"); fechaReservaES = "";
        setHoraReserva("Ninguna hora seleccionada"); setAsistentesReserva(1); setNotasReserva("");
    }
    // Fetch Food Data To Update On Screen
    fun fetchSelectedPaintingData(foodId: String) {
        _availableFoodsList.filterResultList { it._id == foodId }.forEach {
            _id = it._id; _nombreRestaurante = it.nombreRestaurante;
            _direccionRestaurante = it.direccionRestaurante; _tipoComida = it.tipoComida;
            _fechaReserva = it.fechaReserva.substring(0, 10);
            _horaReserva = it.fechaReserva.substring(11, 19);
            fechaReservaES = dateTimeFormatterES(_fechaReserva).toString();
            _asistentesReserva = it.asistentesReserva; _notasReserva = it.notasReserva;
        }
    }
    // Food Reuqest
    private fun createFoodRequest(): FoodModel {
        return FoodModel(
            _id = "", _nombreRestaurante, _direccionRestaurante,
            _tipoComida, "${_fechaReserva}T${_horaReserva}",
            _asistentesReserva, _notasReserva
        )
    }
    //// METHODS OF THE VIEW MODEL
    // GET ALL
    private var _availableFoodsList by mutableStateOf(ApiResponseResultListModel<FoodModel>(data = null, message = "", code = 0))
    val availableFoodsList: ApiResponseResultListModel<FoodModel> get() = _availableFoodsList
    fun fetchAvailableFoods(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchAvailableFoods")) {
            fetchFoodScreenUIState(UiState.Loading)
            foodRepository.getAllFoods().let { _availableFoodsList = it }
            _responseMessage = _availableFoodsList.message
            if (_availableFoodsList.code != 200) fetchFoodScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "fetchAvailableFoods", _responseMessage)
            )
            else {
                setFoodVariables()
                fetchFoodScreenUIState(
                    UiState.Success("screenInit", screenCallName, "fetchAvailableFoods", _responseMessage)
                )
            }
        }
    }
    // GET UNAVAILABLE-DATES
    private var _unavailableFoodDatesList by mutableStateOf(ApiResponseListModel<DatesModel>(data = null, message = "", code = 0))
    val unavailableFoodDatesList: ApiResponseListModel<DatesModel> get() = _unavailableFoodDatesList
    fun fetchUnavailableDates(screenCallName: String = "Unknown Screen Caller") {
        if (screenCallName == "createFoodScreen") {
            viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchUnavailableDates")) {
                fetchCreateFoodScreenUIState(UiState.Loading)
                foodRepository.getUnavailableFoodsDates().let{ _unavailableFoodDatesList = it }
                _responseMessage = _unavailableFoodDatesList.message
                if (_unavailableFoodDatesList.code != 200) fetchCreateFoodScreenUIState(
                    UiState.Error("fetchDataErrorType", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
                else fetchCreateFoodScreenUIState(
                    UiState.Success("screenInit", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
            }
        }
        if (screenCallName == "editFoodScreen") {
            viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "fetchUnavailableDates")) {
                fetchEditFoodScreenUIState(UiState.Loading)
                foodRepository.getUnavailableFoodsDates().let{ _unavailableFoodDatesList = it }
                _responseMessage = _unavailableFoodDatesList.message
                if (_unavailableFoodDatesList.code != 200) fetchEditFoodScreenUIState(
                    UiState.Error("fetchDataErrorType", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
                else fetchEditFoodScreenUIState(
                    UiState.Success("screenInit", screenCallName, "fetchUnavailableDates", _responseMessage)
                )
            }
        }
    }
    // POST
    private var _foodToCreateResponse by mutableStateOf(ApiResponseListModel<GenericApiMessageResponse>(data = null, message = "", code = 0))
    fun createFood(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "createFood")) {
            fetchCreateFoodScreenUIState(UiState.Loading)
            foodRepository.postFood(createFoodRequest()).let { _foodToCreateResponse = it }
            _responseMessage = _foodToCreateResponse.message
            if (_foodToCreateResponse.code != 201) fetchCreateFoodScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "createFood", _responseMessage)
            )
            else {
                setFoodVariables()
                fetchCreateFoodScreenUIState(
                    UiState.Success("screenRunning", screenCallName, "createFood", _responseMessage)
                )
            }
        }
    }
    // PUT
    private var _foodToPutResponse by mutableStateOf(ApiResponseListModel<GenericApiMessageResponse>(data = null, message = "", code = 0))
    /** Actualiza una reserva existente y actualiza el estado de la UI.
     * Lanza una coroutine en el viewModelScope, asegurando la cancelación automática si el ViewModel es destruido evitando fugas de memoria o llamadas innecesarias.
     */
    fun putFood(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller("editFoodScreen", "putFood")) {
            fetchEditFoodScreenUIState(UiState.Loading)
            foodRepository.putFood(_id, createFoodRequest()).let { _foodToPutResponse = it }
            _responseMessage = _foodToPutResponse.message
            if (_foodToPutResponse.code != 200) fetchEditFoodScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "putFood", _responseMessage)
            )
            else {
                setFoodVariables()
                fetchEditFoodScreenUIState(
                    UiState.Success("screenRunning", screenCallName, "putFood", _responseMessage)
                )
            }
        }
    }
    // DELETE
    private var _foodToDeleteResponse by mutableStateOf(ApiResponseListModel<GenericApiMessageResponse>(data = null, message = "", code = 0))
    /** Elimina una reserva existente y actualiza el estado de la UI.
     * Lanza una coroutine en el viewModelScope, asegurando la cancelación automática si el ViewModel es destruido evitando fugas de memoria o llamadas innecesarias.
     */
    fun deleteFood(screenCallName: String = "Unknown Screen Caller") {
        viewModelScope.launch(_exceptionHandler + CoroutineCaller("editFoodScreen", "deleteFood")) {
            fetchEditFoodScreenUIState(UiState.Loading)
            foodRepository.deleteFood(_id).let { _foodToDeleteResponse = it }
            _responseMessage = _foodToDeleteResponse.message
            if (_foodToDeleteResponse.code != 204) fetchEditFoodScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "deleteFood", _responseMessage)
            )
            else {
                setFoodVariables()
                fetchEditFoodScreenUIState(
                    UiState.Success("screenRunning", screenCallName, "deleteFood", _responseMessage)
                )
            }
        }
    }
    //// FOOD SEARCHER
    private var _showSearchedFoods by  mutableStateOf(false)
    val showSearchedFoods: Boolean get() = _showSearchedFoods
    fun setShowSearchedFoods(value: Boolean) { _showSearchedFoods = value }
    //
    private var _foodNameToSearch by mutableStateOf("")
    private var _matchFoodList by mutableStateOf(ApiResponseResultListModel<FoodModel>(data = null, message = "", code = 0))
    val matchFoodsList: ApiResponseResultListModel<FoodModel> get() = _matchFoodList
    /** Función que busca salas en base al nombre recibido por parametro.
     * @param nameToSearch Nombre de la sala recibido desde el buscador.
     */
    fun fetchFoodToSearch(nameToSearch: String) {
        // Guardamos el nombre de la pelicula a buscar
        _foodNameToSearch = nameToSearch
        // Guardamos los objetos de tipo '' para mostrar las salas que coincidan con el nombre de la sala a buscar(_roomToSearch).
        _matchFoodList = _availableFoodsList.filterResultList { it.nombreRestaurante.contains(_foodNameToSearch, ignoreCase = true) }
        // La lista solo se mostrara en caso de haber una sala que buscar que coincida con el patron indicado.
        _showSearchedFoods = Regex("[a-zA-Z0-9áéíóúÁÉÍÓÚüÜñÑ -]+").containsMatchIn(_foodNameToSearch)
    }
}
