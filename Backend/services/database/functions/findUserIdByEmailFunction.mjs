// Modulos locales
import { connectDB } from '../connection/mongoDbConnection.mjs';
import { AUTH_COLLECTION_NAME } from '../../../utils/export/GenericEnvConfig.mjs';
// Funcion asincrona para encontrar el id del usuario que coincide con el email recibido.
export async function findUserIdByEmailFunction(usEmail) {
    const db = await connectDB();
    // Buscamos el id del usuario que coincide con el email recibido.
    // Indicamos que busque el documento cuyo campo userEmail sea igual al email recibido.
    const user = await db.collection(AUTH_COLLECTION_NAME).findOne({ userEmail: usEmail });
    return user ? user._id : null;
}