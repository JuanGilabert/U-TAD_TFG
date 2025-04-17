// Modulos de node
import { randomUUID } from 'node:crypto'
//
import { connectDB } from '../../services/database/connection/mongoDbConection.js';
import { ReturnDocument } from 'mongodb';
const travelCollectionName = process.env.TRAVEL_COLLECTION_NAME;
////
export class TravelModel {
    static async getAllTravels(userId) {
        const db = await connectDB();
        const travels = await db.collection(travelCollectionName).find({ userId: userId }).toArray();
        return travels.length ? travels : false;
    }
    static async getTravelById(id, userId) {
        const db = await connectDB();
        return db.collection(travelCollectionName).findOne({ userId: userId, _id: id }, { projection: { _id: 0 } });
    }
    static async postNewTravel({ travel, userId }) {
        const db = await connectDB();
        const newTravel = {
            id: randomUUID(),
            ...travel, 
            userId: userId
        }
        //
        const { insertedId } = await db.collection(travelCollectionName).insertOne(newTravel);
        return { id: insertedId, ...newTravel };
    }
    static async putUpdateTravel({ id, travel, userId }) {
        const db = await connectDB();
        const { ok, value } = await db.collection(travelCollectionName).findOneAndReplace(
            { userId: userId, _id: id }, { $set: travel }, { returnDocument: ReturnDocument.AFTER }
        );
        //
        if (!ok) return false;
        return value;
    }
    static async patchUpdateTravel({ id, travel, userId }) {
        const db = await connectDB();
        const { ok, value } = await db.collection(travelCollectionName).findOneAndUpdate(
            { userId: userId, _id: id }, { $set: travel }, { returnNewDocument: true}
        );
        //
        if (!ok) return false;
        return value;
    }
    static async deleteTravel(id, userId) {
        const db = await connectDB();
        return (await db.collection(travelCollectionName).findOneAndDelete({ userId: userId, _id: id })) !== null;
    }
}