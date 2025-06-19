// Modulos de node
import { randomUUID } from 'node:crypto';
// Modulos locales
import { connectDB } from '../../services/database/connection/mongoDbConnection.mjs';
import { MEETING_COLLECTION_NAME, RETURN_DOCUMENT_VALUE } from '../../utils/export/GenericEnvConfig.mjs';
//// Exportamos la clase.
export class MeetingModel {
    static async getAllMeetings(userId, fechaInicioReunion, fechaFinReunion) {
        const db = await connectDB();
        // Verificamos que existen documentos en la coleccion relativos al usuario logueado mediante su id(userId).
        // Si no existen documentos devolvemos null para que se devuelva un 404 en el controlador.
        if (!(await db.collection(MEETING_COLLECTION_NAME).findOne({ userId }))) return null;
        //
        if (fechaInicioReunion === "hasNoValue") {
            if (fechaFinReunion === "hasNoValue") {
                const meetings = await db.collection(MEETING_COLLECTION_NAME).find(
                    { userId: userId },
                    { projection: { userId: 0 } }
                ).toArray();
                return meetings.length ? meetings : null;
            }
            // Si por lo que sea alguien envia solo la fecha de fin se devolvera false indicando un error
            // en los parametros de la peticion ya que no se ha enviado la fecha de inicio previamente.
            return false;
        }
        //
        const dayOfInitMeetingDate = new Date(fechaInicioReunion.split("T")[0]);
        const endDateOfInitMeetingDay = new Date(dayOfInitMeetingDate);
        endDateOfInitMeetingDay.setUTCDate(endDateOfInitMeetingDay.getUTCDate() + 1);
        //
        const dayOfEndMeetingtDate = new Date(fechaFinReunion.split("T")[0]);
        const endDateOfEndMeetingDay = new Date(dayOfEndMeetingtDate);
        endDateOfEndMeetingDay.setUTCDate(endDateOfEndMeetingDay.getUTCDate() + 1);
        //
        if (fechaInicioReunion !== "hasNoValue") {
            let meetings = "";
            if (fechaFinReunion !== "hasNoValue") {
                // Como hay query params, devolvemos las fechas de las reservas que coinciden con la fecha de la pelicula.
                meetings = await db.collection(MEETING_COLLECTION_NAME).find(
                    {
                        userId: userId,
                        fechaInicioReunion: { $gte: dayOfInitMeetingDate, $lt: endDateOfInitMeetingDay },
                        fechaFinReunion: { $gte: dayOfEndMeetingtDate, $lt: endDateOfEndMeetingDay }
                    },
                    { projection: { userId: 0 } }
                ).toArray();
                return meetings.length ? meetings : false;
            }
            //
            meetings = await db.collection(MEETING_COLLECTION_NAME).find(
                { userId: userId,
                    fechaInicioReunion: { $gte: dayOfInitMeetingDate, $lt: endDateOfInitMeetingDay }
                },
                { projection: { userId: 0 } }
            ).toArray();
            //
            return meetings.length ? meetings : false;
        }
    }
    static async getMeetingById(id, userId) {
        const db = await connectDB();
        return db.collection(MEETING_COLLECTION_NAME).findOne({ userId: userId, _id: id }, { projection: { userId: 0 } });
    }
    static async getMeetingUnavailableDates(userId) {
        const db = await connectDB();
        // Verificamos que existen documentos en la coleccion relativos al usuario logueado mediante su id(userId).
        // Si no existen documentos devolvemos null para que se devuelva un 404 en el controlador.
        if (!(await db.collection(MEETING_COLLECTION_NAME).findOne({ userId }))) return null;
        // Obtenemos una lista de fechas de las fechas de los documentos
        // donde haya 3 o mas reservas en una misma fecha. Dia: (2025-06-22).
        const unavailableDates = await db.collection(MEETING_COLLECTION_NAME).aggregate([
            { $match: { userId: userId } },
            // Agrupar por solo la parte de la fecha (ignorando la hora)
            {
                $group: {
                    _id: { $dateTrunc: { date: "$fechaInicioReunion", unit: "day", timezone: "UTC" } },
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
    static async postMeeting({ meeting, userId }) {
        const db = await connectDB();
        const newMeeting = {
            ...meeting,
            userId: userId,
            _id: randomUUID()
        };
        // Obtenemos el resultado de la operación de inserción.
        try {
            const { acknowledged, insertedId } = await db.collection(MEETING_COLLECTION_NAME).insertOne(newMeeting);
            //if (!acknowledged) throw new Error('La operación de inserción no fue reconocida por MongoDB.');
            return !acknowledged ? false : { id: insertedId, ...newMeeting };
        } catch (error) {
            console.error('Error en postMeeting:', error);
            throw new Error(`No se pudo insertar la cita en la base de datos: ${error}`);
        }
    }
    static async putMeeting({ id, meeting, userId }) {
        const db = await connectDB();
        const newMeeting = {
            ...meeting,
            userId: userId,
            _id: id
        };
        // Obtenemos el resultado de la operación de actualización.
        const { value, lastErrorObject, ok } = await db.collection(MEETING_COLLECTION_NAME).findOneAndReplace(
            { userId: userId, _id: id }, newMeeting, { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Si ok es 0 devolvemos el error obtenido.
        if (!ok && lastErrorObject) throw new Error(`No se pudo actualizar la cita: ${lastErrorObject}`);
        // Devolvemos false si no hay valor(null).
        if (ok && !value) return false;
        // Devolvemos el documento actualizado.
        if (ok && value) return value;
    }
    static async patchMeeting({ id, meeting, userId }) {
        const db = await connectDB();
        // Obtenemos el resultado de la operación de actualización.
        const { value, lastErrorObject, ok } = await db.collection(MEETING_COLLECTION_NAME).findOneAndUpdate(
            { userId: userId, _id: id }, { $set: { ...meeting, userId: userId } },
            { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Si ok es 0 devolvemos el error obtenido.
        if (!ok && lastErrorObject) throw new Error(`No se pudo actualizar la cita: ${lastErrorObject}`);
        // Devolvemos false si no hay valor(null).
        if (ok && !value) return false;
        // Devolvemos el documento actualizado.
        if (ok && value) return value;
    }
    static async deleteMeeting(id, userId) {
        const db = await connectDB();
        return (await db.collection(MEETING_COLLECTION_NAME).findOneAndDelete({userId: userId,  _id: id })) !== null;
    }
}