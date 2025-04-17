// Modulos de node
import { randomUUID } from 'node:crypto'
//
import { connectDB } from '../../services/database/connection/mongoDbConection.js';
import { ReturnDocument } from 'mongodb';
const foodCollectionName = process.env.FOOD_COLLECTION_NAME;
////
export class FoodModel {
    static async getAllFoods(userId) {
        const db = await connectDB();
        const foods = await db.collection(foodCollectionName).find({ userId: userId }).toArray();
        return foods.length ? foods : false;
    }
    static async getFoodById(id, userId) {
        const db = await connectDB();
        return db.collection(foodCollectionName).findOne({ userId: userId, _id: id }, { projection: { _id: 0 } });
    }
    static async postNewFood({ food, userId }) {
        const db = await connectDB();
        const newFood = {
            id: randomUUID(),
            ...food,
            userId: userId
        };
        //
        const { insertedId } = await db.collection(foodCollectionName).insertOne(newFood);
        return { id: insertedId, ...newFood };
    }
    static async putUpdateFood({ id, food, userId }) {
        const db = await connectDB();
        const { ok, value } = await db.collection(foodCollectionName).findOneAndReplace(
            { userId: userId, _id: id }, { $set: food }, { ReturnDocument: ReturnDocument.AFTER }
        );
        //
        if (!ok) return false;
        return value;
    }
    static async patchUpdateFood({ id, food, userId }) {
        const db = await connectDB();
        const { ok, value } = await db.collection(foodCollectionName).findOneAndUpdate(
            { userId: userId, _id: id }, { $set: food }, { ReturnDocument: ReturnDocument.AFTER }
        );
        //
        if (!ok) return false;
        return value;
    }
    static async deleteFood(id, userId) {
        const db = await connectDB();
        return (db.collection(foodCollectionName).findOneAndDelete({ userId: userId, _id: id })) !== null;
    }
}