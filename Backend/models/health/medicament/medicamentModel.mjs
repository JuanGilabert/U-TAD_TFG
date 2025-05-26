// Modulos de node.
import { randomUUID } from 'node:crypto';
// Modulos locales.
import { connectDB } from '../../../services/database/connection/mongoDbConnection.mjs';
import { MEDICAMENT_COLLECTION_NAME, RETURN_DOCUMENT_VALUE } from '../../../utils/export/GenericEnvConfig.mjs';
//// Exportamos la clase.
/* Esquema para el modelo de datos de medicamentos en el sistema de salud.
Define la estructura de los datos de un medicamento almacenado en la base de datos. */
export class MedicamentModel {
    static async getAllMedicaments(userId) {
        const db = await connectDB();
        const medicaments = await db.collection(MEDICAMENT_COLLECTION_NAME).find(
            { userId: userId }, { projection: { userId: 0 } }
        ).toArray();
        return medicaments.length ? medicaments : false;
    }
    static async getMedicamentById(id, userId) {
        const db = await connectDB();
        return db.collection(MEDICAMENT_COLLECTION_NAME).findOne(
            { userId: userId, _id: id }, { projection: { userId: 0 } }
        );
    }
    static async getMedicamentExpirationDates(userId, fechaCaducidadMedicamento) {
        const db = await connectDB();
        //// Not query params
        const now = new Date();
        const todayMidnight = new Date(now.getFullYear(), now.getMonth(), now.getDate());//YYYY-MM-DDT00:00:00.000Z
        //const tomorrowMidnight = todayMidnight.setUTCDate(todayMidnight.getUTCDate() + 1);
        /*const expirationDates = await db.collection(MEDICAMENT_COLLECTION_NAME).find(
            { userId: userId, fechaCaducidadMedicamento: { $gte: todayMidnight } },
            { projection: { userId: 0 } }
        ).toArray();*/
        // Obtenemos una lista de fechas de las fechas de los documentos
        // donde haya 3 o mas reservas en una misma fecha. Dia: (2025-06-22).
        const expirationDates = await db.collection(CINEMA_COLLECTION_NAME).aggregate([
            { $match: { userId: userId } },
            // Agrupar por solo la parte de la fecha (ignorando la hora)
            {
                $group: {
                    _id: {
                        $dateTrunc: {
                            date: "$fechaCaducidadMedicamento",
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
        // Si no se pasa la fecha de inicio de la pelicula indica que no hay query params.
        if (fechaCaducidadMedicamento === "hasNoValue") {
            // Si hay fechas de caducidad, es decir si la variable expirationDates tiene valores, devolvemos las fechas en formato ISO-8601(yyyy-mm-ddT00:00:00.000Z).
            if (expirationDates.length) return { dates: expirationDates.map(date => date.fecha.toISOString()) };
            return { message: "medicamentExpirationDatesError" };
        }
        //// Query params
        // Creamos  fechas de inicio y fin del dia sin tener en cuenta las horas.
        const startDate = new Date(fechaCaducidadMedicamento.split("T")[0]);//
        const endDate = new Date(startDate);
        endDate.setUTCDate(endDate.getUTCDate() + 1);
        // Como hay query params, devolvemos las fechas de las reservas que coinciden con la fecha de la pelicula.
        const medicamentExpirationDatesByDate = await db.collection(MEDICAMENT_COLLECTION_NAME).find(
            { userId: userId, fechaCaducidadMedicamento: { $gte: startDate, $lt: endDate } },
            { projection: { userId: 0 } }
        ).toArray();
        // Si hay fechas de caducidad, es decir si la variable medicamentExpirationDatesOnDay tiene valores, devolvemos las fechas.
        if (medicamentExpirationDatesByDate.length) return { dates: medicamentExpirationDatesByDate.map(date => date.fechaCaducidadMedicamento.toISOString()) };
        // Devolvemos el error si no hay fechas de caducidad para la fecha indicada.
        return { message: "medicamentExpirationDatesByDateError" };
    }
    static async postNewMedicament({ medicament, userId }) {
        const db = await connectDB();
        const newMedicament = {
            ...medicament,
            userId: userId,
            _id: randomUUID()
        };
        const { insertedId } = await db.collection(MEDICAMENT_COLLECTION_NAME).insertOne(newMedicament);
        return { id: insertedId, ...newMedicament };
    }
    static async putMedicament({ id, medicament, userId }) {
        const db = await connectDB();
        const newMedicament = {
            ...medicament,
            userId: userId,
            _id: id
        };
        const value = await db.collection(MEDICAMENT_COLLECTION_NAME).findOneAndReplace(
            { userId: userId, _id: id }, newMedicament, { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Devolvemos false si no hay valor.
        if (!value) return false;
        return value;
    }
    static async patchMedicament({ id, medicament, userId }) {
        const db = await connectDB();
        const value = await db.collection(MEDICAMENT_COLLECTION_NAME).findOneAndUpdate(
            { userId: userId, _id: id }, { $set: { ...medicament, userId: userId } }, { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Devolvemos false si no hay valor.
        if (!value) return false;
        return value;
    }
    static async deleteMedicament(id, userId) {
        const db = await connectDB();
        return (await db.collection(MEDICAMENT_COLLECTION_NAME).findOneAndDelete({ userId: userId, _id: id })) !== null;
    }
}