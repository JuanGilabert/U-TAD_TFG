// Modulos de node.
import { randomUUID } from 'node:crypto';
// Modulos locales.
import { connectDB } from '../../../services/database/connection/mongoDbConnection.mjs';
import { MEDICAMENT_COLLECTION_NAME, RETURN_DOCUMENT_VALUE } from '../../../utils/export/GenericEnvConfig.mjs';
//// Exportamos la clase.
/* Esquema para el modelo de datos de medicamentos en el sistema de salud.
Define la estructura de los datos de un medicamento almacenado en la base de datos. */
export class MedicamentModel {
    static async getAllMedicaments(userId, fechaCaducidadMedicamento) {
        const db = await connectDB();
        // Verificamos que existen documentos en la coleccion relativos al usuario logueado mediante su id(userId).
        // Si no existen documentos devolvemos null para que se devuelva un 404 en el controlador.
        if (!(await db.collection(MUSIC_COLLECTION_NAME).findOne({ userId }))) return null;
        //
        if (fechaCaducidadMedicamento === "hasNoValue") {
            const medicaments = await db.collection(MEDICAMENT_COLLECTION_NAME).find(
                { userId: userId }, { projection: { userId: 0 } }
            ).toArray();
            return medicaments.length ? medicaments : null;
        }
        // Creamos  fechas de inicio y fin del dia sin tener en cuenta las horas.
        const startDate = new Date(fechaCaducidadMedicamento.split("T")[0]);//
        const endDate = new Date(startDate);
        endDate.setUTCDate(endDate.getUTCDate() + 1);
        // Como hay query params, devolvemos las fechas de las reservas que coinciden con la fecha de la pelicula.
        const medicaments = await db.collection(MEDICAMENT_COLLECTION_NAME).find(
            { userId: userId, fechaCaducidadMedicamento: { $gte: startDate, $lt: endDate } },
            { projection: { userId: 0 } }
        ).toArray();
        return medicaments.length ? medicaments: false;
    }
    static async getMedicamentById(id, userId) {
        const db = await connectDB();
        return db.collection(MEDICAMENT_COLLECTION_NAME).findOne(
            { userId: userId, _id: id }, { projection: { userId: 0 } }
        );
    }
    static async getMedicamentExpirationDates(userId) {
        const db = await connectDB();
        // Verificamos que existen documentos en la coleccion relativos al usuario logueado mediante su id(userId).
        // Si no existen documentos devolvemos null para que se devuelva un 404 en el controlador.
        if (!(await db.collection(MUSIC_COLLECTION_NAME).findOne({ userId }))) return null;
        // Obtenemos una lista de fechas de las fechas de los documentos
        // donde haya 3 o mas reservas en una misma fecha. Dia: (2025-06-22).
        const expirationDates = await db.collection(MEDICAMENT_COLLECTION_NAME).aggregate([
            { $match: { userId: userId } },
            // Agrupar por solo la parte de la fecha (ignorando la hora)
            {
                $group: {
                    _id: { $dateTrunc: { date: "$fechaCaducidadMedicamento", unit: "day", timezone: "UTC" } },
                    count: { $sum: 1 }
                }
            },
            { $match: { count: { $gte: 3 } } },
            { $project: { _id: 0, fecha: "$_id" } }
        ]).toArray();
        // Si hay fechas de caducidad, es decir si la variable expirationDates tiene valores, devolvemos las fechas en formato ISO-8601(yyyy-mm-ddT00:00:00.000Z).
        return expirationDates.length ?
        { dates: expirationDates.map(document => document.fecha.toISOString()) } : { dates: [] };
    }
    static async postMedicament({ medicament, userId }) {
        const db = await connectDB();
        const newMedicament = {
            ...medicament,
            userId: userId,
            _id: randomUUID()
        };
        // Obtenemos el resultado de la operación de inserción.
        try {
            const { acknowledged, insertedId } = await db.collection(MEDICAMENT_COLLECTION_NAME).insertOne(newMedicament);
            //if (!acknowledged) throw new Error('La operación de inserción no fue reconocida por MongoDB.');
            return !acknowledged ? false : { id: insertedId, ...newMedicament };
        } catch (error) {
            console.error('Error en postNewMedicament:', error);
            throw new Error(`No se pudo insertar el medicamento en la base de datos: ${error}`);
        }
    }
    static async putMedicament({ id, medicament, userId }) {
        const db = await connectDB();
        const newMedicament = {
            ...medicament,
            userId: userId,
            _id: id
        };
        // Obtenemos el resultado de la operación de actualización.
        const { value, lastErrorObject, ok } = await db.collection(MEDICAMENT_COLLECTION_NAME).findOneAndReplace(
            { userId: userId, _id: id }, newMedicament, { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Si ok es 0 devolvemos el error obtenido.
        if (!ok && lastErrorObject) throw new Error(`No se pudo actualizar el medicamento: ${lastErrorObject}`);
        // Devolvemos false si no hay valor(null).
        if (ok && !value) return false;
        // Devolvemos el documento actualizado.
        if (ok && value) return value;
    }
    static async patchMedicament({ id, medicament, userId }) {
        const db = await connectDB();
        // Obtenemos el resultado de la operación de actualización.
        const { value, lastErrorObject, ok } = await db.collection(MEDICAMENT_COLLECTION_NAME).findOneAndUpdate(
            { userId: userId, _id: id }, { $set: { ...medicament, userId: userId } }, { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Si ok es 0 devolvemos el error obtenido.
        if (!ok && lastErrorObject) throw new Error(`No se pudo actualizar el medicamento: ${lastErrorObject}`);
        // Devolvemos false si no hay valor(null).
        if (ok && !value) return false;
        // Devolvemos el documento actualizado.
        if (ok && value) return value;
    }
    static async deleteMedicament(id, userId) {
        const db = await connectDB();
        return (await db.collection(MEDICAMENT_COLLECTION_NAME).findOneAndDelete({ userId: userId, _id: id })) !== null;
    }
}