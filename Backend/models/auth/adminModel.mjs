// Modulos de node.
import { hash, genSalt, compare } from 'bcrypt';
// Modulos locales.
import { connectDB, closeDbConnection } from '../../services/database/connection/mongoDbConnection.mjs';
import { jwtGenerator } from '../../services/jwt/generator/jwtGenerator.mjs';
import { AUTH_COLLECTION_NAME, HASH_SALT_ROUNDS, RETURN_DOCUMENT_VALUE } from '../../utils/export/GenericEnvConfig.mjs';
import { checkIfUserEmailExistsFunction } from '../../services/database/functions/checkIfUserEmailExistsFunction.mjs';
//// Exportamos la clase.
export class AdminModel {
    // Funcion asincrona para loguear a un usuario
    static async postSignInUser({ userEmail, userPassword }) {
        const db = await connectDB();
        let token = "";
        let hashedToken = "";
        const user = await checkIfUserEmailExistsFunction(userEmail, { returnUser: true });
        if (!user) return { message: "Invalid input." };
        try {
            const isPasswordValid = await compare(userPassword, user.userPassword);
            if (!isPasswordValid) return { message: "Invalid input." };
            // Generar JWT para el usuario.
            token = jwtGenerator({ userEmail: user.userEmail, userRole: user.userRole });
            // Hasheamos el token generado. VERIFICAR SI ESTO PUEDE GENERAR PROBLEMAS PARA LA EXTRACCION DE LA INFORMACION DEL USUARIO.
            //const saltRounds = await genSalt(HASH_SALT_ROUNDS);
            //hashedToken = await hash(token, saltRounds);
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
    static async postSignOutUser({ userJWT }) {
        const db = await connectDB();
        // Hasheamos el token generado. VERIFICAR SI ESTO PUEDE GENERAR PROBLEMAS PARA LA EXTRACCION DE LA INFORMACION DEL USUARIO.
        //const saltRounds = await genSalt(HASH_SALT_ROUNDS);
        //let hashedUserJWT = await hash(token, saltRounds);
        const value = await db.collection(AUTH_COLLECTION_NAME).findOneAndUpdate({ userJWT: userJWT }, { $set: { userJWT: "" } });
        // Devolvemos el error obtenido al intentar cerrar la sesion.
        if (!value) return false;
        // Cerramos la conexion con la base de datos.
        await closeDbConnection();
        // Devolvemos la respuesta obtenida.
        return value;
    }
}