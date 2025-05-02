// Modulos de node.
import { randomUUID } from 'node:crypto';
// Modulos locales.
import { connectDB } from '../../services/database/connection/mongoDbConnection.mjs';
import { WORK_COLLECTION_NAME, RETURN_DOCUMENT_VALUE } from '../../utils/export/GenericEnvConfig.mjs';
//// Exportamos la clase.
export class WorkModel {
    static async getAllWorks(userId) {
        const db = await connectDB();
        const works = await db.collection(WORK_COLLECTION_NAME).find({ userId: userId }, { projection: { userId: 0 } }).toArray();
        return works.length ? works : false;
    }
    static async getWorkById(id, userId) {
        const db = await connectDB();
        return db.collection(WORK_COLLECTION_NAME).findOne({ userId: userId, _id: id }, { projection: { userId: 0 } });
    }
    static async getWorkUnavailableDates(userId, fechaInicioTarea) {
        const db = await connectDB();
        // Si no se pasa la fecha de inicio de la pelicula indica que no hay query params.
        if (fechaInicioTarea === "hasNoValue") {
            // Devolvemos una lista de fechas de las fechas de los documentos
            // donde haya 3 o mas reservas en una misma fecha. Dia: (2025-06-22).
            const unavailableDates = await db.collection(WORK_COLLECTION_NAME).aggregate([
                { $match: { userId: userId } },
                // Agrupar por solo la parte de la fecha (ignorando la hora)
                {
                    $group: {
                        _id: {
                            $dateTrunc: {
                                date: "$fechaInicioTarea",
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
            const startDate = new Date(fechaInicioTarea);
            const endDate = new Date(startDate);
            endDate.setUTCDate(endDate.getUTCDate() + 1); // suma 1 día
            // Devolvemos una lista de fechas ¿¿??.
            const availableDatesOnDay = await db.collection(WORK_COLLECTION_NAME).find(
                {
                    userId: userId,
                    fechaInicioTarea: {
                        $gte: startDate,
                        $lt: endDate
                    }
                },
                {
                    projection: {
                        _id: 0,
                        fechaInicioTarea: 1 // REVISAR.
                    }
                }
            ).toArray();
            // Devolvemos el error si no hay fechas de citas para la fecha indicada.
            if (!availableDatesOnDay.length) return { message: "availableDatesError" };
            // Si hay fechas devolvemos la lista de fechas.
            return availableDatesOnDay.map(r => r.fechaInicioTarea);
        }
    }
    static async postNewWork({ work, userId }) {
        const db = await connectDB();
        const newWork = {
            ...work,
            userId: userId,
            _id: randomUUID()
        };
        const { insertedId } = await db.collection(WORK_COLLECTION_NAME).insertOne(newWork);
        return { id: insertedId, ...newWork };
    }
    static async putUpdateWork({ id, work, userId }) {
        const db = await connectDB();
        const newWork = {
            ...work,
            userId: userId,
            _id: id
        };
        const value = await db.collection(WORK_COLLECTION_NAME).findOneAndReplace(
            { userId: userId, _id: id }, newWork, { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Devolvemos false si no hay valor.
        if (!value) return false;
        return value;
    }
    static async patchUpdateWork({ id, work, userId }) {
        const db = await connectDB();
        const value = await db.collection(WORK_COLLECTION_NAME).findOneAndUpdate(
            { userId: userId, _id: id }, { $set: { ...work, userId: userId } },
            { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Devolvemos false si no hay valor.
        if (!value) return false;
        return value;
    }
    static async deleteWork(id, userId) {
        const db = await connectDB();
        return (await db.collection(WORK_COLLECTION_NAME).findOneAndDelete({ userId: userId, _id: id })) !== null;
    }
}