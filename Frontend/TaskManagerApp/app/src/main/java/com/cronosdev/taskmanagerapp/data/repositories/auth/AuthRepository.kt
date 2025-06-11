package com.cronosdev.taskmanagerapp.data.repositories.auth
//
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.ApiResponseListModel
import com.cronosdev.taskmanagerapp.data.model.apiResponseModels.GenericApiMessageResponse
import com.cronosdev.taskmanagerapp.data.model.auth.SigninResponseModel
import com.cronosdev.taskmanagerapp.data.model.auth.SigninRequestModel
import com.cronosdev.taskmanagerapp.data.model.auth.SignoutRequestModel
import com.cronosdev.taskmanagerapp.data.model.auth.SignupRequestModel
import com.cronosdev.taskmanagerapp.utils.apiErrorMessageExtractor
import com.cronosdev.taskmanagerapp.data.remote.RetrofitInstance
//
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
/** Repositorio de autenticación que proporciona métodos para manejar el inicio y cierre de sesión de los usuarios.
 * Esta clase actúa como intermediaria entre la aplicación y la API, gestionando las solicitudes de autenticación.
 *
 * @author Juan Gilabert Lopez
 * @constructor Crea una instancia del repositorio de autenticación.
 * @since 1.0
 */
class AuthRepository @Inject constructor() {
    /**
     *
     */
    suspend fun signup(signupRequest: SignupRequestModel): ApiResponseListModel<GenericApiMessageResponse> {
        var result = ApiResponseListModel<GenericApiMessageResponse>(null,"",0)
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.utadApi.signup(signupRequest)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiErrorMessageExtractor(response, "signup")
            result.code  = response.code()
            // Devolvemos el resultado.
            result
        }
    }
    /**
     *
     */
    suspend fun signin(signinRequest: SigninRequestModel): ApiResponseListModel<SigninResponseModel> {
        var result = ApiResponseListModel<SigninResponseModel>(null,"",0)
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.utadApi.signin(signinRequest)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiErrorMessageExtractor(response, "signin")
            result.code  = response.code()
            // Devolvemos el resultado.
            result
        }
    }

    /**
     *
     */
    suspend fun signout(signoutRequest: SignoutRequestModel): ApiResponseListModel<GenericApiMessageResponse> {
        var result = ApiResponseListModel<GenericApiMessageResponse>(null,"",0)
        return withContext(Dispatchers.IO) {
            val response =  RetrofitInstance.utadApi.signout(signoutRequest)
            result.data = if (response.isSuccessful) response.body() else null
            result.message = apiErrorMessageExtractor(response, "signout")
            result.code  = response.code()
            // Devolvemos el resultado.
            result
        }
    }
}