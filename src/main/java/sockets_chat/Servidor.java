/*
 * Clase encargada de construir la aplicación del SERVIDOR.
 * Se implementan métodos para crear un Socket para comunicar mensajes.
 */
package sockets_chat;

// Librerías o packetes
import java.awt.BorderLayout;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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

            // Ciclo indefinido para recibir mensajes indefinidos
            while (true) {
                // Creando un Socket que acepte las conexiones con el Socket servidor
                try ( Socket socket = socket_servidor.accept()) {
                    // Creando el flujo de datos de entrada
                    DataInputStream flujo_entrada_datos = new DataInputStream(socket.getInputStream());

                    // Leyendo el flujo de datos y almacenandolo en una variable String
                    String mensaje_texto = flujo_entrada_datos.readUTF();
                    // Añadiendo el mensaje recibido al area de texto
                    area_texto.append("\n" + mensaje_texto);
                }
            }
        } catch (IOException ex) {
            System.err.println("Ocurrió un error " + ex.getMessage());
        }
    }
}
