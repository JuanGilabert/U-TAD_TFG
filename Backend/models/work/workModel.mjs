// Modulos de node.
import { randomUUID } from 'node:crypto';
// Modulos locales.
import { connectDB } from '../../services/database/connection/mongoDbConnection.mjs';
import { WORK_COLLECTION_NAME, RETURN_DOCUMENT_AFTER_VALUE } from '../../config/GenericEnvConfig.mjs';
//// Exportamos la clase.
export class WorkModel {
    static async getAllWorks(userId, fechaInicioTarea, fechaEntregaTarea) {
        const db = await connectDB();
        // Verificamos que existen documentos en la coleccion relativos al usuario logueado mediante su id(userId).
        // Si no existen documentos devolvemos null para que se devuelva un 404 en el controlador.
        if (!(await db.collection(WORK_COLLECTION_NAME).findOne({ userId }))) return null;
        if (fechaInicioTarea === "hasNoValue") {
            if (fechaEntregaTarea === "hasNoValue") {
                const works = await db.collection(WORK_COLLECTION_NAME).find({ userId: userId }, { projection: { userId: 0 } }).toArray();
                return works.length ? works : null;
            }
            return false;
        }
        //
        const dayOfInitWorkDate = new Date(fechaInicioTarea.split("T")[0]);
        const endDateOfInitWorkDay = new Date(dayOfInitWorkDate);
        endDate.setUTCDate(endDate.getUTCDate() + 1);
        const dayOfEndWorkDate = new Date(fechaEntregaTarea.split("T")[0]);
        const endDateOfEndWorkDay = new Date(dayOfEndWorkDate);
        endDate.setUTCDate(endDate.getUTCDate() + 1);
        if (fechaInicioTarea !== "hasNoValue") {
            if (fechaEntregaTarea !== "hasNoValue") {
                const works = await db.collection(WORK_COLLECTION_NAME).find(
                    {
                        userId: userId,
                        fechaInicioTarea: { $gte: dayOfInitWorkDate, $lt: endDateOfInitWorkDay },
                        fechaEntregaTarea: { $gte: dayOfEndWorkDate, $lt: endDateOfEndWorkDay }
                    },
                    { projection: { _id: 0, fechaInicioTarea: 1 } }
                ).toArray();
                return works.length ? works : false;
            }
            const works = await db.collection(WORK_COLLECTION_NAME).find(
                {
                    userId: userId,
                    fechaInicioTarea: { $gte: dayOfInitWorkDate, $lt: endDateOfInitWorkDay },
                },
                { projection: { _id: 0, fechaInicioTarea: 1 } }
            ).toArray();
            return works.length ? works : false;
        }
    }
    static async getWorkById(id, userId) {
        const db = await connectDB();
        return db.collection(WORK_COLLECTION_NAME).findOne({ userId: userId, _id: id }, { projection: { userId: 0 } });
    }
    static async getWorkUnavailableDates(userId) {
        const db = await connectDB();
        // Verificamos que existen documentos en la coleccion relativos al usuario logueado mediante su id(userId).
        // Si no existen documentos devolvemos null para que se devuelva un 404 en el controlador.
        if (!(await db.collection(WORK_COLLECTION_NAME).findOne({ userId }))) return null;
        // Obtenemos una lista de fechas de las fechas de los documentos
        // donde haya 3 o mas reservas en una misma fecha. Dia: (2025-06-22).
        const unavailableDates = await db.collection(WORK_COLLECTION_NAME).aggregate([
            { $match: { userId: userId } },
            // Agrupar por solo la parte de la fecha (ignorando la hora)
            {
                $group: {
                    _id: { $dateTrunc: { date: "$fechaInicioTarea", unit: "day", timezone: "UTC" } },
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
    static async postWork({ work, userId }) {
        const db = await connectDB();
        const newWork = {
            ...work,
            userId: userId,
            _id: randomUUID()
        };
        // Obtenemos el resultado de la operación de inserción.
        try {
            const { acknowledged, insertedId } = await db.collection(WORK_COLLECTION_NAME).insertOne(newWork);
            //if (!acknowledged) throw new Error('La operación de inserción no fue reconocida por MongoDB.');
            return !acknowledged ? false : { id: insertedId, ...newWork };
        } catch (error) {
            console.error('Error en postWork:', error);
            throw new Error(`No se pudo insertar la tarea en la base de datos: ${error}`);
        }
    }
    static async putWork({ id, work, userId }) {
        const db = await connectDB();
        const newWork = {
            ...work,
            userId: userId,
            _id: id
        };
        // Obtenemos el resultado de la operación de actualización.
        const { value, lastErrorObject, ok } = await db.collection(WORK_COLLECTION_NAME).findOneAndReplace(
            { userId: userId, _id: id }, newWork, { returnDocument: RETURN_DOCUMENT_AFTER_VALUE, projection: { userId: 0 } }
        );
        // Si ok es 0 devolvemos el error obtenido.
        if (!ok && lastErrorObject) throw new Error(`No se pudo actualizar la tarea: ${lastErrorObject}`);
        // Devolvemos false si no hay valor(null).
        if (ok && !value) return false;
        // Devolvemos el documento actualizado.
        if (ok && value) return value;
    }
    static async patchWork({ id, work, userId }) {
        const db = await connectDB();
        // Obtenemos el resultado de la operación de actualización.
        const { value, lastErrorObject, ok } = await db.collection(WORK_COLLECTION_NAME).findOneAndUpdate(
            { userId: userId, _id: id }, { $set: { ...work, userId: userId } },
            { returnDocument: RETURN_DOCUMENT_AFTER_VALUE, projection: { userId: 0 } }
        );
        // Si ok es 0 devolvemos el error obtenido.
        if (!ok && lastErrorObject) throw new Error(`No se pudo actualizar la tarea: ${lastErrorObject}`);
        // Devolvemos false si no hay valor(null).
        if (ok && !value) return false;
        // Devolvemos el documento actualizado.
        if (ok && value) return value;
    }
    static async deleteWork(id, userId) {
        const db = await connectDB();
        return (await db.collection(WORK_COLLECTION_NAME).findOneAndDelete({ userId: userId, _id: id })) !== null;
    }
}