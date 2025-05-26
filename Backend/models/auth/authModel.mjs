// Modulos de node.
import { randomUUID } from 'node:crypto';
import { hash, genSalt, compare } from 'bcrypt';
// Modulos locales.
import { connectDB, closeDbConnection } from '../../services/database/connection/mongoDbConnection.mjs';
import { jwtGenerator } from '../../services/jwt/generator/jwtGenerator.mjs';
import { AUTH_COLLECTION_NAME, HASH_SALT_ROUNDS, RETURN_DOCUMENT_VALUE } from '../../utils/export/GenericEnvConfig.mjs';
import { checkIfUserEmailExistsFunction } from '../../services/database/functions/checkIfUserEmailExistsFunction.mjs';
//// Exportamos la clase.
export class AuthModel {
    // Funcion asincrona para crear un nuevo usuario
    static async postNewUser({ userName, userPassword, userEmail }) {
        const db = await connectDB();
        // Comprobamos que no exista el email para poder registrar al usuario. Si existe, devolvemos un error.
        const userEmailExists = await checkIfUserEmailExistsFunction(userEmail);
        if (userEmailExists) return { message: "Existing email." };
        try {
            const saltRounds = await genSalt(HASH_SALT_ROUNDS);
            // Rezlizamos el hash de la contraseña recibida con el salt correspondiente.
            const hashedPassword = await hash(userPassword, saltRounds);
            // Creamos el nuevo usuario con los valores correspondientes.
            const newUser = { _id: randomUUID(), userName, userPassword: hashedPassword, userEmail, userJWT: "" };
            // Guardamos el nuevo usuario.
            const { insertedId } = await db.collection(AUTH_COLLECTION_NAME).insertOne(newUser);
            return { id: insertedId, ...newUser };
        } catch (error) {
            console.error("Error creating user: ", error);
            return { message: "Error creating user." };
        }
    }
    // Funcion asincrona para loguear a un usuario
    static async postLoginUser({ userEmail, userPassword }) {
        const db = await connectDB();
        let token = "";
        const user = await checkIfUserEmailExistsFunction(userEmail, { returnUser: true });
        if (!user) return { message: "Invalid input." };
        try {
            const isPasswordValid = await compare(userPassword, user.userPassword);
            if (!isPasswordValid) return { message: "Invalid input." };
            // Generar JWT para el usuario.
            token = jwtGenerator({ userEmail: user.userEmail });
        } catch (error) {
            console.error("Error al loguear el usuario:", error);
            return { message: "Login error." };
        }
        // Guardamos el JWT en la base de datos asociado al usuario en la propiedad userJWT.
        const value = await db.collection(AUTH_COLLECTION_NAME).findOneAndUpdate(
            { _id: user._id }, { $set: { userJWT: token } }, { returnDocument: RETURN_DOCUMENT_VALUE }
        );
        // Devolvemos el error obtenido al intentar loguear al usuario.
        if (!value) return false;
        // Enviamos el JWT en la respuesta para que el cliente lo reciba y pueda usar la aplicacion.
        return value.userJWT;
    }
    // Funcion asincrona para cerrar la sesion de un usuario. SE SUPONE QUE SOLO SE RECIBE UN TOKEN PARA ACTUALIZAR AL USUARIO LOGUEADO.
    static async postLogoutUser({ userJWT }) {
        const db = await connectDB();
        const value = await db.collection(AUTH_COLLECTION_NAME).findOneAndUpdate({ userJWT: userJWT }, { $set: { userJWT: "" } });
        // Devolvemos el error obtenido al intentar cerrar la sesion.
        if (!value) return false;
        // Cerramos la conexion con la base de datos.
        await closeDbConnection();
        // Devolvemos la respuesta obtenida.
        return value;
    }
    //
    static async getUserProfileData(userId) {
        const db = await connectDB();
        const user = await db.collection(AUTH_COLLECTION_NAME).findOne(
            { userId: userId }, { projection: { _id: 0, userPassword: 0, userJWT: 0 } }
        );
        return user || false;
    }
}