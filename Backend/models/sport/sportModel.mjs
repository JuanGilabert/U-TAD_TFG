// Modulos de node
import { randomUUID } from 'node:crypto';
// Modulos locales
import { connectDB } from '../../services/database/connection/mongoDbConnection.mjs';
import { SPORT_COLLECTION_NAME, RETURN_DOCUMENT_VALUE } from '../../utils/export/GenericEnvConfig.mjs';
//// Exportamos la clase.
export class SportModel {
    static async getAllSports(userId, fechaInicioActividad, duracionActividadMinutos) {
        const db = await connectDB();
        // Verificamos que existen documentos en la coleccion relativos al usuario logueado mediante su id(userId).
        // Si no existen documentos devolvemos null para que se devuelva un 404 en el controlador.
        if (!(await db.collection(SPORT_COLLECTION_NAME).findOne({ userId }))) return null;
        //
        if (fechaInicioActividad === "hasNoValue" && duracionActividadMinutos === "hasNoValue") {
            const sports = await db.collection(SPORT_COLLECTION_NAME).find({ userId: userId }, { projection: { userId: 0 } }).toArray();
            return sports.length ? sports : null;
        }
        //
        const startDate = new Date(fechaInicioActividad.split("T")[0]);
        const endDate = new Date(startDate);
        endDate.setUTCDate(endDate.getUTCDate() + 1);
        //
        if (fechaInicioActividad !== "hasNoValue" && duracionActividadMinutos !== "hasNoValue") {
            const sports = await db.collection(SPORT_COLLECTION_NAME).find(
                {
                    userId: userId,
                    fechaInicioActividad: { $gte: startDate, $lt: endDate },
                    duracionActividadMinutos: duracionActividadMinutos
                },
                { projection: { userId: 0 } }
            ).toArray();
            return sports.length ? sports : false;
        }
        //
        if (fechaInicioActividad !== "hasNoValue") {
            const sports = await db.collection(SPORT_COLLECTION_NAME).find(
                { userId: userId, fechaInicioActividad: { $gte: startDate, $lt: endDate } },
                { projection: { userId: 0 } }
            ).toArray();
            return sports.length ? sports : false;
        }
        //
        if (duracionActividadMinutos !== "hasNoValue") {
            const sports = await db.collection(SPORT_COLLECTION_NAME).find(
                { userId: userId, duracionActividadMinutos: duracionActividadMinutos },
                { projection: { userId: 0 } }
            ).toArray();
            return sports.length ? sports : false;
        }
    }
    static async getSportById(id, userId) {
        const db = await connectDB();
        return db.collection(SPORT_COLLECTION_NAME).findOne({ userId: userId, _id: id }, { projection: { userId: 0 } });
    }
    static async getSportUnavailableDates(userId) {
        const db = await connectDB();
        // Verificamos que existen documentos en la coleccion relativos al usuario logueado mediante su id(userId).
        // Si no existen documentos devolvemos null para que se devuelva un 404 en el controlador.
        if (!(await db.collection(SPORT_COLLECTION_NAME).findOne({ userId }))) return null;
        // Obtenemos una lista de fechas de las fechas de los documentos
        // donde haya 3 o mas reservas en una misma fecha. Dia: (2025-06-22).
        const unavailableDates = await db.collection(SPORT_COLLECTION_NAME).aggregate([
            { $match: { userId: userId } },
            // Agrupar por solo la parte de la fecha (ignorando la hora)
            {
                $group: {
                    _id: { $dateTrunc: { date: "$fechaInicioActividad", unit: "day", timezone: "UTC" } },
                    count: { $sum: 1 }
                }
            },
            { $match: { count: { $gte: 3 } } },
            { $project: { _id: 0, fecha: "$_id" } }
        ]).toArray();
        // Si no hay fechas no disponibles, es decir si la variable unavailableDates no tiene valores, devolvemos el error.
        return unavailableDates.length ?
        { dates: unavailableDates.map(document => document.fecha.toISOString()) } : { dates: [] };
    }
    static async postSport({ sport, userId }) {
        const db = await connectDB();
        const newSport =  {
            ...sport,
            userId: userId,
            _id: randomUUID()
        };
        // Obtenemos el resultado de la operación de inserción.
        try {
            const { acknowledged, insertedId } = await db.collection(SPORT_COLLECTION_NAME).insertOne(newSport);
            //if (!acknowledged) throw new Error('La operación de inserción no fue reconocida por MongoDB.');
            return !acknowledged ? false : { id: insertedId, ...newSport };
        } catch (error) {
            console.error('Error en postSport:', error);
            throw new Error(`No se pudo insertar la actividad en la base de datos: ${error}`);
        }
    }
    static async putSport({ id, sport, userId }) {
        const db = await connectDB();
        const newSport =  {
            ...sport,
            userId: userId,
            _id: id
        };
        // Obtenemos el resultado de la operación de actualización.
        const { value, lastErrorObject, ok } = await db.collection(SPORT_COLLECTION_NAME).findOneAndReplace(
            { userId: userId, _id: id }, newSport, { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Si ok es 0 devolvemos el error obtenido.
        if (!ok && lastErrorObject) throw new Error(`No se pudo actualizar la actividad: ${lastErrorObject}`);
        // Devolvemos false si no hay valor(null).
        if (ok && !value) return false;
        // Devolvemos el documento actualizado.
        if (ok && value) return value;
    }
    static async patchSport({ id, sport, userId }) {
        const db = await connectDB();
        // Obtenemos el resultado de la operación de actualización.
        const { value, lastErrorObject, ok } = await db.collection(SPORT_COLLECTION_NAME).findOneAndUpdate(
            { userId: userId, _id: id }, { $set: { ...sport, userId: userId } },
            { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Si ok es 0 devolvemos el error obtenido.
        if (!ok && lastErrorObject) throw new Error(`No se pudo actualizar la actividad: ${lastErrorObject}`);
        // Devolvemos false si no hay valor(null).
        if (ok && !value) return false;
        // Devolvemos el documento actualizado.
        if (ok && value) return value;
    }
    static async deleteSport(id, userId) {
        const db = await connectDB();
        return (await db.collection(SPORT_COLLECTION_NAME).findOneAndDelete({ userId: userId, _id: id })) !== null;
    }
}