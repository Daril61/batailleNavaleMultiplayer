package main;

import Utils.GameUtils;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Classe principale pour que le joueur puisse soit héberger ou rejoindre une partie
 *
 * @since 05/03/2023
 * @author Romain Veydarier
 */
public class Joueur {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        int l = -1;
        while(l < 1 || l > 2) {
            System.out.print("Voulez-vous héberger (1) ou rejoindre une partie (2) : ");
            if(scanner.hasNextInt())
                l = scanner.nextInt();

            scanner.nextLine();
        }

        System.out.println(l);

        Socket socket = null;

        switch(l) {
            // Héberger une partie
            case 1:
            {
                GameUtils.cls();
                ServerSocket serveurSocket = new ServerSocket(6066);

                System.out.println("En attente d'un autre joueur...");

                socket = serveurSocket.accept();
                GameUtils.cls();
                System.out.println("Connexion d'un client...");
                System.out.println();
            }
            break;
            // Rejoindre une partie
            case 2:
            {
                GameUtils.cls();
                System.out.print("Veuillez mettre l'ip de l'hébergeur : ");

                String ip = scanner.nextLine();
                socket = new Socket("192.168.43.183", 6066);

                GameUtils.cls();
                System.out.println("Connexion au serveur...");
                System.out.println();
            }
            break;
        }

        Bataille bataille = new Bataille(socket);
    }
}
