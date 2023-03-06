package main;

import java.io.IOException;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws IOException {
        //int Port = Integer.parseInt(JOptionPane.showInputDialog("Input Your Port : "));
        //String IP = JOptionPane.showInputDialog("Input Your IP Server : ");
        Socket socket = new Socket("localhost", 6066);

        Bataille bataille = new Bataille(socket);
    }
}
