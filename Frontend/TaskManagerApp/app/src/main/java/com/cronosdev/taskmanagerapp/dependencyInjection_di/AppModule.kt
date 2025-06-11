package com.cronosdev.taskmanagerapp.dependencyInjection_di
//
import com.cronosdev.taskmanagerapp.data.repositories.art.PaintingRepository
import com.cronosdev.taskmanagerapp.data.repositories.art.MusicRepository
import com.cronosdev.taskmanagerapp.data.repositories.art.CinemaRepository
import com.cronosdev.taskmanagerapp.data.repositories.auth.AuthRepository
import com.cronosdev.taskmanagerapp.data.repositories.food.FoodRepository
import com.cronosdev.taskmanagerapp.data.repositories.health.MedicamentRepository
import com.cronosdev.taskmanagerapp.data.repositories.health.MedicalAppointmentRepository
import com.cronosdev.taskmanagerapp.data.repositories.meeting.MeetingRepository
import com.cronosdev.taskmanagerapp.data.repositories.sport.SportRepository
import com.cronosdev.taskmanagerapp.data.repositories.travel.TravelRepository
import com.cronosdev.taskmanagerapp.data.repositories.work.WorkRepository
//
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
/** Módulo de Hilt que proporciona las dependencias necesarias para la aplicación.
 * @Provides Define una función que proporciona una dependencia
 *
 * @author Juan Gilabert Lopez
 * @since 1.0
 */
@Module // Declara esta clase como un módulo de Hilt.
@InstallIn(SingletonComponent::class) // Indica que las dependencias estarán disponibles en toda la aplicación
object AppModule {
    /** Singleton para inyectar el repositorio de las salas/rooms.
     * @return  Devolvemos el objeto del repositorio de salas llamado "RoomsRepository".
     */
    @Provides
    @Singleton
    fun provideCinemaRepository(): CinemaRepository { return CinemaRepository() }
    /** Singleton para inyectar el repositorio de reservas/bookings
     * @return  Devolvemos el objeto del repositorio de reservas llamado "BookingsRepository".
     */
    @Provides
    @Singleton
    fun provideMusicRepository(): MusicRepository { return MusicRepository() }
    /** Singleton para inyectar el repositorio de las salas/rooms.
     * @return  Devolvemos el objeto del repositorio de salas llamado "RoomsRepository".
     */
    @Provides
    @Singleton
    fun providePaintingRepository(): PaintingRepository { return PaintingRepository() }
    /** Singleton para inyectar el repositorio de autenticacion.
     * @return  Devolvemos el objeto del repositorio de autenticacion llamado "AuthRepository".
     */
    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository { return AuthRepository() }
    /** Singleton para inyectar el repositorio de reservas/bookings
     * @return  Devolvemos el objeto del repositorio de reservas llamado "BookingsRepository".
     */
    @Provides
    @Singleton
    fun provideFoodRepository(): FoodRepository { return FoodRepository() }
    /** Singleton para inyectar el repositorio de las salas/rooms.
     * @return  Devolvemos el objeto del repositorio de salas llamado "RoomsRepository".
     */
    @Provides
    @Singleton
    fun provideMedicamentRepository(): MedicamentRepository { return MedicamentRepository() }
    /** Singleton para inyectar el repositorio de reservas/bookings
     * @return  Devolvemos el objeto del repositorio de reservas llamado "BookingsRepository".
     */
    @Provides
    @Singleton
    fun provideMedicalAppointmentRepository(): MedicalAppointmentRepository { return MedicalAppointmentRepository() }
    /** Singleton para inyectar el repositorio de reservas/bookings
     * @return  Devolvemos el objeto del repositorio de reservas llamado "BookingsRepository".
     */
    @Provides
    @Singleton
    fun provideMeetingRepository(): MeetingRepository { return MeetingRepository() }
    /** Singleton para inyectar el repositorio de reservas/bookings
     * @return  Devolvemos el objeto del repositorio de reservas llamado "BookingsRepository".
     */
    @Provides
    @Singleton
    fun provideSportRepository(): SportRepository { return SportRepository() }
    /** Singleton para inyectar el repositorio de reservas/bookings
     * @return  Devolvemos el objeto del repositorio de reservas llamado "BookingsRepository".
     */
    @Provides
    @Singleton
    fun provideTravelRepository(): TravelRepository { return TravelRepository() }
    /** Singleton para inyectar el repositorio de reservas/bookings
     * @return  Devolvemos el objeto del repositorio de reservas llamado "BookingsRepository".
     */
    @Provides
    @Singleton
    fun provideWorkRepository(): WorkRepository { return WorkRepository() }
}