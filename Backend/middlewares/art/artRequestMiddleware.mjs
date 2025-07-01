// Importamos los modelos/schemas para validar los datos de las peticiones.
import { validateCinema, validatePartialCinema } from '../../models/art/cinema/cinemaModelValidator.mjs';
import { validateMusic, validatePartialMusic, validateMusicVideoDownload } from '../../models/art/music/musicModelValidator.mjs';
import { validatePainting, validatePartialPainting } from '../../models/art/painting/paintingModelValidator.mjs';
// Importamos los valores de las rutas/endpoints.
import { ART_CINEMA_ROUTE_PATH, ART_MUSIC_ROUTE_PATH, ART_PAINTING_ROUTE_PATH,
    PAINTING_EXPOSURE_ROUTE_PATH, EVENT_ROUTE_PATH, IDENTIFIER_ROUTE_PATH, MUSIC_VIDEO_DOWNLOADER_ROUTE_PATH
} from '../../config/GenericEnvConfig.mjs';
//// Exportamos el middleware.
export async function artRequestMiddleware(req, res, next) {
    // Validamos las queries de la peticion.
    if (req.method === 'GET' && Object.keys(req.query).length > 0) {
        // En caso de que la ruta sea /cinema/event/ evaluamos el cuerpo de la peticion en concreto.
        if(req.path === `${ART_CINEMA_ROUTE_PATH}${EVENT_ROUTE_PATH}/`) {
            // Obtenemos los valores de la peticion.
            const { fechaInicioPelicula = "hasNoValue", duracionPeliculaMinutos = "0" } = req.query;
            // Verificamos que los valores de la peticion sean validos.
            if (fechaInicioPelicula !== "hasNoValue") {
                const fechaInicioPeliculaResult = await validatePartialCinema({ fechaInicioPelicula });
                if (fechaInicioPeliculaResult.error)
                    return res.status(400).json({ message: fechaInicioPeliculaResult.error.message });
            }
            if (duracionPeliculaMinutos !== "0") { 
                // Validar duracionPeliculaMinutos (convertir string a number primero)
                const duracionNumber = Number(duracionPeliculaMinutos); // 0 o el numero de la query param.
                if (!Number.isInteger(duracionNumber))
                    return res.status(400).json({ message: 'duracionPeliculaMinutos debe ser un número entero válido.' });
                const duracionPeliculaMinutosResult = await validatePartialCinema({ duracionPeliculaMinutos: duracionNumber });
                if (duracionPeliculaMinutosResult.error)
                    return res.status(400).json({ message: duracionPeliculaMinutosResult.error.message });
            }
        }
    }
    // Creamos la variable que contendra el resultado de la validacion de zod de los datos recibidos en la peticion.
    let zodValidationResult = "";
    // Validamos el cuerpo de la peticion.
    if (req.method === 'POST') {// obtener result.error.message y result.error.path al ser la base de ZodIssueBase
        // En caso de que la ruta sea /cinema/event/ evaluamos el cuerpo de la peticion en concreto.
        if(req.path === `${ART_CINEMA_ROUTE_PATH}${EVENT_ROUTE_PATH}/`) {
            // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
            zodValidationResult = await validateCinema(req.body);
            //
            req.body = result.data;
        }
        // En caso de que la ruta sea /music/event/ evaluamos el cuerpo de la peticion en concreto.
        if(req.path === `${ART_MUSIC_ROUTE_PATH}${EVENT_ROUTE_PATH}/`) {
            // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
            zodValidationResult = await validateMusic(req.body);
        }
        // En caso de que la ruta sea /music/video-downloader evaluamos el cuerpo de la peticion en concreto.
        if(req.path === `${ART_MUSIC_ROUTE_PATH}${MUSIC_VIDEO_DOWNLOADER_ROUTE_PATH}/`) {
            zodValidationResult = await validateMusicVideoDownload(req.body);
        }
        // En caso de que la ruta sea /painting/exposure/ evaluamos el cuerpo de la peticion en concreto.
        if(req.path === `${ART_PAINTING_ROUTE_PATH}${PAINTING_EXPOSURE_ROUTE_PATH}/`) {
            // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
            zodValidationResult = await validatePainting(req.body);
        }
    }
    // Validamos el cuerpo de la peticion.
    if (req.method === 'PUT') {
        // En caso de que la ruta sea /cinema/event/:id/ evaluamos el cuerpo de la peticion en concreto.
        if(req.path === `${ART_CINEMA_ROUTE_PATH}${EVENT_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}/`) {
            // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
            zodValidationResult = await validateCinema(req.body);
            //
            req.body = result.data;
        }
        // En caso de que la ruta sea /music/event/:id/ evaluamos el cuerpo de la peticion en concreto.
        if(req.path === `${ART_MUSIC_ROUTE_PATH}${EVENT_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}/`) {
            // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
            zodValidationResult = await validateMusic(req.body);
        }
        // En caso de que la ruta sea /painting/exposure/:id/ evaluamos el cuerpo de la peticion en concreto.
        if(req.path === `${ART_PAINTING_ROUTE_PATH}${PAINTING_EXPOSURE_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}/`) {
            // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
            zodValidationResult = await validatePainting(req.body);
        }
    }
    // Validamos el cuerpo de la peticion.
    if (req.method === 'PATCH') {
        // En caso de que la ruta sea /cinema/event/:id/ evaluamos el cuerpo de la peticion en concreto.
        if(req.path === `${ART_CINEMA_ROUTE_PATH}${EVENT_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}/`) {
            // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
            zodValidationResult = await validatePartialCinema(req.body);
            //
            req.body = result.data;
        }
        // En caso de que la ruta sea /music/event/:id/ evaluamos el cuerpo de la peticion en concreto.
        if(req.path === `${ART_MUSIC_ROUTE_PATH}${EVENT_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}/`) {
            // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
            zodValidationResult = await validatePartialMusic(req.body);
        }
        // En caso de que la ruta sea /painting/exposure/:id/ evaluamos el cuerpo de la peticion en concreto.
        if(req.path === `${ART_PAINTING_ROUTE_PATH}${PAINTING_EXPOSURE_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}/`) {
            // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
            zodValidationResult = await validatePartialPainting(req.body);
        }
    }
    if (zodValidationResult.error) return res.status(422).json({ message: zodValidationResult.error.message });
    // Verificamos que no haya queries en la peticion.
    if (req.method === 'DELETE' && Object.keys(req.query).length > 0)
        return res.status(400).json({ message: BAD_REQUEST_400_QUERY_MESSAGE });
    // Continuamos con el siguiente handler en la cola.
    next();
}