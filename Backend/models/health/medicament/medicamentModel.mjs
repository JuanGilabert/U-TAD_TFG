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
        const medicaments = await db.collection(MEDICAMENT_COLLECTION_NAME).find({ userId: userId }, { projection: { userId: 0 } }).toArray();
        return medicaments.length ? medicaments : false;
    }
    static async getMedicamentById(id, userId) {
        const db = await connectDB();
        return db.collection(MEDICAMENT_COLLECTION_NAME).findOne({ userId: userId, _id: id }, { projection: { userId: 0 } });
    }
    static async getMedicamentAvailableDates(userId, fechaCaducidadMedicamento) {
        const db = await connectDB();
        // Si hay query params devolvemos las fechas de las reservas que coinciden con la fecha de la pelicula.
        const startDate = new Date(fechaCaducidadMedicamento);
        const endDate = new Date(startDate);
        endDate.setUTCDate(endDate.getUTCDate() + 1); // suma 1 día
        // Devolvemos una lista de fechas ¿¿??.
        const availableDatesOnDay = await db.collection(MEDICAMENT_COLLECTION_NAME).find(
            {
                userId: userId,
                fechaCaducidadMedicamento: {
                    $gte: startDate,
                    $lt: endDate
                }
            },
            {
                projection: {
                    _id: 0,
                    fechaCaducidadMedicamento: 1 // REVISAR.
                }
            }
        ).toArray();
        // Devolvemos el error si no hay fechas de citas para la fecha indicada.
        if (!availableDatesOnDay.length) return false;
        // Si hay fechas devolvemos la lista de fechas.
        return availableDatesOnDay.map(r => r.fechaCaducidadMedicamento);
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