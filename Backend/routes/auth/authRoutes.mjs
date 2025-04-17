// Modulos de node.
import { Router } from 'express';
// Importamos el middleware de autenticacion y el controlador
import { authMiddleware } from '../../middlewares/auth/authMiddleware.mjs';
import { AuthController } from '../../controllers/auth/authController.mjs';
////
export const authRouter = ({ AuthModel }) => {
    const authRouter = Router();
    const authController = new AuthController({ AuthModel });
    // POST. Registro de usuario. --> api/auth/signup
    authRouter.post('/signup', authController.postNewUser);
    // POST. Inicio de sesión. --> api/auth/signin
    authRouter.post('/signin', authController.postLoginUser);
    // POST. Cierre de sesion. --> api/auth/signout
    authRouter.post('/signout', [authMiddleware], authController.postLogoutUser);
    // GET. Obtencion de un usuario. --> api/auth/profile
    authRouter.get('/profile', [authMiddleware], authController.getUserProfiledata);
    // Devolvemos el Router.
    return authRouter;
}