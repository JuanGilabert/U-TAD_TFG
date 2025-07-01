# Descripcion de la arquitectura del servidor
## Arquitectura del servidor
seeders
Datos de prueba o inicialización de base de datos.

bin
Archivos ejecutables, como el arranque de la app (www o start.js).

plugin
¿Para qué sirve?

Para alojar módulos o extensiones que agregan funcionalidad reutilizable a tu aplicación, de forma similar a cómo funcionan los plugins en sistemas como Express, Sequelize, Fastify, etc.

Cuándo usarla:

Si estás escribiendo funcionalidades que podrían integrarse fácilmente en otras apps.


hook
¿Para qué sirve?

Para manejar funciones que se ejecutan antes o después de ciertos eventos. Es muy común si usas ORM como Sequelize o Mongoose, o incluso en lógica de negocio (como auditorías o validaciones).

Cuándo usarla:

Si tienes lógica que debe ejecutarse automáticamente antes/después de algo.

Si estás aplicando el patrón de programación orientada a eventos.


## Estructura de carpetas por orden alfabetico
- **`Audio`** → A
- **`Assets`** → Archivos multimedia o recursos usados por el frontend.
- **`b`** → B
- **`config`** → Esta carpeta almacena la `configuración del servidor`.
    Contiene principalmente las `variables de entorno`, de donde viene la mayoria de la configuracion del servidor.
- **`connection`** → Esta carpeta almacena las conexiones a la base de datos.
- **`controllers`** → Esta carpeta almacena los controladores. Contiene las funciones que se ejecutan cuando se hace una petición HTTP.
    El controlador es el encargado de manejar la lógica de la petición llamando al modelo para obtener los datos de la petición.
- **`database`** → Esta carpeta es un servicio que se encarga de gestionar la base de datos.
    Contiene la configuracion y la conexion de la base de datos y funciones de busqueda sobre la misma.
- **`docker`** → Esta carpeta almacena la configuracion de docker. Contiene los archivos para la creacion de la imagen docker y para el arranque del contenedor.
- **`docs`** → Esta carpeta almacena la documentación del proyecto. Contiene documentacion sobre Swagger, markdown, etc ...
- **`email`** → Plantillas y lógica relacionada al envío de `correos`.
- **`export`** → E
- **`frontend`** → F
- **`generator`** → Esta carpeta almacena los generators.
- **`helpers`** → Esta carpeta almacena los `helpers`.
- **`hooks`** → Esta carpeta almacena los `hooks`.
- **`images`** → I
- **`jwt`** → Esta carpeta es un servicio que se encarga de gestionar el `JSON Web Token (JWT)`. Contiene la configuracion y la verificacion del JWT.
- **`logs`** → Archivos de log generados por el servidor.
- **`middlewares`** → Esta carpeta almacena los middlewares. Un middleware es una función que se ejecuta antes y después de una petición HTTP.
    Contiene los middlewares de autenticacion, validacion de la peticion y validacion de los datos de la peticion por cada router de express.
- **`models`** → Esta carpeta almacena los modelos. Contiene las funciones que se ejecutan en los controladores correspondientes
    cuando se hace una petición HTTP para la obtencion de los datos.
- **`plugins`** → Esta carpeta almacena los plugins de express o de otros frameworks.
- **`routes`** → Esta carpeta almacena las rutas de la aplicación. Contiene la definicion de los endpoints de la aplicación divididos por modulos.
    Cada endpoint es una función que se ejecuta cuando se hace una petición HTTP. Dicha funcion es la definida en el controlador de la peticion correspondiente.
- **`scripts`** → Scripts útiles como seeds, backups, deploys, etc.
- **`services`** → Esta carpeta almacena los distintos tipos de servicios que puede tener un servidor, como *(database)* o *(jwt)* o *(email)*.
- **`temp`** → T
- **`test`** → Para pruebas automatizadas (Jest, Mocha, etc.).
- **`utils`** → Esta carpeta almacena las funciones utilitarias de la aplicación o constantes globales.
- **`validator`** → V