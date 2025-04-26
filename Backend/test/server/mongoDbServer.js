import { createApp } from "../../taskManagerServer.mjs";
// Importamos los modelos para inyectarlos en la aplicacion.
import { CinemaModel } from './models/artModels/cinemaModel/cinemaModel.mjs';
import { MusicModel } from './models/artModels/musicModel/musicModel.mjs';
import { PaintingModel } from './models/artModels/paintingModel/paintingModel.mjs';
import { AuthModel } from './models/authModels/userAuthModel.mjs';
import { FoodModel } from './models/foodModels/foodModel/foodModel.mjs';
import { MedicamentModel } from './models/healthModels/medicamentModel/medicamentModel.mjs';
import { MedicalAppointmentModel } from './models/healthModels/medicalAppointmentModel/medicalAppointmentModel.mjs';
import { MeetingModel } from './models/meetingModels/meetingModel.mjs';
import { SportModel } from './models/sportModels/sportModel.mjs';
import { TravelModel } from './models/travelModels/travelModel.mjs';
import { WorkModel } from './models/workModels/workModel.mjs';
////
createApp({ CinemaModel, MusicModel, PaintingModel, AuthModel, FoodModel,
    MedicamentModel, MedicalAppointmentModel, MeetingModel, SportModel, TravelModel, WorkModel
});