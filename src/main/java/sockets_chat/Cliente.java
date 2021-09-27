/*
 * Clase encargada de construir la aplicación del CLIENTE.
 * Se implementan métodos para crear un Socket para comunicar mensajes.
 */
package sockets_chat;

// Librerías y paquetes necesarios
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class Cliente {

    // Método principal por donde se ejecutará el programa
    public static void main(String[] args) {
        MarcoCliente marco = new MarcoCliente();
        marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

// Construyendo el marco
class MarcoCliente extends JFrame {

    public MarcoCliente() {
        this.setTitle("Chat básico CLIENTE");
        this.setBounds(600, 300, 280, 350);
        PanelMarcoCliente panel = new PanelMarcoCliente();
        this.add(panel);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}

// Construyendo el JPanel 
class PanelMarcoCliente extends JPanel {

    // Variables
    private final JTextField campo1;
    private final JButton boton;

    // Constructor de clase
    public PanelMarcoCliente() {
        JLabel texto = new JLabel("CLIENTE");
        this.add(texto);
        campo1 = new JTextField(20);
        this.add(campo1);
        boton = new JButton("ENVIAR");

        // Instancia de la clase interna que mediante un Socket envía texto
        EnviarTexto evento = new EnviarTexto();
        // Añadiendo el evento (clase interna) al botón, para que al presionar dicho botón, se inicie el Socket
        boton.addActionListener(evento);
        this.add(boton);
    }

    // Clase interna que envía mensajes de texto
    private class EnviarTexto implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Creación del Socket
                Socket socket = new Socket("192.168.56.1", 9999);

                // Creación del flujo de datos que viajará por el Socket
                DataOutputStream flujo_datos_salida = new DataOutputStream(socket.getOutputStream());
                flujo_datos_salida.writeUTF(campo1.getText()); // Escribiendo el mensaje
                flujo_datos_salida.close(); // Cerrando el flujo de datos
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }

    }
}
