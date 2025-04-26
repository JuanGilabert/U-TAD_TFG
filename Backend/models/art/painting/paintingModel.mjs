// Modulos de node
import { randomUUID } from 'node:crypto';
// Modulos locales
import { connectDB } from '../../../services/database/connection/mongoDbConnection.mjs';
import { PAINTING_COLLECTION_NAME, RETURN_DOCUMENT_VALUE } from '../../../utils/export/GenericEnvConfig.mjs';
//// Exportamos la clase.
export class PaintingModel {
    static async getAllPaintings(userId) {
        const db = await connectDB();
        const paintings = await db.collection(PAINTING_COLLECTION_NAME).find({ userId: userId }, { projection: { userId: 0 } }).toArray();
        return paintings.length ? paintings : false;
    }
    static async getPaintingById(id, userId) {
        const db = await connectDB();
        return db.collection(PAINTING_COLLECTION_NAME).findOne({ userId: userId, _id: id }, { projection: { userId: 0 } });
    }
    static async postNewPainting({ painting, userId }) {
        const db = await connectDB();
        const newPainting = {
            ...painting,
            userId: userId,
            _id: randomUUID()
        }
        const { insertedId } = await db.collection(PAINTING_COLLECTION_NAME).insertOne(newPainting);
        return { id: insertedId, ...newPainting };
    }
    static async putUpdatePainting({ id, painting, userId }) {
        const db = await connectDB();
        const newPainting = {
            ...painting,
            userId: userId,
            _id: id
        }
        const value = await db.collection(PAINTING_COLLECTION_NAME).findOneAndReplace(
            { userId: userId, _id: id }, newPainting, { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Devolvemos false si no hay valor.
        if (!value) return false;
        return value;
    }
    static async patchUpdatePainting({ id, painting, userId }) {
        const db = await connectDB();
        const value = db.collection(PAINTING_COLLECTION_NAME).findOneAndUpdate(
            { userId: userId, _id: id }, { $set: { ...painting, userId: userId } },
            { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Devolvemos false si no hay valor.
        if (!value) return false;
        return value;
    }
    static async deletePainting(id, userId) {
        const db = await connectDB();
        return (await db.collection(PAINTING_COLLECTION_NAME).findOneAndDelete({ userId: userId, _id: id })) !== null;
    }
}