// Modulos de node.
import { randomUUID } from 'node:crypto';
// Modulos locales.
import { connectDB } from '../../../services/database/connection/mongoDbConnection.mjs';
import { CINEMA_COLLECTION_NAME, RETURN_DOCUMENT_VALUE } from '../../../utils/export/GenericEnvConfig.mjs';
//// Exportamos la clase.
export class CinemaModel {
    static async getAllCinemas(userId) {
        const db = await connectDB();
        const cinemas = await db.collection(CINEMA_COLLECTION_NAME).find({ userId: userId }, { projection: { userId: 0 } }).toArray();
        return cinemas.length ? cinemas : false;
    }
    static async getCinemaById(id, userId) {
        const db = await connectDB();
        return db.collection(CINEMA_COLLECTION_NAME).findOne({ userId: userId, _id: id }, { projection: { userId: 0 } });
    }
    static async postNewCinema({ cinema, userId }) {
        const db = await connectDB();
        const newCinema = {
            ...cinema,
            userId: userId,
            _id: randomUUID()
        };
        const { insertedId } = await db.collection(CINEMA_COLLECTION_NAME).insertOne(newCinema);
        return { id: insertedId, ...newCinema };
    }
    static async putUpdateCinema({ id, cinema, userId }) {
        const db = await connectDB();
        const newCinema = {
            ...cinema,
            userId: userId,
            _id: id
        };
        const value = await db.collection(CINEMA_COLLECTION_NAME).findOneAndReplace(
            { userId: userId, _id: id }, newCinema, { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Devolvemos false si no hay valor.
        if (!value) return false;
        return value;
    }
    static async patchUpdateCinema({ id, cinema, userId }) {
        const db = await connectDB();
        const value  = await db.collection(CINEMA_COLLECTION_NAME).findOneAndUpdate(
            { userId: userId, _id: id }, { $set: { ...cinema, userId: userId } },
            { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Devolvemos false si no hay valor.
        if (!value) return false;
        return value;
    }
    static async deleteCinema(id, userId) {
        const db = await connectDB();
        return (await db.collection(CINEMA_COLLECTION_NAME).findOneAndDelete({ userId: userId, _id: id })) !== null;
    }
}