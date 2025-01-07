Proyecto de Chat Cliente-Servidor en Java
Este proyecto implementa una aplicación de chat simple utilizando sockets en Java. El proyecto consta de dos partes principales: el servidor y el cliente. Ambos componentes se comunican a través de conexiones de red, permitiendo a los usuarios enviar y recibir mensajes en tiempo real.

Estructura del Proyecto
1. Cliente
La clase Cliente permite a los usuarios enviar y recibir mensajes en tiempo real. A continuación se describen las funcionalidades clave del cliente:

Interfaz Gráfica (GUI): La aplicación utiliza la librería javax.swing para crear una interfaz sencilla donde el usuario puede ingresar su apodo (nick), la IP del destinatario y el mensaje a enviar.
Envío de Mensajes: El cliente establece una conexión con el servidor mediante un socket y envía el mensaje a través de un paquete serializado (PaqueteEnvio), que incluye el nick del usuario, la IP del destinatario y el mensaje.
Recepción de Mensajes: Un hilo en segundo plano escucha mensajes entrantes desde el servidor. Los mensajes recibidos se muestran en el área de texto del cliente.
2. Servidor
La clase Servidor recibe los mensajes enviados por los clientes y los reenvía al destinatario adecuado. A continuación se describen las funcionalidades clave del servidor:

Interfaz Gráfica (GUI): Similar al cliente, el servidor también utiliza javax.swing para mostrar los mensajes recibidos en un área de texto.
Recepción de Mensajes: El servidor escucha en el puerto 9999 para aceptar conexiones entrantes. Al recibir un mensaje de un cliente, el servidor extrae la información (nick, IP del destinatario, mensaje) y lo reenvía al destinatario utilizando su dirección IP.
Reenvío de Mensajes: Después de recibir el mensaje, el servidor reenvía el paquete de datos al cliente de destino a través de un socket en el puerto 9090.
Requisitos
JDK 8 o superior
Conexión a una red para permitir la comunicación entre el cliente y el servidor
Ejecución
Inicia el servidor primero ejecutando la clase Servidor. Este esperará a que los clientes se conecten.
Luego, ejecuta la aplicación cliente ejecutando la clase Cliente. El cliente se conectará al servidor y podrás comenzar a enviar mensajes.
Notas
Asegúrate de configurar correctamente las direcciones IP en el cliente y servidor si no estás trabajando en la misma red local.
El servidor puede manejar múltiples clientes en secuencia, pero el diseño actual no implementa un sistema de gestión de conexiones concurrentes.
Tecnologías Utilizadas
Java 8
Sockets TCP/IP
javax.swing para la interfaz gráfica
Serializable para la transmisión de objetos
