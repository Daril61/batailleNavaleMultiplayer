package main;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Classe principale pour que le joueur puisse soit héberger ou rejoindre une partie
 *
 * @since 05/03/2023
 * @author Romain Veydarier
 */
public class Server {

    public static Socket client;

    public static void main(String[] args) throws IOException{
        //int Port = Integer.parseInt(JOptionPane.showInputDialog("Input Your Port : "));
        //String IP = JOptionPane.showInputDialog("Input Your IP Server : ");


        // Héberger une partie
        ServerSocket serveurSocket = new ServerSocket(6066);

        Socket socket = serveurSocket.accept();
        client = socket;
        System.out.println("Connexion d'un client");

        Bataille bataille = new Bataille(socket);

    }
}