// Modulos de node.
import { randomUUID } from 'node:crypto';
// Modulos locales.
import { connectDB } from '../../services/database/connection/mongoDbConnection.mjs';
import { TRAVEL_COLLECTION_NAME, RETURN_DOCUMENT_VALUE } from '../../utils/export/GenericEnvConfig.mjs';
//// Exportamos la clase.
export class TravelModel {
    static async getAllTravels(userId, fechaSalidaViaje, fechaRegresoViaje) {
        const db = await connectDB();
        // Verificamos que existen documentos en la coleccion relativos al usuario logueado mediante su id(userId).
        // Si no existen documentos devolvemos null para que se devuelva un 404 en el controlador.
        if (!(await db.collection(TRAVEL_COLLECTION_NAME).findOne({ userId }))) return null;
        if (fechaSalidaViaje === "hasNoValue" && fechaRegresoViaje === "hasNoValue") {
            const travels = await db.collection(TRAVEL_COLLECTION_NAME).find({ userId: userId }, { projection: { userId: 0 } }).toArray();
            return travels.length ? travels : null;
        }
        //fechaSalidaViaje
        const dayOfInitTravelDate = new Date(fechaSalidaViaje.split("T")[0]);
        const endDateOfInitTravelDay = new Date(dayOfInitTravelDate);
        endDateOfInitTravelDay.setUTCDate(endDateOfInitTravelDay.getUTCDate() + 1);
        //fechaRegresoViaje
        const dayOfEndTravelDate = new Date(fechaRegresoViaje.split("T")[0]);
        const endDateOfEndTravelDay = new Date(dayOfEndTravelDate);
        endDateOfEndTravelDay.setUTCDate(endDateOfEndTravelDay.getUTCDate() + 1);
        if (fechaSalidaViaje !== "hasNoValue" && fechaRegresoViaje !== "hasNoValue") {
            // Como hay query params, devolvemos las fechas de las reservas que coinciden con la fecha de la pelicula.
            const travels = await db.collection(TRAVEL_COLLECTION_NAME).find(
                {
                    userId: userId,
                    fechaSalidaViaje: { $gte: dayOfInitTravelDate, $lt: endDateOfInitTravelDay },
                    fechaRegresoViaje: { $gte: dayOfEndTravelDate, $lt: endDateOfEndTravelDay }
                },
                { projection: { userId: 0 } }
            ).toArray();
            return travels.length ? travels : false;
        }
        if (fechaSalidaViaje !== "hasNoValue") {
            // Como hay query params, devolvemos las fechas de las reservas que coinciden con la fecha de la pelicula.
            const travels = await db.collection(TRAVEL_COLLECTION_NAME).find(
                { userId: userId, fechaSalidaViaje: { $gte: dayOfInitTravelDate, $lt: endDateOfInitTravelDay } },
                { projection: { userId: 0 } }
            ).toArray();
            return travels.length ? travels : false;
        }
        if (fechaRegresoViaje !== "hasNoValue") {
            // Como hay query params, devolvemos las fechas de las reservas que coinciden con la fecha de la pelicula.
            const travels = await db.collection(TRAVEL_COLLECTION_NAME).find(
                { userId: userId, fechaRegresoViaje: { $gte: dayOfEndTravelDate, $lt: endDateOfEndTravelDay } },
                { projection: { userId: 0 } }
            ).toArray();
            return travels.length ? travels : false;
        }
    }
    static async getTravelById(id, userId) {
        const db = await connectDB();
        return db.collection(TRAVEL_COLLECTION_NAME).findOne({ userId: userId, _id: id }, { projection: { userId: 0 } });
    }
    static async getTravelDates(userId) {
        const db = await connectDB();
        // Verificamos que existen documentos en la coleccion relativos al usuario logueado mediante su id(userId).
        // Si no existen documentos devolvemos null para que se devuelva un 404 en el controlador.
        if (!(await db.collection(TRAVEL_COLLECTION_NAME).findOne({ userId }))) return null;
        // Obtenemos una lista de fechas de las fechas de los documentos
        // donde haya 3 o mas reservas en una misma fecha/dia.
        const unavailableDates = await db.collection(TRAVEL_COLLECTION_NAME).aggregate([
            { $match: { userId: userId } },
            // Agrupar por solo la parte de la fecha (ignorando la hora)
            {
                $group: {
                    _id: { $dateTrunc: { date: "$fechaSalidaViaje", unit: "day", timezone: "UTC" } },
                    count: { $sum: 1 }
                }
            },
            { $match: { count: { $gte: 1 } } },
            { $project: { _id: 0, fecha: "$_id" } }
        ]).toArray();
        // Si no hay fechas no disponibles, es decir si la variable unavailableDates no tiene valores, devolvemos el error.
        return unavailableDates.length ?
        { dates: unavailableDates.map(document => document.fecha.toISOString()) } : { dates: [] };
    }
    static async postTravel({ travel, userId }) {
        const db = await connectDB();
        const newTravel = {
            ...travel, 
            userId: userId,
            _id: randomUUID()
        };
        // Obtenemos el resultado de la operación de inserción.
        try {
            const { acknowledged, insertedId } = await db.collection(TRAVEL_COLLECTION_NAME).insertOne(newTravel);
            //if (!acknowledged) throw new Error('La operación de inserción no fue reconocida por MongoDB.');
            return !acknowledged ? false : { id: insertedId, ...newTravel };
        } catch (error) {
            console.error('Error en postTravel:', error);
            throw new Error(`No se pudo insertar el viaje en la base de datos: ${error}`);
        }
    }
    static async putTravel({ id, travel, userId }) {
        const db = await connectDB();
        const newTravel = {
            ...travel, 
            userId: userId,
            _id: id
        };
        // Obtenemos el resultado de la operación de actualización.
        const { value, lastErrorObject, ok } = await db.collection(TRAVEL_COLLECTION_NAME).findOneAndReplace(
            { userId: userId, _id: id }, newTravel, { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Si ok es 0 devolvemos el error obtenido.
        if (!ok && lastErrorObject) throw new Error(`No se pudo actualizar el viaje: ${lastErrorObject}`);
        // Devolvemos false si no hay valor(null).
        if (ok && !value) return false;
        // Devolvemos el documento actualizado.
        if (ok && value) return value;
    }
    static async patchTravel({ id, travel, userId }) {
        const db = await connectDB();
        // Obtenemos el resultado de la operación de actualización.
        const { value, lastErrorObject, ok } = await db.collection(TRAVEL_COLLECTION_NAME).findOneAndUpdate(
            { userId: userId, _id: id }, { $set: { ...travel, userId: userId } },
            { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Si ok es 0 devolvemos el error obtenido.
        if (!ok && lastErrorObject) throw new Error(`No se pudo actualizar el viaje: ${lastErrorObject}`);
        // Devolvemos false si no hay valor(null).
        if (ok && !value) return false;
        // Devolvemos el documento actualizado.
        if (ok && value) return value;
    }
    static async deleteTravel(id, userId) {
        const db = await connectDB();
        return (await db.collection(TRAVEL_COLLECTION_NAME).findOneAndDelete({ userId: userId, _id: id })) !== null;
    }
}