package com.cronosdev.taskmanagerapp.data.remote
//
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.cronosdev.taskmanagerapp.utils.SessionManager
/** Objeto que proporciona una instancia de Retrofit para interactuar con la API.
 *
 * @author Juan Gilabert Lopez
 * @property utadApi Instancia de [TaskManagerAPIService] configurada con Retrofit.
 * @property client Instancia de [OkHttpClient] con configuraciones personalizadas como  un cliente HTTP personalizado con un interceptor.
 * Un interceptor es un componente que permite interceptar y modificar las solicitudes o respuestas HTTP antes de que lleguen al servidor o al cliente.
 * Este interceptor en particular se define como un bloque lambda.
 * @since 1.0
 */
object RetrofitInstance {
    private const val TASK_MANAGER_API_URL = "http://13.60.93.208:2793/api/"
    val utadApi: TaskManagerAPIService by lazy {
        Retrofit.Builder().baseUrl(TASK_MANAGER_API_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build().create(TaskManagerAPIService::class.java)
    }
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS) // Tiempo limite para establecer una conexión
        .readTimeout(30, TimeUnit.SECONDS) // Tiempo limite para la lectura
        .writeTimeout(15, TimeUnit.SECONDS)  //Tiempo limite para la escritura
        .addInterceptor { chain ->
            /** Interceptor que permite modificar la solicitud antes de enviarla.
             * Aquí se obtiene la solicitud original (chain.request()) que se va a enviar al servidor,
             * y se crea un nuevo Request.Builder basado en esa solicitud.
             * Esto permite modificar la solicitud original antes de que se envíe.
             * @param chain Cadena de ejecución de la solicitud HTTP.
             * @return Respuesta HTTP modificada.
             * @since 1.0
             */
            val requestBuilder = chain.request().newBuilder()
            // Se requiere cabecera del contenido soportado por la app.
            requestBuilder.addHeader("Accept", "*/*")
            /** Se obtiene un token de autorización desde el objeto SessionManager. Si el token no es null, se añade un encabezado HTTP llamado "Authorization" con el valor "Bearer $token".
             * Este encabezado es usado comúnmente para enviar tokens de acceso en APIs seguras que usan autenticación basada en Bearer Tokens
             */
            val token = SessionManager.bearerToken;
            if (token != null) requestBuilder.addHeader("Authorization", "Bearer $token")
            /** Se construye la solicitud modificada con requestBuilder.build() y se pasa al siguiente eslabón en la cadena de ejecución (chain.proceed).
             * Este método envía la solicitud al servidor y devuelve la respuesta.
             */
            chain.proceed(requestBuilder.build())
            // Se completa la configuración del cliente con  build(),  que devuelve una instancia inmutable de OkHttpClient con el interceptor configurado.
        }.build()
}