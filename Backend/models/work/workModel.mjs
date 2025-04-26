// Modulos de node.
import { randomUUID } from 'node:crypto';
// Modulos locales.
import { connectDB } from '../../services/database/connection/mongoDbConnection.mjs';
import { WORK_COLLECTION_NAME, RETURN_DOCUMENT_VALUE } from '../../utils/export/GenericEnvConfig.mjs';
//// Exportamos la clase.
export class WorkModel {
    static async getAllWorks(userId) {
        const db = await connectDB();
        const works = await db.collection(WORK_COLLECTION_NAME).find({ userId: userId }, { projection: { userId: 0 } }).toArray();
        return works.length ? works : false;
    }
    static async getWorkById(id, userId) {
        const db = await connectDB();
        return db.collection(WORK_COLLECTION_NAME).findOne({ userId: userId, _id: id }, { projection: { userId: 0 } });
    }
    static async postNewWork({ work, userId }) {
        const db = await connectDB();
        const newWork = {
            ...work,
            userId: userId,
            _id: randomUUID()
        };
        const { insertedId } = await db.collection(WORK_COLLECTION_NAME).insertOne(newWork);
        return { id: insertedId, ...newWork };
    }
    static async putUpdateWork({ id, work, userId }) {
        const db = await connectDB();
        const newWork = {
            ...work,
            userId: userId,
            _id: id
        };
        const value = await db.collection(WORK_COLLECTION_NAME).findOneAndReplace(
            { userId: userId, _id: id }, newWork, { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Devolvemos false si no hay valor.
        if (!value) return false;
        return value;
    }
    static async patchUpdateWork({ id, work, userId }) {
        const db = await connectDB();
        const value = await db.collection(WORK_COLLECTION_NAME).findOneAndUpdate(
            { userId: userId, _id: id }, { $set: { ...work, userId: userId } },
            { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Devolvemos false si no hay valor.
        if (!value) return false;
        return value;
    }
    static async deleteWork(id, userId) {
        const db = await connectDB();
        return (await db.collection(WORK_COLLECTION_NAME).findOneAndDelete({ userId: userId, _id: id })) !== null;
    }
}