import { connectDB } from './services/database/connection/mongoDbConection.js';
import { findAvailablePort } from "./utils/functions/findAvailablePortFunction.mjs";
//
const app = require("./taskManagerServer.mjs")
const portToBind = parseInt(process.env.SERVER_BIND_PORT) ?? 2002;
////
// Conexión a MongoDB openwebinars
connectDB().then(() => findAvailablePort(portToBind))
.then(port => {
  app.listen(port, () => {
    console.log(`✅ Conectado a MongoDB`);
    console.log(`🚀 Servidor corriendo en el puerto: ${port}`);
  });
}).catch(error => {
  console.error("❌ Error en la conexión a MongoDB o al buscar puerto disponible:", error);
  process.exit(1);
});