// Importamos modulos de node.
import { Router } from 'express';
// Importamos el middleware de las request y los controladores.
import { requestMiddleware } from '../../middlewares/requestMiddleware.mjs';
import { AuthController } from '../../controllers/auth/authController.mjs';
/** authRouter.
 * Esta funcion devuelve un router de express que tiene las siguiente rutas:
 * - POST /signup: Registra un nuevo usuario en la base de datos.
 * - POST /signin: Inicia la sesion de un usuario y devuelve un token.
 * - POST /signout: Cierra la sesion del usuario actual.
 * - GET /profile: Devuelve la informacion del usuario actual.
 * 
 * @param {Object} options - Objeto con la configuracion del router.
 * @param {Model} options.AuthModel - Modelo de la base de datos para los usuarios.
 * @returns {Router} Un router de express con las rutas de autenticacion.
 */
export const authRouter = ({ AuthModel }) => {
    const authRouter = Router();
    const authController = new AuthController({ model: AuthModel });
    // POST. Registro de usuario. --> api/auth/signup
    authRouter.post('/signup', authController.postNewUser);
    // POST. Inicio de sesión. --> api/auth/signin
    authRouter.post('/signin', authController.postLoginUser);
    // POST. Cierre de sesion. --> api/auth/signout
    authRouter.post('/signout', [requestMiddleware], authController.postLogoutUser);
    // GET. Obtencion de un usuario. --> api/auth/profile
    authRouter.get('/profile', [requestMiddleware], authController.getUserProfileData);
    // Devolvemos la configuracion del router.
    return authRouter;
}