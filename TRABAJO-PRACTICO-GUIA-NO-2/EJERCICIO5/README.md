## Guía de inicio y prueba de la aplicación.

Sigue estos pasos para levantar el entorno de desarrollo y verificar el funcionamiento de la API y el cliente web.

### 1. Levantar el Backend (Spring Boot)

1. Abre una terminal en la carpeta raíz del proyecto backend.

2. Ejecuta el siguiente comando para iniciar la aplicación:

   ```
   .\mvnw spring-boot:run
   
   ```

3. Confirma en la consola que el servidor haya iniciado correctamente en el puerto **`8080`**.

### 2. Levantar el Frontend (React)

1. Abre una nueva terminal en la carpeta de tu proyecto frontend.

2. Instala las dependencias (si es la primera vez que lo ejecutas):

   ```
   npm install
   
   ```

3. Inicia el servidor de desarrollo de React:

   ```
   npm start
   
   ```

4. La aplicación se abrirá automáticamente en tu navegador en `http://localhost:3001`.

### 3. Registrar un Nuevo Usuario en Postman

1. Abre **Postman**.

2. Configura una nueva petición **`POST`** a la siguiente URL:

   ```
   http://localhost:8080/register
   
   ```

3. En la pestaña **Body**, selecciona **raw** -> **JSON** y pega el siguiente cuerpo:

   ```
   {
       "firstname": "hola",
       "lastname": "hola",
       "password": "chauchau",
       "username": "holaaa"
   }
   
   ```

4. Haz clic en **Send**. Deberías recibir una respuesta HTTP **`200 OK`**.

### 4. Consultar Productos desde Postman (Basic Auth)

1. Crea una nueva petición **`GET`** en Postman hacia la URL:

   ```
   http://localhost:8080/products
   
   ```

2. Ve a la pestaña **Authorization** y configura los siguientes datos:

   * **Type:** `Basic Auth`

   * **Username:** `holaaa`

   * **Password:** `chauchau`

3. Haz clic en **Send**. Recibirás una respuesta **`200 OK`** con el listado JSON de productos.

### 5. Probar el Flujo en la Aplicación Web (Opcional)

1. Ve a la aplicación en tu navegador (`http://localhost:3001/login`).

2. Ingresa con las credenciales creadas:

   * **Username:** `holaaa`

   * **Password:** `chauchau`

3. Al iniciar sesión exitosamente, navegarás al catálogo de productos (`/products`) donde podrás crear, ver, editar y eliminar ítems.