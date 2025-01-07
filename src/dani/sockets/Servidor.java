package dani.sockets;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

// Clase principal del servidor
public class Servidor {

    public static void main(String[] args) {
        // Crea la interfaz gráfica del servidor
        MarcoServidor mimarco = new MarcoServidor();

        // Configura la ventana para que se cierre al pulsar el botón de cerrar
        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

// Clase que define el marco principal de la aplicación servidor
class MarcoServidor extends JFrame implements Runnable {

    private JTextArea areatexto; // Área de texto donde se muestran los mensajes recibidos

    public MarcoServidor() {
        // Define la posición y tamaño de la ventana
        setBounds(1200, 300, 280, 350);

        // Crea el panel que contendrá los componentes
        JPanel milamina = new JPanel();

        // Configura el diseño del panel
        milamina.setLayout(new BorderLayout());

        // Crea el área de texto para mostrar los mensajes
        areatexto = new JTextArea();

        // Añade el área de texto al panel en la zona central
        milamina.add(areatexto, BorderLayout.CENTER);

        // Añade el panel al marco
        add(milamina);

        // Hace visible la ventana
        setVisible(true);

        // Inicia un hilo para escuchar las conexiones entrantes
        Thread mihilo = new Thread(this);
        mihilo.start();
    }

    @Override
    public void run() {
        // Método que se ejecuta en el hilo para escuchar las conexiones entrantes
        try {
            // Crea un servidor en el puerto 9999
            ServerSocket servidor = new ServerSocket(9999);

            // Configura un tiempo de espera de 60 segundos para conexiones
            servidor.setSoTimeout(60000);

            // Variables para almacenar los datos recibidos
            String nick, ip, mensaje;
            PaqueteEnvio paquete_recibido;

            while (true) {
                // Espera a que un cliente se conecte
                Socket misocket = servidor.accept();

                // Recibe el paquete de datos enviado por el cliente
                ObjectInputStream paquete_datos = new ObjectInputStream(misocket.getInputStream());

                // Deserializa el paquete recibido
                paquete_recibido = (PaqueteEnvio) paquete_datos.readObject();

                // Extrae los datos del paquete (nick, ip, mensaje)
                nick = paquete_recibido.getNick();
                ip = paquete_recibido.getIp();
                mensaje = paquete_recibido.getMensaje();

                // Muestra el mensaje recibido en el área de texto
                areatexto.append("\n" + nick + ": " + mensaje + " para " + ip);

                // Crea un nuevo socket para reenviar el mensaje al destinatario en la IP indicada
                Socket enviaDestinatario = new Socket(ip, 9090);

                // Crea un flujo de salida para enviar el paquete de datos
                ObjectOutputStream paqueteReenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());

                // Envía el paquete de datos al destinatario
                paqueteReenvio.writeObject(paquete_recibido);

                // Cierra el flujo de salida
                paqueteReenvio.close();

                // Cierra el socket de comunicación con el destinatario
                enviaDestinatario.close();

                // Cierra el socket de comunicación con el cliente original
                misocket.close();
            }

        } catch (IOException | ClassNotFoundException e) {
            // Maneja las excepciones si ocurren errores durante la comunicación
            e.printStackTrace();
        }
    }
}

