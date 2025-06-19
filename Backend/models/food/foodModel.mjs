// Modulos de node.
import { randomUUID } from 'node:crypto';
// Modulos locales.
import { connectDB } from '../../services/database/connection/mongoDbConnection.mjs';
import { FOOD_COLLECTION_NAME, RETURN_DOCUMENT_VALUE } from '../../utils/export/GenericEnvConfig.mjs';
//// Exportamos la clase.
export class FoodModel {
    static async getAllFoods(userId, tipoComida, fechaReserva) {
        const db = await connectDB();
        // Verificamos que existen documentos en la coleccion relativos al usuario logueado mediante su id(userId).
        // Si no existen documentos devolvemos null para que se devuelva un 404 en el controlador.
        if (!(await db.collection(FOOD_COLLECTION_NAME).findOne({ userId }))) return null;
        //
        if (tipoComida === "hasNoValue" && fechaReserva === "hasNoValue") {
            const foods = await db.collection(FOOD_COLLECTION_NAME).find({ userId: userId }, { projection: { userId: 0 } }).toArray();
            return foods.length ? foods : null;
        }
        // Creamos  fechas de inicio y fin del dia sin tener en cuneta las horas.
        const startDate = new Date(fechaReserva.split("T")[0]);
        const endDate = new Date(startDate);
        endDate.setUTCDate(endDate.getUTCDate() + 1);
        //
        if (tipoComida !== "hasNoValue" && fechaReserva !== "hasNoValue") {
            const foods = await db.collection(FOOD_COLLECTION_NAME).find(
                { userId: userId, tipoComida: tipoComida, fechaReserva: { $gte: startDate, $lt: endDate} },
                { projection: { userId: 0 } }
            ).toArray();
            return foods.length ? foods : false;
        }
        if (tipoComida !== "hasNoValue") {
            const foods = await db.collection(FOOD_COLLECTION_NAME).find(
                { userId: userId, tipoComida: tipoComida },
                { projection: { userId: 0 } }
            ).toArray();
            return foods.length ? foods : false;
        }
        if (fechaReserva !== "hasNoValue") {
            // Como hay query params, devolvemos las fechas de las reservas que coinciden con la fecha de la pelicula.
            const foods = await db.collection(FOOD_COLLECTION_NAME).find(
                { userId: userId, fechaReserva: { $gte: startDate, $lt: endDate } },
                { projection: { userId: 0 } }
            ).toArray();
            return foods.length ? foods : false;
        }
    }
    static async getFoodById(id, userId) {
        const db = await connectDB();
        return db.collection(FOOD_COLLECTION_NAME).findOne({ userId: userId, _id: id }, { projection: { userId: 0 } });
    }
    static async getFoodUnavailableDates(userId) {
        const db = await connectDB();
        // Verificamos que existen documentos en la coleccion relativos al usuario logueado mediante su id(userId).
        // Si no existen documentos devolvemos null para que se devuelva un 404 en el controlador.
        if (!(await db.collection(FOOD_COLLECTION_NAME).findOne({ userId }))) return null;
        // Obtenemos una lista de fechas de las fechas de los documentos
        // donde haya 3 o mas reservas en una misma fecha. Dia: (2025-06-22).
        const unavailableDates = await db.collection(FOOD_COLLECTION_NAME).aggregate([
            { $match: { userId: userId } },
            // Agrupar por solo la parte de la fecha (ignorando la hora)
            {
                $group: {
                    _id: { $dateTrunc: { date: "$fechaReserva", unit: "day", timezone: "UTC" } },
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
    static async postFood({ food, userId }) {
        const db = await connectDB();
        const newFood = {
            ...food,
            userId: userId,
            _id: randomUUID()
        };
        // Obtenemos el resultado de la operación de inserción.
        try {
            const { acknowledged, insertedId } = await db.collection(FOOD_COLLECTION_NAME).insertOne(newFood);
            //if (!acknowledged) throw new Error('La operación de inserción no fue reconocida por MongoDB.');
            return !acknowledged ? false : { id: insertedId, ...newFood };
        } catch (error) {
            console.error('Error en postFood:', error);
            throw new Error(`No se pudo insertar la comida en la base de datos: ${error}`);
        }
    }
    static async putFood({ id, food, userId }) {
        const db = await connectDB();
        const newFood = {
            ...food,
            userId: userId,
            _id: id
        };
        // Obtenemos el resultado de la operación de actualización.
        const { value, lastErrorObject, ok } = await db.collection(FOOD_COLLECTION_NAME).findOneAndReplace(
            { userId: userId, _id: id }, newFood, { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Si ok es 0 devolvemos el error obtenido.
        if (!ok && lastErrorObject) throw new Error(`No se pudo actualizar la comida: ${lastErrorObject}`);
        // Devolvemos false si no hay valor(null).
        if (ok && !value) return false;
        // Devolvemos el documento actualizado.
        if (ok && value) return value;
    }
    static async patchFood({ id, food, userId }) {
        const db = await connectDB();
        // Obtenemos el resultado de la operación de actualización.
        const { value, lastErrorObject, ok } = await db.collection(FOOD_COLLECTION_NAME).findOneAndUpdate(
            { userId: userId, _id: id }, { $set: { ...food, userId: userId } },
            { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Si ok es 0 devolvemos el error obtenido.
        if (!ok && lastErrorObject) throw new Error(`No se pudo actualizar la comida: ${lastErrorObject}`);
        // Devolvemos false si no hay valor(null).
        if (ok && !value) return false;
        // Devolvemos el documento actualizado.
        if (ok && value) return value;
    }
    static async deleteFood(id, userId) {
        const db = await connectDB();
        return (db.collection(FOOD_COLLECTION_NAME).findOneAndDelete({ userId: userId, _id: id })) !== null;
    }
}