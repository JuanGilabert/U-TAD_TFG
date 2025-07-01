// Importamos el generador de routers.
import { expressRouterGenerator } from '../../utils/functions/expressRouterGeneratorFunction.mjs';
// Importamos los controladores
import { UserController } from '../../controllers/user/userController.mjs';
// Importamos los middlewares necesarios.
import { authMiddleware } from '../../middlewares/auth/authMiddleware.mjs';
// Importamos los valores de las rutas/endpoints
import { IDENTIFIER_ROUTE_PATH } from '../../config/GenericEnvConfig.mjs';
//// Exportamos el router.
export const userRouter = ({ UserModel }) => {
    const userRouter = expressRouterGenerator();
    const userController = new UserController({ model: UserModel });
    /* User */
    // GET. Obtencion de todos los usuarios. --> /api/user/
    userRouter.get(`/`, authMiddleware, userController.getAllUsers);
    // GET. Obtencion de un usuario. --> /api/user/:id
    userRouter.get(`${IDENTIFIER_ROUTE_PATH}`, userController.getUserById);
    // POST. Registro de usuario. --> /api/user/
    userRouter.post(`/`, userController.postUser);
    // PUT. Actualizacion de un usuario. --> /api/user/:id
    userRouter.put(`${IDENTIFIER_ROUTE_PATH}`, userController.putUser);
    // PATCH. Actualizacion parcial de un usuario. --> /api/user/:id
    userRouter.patch(`${IDENTIFIER_ROUTE_PATH}`, userController.patchUser);
    // DELETE. Eliminacion de un usuario. --> /api/user/:id
    userRouter.delete(`${IDENTIFIER_ROUTE_PATH}`, userController.deleteUser);
    // Devolvemos la configuracion del router.
    return userRouter;
}