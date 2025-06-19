
export class ModelExceptionHandler extends Error {
    static throwBadRequestError(message) {
        super(message);
    }
}