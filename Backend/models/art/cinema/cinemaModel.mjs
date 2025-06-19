// Modulos de node.
import { randomUUID } from 'node:crypto';
// Modulos locales.
import { connectDB } from '../../../services/database/connection/mongoDbConnection.mjs';
import { CINEMA_COLLECTION_NAME, RETURN_DOCUMENT_VALUE } from '../../../utils/export/GenericEnvConfig.mjs';
//// Exportamos la clase.
export class CinemaModel {
    static async getAllCinemas(userId, fechaInicioPelicula, duracionPeliculaMinutos) {
        const db = await connectDB();
        // Verificamos que existen documentos en la coleccion relativos al usuario logueado mediante su id(userId).
        // Si no existen documentos devolvemos null para que se devuelva un 404 en el controlador.
        if (!(await db.collection(CINEMA_COLLECTION_NAME).findOne({ userId }))) return null;
        // Ningun parametro de la query tiene valor. por lo tanto no hay query params y devolvemos todos los cinemas.
        if(fechaInicioPelicula === "hasNoValue" && duracionPeliculaMinutos === "hasNoValue") {
            const cinemas = await db.collection(CINEMA_COLLECTION_NAME).find({ userId: userId }, { projection: { userId: 0 } }).toArray();
            return cinemas.length ? cinemas : null;
        }
        // Creamos  fechas de inicio y fin del dia sin tener en cuneta las horas.
        const startDate = new Date(fechaInicioPelicula.split("T")[0]);
        const endDate = new Date(startDate);
        endDate.setUTCDate(endDate.getUTCDate() + 1);
        // Ambos parametros de la query tienen valor.
        if(fechaInicioPelicula !== "hasNoValue" && duracionPeliculaMinutos !== "hasNoValue") {
            const cinemas = await db.collection(CINEMA_COLLECTION_NAME).find(
                {
                    userId: userId,
                    fechaInicioPelicula: { $gte: startDate, $lt: endDate },
                    duracionPeliculaMinutos: duracionPeliculaMinutos
                },
                { projection: { userId: 0 } }
            ).toArray();
            return cinemas.length ? cinemas : false;
        }
        if(fechaInicioPelicula !== "hasNoValue") {
            // Como hay query params, devolvemos las fechas de las reservas que coinciden con la fecha de la pelicula.
            const cinemas = await db.collection(CINEMA_COLLECTION_NAME).find(
                { userId: userId, fechaInicioPelicula: { $gte: startDate, $lt: endDate } },
                { projection: { userId: 0 } }
            ).toArray();
            return  cinemas.length ? cinemas : false;
        }
        if(duracionPeliculaMinutos !== "hasNoValue") {
            const cinemas = await db.collection(CINEMA_COLLECTION_NAME).find(
                { userId: userId, duracionPeliculaMinutos: duracionPeliculaMinutos },
                { projection: { userId: 0 } }
            ).toArray();
            return cinemas.length ? cinemas : false;
        }
    }
    static async getCinemaById(id, userId) {
        /* El metodo .findOne() nos devuelve el documento en caso de existir o null en caso de no existir el documento indicado.
        Tambien puede provocar excepciones en tiempo de ejecucion si el id es incorrecto. Se captura en el controlador. */
        const db = await connectDB();
        return db.collection(CINEMA_COLLECTION_NAME).findOne({ userId: userId, _id: id }, { projection: { userId: 0 } });
    }
    static async getCinemaUnavailableDates(userId) {
        const db = await connectDB();
        // Verificamos que existen documentos en la coleccion relativos al usuario logueado mediante su id(userId).
        // Si no existen documentos devolvemos null para que se devuelva un 404 en el controlador.
        if (!(await db.collection(CINEMA_COLLECTION_NAME).findOne({ userId }))) return null;
        // Obtenemos una lista de fechas de las fechas de los documentos
        // donde haya 3 o mas reservas en una misma fecha. Dia: (2025-06-22).
        const unavailableDates = await db.collection(CINEMA_COLLECTION_NAME).aggregate([
            { $match: { userId: userId } },
            // Agrupar por solo la parte de la fecha (ignorando la hora)
            {
                $group: {
                    _id: { $dateTrunc: { date: "$fechaInicioPelicula", unit: "day", timezone: "UTC" } },
                    count: { $sum: 1 }
                }
            },
            { $match: { count: { $gte: 3 } } },
            { $project: { _id: 0, fecha: "$_id" } }
        ]).toArray();
        // Si no hay fechas no disponibles, es decir si la variable unavailableDates no tiene al menos 1 valor, devolvemos el error.
        return unavailableDates.length ?
        { dates: unavailableDates.map(document => document.fecha.toISOString()) } : false;
    } 
    static async postCinema({ cinema, userId }) {
        const db = await connectDB();
        const newCinema = {
            ...cinema,
            userId: userId,
            _id: randomUUID()
        };
        // Obtenemos el resultado de la operación de inserción.
        try {
            const { acknowledged, insertedId } = await db.collection(CINEMA_COLLECTION_NAME).insertOne(newCinema);
            //if (!acknowledged) throw new Error('La operación de inserción no fue reconocida por MongoDB.');
            return !acknowledged ? false : { id: insertedId, ...newCinema };
        } catch (error) {
            console.error('Error en postNewMusic:', error);
            throw new Error(`No se pudo insertar la música en la base de datos: ${error}`);
        }
    }
    static async putCinema({ id, cinema, userId }) {
        const db = await connectDB();
        const newCinema = {
            ...cinema,
            userId: userId,
            _id: id
        };
        // Obtenemos el resultado de la operación de actualización.
        const { value, lastErrorObject, ok } = await db.collection(CINEMA_COLLECTION_NAME).findOneAndReplace(
            { userId: userId, _id: id }, newCinema, { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Si ok es 0 devolvemos el error obtenido.
        if (!ok && lastErrorObject) throw new Error(`No se pudo actualizar la actividad: ${lastErrorObject}`);
        // Devolvemos false si no hay valor(null).
        if (ok && !value) return false;
        // Devolvemos el documento actualizado.
        if (ok && value) return value;
    }
    static async patchCinema({ id, cinema, userId }) {
        const db = await connectDB();
        // Obtenemos el resultado de la operación de actualización.
        const { value, lastErrorObject, ok } = await db.collection(CINEMA_COLLECTION_NAME).findOneAndUpdate(
            { userId: userId, _id: id }, { $set: { ...cinema, userId: userId } },
            { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Si ok es 0 devolvemos el error obtenido.
        if (!ok && lastErrorObject) throw new Error(`No se pudo actualizar la actividad: ${lastErrorObject}`);
        // Devolvemos false si no hay valor(null).
        if (ok && !value) return false;
        // Devolvemos el documento actualizado.
        if (ok && value) return value;
    }
    static async deleteCinema(id, userId) {
        const db = await connectDB();
        return (await db.collection(CINEMA_COLLECTION_NAME).findOneAndDelete({ userId: userId, _id: id })) !== null;
    }
}