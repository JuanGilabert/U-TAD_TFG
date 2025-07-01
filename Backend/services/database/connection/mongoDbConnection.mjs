// Modulos
import { MongoClient, ServerApiVersion } from 'mongodb';
// Modulos locales
//import { mongoConnectionUrl } from '../config/dbConnectionData.js';
import { MONGODB_URI, DB_NAME } from '../../../config/GenericEnvConfig.mjs';
// MongoDB Atlas URI
const client = new MongoClient(MONGODB_URI, {
    serverApi: ServerApiVersion.v1,
    maxPoolSize: 50,
    connectTimeoutMS: 10000,
    socketTimeoutMS: 45000,
});
let connectionPromise;
/** Establishes a connection to the MongoDB database.
 * This asynchronous function attempts to connect to a MongoDB database using the
 * configuration specified in the MongoClient instance. If a connection has not
 * already been established, it will initiate a new connection and store the
 * resulting promise. If the connection fails, the promise is reset to null and
 * an error is thrown.
 *
 * @returns {Promise<Db>} A promise that resolves to the database instance if the
 * connection is successful.
 * @throws {Error} Throws an error if the connection attempt fails.
 */
export async function connectDB() {
    if (!connectionPromise) {
        connectionPromise = await client.connect().then(() => client.db(DB_NAME))
        .catch((err) => {
            connectionPromise = null;// reset on failure
            console.error("Error connecting to MongoDB:", err);
            throw err;
        });
    }
    return connectionPromise;
}
/** Cierra la conexi n actual con la base de datos.
 * Este m todo se encarga de cerrar la conexi n actual con la base de datos
 * de MongoDB. Si no se ha establecido una conexi n se muestra un mensaje
 * de error.
 *
 * @returns {Promise<void>}
 */
export async function closeDbConnection() {
    // Cerramos la conexion con la base de datos.
    if (connectionPromise) {
        try {
            await connectionPromise;
            await client.close();
            connectionPromise = null;
        } catch (error) {
            console.error("Error closing MongoDB connection:", error);
            throw error;
        }
    } else {
        console.error("No active MongoDB-connection to close.");
    }
}
// Getter para saber si la conexion con la base de datos esta abierta, es decir si tiene algun valor.
// Devolvemos false en caso de ser false, null, undefined ...
export const getConnectionPromise = () => !!connectionPromise;