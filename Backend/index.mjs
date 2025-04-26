// Modulos de node.
import dotenv from 'dotenv';
// Habilitamos dotenv para cargar las variables de entorno del archivo .env antes de realizar cualquier accion en el servidor.
dotenv.config();
// Modulos locales.
import { connectDB, closeDbConnection, getConnectionPromise } from './services/database/connection/mongoDbConnection.mjs';
import { findAvailablePort } from "./utils/functions/findAvailablePortFunction.mjs";
import { SERVER_BIND_PORT } from './utils/export/GenericEnvConfig.mjs';
import { app } from "./taskManagerServer.mjs";
// Verificamos que el puerto sea valido.
const isValidPort = !isNaN(SERVER_BIND_PORT) && SERVER_BIND_PORT > 0 && SERVER_BIND_PORT < 65536;
const safePort = isValidPort ? SERVER_BIND_PORT : 2002;
//// Conexión a MongoDB.
connectDB().then(() => findAvailablePort(safePort))
.then(port => {
  app.listen(port, () => console.log(`✅ Conectado a MongoDB.🚀 Servidor corriendo en el puerto: ${port}`));
  app.on("error", error => console.error("❌ Error en el servidor:", error));
  app.on("close", () => {
    // Cerramos la conexion con la base de datos en caso de existir.
    if (getConnectionPromise) closeDbConnection();
    console.log("❌ Servidor cerrado");
  });
}).catch(error => {
  console.error("❌ Error en la conexión a MongoDB o al buscar puerto disponible:", error);
  process.exit(1);
});