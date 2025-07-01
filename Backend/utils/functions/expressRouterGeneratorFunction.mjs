import { Router } from 'express';
//// Exportamos la factoria de routers.
export const expressRouterGenerator = (routerOptions = {}) => { return Router(routerOptions); };