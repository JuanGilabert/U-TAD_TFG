import { MongoClient, ServerApiVersion } from 'mongodb';
import { mongoConnectionUrl } from '../config/dbConnectionData.js';
// MongoDB Atlas URI
const client = new MongoClient(mongoConnectionUrl, {
    maxPoolSize: 50
});
let connectionPromise;
export async function connectDB() {
    if (!connectionPromise) {
        connectionPromise = client.connect()
            .then(() => client.db(process.env.DB_NAME))
            .catch((err) => {
                connectionPromise = null; // reset on failure
                console.error("Error connecting to MongoDB:", err);
                throw err;
            });
    }
    return connectionPromise;
}