Servidor Web
======================



  
  - [__Introducción__](#introducción)
  - [__Especificación__](#especificación)
  - [__Implementación__](#implementación )
  - [__Uso__](#uso)
  
 


#### Introducción 

Un servidor web no es más que un programa que implementa el lado servidor del protocolo
HTTP. En esta práctica vamos a **implementar nuestro propio servidor web usando sockets
Java.**


#### Especificación 

El puerto por defecto de los servidores HTTP (esto es, el 80) no puede ser usando a no ser que
ejecutes tu servidor Java como administrador (root en sistemas Linux o Mac). Para evitar este
problema, vamos a implementar nuestro servidor web en el puerto **8080**.
Nuestro servidor debe aceptar **peticiones concurrentes**. Por esta razón, un hilo (Thread)
tiene que ser creado con cada petición recibida. Nuestro servidor sólo servirá archivos que
estarán almacenados en la raíz de nuestro proyecto Eclipse. Como cliente HTTP vamos **a usar
un navegador real** (Chrome, Firefox, etc).


#### Implementación 

Pon el servidor en bucle infinito escuchando peticiones. Cuando se acepte una nueva
petición entrante, crea un hilo que atienda a la petición.

Lo más cómodo de cara a la implementación es que la raíz de tu servidor sea la misma raíz
de tu proyecto Java. Toma como ejemplo esta captura:

![alt tag](https://github.com/carlos-camara/ServidorWeb/blob/master/Imagenes/fotoProyecto.PNG)

Observa que en la raíz de este proyecto ejemplo hay dos ficheros: **index.html** y
**github-octocat.png**. Ambos ficheros (y otros más si los hubiese) deben ser visibles a través
del protocolo HTTP desde un navegador.
El contenido de **index.html** puede ser el siguiente contenido de pruebas, que además
hace referencia a la **github-octocat.png** (que puedes descargar de la esta [URL](https://www.google.es/search?q=github&rlz=1C1FWBB_enES657ES657&source=lnms&tbm=isch&sa=X&ved=0ahUKEwj6weTYjePQAhXJfxoKHdRDCzIQ_AUICCgB&biw=1093&bih=518#imgrc=KKYZyyfNJdIycM%3A)).

```html
  <html>
    <body>
      <br>Hello World!<br>
      <img src="github-octocat.png">
    </body>
  </html>
```

Una petición al fichero **index.html** desde un navegador generará esta petición HTTP:

![alt tag](https://github.com/carlos-camara/ServidorWeb/blob/master/Imagenes/peticion.PNG)

Para esta petición, el servidor deberá leer el contenido del fichero (**index.html** en este
caso) y escribir el contenido del fichero correspondiente en la respuesta HTTP.

El mensaje de respuesta del servidor incluir al menos las cabeceras **Content-Type** y
**Content-Length**. Para obtener la longitud el fichero puedes usar el método **length**
de la clase **java.io.File**. Para obtener el atributo *Content-Type* puedes usar el
método **probeContentType** de la **clase java.nio.Files**:

```java
Path path = Paths.get(nombreFichero);
tipoFichero = Files.probeContentType(path);
contenido = Files.readAllBytes(path);
tamañoRespuesta = fichero.length();
```


En caso de hacer una petición a que no está disponible, el servidor deberá enviar la
respuesta adecuada, esto es, **HTTP 404 Not Found**.

![alt tag](https://github.com/carlos-camara/ServidorWeb/blob/master/Imagenes/notFound.PNG)

Puedes usar un navegador web para acceder a la URL **http://localhost:8080/index.html**. Si
todo funciona correctamente deberías visualizar lo siguiente:

![alt tag](https://github.com/carlos-camara/ServidorWeb/blob/master/Imagenes/resultadoFinal.PNG)



#### Uso 
Clonar el repositorio ServidorWeb
 >$ git clone https://github.com/carlos-camara/ServidorWeb.git
 

