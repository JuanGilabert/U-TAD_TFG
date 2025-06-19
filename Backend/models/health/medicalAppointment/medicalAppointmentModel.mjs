// Modulos de node.
import { randomUUID } from 'node:crypto';
// Modulos locales.
import { connectDB } from '../../../services/database/connection/mongoDbConnection.mjs';
import { MEDICAL_APPOINTMENT_COLLECTION_NAME, RETURN_DOCUMENT_VALUE } from '../../../utils/export/GenericEnvConfig.mjs';
//// Exportamos la clase.
export class MedicalAppointmentModel {
    static async getAllMedicalAppointments(userId, fechaCitaMedica, tipoPruebaCitaMedica) {
        const db = await connectDB();
        // Verificamos que existen documentos en la coleccion relativos al usuario logueado mediante su id(userId).
        // Si no existen documentos devolvemos null para que se devuelva un 404 en el controlador.
        if (!(await db.collection(MUSIC_COLLECTION_NAME).findOne({ userId }))) return null;
        //
        if (fechaCitaMedica === "hasNoValue" && tipoPruebaCitaMedica === "hasNoValue") {
            const medicalAppointments = await db.collection(MEDICAL_APPOINTMENT_COLLECTION_NAME).find(
                { userId: userId }, { projection: { userId: 0 } }
            ).toArray();
            return medicalAppointments.length ? medicalAppointments : null;
        }
        //
        // Creamos  fechas de inicio y fin del dia sin tener en cuneta las horas.
        const startDate = new Date(fechaCitaMedica.split("T")[0]);
        const endDate = new Date(startDate);
        endDate.setUTCDate(endDate.getUTCDate() + 1);
        //
        if (fechaCitaMedica !== "hasNoValue" && tipoPruebaCitaMedica !== "hasNoValue") {
            const medicalAppointments = await db.collection(MEDICAL_APPOINTMENT_COLLECTION_NAME).find(
                { userId: userId, fechaCitaMedica: { $gte: startDate, $lt: endDate }, tipoPruebaCitaMedica: tipoPruebaCitaMedica },
                { projection: { userId: 0 } }
            ).toArray();
            return medicalAppointments.length ? medicalAppointments : false;
        }
        if (fechaCitaMedica !== "hasNoValue") {
            // Como hay query params, devolvemos las fechas de las reservas que coinciden con la fecha de la pelicula.
            const medicalAppointments = await db.collection(MEDICAL_APPOINTMENT_COLLECTION_NAME).find(
                { userId: userId, fechaCitaMedica: { $gte: startDate, $lt: endDate } },
                { projection: { userId: 0 } }
            ).toArray();
            return medicalAppointments.length ? medicalAppointments : false;
        }
        //
        if (tipoPruebaCitaMedica !== "hasNoValue") {
            const medicalAppointments = await db.collection(MEDICAL_APPOINTMENT_COLLECTION_NAME).find(
                { userId: userId, tipoPruebaCitaMedica: tipoPruebaCitaMedica },
                { projection: { userId: 0 } }
            ).toArray();
            return medicalAppointments.length ? medicalAppointments : false;
        }
    }
    static async getMedicalAppointmentsById(id, userId) {
        const db = await connectDB();
        return db.collection(MEDICAL_APPOINTMENT_COLLECTION_NAME).findOne({ userId: userId, _id: id }, { projection: { userId: 0 } });
    }
    static async getMedicalAppointmentUnavailableDates(userId) {
        const db = await connectDB();
        // Verificamos que existen documentos en la coleccion relativos al usuario logueado mediante su id(userId).
        // Si no existen documentos devolvemos null para que se devuelva un 404 en el controlador.
        if (!(await db.collection(MEDICAL_APPOINTMENT_COLLECTION_NAME).findOne({ userId }))) return null;
        // Obtenemos una lista de fechas de las fechas de los documentos
        // donde haya 3 o mas reservas en una misma fecha. Dia: (2025-06-22).
        const unavailableDates = await db.collection(MEDICAL_APPOINTMENT_COLLECTION_NAME).aggregate([
            { $match: { userId: userId } },
            // Agrupar por solo la parte de la fecha (ignorando la hora)
            {
                $group: {
                    _id: { $dateTrunc: { date: "$fechaCitaMedica", unit: "day", timezone: "UTC" } },
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
    static async postMedicalAppointment({ medicalAppointment, userId }) {
        const db = await connectDB();
        const newMedicalAppointment = {
            ...medicalAppointment,
            userId: userId,
            _id: randomUUID()
        }
        // Obtenemos el resultado de la operación de inserción.
        try {
            const { acknowledged, insertedId } = await db.collection(MEDICAL_APPOINTMENT_COLLECTION_NAME).insertOne(newMedicalAppointment);
            //if (!acknowledged) throw new Error('La operación de inserción no fue reconocida por MongoDB.');
            return !acknowledged ? false : { id: insertedId, ...newMedicalAppointment };
        } catch (error) {
            console.error('Error en postMedicalAppointment:', error);
            throw new Error(`No se pudo insertar la cita medica en la base de datos: ${error}`);
        }
    }
    static async putMedicalAppointment({ id, medicalAppointment, userId }) {
        const db = await connectDB();
        const newMedicalAppointment = {
            ...medicalAppointment,
            userId: userId,
            _id: id
        }
        // Obtenemos el resultado de la operación de actualización.
        const { value, lastErrorObject, ok } = await db.collection(MEDICAL_APPOINTMENT_COLLECTION_NAME).findOneAndReplace(
            { userId: userId, _id: id }, newMedicalAppointment,{ returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Si ok es 0 devolvemos el error obtenido.
        if (!ok && lastErrorObject) throw new Error(`No se pudo actualizar la cita medica: ${lastErrorObject}`);
        // Devolvemos false si no hay valor(null).
        if (ok && !value) return false;
        // Devolvemos el documento actualizado.
        if (ok && value) return value;
    }
    static async patchMedicalAppointment({ id, medicalAppointment, userId }) {
        const db = await connectDB();
        // Obtenemos el resultado de la operación de actualización.
        const { value, lastErrorObject, ok } = await db.collection(MEDICAL_APPOINTMENT_COLLECTION_NAME).findOneAndUpdate(
            { userId: userId, _id: id }, { $set: { ...medicalAppointment, userId: userId } },
            { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Si ok es 0 devolvemos el error obtenido.
        if (!ok && lastErrorObject) throw new Error(`No se pudo actualizar la cita medica: ${lastErrorObject}`);
        // Devolvemos false si no hay valor(null).
        if (ok && !value) return false;
        // Devolvemos el documento actualizado.
        if (ok && value) return value;
    }
    static async deleteMedicalAppointment(id, userId) {
        const db = await connectDB();
        return (await db.collection(MEDICAL_APPOINTMENT_COLLECTION_NAME).findOneAndDelete({ userId: userId, _id: id })) !== null;
    }
}