// Modulos de node
import { randomUUID } from 'node:crypto';
// Modulos locales
import { connectDB } from '../../services/database/connection/mongoDbConnection.mjs';
import { MEETING_COLLECTION_NAME, RETURN_DOCUMENT_VALUE } from '../../utils/export/GenericEnvConfig.mjs';
//// Exportamos la clase.
export class MeetingModel {
    static async getAllMeetings(userId) {
        const db = await connectDB();
        const meetings = await db.collection(MEETING_COLLECTION_NAME).find({ userId: userId }, { projection: { userId: 0 } }).toArray();
        return meetings.length ? meetings : false;
    }
    static async getMeetingById(id, userId) {
        const db = await connectDB();
        return db.collection(MEETING_COLLECTION_NAME).findOne({ userId: userId, _id: id }, { projection: { userId: 0 } });
    }
    static async postNewMeeting({ meeting, userId }) {
        const db = await connectDB();
        const newMeeting = {
            ...meeting,
            userId: userId,
            _id: randomUUID()
        };
        const { insertedId } = await db.collection(MEETING_COLLECTION_NAME).insertOne(newMeeting);
        return { id: insertedId, ...newMeeting };
    }
    static async putUpdateMeeting({ id, meeting, userId }) {
        const db = await connectDB();
        const newMeeting = {
            ...meeting,
            userId: userId,
            _id: id
        };
        const value = await db.collection(MEETING_COLLECTION_NAME).findOneAndReplace(
            { userId: userId, _id: id }, newMeeting, { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Devolvemos false si no hay valor.
        if (!value) return false;
        return value;
    }
    static async patchUpdateMeeting({ id, meeting, userId }) {
        const db = await connectDB();
        const value = await db.collection(MEETING_COLLECTION_NAME).findOneAndUpdate(
            { userId: userId, _id: id }, { $set: { ...meeting, userId: userId } },
            { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Devolvemos false si no hay valor.
        if (!value) return false;
        return value;
    }
    static async deleteMeeting(id, userId) {
        const db = await connectDB();
        return (await db.collection(MEETING_COLLECTION_NAME).findOneAndDelete({userId: userId,  _id: id })) !== null;
    }
}