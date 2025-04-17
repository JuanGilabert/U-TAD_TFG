// Modulos de node
import { randomUUID } from 'node:crypto';
import { hash, genSalt, compare } from 'bcrypt';
// Modulos locales
import { jwtGenerator } from '../../services/jwt/generator/jwtGenerator.mjs';
import { connectDB, closeDbConnection } from '../../services/database/connection/mongoDbConection.js';
import { ReturnDocument } from 'mongodb';
// Constantes a nivel de modulo.
const userCollectionName = process.env.USER_COLLECTION_NAME;
const hashRounds = parseInt(process.env.HASH_SALT_ROUNDS);
const returnDocumentValue = ReturnDocument.AFTER;
////
export class AuthModel {
    // Funcion asincrona para crear un nuevo usuario
    static async postNewUser({ input }) {
        const db = await connectDB();
        // Obtenemos los datos.
        const { userName, userPassword, userEmail } = input;
        // Comprobamos que no exista el email para poder registrar al usuario. Si existe, devolvemos un error.
        if (await AuthModel.checkIfUserEmailExists(userEmail)) return { message: "Existing email." };
        try {
            // Rezlizamos el hash de la contraseña recibida con el salt correspondiente.
            const hashedPassword = await hash(userPassword, await genSalt(hashRounds));
            // Creamos el nuevo usuario con los valores correspondientes.
            const newUser = { id: randomUUID(), userName, hashedPassword, userEmail, userJWT: "" };
            // Guardamos el nuevo usuario.
            const { insertedId } = await db.collection(userCollectionName).insertOne(newUser);
            return { id: insertedId, ...newUser };
        } catch (error) {
            console.error("Error creating user: ", error);
            return { message: "Error creating user." };
        }
    }
    // Funcion asincrona para loguear a un usuario
    static async postLoginUser(input) {
        const db = await connectDB();
        // Obtenemos los datos.
        const { userEmail, userPassword } = input;
        let token = "";
        const user = await AuthModel.checkIfUserEmailExists(userEmail, { returnUser: true });
        try {
            if (!user || !(await compare(userPassword, user.userPassword)))
                return { message: "Invalid input." };
            // Generar JWT para el usuario.
            token = jwtGenerator({ userEmail: user.userEmail });
        } catch (error) {
            console.error("Error al loguear el usuario:", error);
            return { message: "Login error." };
        }
        // Guardamos el JWT en la base de datos asociado al usuario en la propiedad userJWT.
        const { ok, value } = await db.collection(userCollectionName).findOneAndUpdate(
            { _id: user._id }, { $set: { userJWT: token } }, { returnDocument: returnDocumentValue }
        );
        // ESTO PUEDE CAUSAR PROBLEMAS(|| !value). REVISAR
        if (!ok || !value) return false;
        // Enviamos el JWT en la respuesta para que el cliente lo reciba y pueda usar la aplicacion.
        return value.userJWT;
    }
    // Funcion asincrona para cerrar la sesion de un usuario. SE SUPONE QUE SOLO SE RECIBE UN TOKEN PARA ACTUALIZAR AL USUARIO LOGUEADO.
    static async postLogoutUser(userId) {
        const db = await connectDB();
        const { ok, value } = await db.collection(userCollectionName).findOneAndUpdate({ userId: userId }, { userJWT: "" });
        // Devolvemos el error obtenido al intentar cerrar la sesion.
        if (!ok) return false;
        // Cerramos la conexion con la base de datos.
        await closeDbConnection();
        // Devolvemos la respuesta obtenida.
        return value;
    }
    //
    static async getUserProfileData(userId) {
        const db = await connectDB();
        const user = await db.collection(userCollectionName).findOne(
            { userId }, { projection: { _id: 0, userPassword: 0, userJWT: 0 } }
        );
        return user || false;
    }
    /* Funcion asincrona para comprobar si un email ya existe o no en la base de datos.
    En funcion del metodo que llame a esta funcion devolvera un valor u otro. En el metodo postNewUser devolvera true o false.
    En el metodo postLoginUser devolvera el usuario encontrado en base al email o false en caso de no encontrar a ningun usuario. */
    static async checkIfUserEmailExists (userEmail, { returnUser = false } = {} ){
        const db = await connectDB();
        const user = await db.collection(userCollectionName).findOne({ userEmail });
        // devolvemos true o false en funcion del metodo que llame a checkIfUserEmailExists.
        return returnUser ? user || false : user !== null;
    }
}