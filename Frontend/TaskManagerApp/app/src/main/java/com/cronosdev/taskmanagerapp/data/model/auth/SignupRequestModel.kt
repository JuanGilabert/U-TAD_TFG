package com.cronosdev.taskmanagerapp.data.model.auth
//
/** Modelo de datos que representa la solicitud de acceso a la api cuando el usuario hace Login.
 * Esta clase se utiliza para gestionar la información acerca del Login.
 *
 * @author Juan Gilabert Lopez
 * @property userPassword Clave de acceso del usuario que hace Login.
 * @property userEmail Email universitario del usuario que hace Login.
 * @since 1.0
 */
data class SignupRequestModel (
    val userName: String,
    val userPassword: String,
    val userEmail: String
)