package main;

import Utils.GameUtils;
import Utils.Message.Message;
import Utils.Message.MouvementMessage;
import Utils.Message.ReadyMessage;
import Utils.Message.ToucheMessage;
import Utils.Reader;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * Classe principale du jeu de bataille navale
 *
 * @since 08/02/2023
 * @author Romain Veydarier
 */
public class Bataille {

    /**
     * Grille qui contient la carte du joueur
     */
    public static int[][] grilleJeu = new int[10][10];

    /**
     * Variable pour pouvoir générer des nombres aléatoires
     */
    public static Random rand = new Random();

    /**
     * Variable qui indique si le joueur est prêt
     */
    public boolean estPret;

    /**
     * Variable qui permet de communiquer avec l'autre joueur
     */
    private final Socket socket;
    public Socket getSocket() { return socket; }

    /**
     * Variable qui permet d'envoyer un des messages à l'aide du socket
     */
    private final ObjectOutputStream ecritureFlux;


    /**
     * Constructeur de la classe Bataille
     *
     * @since 04/03/2023
     *
     * @param socket Socket qui permet de faire le lien entre les 2 clients
     */
    public Bataille(Socket socket) throws IOException {
        this.socket = socket;

        // Initialisation des modules de lecture et d'écriture
        ecritureFlux = new ObjectOutputStream(this.socket.getOutputStream());

        // Lancement du système d'écoute
        Reader lecteur = new Reader(this);
        lecteur.start();

        // Initialisation de la grille du joueur
        initGrilleJeu();
        estPret = true;
        SendMessage(new ReadyMessage());

        AfficherGrille(grilleJeu);
    }

    /**
     * Fonction qui permet d'envoyer un message à un autre client
     *
     * @param message Message que l'on envoie
     */
    public void SendMessage(Message message) {
        try {
            ecritureFlux.writeObject(message);
        } catch (IOException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    /**
     * Fonction qui permet de jouer un tour
     */
    public void jouer() {
        GameUtils.cls();
        AfficherGrille(grilleJeu);

        // On demande une position au joueur
        int[] position = demandePosition();

        SendMessage(new MouvementMessage(position));
    }

    /**
     * Fonction qui indique au joueur qu'il a gagné
     */
    public void victoire() {
        System.out.println();
        System.out.println("Victoire !");
        System.out.println();

        // Désactivation du socket
        try {
            this.socket.close();
        } catch (IOException e) {
            System.out.println("Une erreur est survenue lors de la fermeture de la connexion !");
        }
    }

    /**
     * Fonction qui indique au joueur qu'il a perdu
     */
    public void defaite() {
        System.out.println();
        System.out.println("Défaite !");
        System.out.println();

        // Désactivation du socket
        try {
            this.socket.close();
        } catch (IOException e) {
            System.out.println("Une erreur est survenue lors de la fermeture de la connexion !");
        }
    }

    /**
     * Fonction qui demande à l'utilisateur, un numéro de ligne et un numéro de colonne pour faire une position
     *
     * @since 06/02/2023
     *
     * @return Un tableau d'entier composé de 2 valeurs (0 => numéro de ligne | 1 => numéro de colonne)
     */
    public int[] demandePosition() {
        Scanner scanner = new Scanner(System.in);

        int l = -1;
        while(l < 1 || l > 10) {
            System.out.print("Entrer le numéro de ligne (1, 2, ..., 10): ");
            if(scanner.hasNextInt())
                l = scanner.nextInt();

            scanner.nextLine();
        }
        // On retire 1 à la variable 'l' car on utilise de 0 à 9 et non de 1 à 10
        l--;

        int c = -1;
        while(c < 0) {
            System.out.print("Entrer le numéro de colonne (A, B, ..., J): ");

            String strColonne = scanner.nextLine();
            if(strColonne.length() > 0) {
                char charColonne = strColonne.charAt(0);
                c = Arrays.binarySearch(GameUtils.colonne, charColonne);
            }
        }

        int[] position = new int[]{l, c};
        return position;
    }

    /**
     * Fonction pour vérifier que l'on puisse placer un bateau par rapport à plusieurs paramètres
     *
     * @since 06/02/2023
     *
     * @param l Un numéro de ligne (compris entre 0 et 9)
     * @param c Un numéro de colonne (compris entre 0 et 9)
     * @param d Une direction (1 => Horizontal | 2 => Vertical)
     * @param t Nombre de cases que prend le bateau
     *
     * @return Retourne vraie (true) si on peut mettre le bateau sur les cases correspondantes
     */
    public boolean posOk(int l, int c, int d, int t) {

        // Cas horizontal
        if (d == 1) {

            // Vérification que le bateau puisse rentrer
            if (c - t < -1) {
                return false;
            }

            // Vérification qu'il n'y ait aucun bateau sur les cases analysées
            for (int i = c; i > (c - t); i--) {
                if(grilleJeu[l][i] != 0) {
                    return false;
                }
            }

            // Cas vertical
        } else {
            // Vérification que le bateau puisse rentrer
            if(l + t > 10) {
                return false;
            }

            // Vérification qu'il n'y ait aucun bateau sur les cases analysées
            for (int i = l; i < (l + t); i++) {
                if(grilleJeu[i][c] != 0) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Fonction pour ajouter un bateau à une grille
     *
     * @since 06/02/2023
     *
     * @param l Un numéro de ligne (compris entre 0 et 9)
     * @param c Un numéro de colonne (compris entre 0 et 9)
     * @param d Une direction (1 => Horizontal | 2 => Vertical)
     * @param t Nombre de cases que prend le bateau
     * @param idBateauGrille Identifiant du bateau à afficher sur la grille
     */
    public void ajouterBateau(int l, int c, int d, int t, int idBateauGrille) {
        // Ajout du bateau sur le plateau de l'ordinateur
        if(d == 1) {
            for (int i = c; i > (c - t); i--) {
                grilleJeu[l][i] = idBateauGrille;
            }
        } else {
            for (int i = l; i < (l + t); i++) {
                grilleJeu[i][c] = idBateauGrille;
            }
        }
    }

    /**
     * Fonction pour initialiser la grille du joueur par rapport aux informations que le joueur nous fournit
     *
     * @since 06/02/2023
     */
    public void initGrilleJeu() {
        AfficherGrille(grilleJeu);

        int idBateau = 0;
        int t;

        while (idBateau < GameUtils.bateauxTaille.length) {
            t = GameUtils.bateauxTaille[idBateau];
            Scanner scanner = new Scanner(System.in);

            System.out.println("Placement d'un " + GameUtils.bateauxNom[idBateau]);
            int[] position = demandePosition();
            int l = position[0];
            int c = position[1];

            int d = -1;
            while(!(d >= 1 && d <= 2)) {
                System.out.print("Entrer la direction (1 : horizontal | 2 : vertical) : ");
                if(scanner.hasNextInt())
                    d = scanner.nextInt();

                scanner.nextLine();
            }

            // Si on peut placer le bateau
            if(posOk(l, c, d, t)) {
                ajouterBateau(l, c, d, t, (idBateau + 1));
                AfficherGrille(grilleJeu);
                idBateau++;

            } else {
                System.out.println("Erreur: Le " + GameUtils.bateauxNom[idBateau] + " ne rentre pas dans la grille.");
            }

            GameUtils.cls();
            AfficherGrille(grilleJeu);
        }
    }

    /**
     * Fonction qui nous permet de savoir si un bateau est coulé
     *
     * @since 07/02/2023
     *
     * @param grille Une grille de 10 x 10
     * @param idBateau Identifiant d'un bateau
     *
     * @return Retourne vraie (true) si le bateau est coulé (qu'il n'est plus présent dans la grille)
     */
    public boolean couler(int[][] grille, int idBateau) {
        if(idBateau < 1 || idBateau > 5) {
            System.out.println("Attention, la variable idBateau n'est pas comprise entre 1 et 5");
            return true;
        }

        for (int y = 0; y < grille.length; y++) {
            for (int x = 0; x < grille[y].length; x++) {
                if(grille[y][x] == idBateau) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Fonction qui permet de tirer
     *
     * @since 07/02/2023
     *
     * @param l Un numéro de ligne
     * @param c Un numéro de colonne
     */
    public void mouvement(int l, int c) {
        // Vérification que la position touche de l'eau ou un bateau déjà touché
        if(grilleJeu[l][c] <= 0 || grilleJeu[l][c] >= 6) {
            System.out.println("[" + GameUtils.colonne[c] + " " + (l + 1) + "] À l’eau ");

            SendMessage(new ToucheMessage(l, c, grilleJeu[l][c], false, false));

            // Le joueur qui vient de recevoir le missile joue
            jouer();
            return;
        }

        // Récupération de l'id du bateau
        int idBateau = grilleJeu[l][c];
        grilleJeu[l][c] = 6;
        // Si le bateau est coulé alors on affiche Coulé sinon Touché
        if(couler(grilleJeu, idBateau)) {
            System.out.println("[" + GameUtils.colonne[c] + " " + (l + 1) + "] Coulé, il s'agissait d'un " + GameUtils.bateauxNom[idBateau-1]);
            SendMessage(new ToucheMessage(l, c, idBateau, true, vainqueur()));

            // Si l'autre joueur a tiré sur la totalité des bateaux du joueur
            if(vainqueur()) {
                defaite();
                return;
            }

            // Le joueur qui vient de recevoir le missile joue
            jouer();
            return;
        } else {
            System.out.println("[" + GameUtils.colonne[c] + " " + (l + 1) + "] Touché, il s'agit d'un " + GameUtils.bateauxNom[idBateau-1]);
            SendMessage(new ToucheMessage(l, c, idBateau, false, false));

            // Le joueur qui vient de recevoir le missile joue
            jouer();
        }
    }

    /**
     * Fonction qui indique au client le résultat de son tir
     * @param l Numéro de ligne
     * @param c Numéro de colonne
     * @param touche Entier qui prend la valeur de 0 si ça touche de l'eau ou un bateau déjà touché, sinon l'id du bateau
     * @param couler Boolean qui nous indique si le bateau touché est coulé
     */
    public void mouvementEnTexte(int l, int c, int touche, boolean couler) {
        if(touche <= 0 || touche >= 6) {
            System.out.println("[" + GameUtils.colonne[c] + " " + (l + 1) + "] À l’eau ");
            return;
        }

        if(couler) {
            System.out.println("[" + GameUtils.colonne[c] + " " + (l + 1) + "] Coulé, il s'agissait d'un " + GameUtils.bateauxNom[touche-1]);
        } else {
            System.out.println("[" + GameUtils.colonne[c] + " " + (l + 1) + "] Touché, il s'agit d'un " + GameUtils.bateauxNom[touche-1]);
        }
    }

    /**
     * Fonction qui nous permet de savoir s'il reste des bateaux sur la grille
     *
     * @since 05/03/2023

     * @return Retourne vraie (true) si tous les bateaux de la grille ont été coulés
     */
    public boolean vainqueur() {
        for (int y = 0; y < grilleJeu.length; y++) {
            for (int x = 0; x < grilleJeu[y].length; x++) {
                if(grilleJeu[y][x] > 0 && grilleJeu[y][x] < 6) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Fonction qui permet d'afficher une grille de jeu mis en paramètre
     *
     * @since 06/02/2023
     *
     * @param grille Une grille de 10 x 10
     */
    public void AfficherGrille(int[][] grille) {
        System.out.print("  ");
        // Affichage des lettres
        for (int i = 0; i < GameUtils.colonne.length; i++) {
            System.out.print(" " + GameUtils.colonne[i]);
        }

        System.out.println();
        for (int i = 0; i < grille.length; i++) {
            String.format("|% d|", 101);
            System.out.print(String.format("%2d ", (i+1)));
            for (int j = 0; j < grille[i].length; j++) {
                System.out.print(grille[i][j] + " ");
            }

            System.out.println();
        }

        System.out.println();
    }
}