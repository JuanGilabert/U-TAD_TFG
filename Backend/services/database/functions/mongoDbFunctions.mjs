// Modulos locales
import { connectDB } from '../connection/mongoDbConnection.mjs';
import { USER_COLLECTION_NAME } from '../../../config/GenericEnvConfig.mjs';
/* Funcion asincrona para comprobar si un email ya existe o no en la base de datos.
En funcion del metodo que llame a esta funcion devolvera un valor u otro. En el metodo postNewUser devolvera true o false.
En el metodo postLoginUser devolvera el usuario encontrado en base al email o false en caso de no encontrar a ningun usuario. */
export async function checkIfUserExistsFunction(userEmail, { returnUser = false } = {} ) {
    const db = await connectDB();
    const user = await db.collection(USER_COLLECTION_NAME).findOne({ userEmail: userEmail }, { projection: {} });
    // En funcion del metodo que llame a checkIfUserEmailExists se devuelve el usuario encontrado o null en caso de no encontrar a ningun usuario
    // o se devuelve el valor booleano de la busqueda del usuario en caso de no devolver el usuario.
    return returnUser ? user : !!user;
}
////
export async function checkIfUserIsAuthorizedFunction(id, userId, collectionNameToSearch = "") {
    const db = await connectDB();
    try {
        const isAuthorized = await db.collection(collectionNameToSearch).findOne({ userId: userId, _id: id });
        return isAuthorized === null ? false : true;
    } catch (error) {
        console.error(error);
        return res.status(500).json({ message: `${INTERNAL_SERVER_ERROR_500_MESSAGE} ${error}` });
    }
}