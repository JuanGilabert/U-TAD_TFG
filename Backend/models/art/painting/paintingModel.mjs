// Modulos de node
import { randomUUID } from 'node:crypto';
// Modulos locales
import { connectDB } from '../../../services/database/connection/mongoDbConnection.mjs';
import { PAINTING_COLLECTION_NAME, RETURN_DOCUMENT_VALUE } from '../../../utils/export/GenericEnvConfig.mjs';
//// Exportamos la clase.
export class PaintingModel {
    static async getAllPaintings(userId) {
        const db = await connectDB();
        const paintings = await db.collection(PAINTING_COLLECTION_NAME).find({ userId: userId }, { projection: { userId: 0 } }).toArray();
        return paintings.length ? paintings : false;
    }
    static async getPaintingById(id, userId) {
        const db = await connectDB();
        return db.collection(PAINTING_COLLECTION_NAME).findOne({ userId: userId, _id: id }, { projection: { userId: 0 } });
    }
    static async getPaintingUnavailableDates(userId, fechaInicioExposicionArte) {
        const db = await connectDB();
        // Si no se pasa la fecha de inicio de la pelicula indica que no hay query params.
        if (fechaInicioExposicionArte === "hasNoValue") {
            // Devolvemos una lista de fechas de las fechas de los documentos
            // donde haya 3 o mas reservas en una misma fecha. Dia: (2025-06-22).
            const unavailableDates = await db.collection(PAINTING_COLLECTION_NAME).aggregate([
                { $match: { userId: userId } },
                // Agrupar por solo la parte de la fecha (ignorando la hora)
                {
                    $group: {
                        _id: {
                            $dateTrunc: {
                                date: "$fechaInicioExposicionArte",
                                unit: "day",
                                timezone: "UTC"
                            }
                        },
                        count: { $sum: 1 }
                    }
                },
                { $match: { count: { $gte: 3 } } },
                { $project: { _id: 0, fecha: "$_id" } }
            ]).toArray();
            // Si no hay fechas no disponibles, es decir si la variable unavailableDates no tiene valores, devolvemos false.
            if (!unavailableDates.length) return { message: "unavailableDatesError" };
            // Si hay fechas no disponibles devolvemos una lista de fechas.
            return unavailableDates.map(r => r.fecha);
        } else {
            // Si hay query params devolvemos las fechas de las reservas que coinciden con la fecha de la pelicula.
            const startDate = new Date(fechaInicioExposicionArte);
            const endDate = new Date(startDate);
            endDate.setUTCDate(endDate.getUTCDate() + 1); // suma 1 día
            // Devolvemos una lista de fechas ¿¿??.
            const availableDatesOnDay = await db.collection(PAINTING_COLLECTION_NAME).find(
                {
                    userId: userId,
                    fechaInicioExposicionArte: {
                        $gte: startDate,
                        $lt: endDate
                    }
                },
                {
                    projection: {
                        _id: 0,
                        fechaInicioExposicionArte: 1 // REVISAR.
                    }
                }
            ).toArray();
            // Devolvemos el error si no hay fechas de citas para la fecha indicada.
            if (!availableDatesOnDay.length) return { message: "availableDatesError" };
            // Si hay fechas devolvemos la lista de fechas.
            return availableDatesOnDay.map(r => r.fechaInicioExposicionArte);
        }
    }
    static async postNewPainting({ painting, userId }) {
        const db = await connectDB();
        const newPainting = {
            ...painting,
            userId: userId,
            _id: randomUUID()
        }
        const { insertedId } = await db.collection(PAINTING_COLLECTION_NAME).insertOne(newPainting);
        return { id: insertedId, ...newPainting };
    }
    static async putUpdatePainting({ id, painting, userId }) {
        const db = await connectDB();
        const newPainting = {
            ...painting,
            userId: userId,
            _id: id
        }
        const value = await db.collection(PAINTING_COLLECTION_NAME).findOneAndReplace(
            { userId: userId, _id: id }, newPainting, { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Devolvemos false si no hay valor.
        if (!value) return false;
        return value;
    }
    static async patchUpdatePainting({ id, painting, userId }) {
        const db = await connectDB();
        const value = db.collection(PAINTING_COLLECTION_NAME).findOneAndUpdate(
            { userId: userId, _id: id }, { $set: { ...painting, userId: userId } },
            { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Devolvemos false si no hay valor.
        if (!value) return false;
        return value;
    }
    static async deletePainting(id, userId) {
        const db = await connectDB();
        return (await db.collection(PAINTING_COLLECTION_NAME).findOneAndDelete({ userId: userId, _id: id })) !== null;
    }
}