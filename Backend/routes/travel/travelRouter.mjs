// Importamos el generador de routers.
import { expressRouterGenerator } from '../../utils/functions/expressRouterGeneratorFunction.mjs';
// Importamos los controladores necesarios para este router.
import { TravelController } from '../../controllers/travel/travelController.mjs';
// Importamos los valores de las rutas/endpoints necesarios para este router.
import { TRAVEL_TRIP_ROUTE_PATH, UNAVAILABLE_DATES_ROUTE_PATH, IDENTIFIER_ROUTE_PATH } from '../../config/GenericEnvConfig.mjs';
////
export const travelRouter = ({ TravelModel }) => {
    const travelRouter = expressRouterGenerator();
    const travelController = new TravelController({ model: TravelModel });
    /* Travel */
    // GET /api/travel/trip
    travelRouter.get(`${TRAVEL_TRIP_ROUTE_PATH}`, travelController.getAllTravels);
    // GET-UNAVAILABLE-DATES --> /api/travel/trip/unavailable-dates
    travelRouter.get(
        `${TRAVEL_TRIP_ROUTE_PATH}${UNAVAILABLE_DATES_ROUTE_PATH}`,
        travelController.getTravelDates
    );
    // GET-ID api/travel/trip/:id
    travelRouter.get(
        `${TRAVEL_TRIP_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`,
        travelController.getTravelById
    );
    // POST /api/travel/trip
    travelRouter.post(`${TRAVEL_TRIP_ROUTE_PATH}`, travelController.postTravel);
    // PUT
    travelRouter.put(
        `${TRAVEL_TRIP_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`,
        travelController.putTravel
    );
    // PATCH
    travelRouter.patch(
        `${TRAVEL_TRIP_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`,
        travelController.patchTravel
    );
    // DELETE
    travelRouter.delete(
        `${TRAVEL_TRIP_ROUTE_PATH}${IDENTIFIER_ROUTE_PATH}`,
        travelController.deleteTravel
    );
    // Devolvemos la configuración del router.
    return travelRouter;
}