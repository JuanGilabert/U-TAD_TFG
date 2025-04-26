import fs from 'node:fs'
import vm from 'node:vm'
let ficheroTest = fs.readFileSync('travelModel.js');
vm.runInThisContext(ficheroTest);
let assert = require("assert");
/**
 * 
 */
describe("Test unitario para comprobar la funcion que loguea a un usuario llamada  postLoginUser()", function() {
    // caso en que la funcion es correcta
    it('esta funcion debe realizar el login correctamente', function() {
        // Creamos el objeto de tipo User que la api nos debe devolver cuando se hace login.
        // Esto sera probado mediante un usuario que ya existe en la base de datos.
        /* userName=juang userPassword=2793 userEmail=juangilabert2002@gmail.com userJWT=tokenGeneradoPorNodeServer */
        let loginApiResponse = ""
        assert.equals(postLoginUser(), loginUserData)
    });
    // caso en que la funcion  postLoginUser  es incorrecta
    it('esta funcion debe realizar el login incorrectamente', function() {
        // Creamos el objeto que recibira el mensaje de error devuelto por la API. { messagge: "El usuario o la clave son incorrectos. Revise las credenciales de acceso." }
        // Esto sera probado mediante un usuario que no existe en la base de datos.
        /* userName=abcd userPassword=1234 userEmail=a@gmail.com userJWT=tokenInvalido */
        let loginApiResponse = "El usuario o la clave son incorrectos. Revise las credenciales de acceso."
        assert.equals(postLoginUser(), loginApiResponse)
    });
});