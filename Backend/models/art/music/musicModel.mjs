// Modulos de node
import { randomUUID } from 'node:crypto';
// Modulos locales.
import { connectDB } from '../../../services/database/connection/mongoDbConnection.mjs';
import { MUSIC_COLLECTION_NAME, RETURN_DOCUMENT_VALUE } from '../../../utils/export/GenericEnvConfig.mjs';
//// Exportamos la clase.
export class MusicModel {
    static async getAllMusics(userId){
        const db = await connectDB();
        const musics = await db.collection(MUSIC_COLLECTION_NAME).find({ userId: userId }, { projection: { userId: 0 } }).toArray();
        return musics.length ? musics : false;
    }
    static async getMusicById(id, userId){
        const db = await connectDB();
        return db.collection(MUSIC_COLLECTION_NAME).findOne({ userId: userId, _id: id }, { projection: { userId: 0 } });
    }
    static async postNewMusic({ music, userId }){
        const db = await connectDB();
        const newMusic = {
            ...music,
            userId: userId,
            _id: randomUUID()
        };
        const { insertedId } = await db.collection(MUSIC_COLLECTION_NAME).insertOne(newMusic);
        return { id: insertedId, ...newMusic };
    }
    static async putUpdateMusic({ id, music, userId }){
        const db = await connectDB();
        const newMusic = {
            ...music,
            userId: userId,
            _id: id
        };
        const value = await db.collection(MUSIC_COLLECTION_NAME).findOneAndReplace(
            { userId: userId, _id: id }, newMusic, { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Devolvemos false si no hay valor.
        if (!value) return false;   
        return value;
    }
    static async patchUpdateMusic({ id, music, userId }){
        const db = await connectDB();
        const value = await db.collection(MUSIC_COLLECTION_NAME).findOneAndUpdate(
            { userId: userId, _id: id }, { $set: { ...music, userId: userId } },
            { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Devolvemos false si no hay valor.
        if (!value) return false;
        return value;
    }
    static async deleteMusic(id, userId){
        const db = await connectDB();
        return (await db.collection(MUSIC_COLLECTION_NAME).findOneAndDelete({ userId: userId, _id: id })) !== null;
    }
}