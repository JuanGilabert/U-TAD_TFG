// Modulos de node
import { randomUUID } from 'node:crypto'
//
import { connectDB } from '../../../services/database/connection/mongoDbConection.js';
import { ReturnDocument } from 'mongodb';
const musicCollectionName = process.env.MUSIC_COLLECTION_NAME;
////
export class MusicModel {
    static async getAllMusics(userId){
        const db = await connectDB();
        const musics = await db.collection(musicCollectionName).find({ userId: userId }).toArray();
        return musics.length ? musics : false;
    }
    static async getMusicById(id, userId){
        const db = await connectDB();
        return db.collection(musicCollectionName).findOne({ userId: userId, _id: id }, { projection: { _id: 0 } });
    }
    static async postNewMusic({ music, userId }){
        const db = await connectDB();
        const newMusic = {
            id: randomUUID(),
            ...music,
            userId: userId
        };
        //
        const { insertedId } = await db.collection(musicCollectionName).insertOne(newMusic);
        return { id: insertedId, ...newMusic };
    }
    static async putUpdateMusic({ id, music, userId }){
        const db = await connectDB();
        const { ok, value } = await db.collection(musicCollectionName).findOneAndReplace(
            { userId: userId, _id: id }, { $set: music }, { ReturnDocument: ReturnDocument.AFTER }
        );
        //
        if (!ok) return false;
        return value;
    }
    static async patchUpdateMusic({ id, music, userId }){
        const db = await connectDB();
        const { ok, value } = await db.collection(musicCollectionName).findOneAndUpdate(
            { userId: userId, _id: id }, { $set: music }, { returnDocument: ReturnDocument.AFTER }
        );
        //
        if (!ok) return false;
        return value;
    }
    static async deleteMusic(id, userId){
        const db = await connectDB();
        return (await db.collection(musicCollectionName).findOneAndDelete({ userId: userId, _id: id })) !== null;
    }
}