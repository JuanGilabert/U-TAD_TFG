package com.cronosdev.taskmanagerapp.ui.screens.signin
//
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseListModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.GenericApiMessageResponse
import com.cronosdev.taskmanagerapp.data.model.auth.SigninResponseModel
import com.cronosdev.taskmanagerapp.data.model.auth.SigninRequestModel
import com.cronosdev.taskmanagerapp.data.model.auth.SignoutRequestModel
import com.cronosdev.taskmanagerapp.data.model.auth.SignupRequestModel
import com.cronosdev.taskmanagerapp.data.model.courrutines.CoroutineCaller
import com.cronosdev.taskmanagerapp.data.repositories.auth.AuthRepository
import com.cronosdev.taskmanagerapp.ui.state.UiState
import com.cronosdev.taskmanagerapp.ui.navigation.Destinations
import com.cronosdev.taskmanagerapp.utils.SessionManager
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
import javax.inject.Inject
import javax.net.ssl.SSLHandshakeException

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {
    //
    private val _signupScreenUiState = MutableStateFlow<UiState?>(null)
    val signupScreenUiState: StateFlow<UiState?> = _signupScreenUiState
    //
    private val _signinScreenUiState = MutableStateFlow<UiState?>(null)
    val signinScreenUiState: StateFlow<UiState?> = _signinScreenUiState
    //
    private val _signoutResultUiState = MutableStateFlow<UiState?>(null)
    val signoutResultUiState: StateFlow<UiState?> = _signoutResultUiState
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
        if (screenCaller == Destinations.SIGNIN_SCREEN_URL)
            fetchSigninScreenUIState(UiState.Error("throwableErrorType", screenCaller, methodCaller, errorMessage))
        if (screenCaller == Destinations.SIGNUP_SCREEN_URL)
            fetchSignupScreenUIState(UiState.Error("throwableErrorType", screenCaller, methodCaller, errorMessage))
    }
    //
    fun fetchSignupScreenUIState(state: UiState) { _signupScreenUiState.value = state }
    fun fetchSigninScreenUIState(state: UiState) { _signinScreenUiState.value = state }
    private var _responseMessage by mutableStateOf("")
    //// GETTERS & SETTERS
    //
    private var _userName by mutableStateOf("")
    val userName: String get() = _userName
    fun setUserName(newName: String) { _userName = newName }
    //
    private var _userEmail by mutableStateOf("juangilabert013@gmail.com")
    val userEmail: String get() = _userEmail
    fun setUserEmail(newEmail: String) { _userEmail = newEmail }
    //
    private var _userPassword by mutableStateOf("123abc")
    val userPassword: String get() = _userPassword
    fun setUserPassword(newPassword: String) { _userPassword = newPassword }
    ////
    // POST Crear una cuenta
    private var _signupApiResponse  by mutableStateOf(ApiResponseListModel<GenericApiMessageResponse>(data = null, message = "", code = 0))
    val signupApiResponse: ApiResponseListModel<GenericApiMessageResponse> get() = _signupApiResponse
    fun signup(screenCallName: String = "Unknown Screen Caller") {
        fetchSignupScreenUIState(UiState.Loading)
        viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "signup")) {
            _signupApiResponse = authRepository.signup(SignupRequestModel(_userName, _userEmail, _userPassword))
            _responseMessage = _signupApiResponse.message
            if (_signupApiResponse.code != 201) fetchSignupScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "signup", _responseMessage)
            )
            else {
                fetchSignupScreenUIState(
                    UiState.Success("screenRunning", screenCallName, "signup", _responseMessage)
                )
            }

        }
    }
    // Iniciar sesion.
    private var _signinApiResponse by mutableStateOf(ApiResponseListModel<SigninResponseModel>(data = null, message = "", code = 0))
    val signinApiResponse: ApiResponseListModel<SigninResponseModel> get() = _signinApiResponse
    fun signin(screenCallName: String = "Unknown Screen Caller") {
        fetchSigninScreenUIState(UiState.Loading)
        viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "signin")) {
            // Se hace una llamada al método login del repositorio y este método devuelve el token si la autenticación es exitosa, o null si falla.
            _signinApiResponse = authRepository.signin(SigninRequestModel(_userEmail, _userPassword))
            _responseMessage = _signinApiResponse.message
            // Si el token es nulo 'null', quiere decir que el login no se ha hecho correctamente. Si el token no es null guardamos el token en 'SessionManager.bearerToken'.
            if (_signinApiResponse.code != 200) fetchSigninScreenUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "signin", _responseMessage)
            )
            else {
                SessionManager.bearerToken = _signinApiResponse.data?.token
                fetchSigninScreenUIState(
                    UiState.Success("screenRunning", screenCallName, "signin", _responseMessage)
                )
            }
        }
    }
    // Cerrar sesion.
    private var _logOutApiResponse by mutableStateOf(ApiResponseListModel<GenericApiMessageResponse>(data = null, message = "", code = 0))
    val logOutApiResponse: ApiResponseListModel<GenericApiMessageResponse> get() = _logOutApiResponse
    private var _signoutResult = MutableStateFlow<Boolean?>(null)
    val signoutResult: StateFlow<Boolean?> = _signoutResult
    /** Funcion utilizada para cerrar la sesion de la aplicacion y eliminar el token correspondiente al usuario que ha cerrado sesion.
     * Lanza una coroutine que realiza la solicitud de reserva al repositorio y actualiza el estado de la UI en función de la respuesta obtenida.
     * @sample signout()
     * @since 1.0
     * @throws _exceptionHandler En caso de fallo en la carga de datos.
     */
    fun signout(screenCallName: String = "Unknown Screen Caller") { //drawerContent
        //fetchSignoutResultUIState(UiState.Loading)
        viewModelScope.launch(_exceptionHandler + CoroutineCaller(screenCallName, "signout")) {
            _logOutApiResponse = authRepository.signout(SignoutRequestModel(SessionManager.bearerToken!!))
            _responseMessage = _logOutApiResponse.message
            // El resultado dependera del codigo http devuelto por la api.
            _signoutResult.value = _signinApiResponse.code != 200
            /*if (_signinApiResponse.code != 200) fetchSignoutResultUIState(
                UiState.Error("fetchDataErrorType", screenCallName, "signout", _responseMessage)
            )
            else {
                fetchSignoutResultUIState(
                    UiState.Success("screenRunning", screenCallName, "signout", _responseMessage)
                )
            }*/
        }
    }
}