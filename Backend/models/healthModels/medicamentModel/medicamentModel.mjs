// Modulos de node
import { randomUUID } from 'node:crypto'
//
import { connectDB } from '../../../services/database/connection/mongoDbConection.js';
import { ReturnDocument } from 'mongodb';
const medicamentCollectionName = process.env.MEDICAMENT_COLLECTION_NAME;
/* Esquema para el modelo de datos de medicamentos en el sistema de salud.
Define la estructura de los datos de un medicamento almacenado en la base de datos. */
export class MedicamentModel {
    static async getAllMedicaments(userId) {
        const db = await connectDB();
        const medicaments = await db.collection(medicamentCollectionName).find({ userId: userId }).toArray();
        return medicaments.length ? medicaments : false;
    }
    static async getMedicamentById(id, userId) {
        //
        const db = await connectDB();
        return db.collection(medicamentCollectionName).findOne({ userId: userId, _id: id }, { projection: { _id: 0 } });
    }
    static async postNewMedicament({ medicament, userId }) {
        const db = await connectDB();
        const newMedicament = {
            id: randomUUID(),
            ...medicament,
            userId: userId
        };
        //
        const { insertedId } = await db.collection(medicamentCollectionName).insertOne(newMedicament);
        return { id: insertedId, ...newMedicament };
    }
    static async putMedicament({ id, medicament, userId }) {
        //
        const db = await connectDB();
        const { ok, value } = await db.collection(medicamentCollectionName).findOneAndReplace(
            { userId: userId, _id: id }, { $set: medicament }, { returnDocument: ReturnDocument.AFTER }
        );
        //
        if (!ok) return false
        return value
    }
    static async patchMedicament({ id, medicament, userId }) {
        const db = await connectDB();
        const { ok, value } = await db.collection(medicamentCollectionName).findOneAndUpdate(
            { userId: userId, _id: id }, { $set: medicament }, { returnDocument: ReturnDocument.AFTER }
        );
        //
        if (!ok) return false
        return value
    }
    static async deleteMedicament(id, userId) {
        const db = await connectDB();
        return (await db.collection(medicamentCollectionName).findOneAndDelete({ userId: userId, _id: id })) !== null;
    }
}