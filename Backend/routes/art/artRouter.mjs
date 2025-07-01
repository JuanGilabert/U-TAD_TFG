// Importamos el generador de routers.
import { expressRouterGenerator } from '../../utils/functions/expressRouterGeneratorFunction.mjs';
// Importamos los controladores necesarios para este router.
import { CinemaController } from '../../controllers/art/cinemaController.mjs';
import { MusicController } from '../../controllers/art/musicController.mjs';
import { PaintingController } from '../../controllers/art/paintingController.mjs';
// Importamos los valores de las rutas/endpoints necesarios para este router.
import { ART_CINEMA_ROUTE_PATH, ART_MUSIC_ROUTE_PATH, ART_PAINTING_ROUTE_PATH,
    EVENT_ROUTE_PATH, ART_PAINTING_EXPOSURE_ROUTE_PATH, IDENTIFIER_ROUTE_PATH,
    UNAVAILABLE_DATES_ROUTE_PATH, CINEMA_MOVIE_DOWNLOADER_ROUTE_PATH,
    MUSIC_VIDEO_DOWNLOADER_ROUTE_PATH
} from '../../config/GenericEnvConfig.mjs';
/** Configures and returns an Express router for handling art-related routes.
 *
 * @param {Object} params - The parameters for configuring the router.
 * @param {Object} params.CinemaModel - The model for cinema resources.
 * @param {Object} params.MusicModel - The model for music resources.
 * @param {Object} params.PaintingModel - The model for painting resources.
 * @returns {Router} The configured Express router for art-related routes.
 */
export const artRouter = ({ CinemaModel, MusicModel, PaintingModel }) => {
    const artRoute = expressRouterGenerator();
    //// Cinema
    const artCinemaController = new CinemaController({ model: CinemaModel });
    // GET /api/art/cinema/event
    artRoute.get(`${ART_CINEMA_ROUTE_PATH}${EVENT_ROUTE_PATH}`, artCinemaController.getAllCinemas);
    // GET-UNAVAILABLE-DATES api/art/cinema/event/unavailable-dates
    artRoute.get(`${ART_CINEMA_ROUTE_PATH}${EVENT_ROUTE_PATH}${UNAVAILABLE_DATES_ROUTE_PATH}`, artCinemaController.getCinemaUnavailableDates);
    // GET-ID api/art/cinema/event/:id
    artRoute.get(`${ART_CINEMA_ROUTE_PATH}${EVENT_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`, artCinemaController.getCinemaById);
    // POST
    artRoute.post(`${ART_CINEMA_ROUTE_PATH}${EVENT_ROUTE_PATH}`, artCinemaController.postCinema);
    // PUT
    artRoute.put(`${ART_CINEMA_ROUTE_PATH}${EVENT_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`, artCinemaController.putCinema);
    // PATCH
    artRoute.patch(`${ART_CINEMA_ROUTE_PATH}${EVENT_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`, artCinemaController.patchCinema);
    // DELETE
    artRoute.delete(`${ART_CINEMA_ROUTE_PATH}${EVENT_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`, artCinemaController.deleteCinema);
    //// Music
    const artMusicController = new MusicController({ model: MusicModel });
    // GET /api/art/music/event
    artRoute.get(`${ART_MUSIC_ROUTE_PATH}${EVENT_ROUTE_PATH}`, artMusicController.getAllMusics);
    // GET-UNAVAILABLE-DATES api/art/music/event/unavailable-dates
    artRoute.get(`${ART_MUSIC_ROUTE_PATH}${EVENT_ROUTE_PATH}${UNAVAILABLE_DATES_ROUTE_PATH}`, artMusicController.getMusicUnavailableDates);
    // GET-ID api/art/music/event/:id
    artRoute.get(`${ART_MUSIC_ROUTE_PATH}${EVENT_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`, artMusicController.getMusicById);
    // POST
    artRoute.post(`${ART_MUSIC_ROUTE_PATH}${EVENT_ROUTE_PATH}`, artMusicController.postMusic);
    // PUT
    artRoute.put(`${ART_MUSIC_ROUTE_PATH}${EVENT_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`, artMusicController.putMusic);
    // PATCH
    artRoute.patch(`${ART_MUSIC_ROUTE_PATH}${EVENT_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`, artMusicController.patchMusic);
    // DELETE
    artRoute.delete(`${ART_MUSIC_ROUTE_PATH}${EVENT_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`, artMusicController.deleteMusic);
    // POST /api/art/music/video-downloader
    artRoute.post(`${ART_MUSIC_ROUTE_PATH}${MUSIC_VIDEO_DOWNLOADER_ROUTE_PATH}`, artMusicController.postMusicVideoDownload);
    //// Painting
    const artPaintingController = new PaintingController({ model: PaintingModel });
    // GET /api/art/painting/exposure
    artRoute.get(`${ART_PAINTING_ROUTE_PATH}${ART_PAINTING_EXPOSURE_ROUTE_PATH}`, artPaintingController.getAllPaintings);
    // GET-UNAVAILABLE-DATES api/art/painting/exposure/unavailable-dates
    artRoute.get(`${ART_PAINTING_ROUTE_PATH}${ART_PAINTING_EXPOSURE_ROUTE_PATH}${UNAVAILABLE_DATES_ROUTE_PATH}`, artPaintingController.getPaintingUnavailableDates);
    // GET-ID
    artRoute.get(`${ART_PAINTING_ROUTE_PATH}${ART_PAINTING_EXPOSURE_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`, artPaintingController.getPaintingById);
    // POST
    artRoute.post(`${ART_PAINTING_ROUTE_PATH}${ART_PAINTING_EXPOSURE_ROUTE_PATH}`, artPaintingController.postPainting);
    // PUT
    artRoute.put(`${ART_PAINTING_ROUTE_PATH}${ART_PAINTING_EXPOSURE_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`, artPaintingController.putPainting);
    // PATCH
    artRoute.patch(`${ART_PAINTING_ROUTE_PATH}${ART_PAINTING_EXPOSURE_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`, artPaintingController.patchPainting);
    // DELETE
    artRoute.delete(`${ART_PAINTING_ROUTE_PATH}${ART_PAINTING_EXPOSURE_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`, artPaintingController.deletePainting);
    // Devolvemos la configuración del router.
    return artRoute;
}