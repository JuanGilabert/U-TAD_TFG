// Importamos modulos de node.
import { Router } from 'express';
// Importamos el middleware de las request y los controladores.
import { requestMiddleware } from '../../middlewares/requestMiddleware.mjs';
import { authMiddleware } from '../../middlewares/authMiddleware.mjs';
import { AdminController } from '../../controllers/auth/adminController.mjs';
/** adminRouter.
 * Esta funcion devuelve un router de express que tiene las siguiente rutas:
 * - POST /signup: Registra un nuevo usuario en la base de datos.
 * - POST /signin: Inicia la sesion de un usuario y devuelve un token.
 * - POST /signout: Cierra la sesion del usuario actual.
 * - GET /profile: Devuelve la informacion del usuario actual.
 * 
 * @param {Object} options - Objeto con la configuracion del router.
 * @param {Model} options.AdminModel - Modelo de la base de datos para los usuarios.
 * @returns {Router} Un router de express con las rutas de autenticacion.
 */
export const adminRouter = ({ AdminModel }) => {
    const adminRouter = Router();
    const adminController = new AdminController({ model: AdminModel });
    // POST. Inicio de sesión. --> /api/admin/auth/signin
    adminRouter.post('/auth/sign-in', [requestMiddleware], adminController.postSignInUser);
    // POST. Cierre de sesion. --> /api/admin/auth/signout
    adminRouter.post('/auth/sign-out', [authMiddleware], [requestMiddleware], adminController.postSignOutUser);
    // Devolvemos la configuracion del router.
    return adminRouter;
}