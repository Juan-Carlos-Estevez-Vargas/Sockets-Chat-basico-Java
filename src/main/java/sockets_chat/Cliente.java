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
import javax.swing.JTextArea;
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
        this.setTitle("Chat básico");
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
    private final JTextField campo1, nick, ip;
    private final JButton boton;
    private final JTextArea area_texto;

    // Constructor de clase
    public PanelMarcoCliente() {
        nick = new JTextField(5);
        ip = new JTextField(8);
        JLabel texto = new JLabel("--- CHAT ---");
        area_texto = new JTextArea(12, 20);
        campo1 = new JTextField(20);
        boton = new JButton("ENVIAR");

        // Instancia de la clase interna que mediante un Socket envía texto
        EnviarTexto evento = new EnviarTexto();
        // Añadiendo el evento (clase interna) al botón, para que al presionar dicho botón, se inicie el Socket
        boton.addActionListener(evento);

        // Añadiendo los componenetes Swing al Panel
        this.add(nick);
        this.add(texto);
        this.add(ip);
        this.add(area_texto);
        this.add(campo1);
        this.add(boton);
    }

    // Clase interna que envía mensajes de texto
    private class EnviarTexto implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Creación del Socket
                Socket socket = new Socket("192.168.56.1", 9999);

                // Instancia de la clase PaqueteEnvio
                PaqueteEnvio datos = new PaqueteEnvio();

                // Seteando atributos de la clase Paquete Envio
                datos.setNick(nick.getText());
                datos.setIp(ip.getText());
                datos.setMensaje(campo1.getText());
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }

    }
}

// Clase encargada de encapsular los atributos a enviar como mensaje (Nick, IP, Mensaje)
class PaqueteEnvio {

    private String nick, ip, mensaje;

    // Getters and Setters
    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

}
