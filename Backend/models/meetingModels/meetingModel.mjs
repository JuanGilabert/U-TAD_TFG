// Modulos de node
import { randomUUID } from 'node:crypto'
//
import { connectDB } from '../../services/database/connection/mongoDbConection.js';
import { ReturnDocument } from 'mongodb';
const meetingCollectionName = process.env.MEETING_COLLECTION_NAME;
////
export class MeetingModel {
    static async getAllMeetings() {
        const db = await connectDB(userId);
        const meetings = await db.collection(meetingCollectionName).find({ userId: userId }).toArray();
        return meetings.length ? meetings : false;
    }
    static async getMeetingById(id, userId) {
        const db = await connectDB();
        return db.collection(meetingCollectionName).findOne({ userId: userId, _id: id }, { projection: { _id: 0 } });
    }
    static async postNewMeeting({ meeting, userId }) {
        const db = await connectDB();
        const newMeeting = {
            id: randomUUID(),
            ...meeting,
            userId: userId
        }
        //
        const { insertedId } = await db.collection(meetingCollectionName).insertOne(newMeeting);
        return { id: insertedId, ...newMeeting };
    }
    static async putUpdateMeeting({ id, meeting, userId }) {
        const db = await connectDB();
        const { ok, value } = await db.collection(meetingCollectionName).findOneAndReplace(
            { userId: userId, _id: id }, { $set: meeting }, { returnDocument: ReturnDocument.AFTER }
        );
        //
        if (!ok) return false;
        return value;
    }
    static async patchUpdateMeeting({ id, meeting, userId }) {
        const db = await connectDB();
        const { ok, value } = await db.collection(meetingCollectionName).findOneAndUpdate(
            { userId: userId, _id: id }, { $set: meeting }, { returnDocument: ReturnDocument.AFTER }
        );
        //
        if (!ok) return false;
        return value;
    }
    static async deleteMeeting(id, userId) {
        const db = await connectDB();
        return (await db.collection(meetingCollectionName).findOneAndDelete({userId: userId,  _id: id })) !== null;
    }
}