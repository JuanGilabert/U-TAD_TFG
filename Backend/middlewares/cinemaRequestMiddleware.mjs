// Importamos los modelos/schemas para validar los datos de las peticiones
import { validateCinema, validatePartialCinema } from '../models/art/cinema/cinemaModelValidator.mjs';
////
export async function cinemaRequestMiddleware(req, res, next) {
    if (req.method === 'GET') {
        if (Object.keys(req.query).length > 0) {
            // Obtenemos los valores de la peticion.
            const { fechaInicioPelicula = "hasNoValue", duracionPeliculaMinutos = "hasNoValue" } = req.query;
            // Verificamos que los valores de la peticion sean validos.
            if (fechaInicioPelicula !== "hasNoValue") {
                const fechaInicioPeliculaResult = await validatePartialCinema({ fechaInicioPelicula });
                if (fechaInicioPeliculaResult.error)
                    return res.status(400).json({ message: fechaInicioPeliculaResult.error.message });
            }
            if (duracionPeliculaMinutos !== "hasNoValue") {
                const duracionPeliculaMinutosResult = await validatePartialCinema({ duracionPeliculaMinutos });
                if (duracionPeliculaMinutosResult.error)
                    return res.status(400).json({ message: duracionPeliculaMinutosResult.error.message });
            }
        }
    }
    // Validamos el cuerpo de la peticion.
    if (req.method === 'POST' || req.method === 'PUT') {
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validateCinema(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
    }
    // Validamos el cuerpo de la peticion.
    if (req.method === 'PATCH') {
        // Validaciones del objeto a insertar. Si no hay body valido devolvemos un error.
        const result = await validatePartialCinema(req.body);
        if (result.error) return res.status(422).json({ message: result.error.message });
    }
    //
    if (req.method === 'DELETE') {}
    // Continuamos con el siguiente handler en la cola.
    next();
    return;
}