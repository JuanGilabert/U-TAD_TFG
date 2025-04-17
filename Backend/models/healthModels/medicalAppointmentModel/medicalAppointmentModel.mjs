// Modulos de node
import { randomUUID } from 'node:crypto'
//
import { connectDB } from '../../../services/database/connection/mongoDbConection.js';
import { ReturnDocument } from 'mongodb';
const medicalAppointmentCollectionName = process.env.MEDICAL_APPOINTMENT_COLLECTION_NAME;
////
export class MedicalAppointmentModel {
    static async getAllMedicalAppointments(userId) {
        const db = await connectDB();
        const medicalAppointments = await db.collection(medicalAppointmentCollectionName).find({ userId: userId }).toArray();
        return medicalAppointments.length ? medicalAppointments : false;
    }
    static async getMedicalAppointmentsById(id, userId) {
        const db = await connectDB();
        return db.collection(medicalAppointmentCollectionName).findOne({ userId: userId, _id: id }, { projection: { _id: 0 } });
    }
    static async postNewMedicalAppointment({ medicalAppointment, userId }) {
        const db = await connectDB();
        const newMedicalAppointment = {
            id: randomUUID(),
            ...medicalAppointment,
            userId: userId
        }
        const { insertedId } = await db.collection(medicalAppointmentCollectionName).insertOne(newMedicalAppointment);
        return { id: insertedId, ...newMedicalAppointment };
    }
    static async putUpdateMedicalAppointment({ id, medicalAppointment, userId }) {
        const db = await connectDB();
        //
        const { ok, value } = await db.collection(medicalAppointmentCollectionName).findOneAndReplace(
            { userId: userId, _id: id }, { $set: medicalAppointment }, { returnDocument: ReturnDocument.AFTER }
        );
        if (!ok) return false
        return value
    }
    static async patchUpdateMedicalAppointment({ id, medicalAppointment, userId }) {
        const db = await connectDB();
        const { ok, value } = await db.collection(medicalAppointmentCollectionName).findOneAndUpdate(
            { userId: userId, _id: id }, { $set: medicalAppointment }, { returnDocument: ReturnDocument.AFTER }
        );
        //
        if (!ok) return false
        return value
    }
    static async deleteMedicalAppointment(id, userId) {
        const db = await connectDB();
        return (await db.collection(medicalAppointmentCollectionName).findOneAndDelete({ userId: userId, _id: id })) !== null;
    }
}