// Modulos de node.
import { randomUUID } from 'node:crypto';
// Modulos locales.
import { connectDB } from '../../services/database/connection/mongoDbConnection.mjs';
import { FOOD_COLLECTION_NAME, RETURN_DOCUMENT_VALUE } from '../../utils/export/GenericEnvConfig.mjs';
//// Exportamos la clase.
export class FoodModel {
    static async getAllFoods(userId) {
        const db = await connectDB();
        const foods = await db.collection(FOOD_COLLECTION_NAME).find({ userId: userId }, { projection: { userId: 0 } }).toArray();
        return foods.length ? foods : false;
    }
    static async getFoodById(id, userId) {
        const db = await connectDB();
        return db.collection(FOOD_COLLECTION_NAME).findOne({ userId: userId, _id: id }, { projection: { userId: 0 } });
    }
    static async postNewFood({ food, userId }) {
        const db = await connectDB();
        const newFood = {
            ...food,
            userId: userId,
            _id: randomUUID()
        };
        //
        const { insertedId } = await db.collection(FOOD_COLLECTION_NAME).insertOne(newFood);
        return { id: insertedId, ...newFood };
    }
    static async putUpdateFood({ id, food, userId }) {
        const db = await connectDB();
        const newFood = {
            ...food,
            userId: userId,
            _id: id
        };
        const value = await db.collection(FOOD_COLLECTION_NAME).findOneAndReplace(
            { userId: userId, _id: id }, newFood, { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Devolvemos false si no hay valor.
        if (!value) return false;
        return value;
    }
    static async patchUpdateFood({ id, food, userId }) {
        const db = await connectDB();
        const value = await db.collection(FOOD_COLLECTION_NAME).findOneAndUpdate(
            { userId: userId, _id: id }, { $set: { ...food, userId: userId } },
            { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userId: 0 } }
        );
        // Devolvemos false si no hay valor.
        if (!value) return false;
        return value;
    }
    static async deleteFood(id, userId) {
        const db = await connectDB();
        return (db.collection(FOOD_COLLECTION_NAME).findOneAndDelete({ userId: userId, _id: id })) !== null;
    }
}