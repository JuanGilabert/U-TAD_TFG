// Importamos modulos de node.
import { Router } from 'express';
// Importamos el middleware de las request y los controladores.
import { requestMiddleware } from '../../middlewares/requestMiddleware.mjs';
import { authMiddleware } from '../../middlewares/authMiddleware.mjs';
import { UserController } from '../../controllers/user/userController.mjs';

export const userRouter = ({ UserModel }) => {
    const userRouter = Router();
    const userController = new UserController({ model: UserModel });
    // GET. Obtencion de todos los usuarios. --> /api/user/
    userRouter.get('/', [authMiddleware], [requestMiddleware], userController.getAllUsers);
    // GET. Obtencion de un usuario. --> /api/user/:id
    userRouter.get('/:id', [authMiddleware], [requestMiddleware], userController.getUserById);
    // POST. Registro de usuario. --> /api/user/
    userRouter.post('/', [requestMiddleware], userController.postUser);
    // PUT. Actualizacion de un usuario. --> /api/user/:id
    userRouter.put('/:id', [authMiddleware], [requestMiddleware], userController.putUser);
    // PATCH. Actualizacion parcial de un usuario. --> /api/user/:id
    userRouter.patch('/:id', [authMiddleware], [requestMiddleware], userController.patchUser);
    // DELETE. Eliminacion de un usuario. --> /api/user/:id
    userRouter.delete('/:id', [authMiddleware], [requestMiddleware], userController.deleteUser);
    // Devolvemos la configuracion del router.
    return userRouter;
}