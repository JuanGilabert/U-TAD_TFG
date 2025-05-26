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
        // Obtenemos una lista de fechas de las fechas de los documentos
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
        const unavailableDatesList = unavailableDates.map(d => d.fecha);
        /* Si no se pasa la fecha de inicio de la pelicula indica que no hay query params
        y por lo tanto si hay fechas no disponibles devolvemos una lista de fechas. */
        if (fechaInicioTarea === "hasNoValue") {
            // Si no hay fechas no disponibles, es decir si la variable unavailableDates no tiene valores, devolvemos el error.
            if (unavailableDates.length) return { dates: unavailableDates.map(date => date.fecha.toISOString()) };
            return { message: "unavailableDatesError" };
        }
        // Creamos un Set con las fechas no disponibles en milisegundos para búsqueda rápida y efectiva.
        const unavailableDateSet = new Set(unavailableDatesList.map(d => d.getTime()));
        // Creamos  fechas de inicio y fin del dia sin tener en cuneta las horas.
        const startDate = new Date(fechaInicioTarea.split("T")[0]);
        const endDate = new Date(startDate);
        endDate.setUTCDate(endDate.getUTCDate() + 1);
        // Como hay query params, devolvemos las fechas de las reservas que coinciden con la fecha de la pelicula.
        const availableDatesOnDay = await db.collection(WORK_COLLECTION_NAME).find(
            { userId: userId, fechaInicioTarea: { $gte: startDate, $lt: endDate } },
            { projection: { _id: 0, fechaInicioTarea: 1 } }
        ).toArray();
        // Devolvemos el error si no hay fechas de citas para la fecha indicada.
        if (!availableDatesOnDay.length) return { message: "availableDatesError" };
        // Si hay reservas en la fecha indicada devolvemos la lista de reservas en la fecha indicada
        // mapeada para devolver una lista de string.
        const filteredDates = availableDatesOnDay.filter(d => {
            const dateStr = d.fechaInicioTarea.toISOString().split("T")[0];
            return !unavailableDateSet.has(new Date(dateStr).getTime());
        });
        if (!filteredDates.length) return { message: "filteredAvailableDatesError" };
        return { dates: filteredDates.map(date => date.fechaInicioTarea.toISOString()) };
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