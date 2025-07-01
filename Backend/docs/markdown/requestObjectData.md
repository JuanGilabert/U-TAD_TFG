# Propiedades y Métodos del Objeto `req` en Express
## Propiedades añadidas por Express
- **`req.app`** → Referencia a la instancia de Express.
- **`req.hostname`** → El hostname de la petición.
- **`req.method`** → Método HTTP que realiza la petición (`GET`, `POST`, `PUT`, etc).
- **`req.header`** → Obtiene el valor de un encabezado como `Authorization`.
- **`req.params`** → Parámetros de ruta.
- **`req.query`** → Parámetros de consulta.
- **`req.body`** → Contenido del cuerpo de la petición *(requiere middleware como `express.json()` o `express.urlencoded()`)*.
- **`req.originalUrl`** → El URL original antes de aplicar routers. Ejemplo `/api/art/music/event/`
- **`req.baseUrl`** → El URL base del router donde se definió el middleware. Ejemplo: `/api/art`.
- **`req.path`** → Ruta de la petición situandose en el router y suando por ejemplo .get(). Ejemplo: `/music/event/`.
- **`req.cookies`** → Cookies parseadas *(requiere el middleware `cookie-parser`)*.
- **`req.fresh`** → Devuelve `true` si la respuesta aún está en caché.
- **`req.ip`** → Dirección IP del cliente.
- **`req.ips`** → Lista de IPs si se usa `trust proxy`.
- **`req.protocol`** → `http` o `https`.
- **`req.route`** → Ruta coincidente.
- **`req.secure`** → `true` si se usa HTTPS.
- **`req.signedCookies`** → Cookies firmadas *(requiere `cookie-parser` con clave)*.
- **`req.stale`** → Lo opuesto de `req.fresh`.
- **`req.subdomains`** → Subdominios en el `hostname`.
- **`req.xhr`** → `true` si la petición es AJAX *(encabezado `X-Requested-With: XMLHttpRequest`)*.
---
## Métodos útiles
- **`req.accepts([types])`** → Verifica tipos de contenido aceptados.
- **`req.get(header)`** → Obtiene el valor de un encabezado.
- **`req.is(type)`** → Verifica el tipo `Content-Type` de la petición.