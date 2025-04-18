// Modulos de node.
import dotenv from 'dotenv';
// Modulos locales.
import { app } from "./taskManagerServer.mjs";
import { connectDB, closeDbConnection, getConnectionPromise } from './services/database/connection/mongoDbConection.js';
import { findAvailablePort } from "./utils/functions/findAvailablePortFunction.mjs";
// Puerto de escucha. Forzamos base 10 para evitar cosas raras como 0x10.
const parsedPort = parseInt(process.env.SERVER_BIND_PORT, 10);
// Verificamos que el puerto sea valido.
const isValidPort = !isNaN(parsedPort) && parsedPort > 0 && parsedPort < 65536;
const safePort = isValidPort ? parsedPort : 2002;
//// Habilitamos dotenv para cargar las variables de entorno.
dotenv.config();
// Conexión a MongoDB openwebinars
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