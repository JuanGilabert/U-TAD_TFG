// Modulos de node
import { randomUUID } from 'node:crypto'
//
import { connectDB } from '../../services/database/connection/mongoDbConection.js';
import { ReturnDocument } from 'mongodb';
const sportCollectionName = process.env.SPORT_COLLECTION_NAME;
////
export class SportModel {
    static async getAllSports(userId) {
        const db = await connectDB();
        const sports = await db.collection(sportCollectionName).find({ userId: userId }).toArray();
        return sports.length ? sports : false;
    }
    static async getSportById(id, userId) {
        const db = await connectDB();
        return db.collection(sportCollectionName).findOne({ userId: userId, _id: id }, { projection: { _id: 0 } });
    }
    static async postNewSport({ sport, userId }) {
        const db = await connectDB();
        const newSport =  {
            id: randomUUID(),
            ...sport,
            userId: userId
        };
        const { insertedId } = await db.collection(sportCollectionName).insertOne(newSport);
        return { id: insertedId, ...newSport };
    }
    static async putUpdateSport({ id, sport, userId }) {
        const db = await connectDB();
        const { ok, value } = await db.collection(sportCollectionName).findOneAndReplace(
            { userId: userId, _id: id }, { $set: sport }, { ReturnDocument: ReturnDocument.AFTER }
        );
        //
        if (!ok) return false;
        return value;
    }
    static async patchUpdateSport({ id, sport, userId }) {
        const db = await connectDB();
        const { ok, value } = await db.collection(sportCollectionName).findOneAndUpdate(
            { userId: userId, _id: id }, { $set: sport }, { returnDocument: ReturnDocument.AFTER }
        );
        //
        if (!ok) return false;
        return value;
    }
    static async deleteSport(id, userId) {
        const db = await connectDB();
        return (await db.collection(sportCollectionName).findOneAndDelete({ userId: userId, _id: id })) !== null;
    }
}