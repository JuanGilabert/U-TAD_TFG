// Modulos de node
import { randomUUID } from 'node:crypto'
//
import { connectDB } from '../../../services/database/connection/mongoDbConection.js';
import { ReturnDocument } from 'mongodb';
const cinemaCollectionName = process.env.CINEMA_COLLECTION_NAME;
////
export class CinemaModel {
    static async getAllCinemas(userId) {
        const db = await connectDB();
        const cinemas = await db.collection(cinemaCollectionName).find({ userId: userId }).toArray();
        return cinemas.length ? cinemas : false;
    }
    static async getCinemaById(id, userId) {
        const db = await connectDB();
        return db.collection(cinemaCollectionName).findOne({ userId: userId, _id: id }, { projection: { _id: 0 } });
    }
    static async postNewCinema({ cinema, userId }) {
        const db = await connectDB();
        const newCinema = {
            id: randomUUID(),
            ...cinema,
            userId: userId
        };
        const { insertedId } = await db.collection(cinemaCollectionName).insertOne(newCinema);
        return { id: insertedId, ...newCinema };
    }
    static async putUpdateCinema({ id, cinema, userId }) {
        const db = await connectDB();
        const { ok, value } = await db.collection(cinemaCollectionName).findOneAndReplace(
            { userId: userId, _id: id }, { $set: cinema }, { returnDocument: ReturnDocument.AFTER }
        );
        //
        if (!ok) return false
        return value
    }
    static async patchUpdateCinema({ id, cinema, userId }) {
        const db = await connectDB();
        const { ok, value } = await db.collection(cinemaCollectionName).findOneAndUpdate(
            { userId: userId, _id: id }, { $set: cinema }, { returnDocument: ReturnDocument.AFTER });
        //
        if (!ok) return false
        return value
    }
    static async deleteCinema(id, userId) {
        const db = await connectDB();
        return (await db.collection(cinemaCollectionName).findOneAndDelete({ userId: userId, _id: id })) !== null;
        const { deletedCount } = await db.deleteOne({ _id: id });
        //
        return deletedCount > 0
    }
}