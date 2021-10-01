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
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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
            
            // Array de direcciones ip donde se almacenarán las IP cada que alguien se conecte al chat
            ArrayList<String> direcciones_ip = new ArrayList<>();

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

                    // Si el mensaje es distinto a online (Si el usuario ya inició sesión)
                    if (!mensaje.equals("online")) {
                        // Añadiendo el mensaje recibido al area de texto
                        area_texto.append("\n" + nick + ": " + mensaje);

                        // Socket para reenviar la información recibida a su correspondiente cliente
                        try ( Socket enviar_destinatario = new Socket(ip, 9090)) {
                            try (ObjectOutputStream paquete_reenvio = new ObjectOutputStream(enviar_destinatario.getOutputStream())) {
                                paquete_reenvio.writeObject(paquete_recibido);
                            }
                            enviar_destinatario.close();
                        }

                        // Cerrando Sockets
                        socket.close();
                        
                    } else { // Si se inicia sesión por primera vez (mensaje = online)

                        // ---------------  DETECTANDO USUARIOS ONLINE  ------------------------
                        InetAddress localizacion = socket.getInetAddress();
                        String ip_remota = localizacion.getHostAddress();
                        direcciones_ip.add(ip_remota); // Añadiendo IP al array
                        paquete_recibido.setDirecciones_ip(direcciones_ip); // Añadiendo array al paquete
                        
                        // Bucle para que se pueda enviar mensaje a cada ip almacenada en el arraylist
                        for (String z : direcciones_ip){
                            try (Socket enviar_destinatario = new Socket(z, 9090)) {
                                ObjectOutputStream paquete_reenvio = new ObjectOutputStream(enviar_destinatario.getOutputStream());
                                paquete_reenvio.writeObject(paquete_recibido);
                                paquete_reenvio.close();
                            }
                            socket.close();
                        }
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(MarcoServidor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            System.err.println("Ocurrió un error " + ex.getMessage());
        }
    }
}
