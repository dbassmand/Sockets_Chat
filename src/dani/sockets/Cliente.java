package dani.sockets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.io.IOException;
import javax.swing.*;
import java.net.*;

// Clase principal que lanza el cliente
public class Cliente {

    public static void main(String[] args) {
        // Crea la interfaz gráfica del cliente
        MarcoCliente mimarco = new MarcoCliente();

        // Configura la ventana para que se cierre al pulsar el botón de cerrar
        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

// Clase que define el marco principal de la aplicación cliente
class MarcoCliente extends JFrame {

    public MarcoCliente() {
        // Define la posición y tamaño de la ventana
        setBounds(600, 300, 280, 350);

        // Crea la lámina (panel) donde estarán los componentes de la interfaz
        LaminaMarcoCliente milamina = new LaminaMarcoCliente();

        // Añade la lámina al marco principal
        add(milamina);

        // Hace visible la ventana
        setVisible(true);
    }
}

// Clase que define la lámina con los componentes del cliente
class LaminaMarcoCliente extends JPanel implements Runnable {

    private JTextField campo1, nick, ip; // Campos de texto para escribir el mensaje, el nick y la IP
    private JTextArea campochat; // Área de texto para mostrar los mensajes del chat
    private JButton miboton; // Botón para enviar mensajes

    public LaminaMarcoCliente() {
        // Campo para escribir el "nick" del usuario
        nick = new JTextField(5);
        add(nick);

        // Etiqueta para identificar el chat
        JLabel texto = new JLabel("-CHAT-");
        add(texto);

        // Campo para introducir la IP del destinatario
        ip = new JTextField(8);
        add(ip);

        // Área de texto donde se mostrarán los mensajes del chat
        campochat = new JTextArea(12, 20);
        add(campochat);

        // Campo de texto para escribir el mensaje a enviar
        campo1 = new JTextField(20);
        add(campo1);

        // Botón para enviar mensajes
        miboton = new JButton("Enviar");

        // Asocia un evento al botón que envía el mensaje
        EnviaTexto mievento = new EnviaTexto();
        miboton.addActionListener(mievento);
        add(miboton);

        // Inicia un hilo para escuchar mensajes entrantes
        Thread mihilo = new Thread(this);
        mihilo.start();
    }

    // Clase interna que gestiona el envío de mensajes cuando se pulsa el botón
    private class EnviaTexto implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // Añade el mensaje enviado al área del chat con el prefijo "Yo"
            campochat.append("\n" + "Yo: " + campo1.getText());

            try {
                // Crea un socket para conectarse al servidor en la dirección IP y puerto indicados
                Socket misocket = new Socket("192.168.1.97", 9999); // Cambia la IP según sea necesario

                // Crea un paquete de envío con el nick, IP del destinatario y mensaje
                PaqueteEnvio datos = new PaqueteEnvio();
                datos.setNick(nick.getText());
                datos.setIp(ip.getText());
                datos.setMensaje(campo1.getText());

                // Envía el paquete de datos al servidor
                ObjectOutputStream paquete_datos = new ObjectOutputStream(misocket.getOutputStream());
                paquete_datos.writeObject(datos);

                // Cierra la conexión del socket
                misocket.close();

            } catch (UnknownHostException e1) {
                // Maneja el error si no se puede encontrar el host
                e1.printStackTrace();
            } catch (IOException e1) {
                // Maneja otros errores relacionados con la entrada/salida
                System.out.println(e1.getMessage());
            }
        }
    }

    @Override
    public void run() {
        // Hilo que escucha mensajes entrantes desde el servidor
        try {
            // Crea un socket servidor en el puerto 9090 para recibir mensajes
            ServerSocket servidor_cliente = new ServerSocket(9090);
            Socket cliente;
            PaqueteEnvio paqueteRecibido;

            while (true) {
                // Acepta conexiones entrantes desde el servidor
                cliente = servidor_cliente.accept();

                // Recibe el paquete enviado por el servidor
                ObjectInputStream flujoentrada = new ObjectInputStream(cliente.getInputStream());
                paqueteRecibido = (PaqueteEnvio) flujoentrada.readObject();

                // Muestra el mensaje recibido en el área del chat
                campochat.append("\n" + paqueteRecibido.getNick() + ": " + paqueteRecibido.getMensaje());
            }

        } catch (Exception e) {
            // Maneja errores durante la ejecución del hilo
            System.out.println(e.getMessage());
        }
    }
}

// Clase que define el paquete de datos a enviar, implementa Serializable para permitir su transmisión
class PaqueteEnvio implements Serializable {

    private String nick; // Nickname del remitente
    private String ip; // Dirección IP del destinatario
    private String mensaje; // Contenido del mensaje

    // Métodos getter y setter para el campo "nick"
    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    // Métodos getter y setter para el campo "ip"
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    // Métodos getter y setter para el campo "mensaje"
    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
