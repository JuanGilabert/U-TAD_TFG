// Importamos modulos de node.
import { Router } from 'express';
// Importamos el middleware de las request y los controladores.
import { requestMiddleware } from '../../middlewares/requestMiddleware.mjs';
import { CinemaController } from '../../controllers/art/artCinemaController.mjs';
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
    const artCinemaController = new CinemaController({ model: CinemaModel });
    // GET api/art/cinema
    artRoute.get(`${cinemaEndpoint}`, [requestMiddleware], artCinemaController.getAllCinemas);
    // GET-UNAVAILABLE-DATES api/art/cinema/unavailable-dates
    artRoute.get(`${cinemaEndpoint}${unavailableDates}`, [requestMiddleware], artCinemaController.getCinemaUnavailableDates);
    // GET-ID api/art/cinema/:id
    artRoute.get(`${cinemaEndpoint}${identifier}`, [requestMiddleware], artCinemaController.getCinemaById);
    // POST
    artRoute.post(`${cinemaEndpoint}`, [requestMiddleware], artCinemaController.postNewCinema);
    // PUT
    artRoute.put(`${cinemaEndpoint}${identifier}`, [requestMiddleware], artCinemaController.putUpdateCinema);
    // PATCH
    artRoute.patch(`${cinemaEndpoint}${identifier}`, [requestMiddleware], artCinemaController.patchUpdateCinema);
    // DELETE
    artRoute.delete(`${cinemaEndpoint}${identifier}`, [requestMiddleware], artCinemaController.deleteCinema);
    /* Music */
    const musicEndpoint = '/music';
    const artMusicController = new MusicController({ model: MusicModel });
    // GET api/art/music
    artRoute.get(`${musicEndpoint}`, [requestMiddleware], artMusicController.getAllMusics);
    // GET-UNAVAILABLE-DATES api/art/music/unavailable-dates
    artRoute.get(`${musicEndpoint}${unavailableDates}`, [requestMiddleware], artMusicController.getMusicUnavailableDates);
    // GET-ID api/art/music/:id
    artRoute.get(`${musicEndpoint}${identifier}`, [requestMiddleware], artMusicController.getMusicById);
    // POST
    artRoute.post(`${musicEndpoint}`, [requestMiddleware], artMusicController.postNewMusic);
    // PUT
    artRoute.put(`${musicEndpoint}${identifier}`, [requestMiddleware], artMusicController.putUpdateMusic);
    // PATCH
    artRoute.patch(`${musicEndpoint}${identifier}`, [requestMiddleware], artMusicController.patchUpdateMusic);
    // DELETE
    artRoute.delete(`${musicEndpoint}${identifier}`, [requestMiddleware], artMusicController.deleteMusic);
    // api/art/music/video_downloader/:url En la query se enviara el directorio de descarga del video hasta revision del modelo.
    //artRoute.get(`${musicEndpoint}/video_downloader/:url`, [requestMiddleware], artMusicController.getVideoDownloadByUrl);
    /* Painting */
    const paintingEndpoint = '/painting';
    const artPaintingController = new PaintingController({ model: PaintingModel });
    // GET
    artRoute.get(`${paintingEndpoint}`, [requestMiddleware], artPaintingController.getAllPaintings);
    // GET-UNAVAILABLE-DATES api/art/painting/unavailable-dates
    artRoute.get(`${paintingEndpoint}${unavailableDates}`, [requestMiddleware], artPaintingController.getPaintingUnavailableDates);
    // GET-ID
    artRoute.get(`${paintingEndpoint}${identifier}`, [requestMiddleware], artPaintingController.getPaintingById);
    // POST
    artRoute.post(`${paintingEndpoint}`, [requestMiddleware], artPaintingController.postNewPainting);
    // PUT
    artRoute.put(`${paintingEndpoint}${identifier}`, [requestMiddleware], artPaintingController.putUpdatePainting);
    // PATCH
    artRoute.patch(`${paintingEndpoint}${identifier}`, [requestMiddleware], artPaintingController.patchUpdatePainting);
    // DELETE
    artRoute.delete(`${paintingEndpoint}${identifier}`, [requestMiddleware], artPaintingController.deletePainting);
    // Devolvemos la configuración del router.
    return artRoute;
}