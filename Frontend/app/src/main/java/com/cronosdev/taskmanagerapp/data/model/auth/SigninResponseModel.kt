package com.cronosdev.taskmanagerapp.data.model.auth
//
/** Modelo de datos que representa la respuesta devuelta por la api cuando el usuario hace doLogin.
 * Esta clase se utiliza para gestionar la información acerca del doLogin.
 *
 * @author Juan Gilabert Lopez
 * @property token Token de acceso devuelto por la appi.
 * @since 1.0
 */
data class SigninResponseModel(
    val token: String
)