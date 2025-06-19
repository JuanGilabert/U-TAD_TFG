// Modulos locales
import { connectDB } from '../connection/mongoDbConnection.mjs';
import { AUTH_COLLECTION_NAME } from '../../../utils/export/GenericEnvConfig.mjs';
/* Funcion asincrona para comprobar si un email ya existe o no en la base de datos.
En funcion del metodo que llame a esta funcion devolvera un valor u otro. En el metodo postNewUser devolvera true o false.
En el metodo postLoginUser devolvera el usuario encontrado en base al email o false en caso de no encontrar a ningun usuario. */
export async function checkIfUserEmailExistsFunction(userEmail, { returnUser = false } = {} ) {
    const db = await connectDB();
    const user = await db.collection(AUTH_COLLECTION_NAME).findOne({ userEmail: userEmail }, { projection: {} });
    // devolvemos el usuario o false en caso de que se necesiten los datos del usuario en funcion del metodo que llame a checkIfUserEmailExists.
    return returnUser ? user || false : !!user;
}