import { Router } from 'express';
// Importamos el middleware de autenticacion y los controladores
import { authMiddleware } from '../../middlewares/auth/authMiddleware.mjs';
import { CinemaController } from '../../controllers/art/artCinemaController.mjs';
import { MusicController } from '../../controllers/art/artMusicController.mjs';
import { PaintingController } from '../../controllers/art/artPaintingController.mjs';
export const artRouter = ({ CinemaModel, MusicModel, PaintingModel }) => {
    const artRoute = Router();
    const identifier = "/:id";
    /* Cinema */
    const cinemaEndpoint = '/cinema';
    const artCinemaController = new CinemaController({ CinemaModel });
    // GET api/art/cinema
    artRoute.get(`${cinemaEndpoint}`, [authMiddleware], artCinemaController.getAllCinemas);
    // GET-ID api/art/cinema/:id
    artRoute.get(`${cinemaEndpoint}${identifier}`, [authMiddleware], artCinemaController.getCinemaById);
    // POST
    artRoute.post(`${cinemaEndpoint}`, [authMiddleware], artCinemaController.postNewCinema);
    // PUT
    artRoute.put(`${cinemaEndpoint}${identifier}`, [authMiddleware], artCinemaController.putUpdateCinema);
    // PATCH
    artRoute.patch(`${cinemaEndpoint}${identifier}`, [authMiddleware], artCinemaController.patchUpdateCinema);
    // DELETE
    artRoute.delete(`${cinemaEndpoint}${identifier}`, [authMiddleware], artCinemaController.deleteCinema);
    /* Music */
    const musicEndpoint = '/music';
    const artMusicController = new MusicController({ MusicModel });
    // GET api/art/music
    artRoute.get(`${musicEndpoint}`, [authMiddleware], artMusicController.getAllMusics);
    // GET-ID api/art/music/:id
    artRoute.get(`${musicEndpoint}${identifier}`, [authMiddleware], artMusicController.getMusicById);
    // POST
    artRoute.post(`${musicEndpoint}`, [authMiddleware], artMusicController.postNewMusic);
    // PUT
    artRoute.put(`${musicEndpoint}${identifier}`, [authMiddleware], artMusicController.putUpdateMusic);
    // PATCH
    artRoute.patch(`${musicEndpoint}${identifier}`, [authMiddleware], artMusicController.patchUpdateMusic);
    // DELETE
    artRoute.delete(`${musicEndpoint}${identifier}`, [authMiddleware], artMusicController.deleteMusic);
    // api/art/music/video_downloader/:url En la query se enviara el directorio de descarga del video hasta revision del modelo.
    artRoute.get(`${musicEndpoint}/video_downloader/:url`, [authMiddleware], artMusicController.getVideoDownloadByUrl);
    /* Painting */
    const paintingEndpoint = '/painting';
    const artPaintingController = new PaintingController({ PaintingModel });
    // GET
    artRoute.get(`${paintingEndpoint}`, [authMiddleware], artPaintingController.getAllPaintings);
    // GET-ID
    artRoute.get(`${paintingEndpoint}${identifier}`, [authMiddleware], artPaintingController.getPaintingById);
    // POST
    artRoute.post(`${paintingEndpoint}`, [authMiddleware], artPaintingController.postNewPainting);
    // PUT
    artRoute.put(`${paintingEndpoint}${identifier}`, [authMiddleware], artPaintingController.putUpdatePainting);
    // PATCH
    artRoute.patch(`${paintingEndpoint}${identifier}`, [authMiddleware], artPaintingController.patchUpdatePainting);
    // DELETE
    artRoute.delete(`${paintingEndpoint}${identifier}`, [authMiddleware], artPaintingController.deletePainting);
    //
    return artRoute;
}