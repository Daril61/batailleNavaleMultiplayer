package main;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static Socket client;

    public static void main(String[] args) throws IOException{
        //int Port = Integer.parseInt(JOptionPane.showInputDialog("Input Your Port : "));
        //String IP = JOptionPane.showInputDialog("Input Your IP Server : ");
        ServerSocket serverSocket = new ServerSocket(6066);

        // Tant que le socket du serveur est activé
        //while(!serverSocket.isClosed()){

            // Si un client est déjà connecté
            //if(client != null) continue;

            Socket socket = serverSocket.accept();
            client = socket;
            System.out.println("Connexion d'un client");

            Bataille bataille = new Bataille(socket);
        //}

    }
}