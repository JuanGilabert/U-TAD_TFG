// Modulos de node.
import { hash, genSalt } from 'bcrypt';
import { randomUUID } from 'node:crypto';
// Modulos locales.
import { connectDB } from '../../services/database/connection/mongoDbConnection.mjs';
import { checkIfUserExistsFunction } from '../../services/database/functions/mongoDbFunctions.mjs';
import { USER_COLLECTION_NAME, ADMIN_ROLE_PASSWORD, HASH_SALT_ROUNDS, RETURN_DOCUMENT_AFTER_VALUE } from '../../config/GenericEnvConfig.mjs';
import { act } from 'react';
//// Exportamos la clase.
export class UserModel {
    // Funcion asincrona para obtener todos los usuarios.
    static async getAllUsers() {
        const db = await connectDB();
        const users = await db.collection(USER_COLLECTION_NAME).find({}).toArray();
        return users || false;
    }
    // Funcion asincrona para obtener un usuario por su id.
    static async getUserById(userId, userRole = "user") {
        const db = await connectDB();
        let user = "";
        if (userRole === "admin") user = await db.collection(USER_COLLECTION_NAME).findOne({ _id: userId });
        else user = await db.collection(USER_COLLECTION_NAME).findOne(
            { _id: userId }, { projection: { userRole: 0, userJWT: 0, userActive: 0 } }
        );
        return user || false;
    }
    // Funcion asincrona para crear un nuevo usuario.
    static async postUser({ userName, userPassword, userEmail }) {
        const db = await connectDB();
        // Comprobamos que no exista el email para poder registrar al usuario. Si existe, devolvemos un error.
        const userEmailExists = await checkIfUserExistsFunction(userEmail);
        if (userEmailExists) return { type: "Email", message: "El email indicado ya existe. Introduzca otro distinto." };
        try {
            // Rezlizamos el hash de la contraseña recibida con el salt correspondiente.
            const saltRounds = await genSalt(HASH_SALT_ROUNDS);
            const hashedPassword = await hash(userPassword, saltRounds);
            // Verificamos si la contraseña contiene la validacion para tener rol de admin o no.
            const userRole = userPassword.includes(ADMIN_ROLE_PASSWORD) ? "admin" : "user";
            const userAcountIsActive = userRole === "admin" ? true : false;
            // Creamos el nuevo usuario con los valores correspondientes.
            const newUser = { _id: randomUUID(), userName, userPassword: hashedPassword, userEmail, userRole, userJWT: "", userActive: userAcountIsActive };
            // Guardamos el nuevo usuario.
            const { acknowledged, insertedId } = await db.collection(USER_COLLECTION_NAME).insertOne(newUser);
            //if (!acknowledged) throw new Error('La operación de inserción no fue reconocida por MongoDB.');
            return !acknowledged ? { type: "Error", message: "Error al crear la el usuario." } : { id: insertedId, ...newUser };
        } catch (error) {
            console.error("Error creating user: ", error);
            throw new Error(`No se pudo insertar el usuario en la base de datos: ${error}`);
        }
    }
    // Funcion asincrona para actualizar un usuario.
    static async putUser({ id, user, userRole }) {
        const db = await connectDB();
        // Guardamos los nuevos datos del usuario.
        const updatedUser = {
            ...user,
            userRole,
            userJWT: "",
            _id: id
        };
        // Obtenemos el resultado de la operación de actualización.
        const { value, lastErrorObject, ok } = await db.collection(USER_COLLECTION_NAME).findOneAndReplace(
            { _id: id }, updatedUser,
            { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userRole: 0, userJWT: 0, confirmed: 0 } }
        );
        // Si ok es 0 devolvemos el error obtenido.
        if (!ok && lastErrorObject) throw new Error(`No se pudo actualizar la tarea: ${lastErrorObject}`);
        // Devolvemos false si no hay valor(null).
        if (ok && !value) return false;
        // Devolvemos el documento actualizado.
        if (ok && value) return value;
    }
    // Funcion asincrona para actualizar un usuario.
    static async patchUser({ id, user }) {
        const db = await connectDB();
        // Obtenemos el resultado de la operación de actualización.
        const { value, lastErrorObject, ok } = await db.collection(USER_COLLECTION_NAME).findOneAndUpdate(
            { _id: id }, { $set: { ...user } },
            { returnDocument: RETURN_DOCUMENT_VALUE, projection: { userRole: 0, userJWT: 0 } }
        );
        // Si ok es 0 devolvemos el error obtenido.
        if (!ok && lastErrorObject) throw new Error(`No se pudo actualizar la tarea: ${lastErrorObject}`);
        // Devolvemos false si no hay valor(null).
        if (ok && !value) return false;
        // Devolvemos el documento actualizado.
        if (ok && value) return value;
    }
    // Funcion asincrona para eliminar un usuario.
    static async deleteUser({ id }) {
        const db = await connectDB();
        // Comprobamos si la respuesta es distinto de null. Si es asi indica que se elimino el usuario con el id recibido.
        // Si es null, indica que no se elimino el usuario con el id recibido debido a
        // (ver si es un error de red o sintactico o si de verdad no existe para saber que devolver en el controlador)
        return (await db.collection(USER_COLLECTION_NAME).findOneAndDelete({ _id: id })) !== null;        
    }
}