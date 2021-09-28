/*
 * Clase encargada de construir la aplicación del SERVIDOR.
 * Se implementan métodos para crear un Socket para comunicar mensajes.
 */
package sockets_chat;

// Librerías o packetes
import java.awt.BorderLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class Servidor {

    public static void main(String[] args) {
        MarcoServidor marco = new MarcoServidor();
        marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

// Clase encargada de crear el marco con la interfaz Runnable para los hilos
class MarcoServidor extends JFrame implements Runnable {

    private final JTextArea area_texto;

    // Constructor de la clase
    public MarcoServidor() {
        this.setTitle("Chat básico SERVIDOR");
        this.setBounds(1200, 300, 280, 350);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        area_texto = new JTextArea();
        panel.add(area_texto, BorderLayout.CENTER);
        this.add(panel);
        this.setVisible(true);
        this.setLocationRelativeTo(null);

        // Creando e iniciando un segundo hilo
        Thread hilo = new Thread(this);
        hilo.start();
    }

    // Método para correr el hilo
    @Override
    public void run() {
        try {
            // Creando el Socket que corre en el servidor
            ServerSocket socket_servidor = new ServerSocket(9999);
            String nick, ip, mensaje;

            // Instancia de paquete envío donde se recibirá la información
            PaqueteEnvio paquete_recibido;

            // Ciclo indefinido para recibir mensajes indefinidos
            while (true) {

                // Creando un Socket que acepte las conexiones con el Socket servidor
                try ( Socket socket = socket_servidor.accept()) {

                    // Creando el flujo de datos de entrada
                    ObjectInputStream flujo_entrada_datos = new ObjectInputStream(socket.getInputStream());

                    // Leyendo el flujo de datos de entrada al paquete recibido
                    paquete_recibido = (PaqueteEnvio) flujo_entrada_datos.readObject();

                    // Obteniendo los datos recibidos del paquete
                    nick = paquete_recibido.getNick();
                    ip = paquete_recibido.getIp();
                    mensaje = paquete_recibido.getMensaje();

                    // Añadiendo el mensaje recibido al area de texto
                    area_texto.append("\n" + nick + ": " + mensaje + " para: " + ip);

                    // Socket para reenviar la información recibida a su correspondiente cliente
                    try ( Socket enviar_destinatario = new Socket(ip, 9090)) {
                        ObjectOutputStream paquete_reenvio = new ObjectOutputStream(enviar_destinatario.getOutputStream());
                        paquete_reenvio.writeObject(paquete_recibido);
                        paquete_reenvio.close();
                        enviar_destinatario.close();
                    }

                    // Cerrando Sockets
                    socket.close();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(MarcoServidor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            System.err.println("Ocurrió un error " + ex.getMessage());
        }
    }
}
