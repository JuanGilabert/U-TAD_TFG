package com.cronosdev.taskmanagerapp.ui.navigation
//
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
// Dependencias relacionadas con la carpeta 'screens'
import com.cronosdev.taskmanagerapp.ui.screens.signin.SigninScreen
import com.cronosdev.taskmanagerapp.ui.screens.signin.AuthViewModel

import com.cronosdev.taskmanagerapp.ui.screens.task.TaskScreen
import com.cronosdev.taskmanagerapp.ui.screens.task.TaskViewModel

import com.cronosdev.taskmanagerapp.ui.screens.art.cinema.CinemaScreen
import com.cronosdev.taskmanagerapp.ui.screens.art.cinema.EditCinemaScreen
import com.cronosdev.taskmanagerapp.ui.screens.art.cinema.CreateCinemaScreen
import com.cronosdev.taskmanagerapp.ui.screens.art.cinema.CinemaViewModel

import com.cronosdev.taskmanagerapp.ui.screens.art.music.MusicScreen
import com.cronosdev.taskmanagerapp.ui.screens.art.music.UpdateMusicScreen
import com.cronosdev.taskmanagerapp.ui.screens.art.music.CreateMusicScreen
import com.cronosdev.taskmanagerapp.ui.screens.art.music.MusicViewModel

import com.cronosdev.taskmanagerapp.ui.screens.art.painting.PaintingScreen
import com.cronosdev.taskmanagerapp.ui.screens.art.painting.UpdatePaintingScreen
import com.cronosdev.taskmanagerapp.ui.screens.art.painting.CreatePaintingScreen
import com.cronosdev.taskmanagerapp.ui.screens.art.painting.PaintingViewModel

import com.cronosdev.taskmanagerapp.ui.screens.health.medicament.MedicamentScreen
import com.cronosdev.taskmanagerapp.ui.screens.health.medicament.UpdateMedicamentScreen
import com.cronosdev.taskmanagerapp.ui.screens.health.medicament.CreateMedicamentScreen
import com.cronosdev.taskmanagerapp.ui.screens.health.medicament.MedicamentViewModel

import com.cronosdev.taskmanagerapp.ui.screens.health.medicalAppointment.MedicalAppointmentScreen
import com.cronosdev.taskmanagerapp.ui.screens.health.medicalAppointment.UpdateMedicalAppointmentScreen
import com.cronosdev.taskmanagerapp.ui.screens.health.medicalAppointment.CreateMedicalAppointmentScreen
import com.cronosdev.taskmanagerapp.ui.screens.health.medicalAppointment.MedicalAppointmentViewModel

import com.cronosdev.taskmanagerapp.ui.screens.food.FoodScreen
import com.cronosdev.taskmanagerapp.ui.screens.food.UpdateFoodScreen
import com.cronosdev.taskmanagerapp.ui.screens.food.CreateFoodScreen
import com.cronosdev.taskmanagerapp.ui.screens.food.FoodViewModel

import com.cronosdev.taskmanagerapp.ui.screens.meeting.MeetingScreen
import com.cronosdev.taskmanagerapp.ui.screens.meeting.UpdateMeetingScreen
import com.cronosdev.taskmanagerapp.ui.screens.meeting.CreateMeetingScreen
import com.cronosdev.taskmanagerapp.ui.screens.meeting.MeetingViewModel
import com.cronosdev.taskmanagerapp.ui.screens.signup.SignupScreen

import com.cronosdev.taskmanagerapp.ui.screens.sport.SportScreen
import com.cronosdev.taskmanagerapp.ui.screens.sport.UpdateSportScreen
import com.cronosdev.taskmanagerapp.ui.screens.sport.CreateSportScreen
import com.cronosdev.taskmanagerapp.ui.screens.sport.SportViewModel

import com.cronosdev.taskmanagerapp.ui.screens.travel.TravelScreen
import com.cronosdev.taskmanagerapp.ui.screens.travel.UpdateTravelScreen
import com.cronosdev.taskmanagerapp.ui.screens.travel.CreateTravelScreen
import com.cronosdev.taskmanagerapp.ui.screens.travel.TravelViewModel

import com.cronosdev.taskmanagerapp.ui.screens.work.WorkScreen
import com.cronosdev.taskmanagerapp.ui.screens.work.UpdateWorkScreen
import com.cronosdev.taskmanagerapp.ui.screens.work.CreateWorkScreen
import com.cronosdev.taskmanagerapp.ui.screens.work.WorkViewModel
/** NavGraph configura la navegación de la aplicación mediante un NavHost.
 * Este composable define y gestiona las rutas y las pantallas asociadas,
 * proporcionando un esquema claro de navegación dentro de la app.
 *
 * El NavHost es un contenedor que se encarga de renderizar las pantallas correspondientes
 * según la ruta activa en el controlador de navegación (NavController).
 * Las rutas son definidas como destinos únicos, y cada una está vinculada a un composable que
 * representa la pantalla o funcionalidad asociada.
 *
 * Funcionalidad principal:
 * 1. Define la ruta inicial de la navegación con `startDestination`.
 * 2. Configura cada ruta mediante el método `composable`, donde se asocia una URL
 *    (definida en el objeto Destinations) a un composable específico.
 * 3. Cada pantalla puede recibir parámetros comunes como el `navController` y otros objetos
 *    necesarios para la lógica de la app, como los ViewModels de las vistas.
 *
 * **Parameters;
 * @param navController Es el controlador de navegación que gestiona las rutas, transiciones, y el estado de navegación de la aplicación. (rememberNavController)
 * @return No retorna un valor directo, ya que es un Composable.
 * @sample MainContentAppNavGraph()
 * @since 1.0
 */
@Composable
fun MainContentAppNavGraph(
    navController: NavHostController, taskViewModel: TaskViewModel = hiltViewModel(), cinemaViewModel: CinemaViewModel = hiltViewModel(),
    musicViewModel: MusicViewModel = hiltViewModel(), paintingViewModel: PaintingViewModel = hiltViewModel(),
    medicamentViewModel: MedicamentViewModel = hiltViewModel(), medicalAppointmentViewModel: MedicalAppointmentViewModel = hiltViewModel(),
    foodViewModel: FoodViewModel = hiltViewModel(), meetingViewModel: MeetingViewModel = hiltViewModel(), travelViewModel: TravelViewModel = hiltViewModel(),
    sportViewModel: SportViewModel = hiltViewModel(), workViewModel: WorkViewModel = hiltViewModel(),
) {
    NavHost(navController = navController, startDestination = Destinations.ALL_TASKS_SCREEN_URL,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth }, // La pantalla entra desde la derecha.
                animationSpec = tween(700)
            ) + fadeIn(animationSpec = tween(700)) // Efecto de desvanecimiento.
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth }, // La pantalla actual se desliza hacia la izquierda.
                animationSpec = tween(700)
            ) + fadeOut(animationSpec = tween(700)) // Efecto de desaparición.
        }
    ) {
        //composable(Destinations.DATE_PICKER_SCREEN_URL) { DatePickerComponent(uiState: UiState?, datesToDisableOnUi: List<String>?, onInitDateChange:(String) -> Unit) }
        // ALL TASKS
        composable(Destinations.ALL_TASKS_SCREEN_URL) { TaskScreen(taskViewModel) }
        // ART CINEMA
        composable(Destinations.ART_CINEMA_SCREEN_URL, ) { CinemaScreen(navController, cinemaViewModel) }
        composable(Destinations.ART_CREATE_CINEMA_SCREEN_URL) { CreateCinemaScreen(navController, cinemaViewModel) }
        composable(Destinations.ART_UPDATE_CINEMA_SCREEN_URL) { EditCinemaScreen(navController, cinemaViewModel) }
        // ART MUSIC
        composable(Destinations.ART_MUSIC_SCREEN_URL) { MusicScreen(navController, musicViewModel) }
        composable(Destinations.ART_CREATE_MUSIC_SCREEN_URL) { CreateMusicScreen(navController, musicViewModel) }
        composable(Destinations.ART_UPDATE_MUSIC_SCREEN_URL) { UpdateMusicScreen(navController, musicViewModel) }
        // ART PAINTING
        composable(Destinations.ART_PAINTING_SCREEN_URL) { PaintingScreen(navController, paintingViewModel) }
        composable(Destinations.ART_CREATE_PAINTING_SCREEN_URL) { CreatePaintingScreen(navController, paintingViewModel) }
        composable(Destinations.ART_UPDATE_PAINTING_SCREEN_URL) { UpdatePaintingScreen(navController, paintingViewModel) }
        // HEALTH MEDICAMENT
        composable(Destinations.HEALTH_MEDICAMENT_SCREEN_URL) { MedicamentScreen(navController, medicamentViewModel) }
        composable(Destinations.HEALTH_CREATE_MEDICAMENT_SCREEN_URL) { CreateMedicamentScreen(navController, medicamentViewModel) }
        composable(Destinations.HEALTH_EDIT_MEDICAMENT_SCREEN_URL) { UpdateMedicamentScreen(navController, medicamentViewModel) }
        // HEALTH MEDICAL APPOINTMENT
        composable(Destinations.HEALTH_MEDICAL_APPOINTMENT_SCREEN_URL) { MedicalAppointmentScreen(navController, medicalAppointmentViewModel) }
        composable(Destinations.HEALTH_CREATE_MEDICAL_APPOINTMENT_SCREEN_URL) { CreateMedicalAppointmentScreen(navController, medicalAppointmentViewModel) }
        composable(Destinations.HEALTH_EDIT_MEDICAL_APPOINTMENT_SCREEN_URL) { UpdateMedicalAppointmentScreen(navController, medicalAppointmentViewModel) }
        // FOOD
        composable(Destinations.FOOD_SCREEN_URL) { FoodScreen(navController, foodViewModel) }
        composable(Destinations.CREATE_FOOD_SCREEN_URL) { CreateFoodScreen(navController, foodViewModel) }
        composable(Destinations.UPDATE_FOOD_SCREEN_URL) { UpdateFoodScreen(navController, foodViewModel) }
        // MEETING
        composable(Destinations.MEETING_SCREEN_URL) { MeetingScreen(navController, meetingViewModel) }
        composable(Destinations.CREATE_MEETING_SCREEN_URL) { CreateMeetingScreen(navController, meetingViewModel) }
        composable(Destinations.EDIT_MEETING_SCREEN_URL) { UpdateMeetingScreen(navController, meetingViewModel) }
        // TRAVEL
        composable(Destinations.TRAVEL_SCREEN_URL) { TravelScreen(navController, travelViewModel) }
        composable(Destinations.CREATE_TRAVEL_SCREEN_URL) { CreateTravelScreen(navController, travelViewModel) }
        composable(Destinations.EDIT_TRAVEL_SCREEN_URL) { UpdateTravelScreen(navController, travelViewModel) }
        // SPORT
        composable(Destinations.SPORT_SCREEN_URL) { SportScreen(navController, sportViewModel) }
        composable(Destinations.CREATE_SPORT_SCREEN_URL) { CreateSportScreen(navController, sportViewModel) }
        composable(Destinations.EDIT_SPORT_SCREEN_URL) { UpdateSportScreen(navController, sportViewModel) }
        // WORK
        composable(Destinations.WORK_SCREEN_URL) { WorkScreen(navController, workViewModel) }
        composable(Destinations.CREATE_WORK_SCREEN_URL) { CreateWorkScreen(navController, workViewModel) }
        composable(Destinations.EDIT_WORK_SCREEN_URL) { UpdateWorkScreen(navController, workViewModel) }
    }
}
////
@Composable
fun AuthContentAppNavGraph(navController: NavHostController, authViewModel: AuthViewModel = hiltViewModel()) {
    NavHost(navController = navController, startDestination = Destinations.SIGNIN_SCREEN_URL,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth }, // La pantalla entra desde la derecha.
                animationSpec = tween(700)
            ) + fadeIn(animationSpec = tween(700)) // Efecto de desvanecimiento.
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth }, // La pantalla actual se desliza hacia la izquierda.
                animationSpec = tween(700)
            ) + fadeOut(animationSpec = tween(700)) // Efecto de desaparición.
        }
    ) {
        // AUTH
        composable(Destinations.SIGNIN_SCREEN_URL) { SigninScreen(navController, authViewModel) }
        composable(Destinations.SIGNUP_SCREEN_URL) { SignupScreen(navController, authViewModel) }
    }
}