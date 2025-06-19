// Modulos de node
import { randomUUID } from 'node:crypto';
// Modulos locales
import { connectDB } from '../../../services/database/connection/mongoDbConnection.mjs';
import { PAINTING_COLLECTION_NAME, RETURN_DOCUMENT_VALUE } from '../../../utils/export/GenericEnvConfig.mjs';
//// Exportamos la clase.
export class PaintingModel {
    static async getAllPaintings(userId, fechaInicioExposicionArte) {
        const db = await connectDB();
        // Verificamos que existen documentos en la coleccion relativos al usuario logueado mediante su id(userId).
        // Si no existen documentos devolvemos null para que se devuelva un 404 en el controlador.
        if (!(await db.collection(MUSIC_COLLECTION_NAME).findOne({ userId }))) return null;
        // No hay query params.
        if (fechaInicioExposicionArte === "hasNoValue") {
            const paintings = await db.collection(PAINTING_COLLECTION_NAME).find(
                { userId: userId }, { projection: { userId: 0 } }
            ).toArray();
            return paintings.length ? paintings : null;
        }
        // Creamos  fechas de inicio y fin del dia sin tener en cuneta las horas.
        const startDate = new Date(fechaInicioExposicionArte.split("T")[0]);
        const endDate = new Date(startDate);
        endDate.setUTCDate(endDate.getUTCDate() + 1);
        // Como hay query params, devolvemos las fechas de las reservas que coinciden con la fecha de la pelicula.
        const paintings = await db.collection(PAINTING_COLLECTION_NAME).find(
            { userId: userId, fechaInicioExposicionArte: { $gte: startDate, $lt: endDate } },
            { projection: { userId: 0 } }
        ).toArray();
        //
        return paintings.length ? paintings : false;
    }
    static async getPaintingById(id, userId) {
        const db = await connectDB();
        return db.collection(PAINTING_COLLECTION_NAME).findOne({ userId: userId, _id: id }, { projection: { userId: 0 } });
    }
    static async getPaintingUnavailableDates(userId) {
        const db = await connectDB();
        // Verificamos que existen documentos en la coleccion relativos al usuario logueado mediante su id(userId).
        // Si no existen documentos devolvemos null para que se devuelva un 404 en el controlador.
        if (!(await db.collection(PAINTING_COLLECTION_NAME).findOne({ userId }))) return null;
        // Obtenemos una lista de fechas de las fechas de los documentos
        // donde haya 3 o mas reservas en una misma fecha. Dia: (2025-06-22).
        const unavailableDates = await db.collection(PAINTING_COLLECTION_NAME).aggregate([
            { $match: { userId: userId } },
            // Agrupar por solo la parte de la fecha (ignorando la hora)
            {
                $group: {
                    _id: { $dateTrunc: { date: "$fechaInicioExposicionArte", unit: "day", timezone: "UTC" } },
                    count: { $sum: 1 }
                }
            },
            { $match: { count: { $gte: 3 } } },
            { $project: { _id: 0, fecha: "$_id" } }
        ]).toArray();
        // Si no hay fechas no disponibles, es decir si la variable unavailableDates no tiene al menos 1 valor, devolvemos el error.
        return unavailableDates.length ?
        { dates: unavailableDates.map(document => document.fecha.toISOString()) } : { dates: [] };
    }
    static async postPainting({ painting, userId }) {
        const db = await connectDB();
        const newPainting = {
            ...painting,
            userId: userId,
            _id: randomUUID()
        }
        // Obtenemos el resultado de la operación de inserción.
        try {
            const { acknowledged, insertedId } = await db.collection(PAINTING_COLLECTION_NAME).insertOne(newPainting);
            //if (!acknowledged) throw new Error('La operación de inserción no fue reconocida por MongoDB.');
            return !acknowledged ? false : { id: insertedId, ...newPainting };
        } catch (error) {
            console.error('Error en postPainting:', error);
            throw new Error(`No se pudo insertar la exposicion en la base de datos: ${error}`);
        }
    }
    static async putPainting({ id, painting, userId }) {
        const db = await connectDB();
        const newPainting = {
            ...painting,
            userId: userId,
            _id: id
        }
        // Obtenemos el resultado de la operación de actualización.
        const { value, lastErrorObject, ok } = await db.collection(PAINTING_COLLECTION_NAME).findOneAndReplace(
            { userId: userId, _id: id }, newPainting, { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Si ok es 0 devolvemos el error obtenido.
        if (!ok && lastErrorObject) throw new Error(`No se pudo actualizar la exposicion: ${lastErrorObject}`);
        // Devolvemos false si no hay valor(null).
        if (ok && !value) return false;
        // Devolvemos el documento actualizado.
        if (ok && value) return value;
    }
    static async patchPainting({ id, painting, userId }) {
        const db = await connectDB();
        // Obtenemos el resultado de la operación de actualización.
        const { value, lastErrorObject, ok } = await db.collection(PAINTING_COLLECTION_NAME).findOneAndUpdate(
            { userId: userId, _id: id }, { $set: { ...painting, userId: userId } },
            { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Si ok es 0 devolvemos el error obtenido.
        if (!ok && lastErrorObject) throw new Error(`No se pudo actualizar la exposicion: ${lastErrorObject}`);
        // Devolvemos false si no hay valor(null).
        if (ok && !value) return false;
        // Devolvemos el documento actualizado.
        if (ok && value) return value;
    }
    static async deletePainting(id, userId) {
        const db = await connectDB();
        return (await db.collection(PAINTING_COLLECTION_NAME).findOneAndDelete({ userId: userId, _id: id })) !== null;
    }
}