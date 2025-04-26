// Modulos de node
import { randomUUID } from 'node:crypto';
// Modulos locales
import { connectDB } from '../../services/database/connection/mongoDbConnection.mjs';
import { SPORT_COLLECTION_NAME, RETURN_DOCUMENT_VALUE } from '../../utils/export/GenericEnvConfig.mjs';
//// Exportamos la clase.
export class SportModel {
    static async getAllSports(userId) {
        const db = await connectDB();
        const sports = await db.collection(SPORT_COLLECTION_NAME).find({ userId: userId }, { projection: { userId: 0 } }).toArray();
        return sports.length ? sports : false;
    }
    static async getSportById(id, userId) {
        const db = await connectDB();
        return db.collection(SPORT_COLLECTION_NAME).findOne({ userId: userId, _id: id }, { projection: { userId: 0 } });
    }
    static async postNewSport({ sport, userId }) {
        const db = await connectDB();
        const newSport =  {
            ...sport,
            userId: userId,
            _id: randomUUID()
        };
        const { insertedId } = await db.collection(SPORT_COLLECTION_NAME).insertOne(newSport);
        return { id: insertedId, ...newSport };
    }
    static async putUpdateSport({ id, sport, userId }) {
        const db = await connectDB();
        const newSport =  {
            ...sport,
            userId: userId,
            _id: id
        };
        const value = await db.collection(SPORT_COLLECTION_NAME).findOneAndReplace(
            { userId: userId, _id: id }, newSport, { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Devolvemos false si no hay valor.
        if (!value) return false;
        return value;
    }
    static async patchUpdateSport({ id, sport, userId }) {
        const db = await connectDB();
        const value = await db.collection(SPORT_COLLECTION_NAME).findOneAndUpdate(
            { userId: userId, _id: id }, { $set: { ...sport, userId: userId } },
            { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Devolvemos false si no hay valor.
        if (!value) return false;
        return value;
    }
    static async deleteSport(id, userId) {
        const db = await connectDB();
        return (await db.collection(SPORT_COLLECTION_NAME).findOneAndDelete({ userId: userId, _id: id })) !== null;
    }
}