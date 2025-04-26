// Modulos de node.
import { randomUUID } from 'node:crypto';
// Modulos locales.
import { connectDB } from '../../services/database/connection/mongoDbConnection.mjs';
import { TRAVEL_COLLECTION_NAME, RETURN_DOCUMENT_VALUE } from '../../utils/export/GenericEnvConfig.mjs';
//// Exportamos la clase.
export class TravelModel {
    static async getAllTravels(userId) {
        const db = await connectDB();
        const travels = await db.collection(TRAVEL_COLLECTION_NAME).find({ userId: userId }, { projection: { userId: 0 } }).toArray();
        return travels.length ? travels : false;
    }
    static async getTravelById(id, userId) {
        const db = await connectDB();
        return db.collection(TRAVEL_COLLECTION_NAME).findOne({ userId: userId, _id: id }, { projection: { userId: 0 } });
    }
    static async postNewTravel({ travel, userId }) {
        const db = await connectDB();
        const newTravel = {
            ...travel, 
            userId: userId,
            _id: randomUUID()
        };
        const { insertedId } = await db.collection(TRAVEL_COLLECTION_NAME).insertOne(newTravel);
        return { id: insertedId, ...newTravel };
    }
    static async putUpdateTravel({ id, travel, userId }) {
        const db = await connectDB();
        const newTravel = {
            ...travel, 
            userId: userId,
            _id: id
        };
        const value = await db.collection(TRAVEL_COLLECTION_NAME).findOneAndReplace(
            { userId: userId, _id: id }, newTravel, { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Devolvemos false si no hay valor.
        if (!value) return false;
        return value;
    }
    static async patchUpdateTravel({ id, travel, userId }) {
        const db = await connectDB();
        const value = await db.collection(TRAVEL_COLLECTION_NAME).findOneAndUpdate(
            { userId: userId, _id: id }, { $set: { ...travel, userId: userId } },
            { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Devolvemos false si no hay valor.
        if (!value) return false;
        return value;
    }
    static async deleteTravel(id, userId) {
        const db = await connectDB();
        return (await db.collection(TRAVEL_COLLECTION_NAME).findOneAndDelete({ userId: userId, _id: id })) !== null;
    }
}