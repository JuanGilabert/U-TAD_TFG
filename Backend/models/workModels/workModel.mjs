// Modulos de node
import { randomUUID } from 'node:crypto'
//
import { connectDB } from '../../services/database/connection/mongoDbConection.js';
import { ReturnDocument } from 'mongodb';
const workCollectionName = process.env.WORK_COLLECTION_NAME;
////
export class WorkModel {
    static async getAllWorks(userId) {
        const db = await connectDB();
        const works = await db.collection(workCollectionName).find({ userId: userId }).toArray();
        return works.length ? works : false;
    }
    static async getWorkById(id, userId) {
        const db = await connectDB();
        return db.collection(workCollectionName).findOne({ userId: userId, _id: id }, { projection: { _id: 0 } });
    }
    static async postNewWork({ work, userId }) {
        const db = await connectDB();
        const newWork = {
            id: randomUUID(),
            ...work,
            userId: userId
        };
        //
        const { insertedId } = await db.collection(workCollectionName).insertOne(newWork);
        return { id: insertedId, ...newWork };
    }
    static async putUpdateWork({ id, work, userId }) {
        const db = await connectDB();
        const { ok, value } = await db.collection(workCollectionName).findOneAndReplace(
            { userId: userId, _id: id }, { $set: work }, { returnDocument: ReturnDocument.AFTER }
        );
        //
        if (!ok) return false;
        return value;
    }
    static async patchUpdateWork({ id, work, userId }) {
        const db = await connectDB();
        const { ok, value } = await db.collection(workCollectionName).findOneAndUpdate(
            { userId: userId, _id: id }, { $set: work }, { returnDocument: ReturnDocument.AFTER }
        );
        //
        if (!ok) return false;
        return value;
    }
    static async deleteWork(id, userId) {
        const db = await connectDB();
        return (await db.collection(workCollectionName).findOneAndDelete({ userId: userId, _id: id })) !== null;
    }
}