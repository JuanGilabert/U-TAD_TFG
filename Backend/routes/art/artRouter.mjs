// Importamos modulos de node.
import { Router } from 'express';
// Importamos el middleware de las request y los controladores.
import { authMiddleware } from '../../middlewares/authMiddleware.mjs';
import { requestMiddleware } from '../../middlewares/requestMiddleware.mjs';
import { cinemaRequestMiddleware } from '../../middlewares/cinemaRequestMiddleware.mjs';
// Importamos los controladores
import { CinemaController } from '../../controllers/art/cinemaController.mjs';
import { MusicController } from '../../controllers/art/artMusicController.mjs';
import { PaintingController } from '../../controllers/art/artPaintingController.mjs';
/** Configures and returns an Express router for handling art-related routes.s
 *
 * @param {Object} params - The parameters for configuring the router.
 * @param {Object} params.CinemaModel - The model for cinema resources.
 * @param {Object} params.MusicModel - The model for music resources.
 * @param {Object} params.PaintingModel - The model for painting resources.
 * @returns {Router} The configured Express router for art-related routes.
 */
export const artRouter = ({ CinemaModel, MusicModel, PaintingModel }) => {
    const artRoute = Router();
    // Common constants
    const identifier = "/:id";
    const unavailableDates = "/unavailable-dates";
    /* Cinema */
    const cinemaEndpoint = "/cinema";
    const cinemaMovieDownloaderType = "/movie-downloader";
    const artCinemaController = new CinemaController({ model: CinemaModel });
    // GET /api/art/cinema/
    artRoute.get(`${cinemaEndpoint}`, cinemaRequestMiddleware, artCinemaController.getAllCinemas);
    // GET-UNAVAILABLE-DATES api/art/cinema/unavailable-dates
    artRoute.get(`${cinemaEndpoint}${unavailableDates}`, cinemaRequestMiddleware, artCinemaController.getCinemaUnavailableDates);
    // GET-ID api/art/cinema/:id
    artRoute.get(`${cinemaEndpoint}${identifier}`, cinemaRequestMiddleware, artCinemaController.getCinemaById);
    // POST
    artRoute.post(`${cinemaEndpoint}`, cinemaRequestMiddleware, artCinemaController.postCinema);
    // PUT
    artRoute.put(`${cinemaEndpoint}${identifier}`, cinemaRequestMiddleware, artCinemaController.putCinema);
    // PATCH
    artRoute.patch(`${cinemaEndpoint}${identifier}`, cinemaRequestMiddleware, artCinemaController.patchCinema);
    // DELETE
    artRoute.delete(`${cinemaEndpoint}${identifier}`, cinemaRequestMiddleware, artCinemaController.deleteCinema);
    /* Music */
    const musicEndpoint = '/music';
    const musicEventType = '/event';
    const musicVideoDownloaderType = '/video-downloader';
    const artMusicController = new MusicController({ model: MusicModel });
    // GET /api/art/music/event
    artRoute.get(`${musicEndpoint}${musicEventType}`, [authMiddleware], [requestMiddleware], artMusicController.getAllMusics);
    // GET-UNAVAILABLE-DATES api/art/music/event/unavailable-dates
    artRoute.get(`${musicEndpoint}${musicEventType}${unavailableDates}`, [authMiddleware], [requestMiddleware], artMusicController.getMusicUnavailableDates);
    // GET-ID api/art/music/event/:id
    artRoute.get(`${musicEndpoint}${musicEventType}${identifier}`, [authMiddleware], [requestMiddleware], artMusicController.getMusicById);
    // POST
    artRoute.post(`${musicEndpoint}${musicEventType}`, [authMiddleware], [requestMiddleware], artMusicController.postMusic);
    // PUT
    artRoute.put(`${musicEndpoint}${musicEventType}${identifier}`, [authMiddleware], [requestMiddleware], artMusicController.putMusic);
    // PATCH
    artRoute.patch(`${musicEndpoint}${musicEventType}${identifier}`, [authMiddleware], [requestMiddleware], artMusicController.patchMusic);
    // DELETE
    artRoute.delete(`${musicEndpoint}${musicEventType}${identifier}`, [authMiddleware], [requestMiddleware], artMusicController.deleteMusic);
    // GET /api/art/music/video-downloader -> En la query se enviara el formato del video y el directorio de descarga del video hasta revision del modelo.
    artRoute.post(`${musicEndpoint}${musicVideoDownloaderType}`, [authMiddleware], [requestMiddleware], artMusicController.postMusicVideoDownload);
    /** */
    /* Painting */
    const paintingEndpoint = '/painting';
    const paintingExposureType = '/exposure';//exposicion
    const artPaintingController = new PaintingController({ model: PaintingModel });
    // GET /api/art/painting/exposure
    artRoute.get(`${paintingEndpoint}${paintingExposureType}`, [authMiddleware], [requestMiddleware], artPaintingController.getAllPaintings);
    // GET-UNAVAILABLE-DATES api/art/painting/exposure/unavailable-dates
    artRoute.get(`${paintingEndpoint}${paintingExposureType}${unavailableDates}`, [authMiddleware], [requestMiddleware], artPaintingController.getPaintingUnavailableDates);
    // GET-ID
    artRoute.get(`${paintingEndpoint}${paintingExposureType}${identifier}`, [authMiddleware], [requestMiddleware], artPaintingController.getPaintingById);
    // POST
    artRoute.post(`${paintingEndpoint}${paintingExposureType}`, [authMiddleware], [requestMiddleware], artPaintingController.postNewPainting);
    // PUT
    artRoute.put(`${paintingEndpoint}${paintingExposureType}${identifier}`, [authMiddleware], [requestMiddleware], artPaintingController.putPainting);
    // PATCH
    artRoute.patch(`${paintingEndpoint}${paintingExposureType}${identifier}`, [authMiddleware], [requestMiddleware], artPaintingController.patchPainting);
    // DELETE
    artRoute.delete(`${paintingEndpoint}${paintingExposureType}${identifier}`, [authMiddleware], [requestMiddleware], artPaintingController.deletePainting);
    // Devolvemos la configuración del router.
    return artRoute;
}