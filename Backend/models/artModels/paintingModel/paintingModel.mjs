// Modulos de node
import { randomUUID } from 'node:crypto'
//
import { connectDB } from '../../../services/database/connection/mongoDbConection.js';
import { ReturnDocument } from 'mongodb';
const paintingCollectionName = process.env.PAINTING_COLLECTION_NAME;
////
export class PaintingModel {
    static async getAllPaintings(userId) {
        const db = await connectDB();
        const paintings = await db.collection(paintingCollectionName).find({ userId: userId }).toArray();
        return paintings.length ? paintings : false;
    }
    static async getPaintingById(id, userId) {
        const db = await connectDB();
        return db.collection(paintingCollectionName).findOne({ userId: userId, _id: id });
    }
    static async postNewPainting({ painting, userId }) {
        const db = await connectDB();
        const newPainting = {
            id: randomUUID(),
            ...painting,
            userId: userId
        }
        const { insertedId } = await db.collection(paintingCollectionName).insertOne(newPainting);
        return { id: insertedId, ...newPainting };
    }
    static async putUpdatePainting({ id, painting, userId }) {
        const db = await connectDB();
        const { ok, value } = await db.collection(paintingCollectionName).findOneAndReplace(
            { userId: userId, _id: id }, { $set: painting }, { returnDocument: ReturnDocument.AFTER }
        );
        //
        if (!ok) return false;
        return value;
    }
    static async patchUpdatePainting({ id, painting, userId }) {
        const db = connectDB();
        const { ok, value } = db.collection(paintingCollectionName).findOneAndUpdate(
            { userId: userId, _id: id }, { $set: painting }, { returnDocument: ReturnDocument.AFTER }
        );
        //
        if (!ok) return false;
        return value;
    }
    static async deletePainting(id, userId) {
        const db = await connectDB();
        return (await db.collection(paintingCollectionName).findOneAndDelete({ userId: userId, _id: id })) !== null;
    }
}