// Modulos de node.
import { randomUUID } from 'node:crypto';
// Modulos locales.
import { connectDB } from '../../../services/database/connection/mongoDbConnection.mjs';
import { MEDICAL_APPOINTMENT_COLLECTION_NAME, RETURN_DOCUMENT_VALUE } from '../../../utils/export/GenericEnvConfig.mjs';
//// Exportamos la clase.
export class MedicalAppointmentModel {
    static async getAllMedicalAppointments(userId) {
        const db = await connectDB();
        const medicalAppointments = await db.collection(MEDICAL_APPOINTMENT_COLLECTION_NAME).find(
            { userId: userId }, { projection: { userId: 0 } }
        ).toArray();
        return medicalAppointments.length ? medicalAppointments : false;
    }
    static async getMedicalAppointmentsById(id, userId) {
        const db = await connectDB();
        return db.collection(MEDICAL_APPOINTMENT_COLLECTION_NAME).findOne({ userId: userId, _id: id }, { projection: { userId: 0 } });
    }
    static async postNewMedicalAppointment({ medicalAppointment, userId }) {
        const db = await connectDB();
        const newMedicalAppointment = {
            ...medicalAppointment,
            userId: userId,
            _id: randomUUID()
        }
        const { insertedId } = await db.collection(MEDICAL_APPOINTMENT_COLLECTION_NAME).insertOne(newMedicalAppointment);
        return { id: insertedId, ...newMedicalAppointment };
    }
    static async putUpdateMedicalAppointment({ id, medicalAppointment, userId }) {
        const db = await connectDB();
        const newMedicalAppointment = {
            ...medicalAppointment,
            userId: userId,
            _id: id
        }
        const value = await db.collection(MEDICAL_APPOINTMENT_COLLECTION_NAME).findOneAndReplace(
            { userId: userId, _id: id }, newMedicalAppointment,{ returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Devolvemos false si no hay valor.
        if (!value) return false;
        return value;
    }
    static async patchUpdateMedicalAppointment({ id, medicalAppointment, userId }) {
        const db = await connectDB();
        const value = await db.collection(MEDICAL_APPOINTMENT_COLLECTION_NAME).findOneAndUpdate(
            { userId: userId, _id: id }, { $set: { ...medicalAppointment, userId: userId } },
            { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Devolvemos false si no hay valor.
        if (!value) return false;
        return value;
    }
    static async deleteMedicalAppointment(id, userId) {
        const db = await connectDB();
        return (await db.collection(MEDICAL_APPOINTMENT_COLLECTION_NAME).findOneAndDelete({ userId: userId, _id: id })) !== null;
    }
}