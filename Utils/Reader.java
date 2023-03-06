package Utils;

import Utils.Message.Message;
import Utils.Message.MouvementMessage;
import Utils.Message.ToucheMessage;
import main.Bataille;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;

/**
 * Classe qui permet de récupérer avec l'utilisation d'un autre thread tous les messages reçu de l'autre joueur
 *
 * @since 05/03/2023
 * @author Romain Veydarier
 */
public class Reader extends Thread {

    /**
     * Classe de jeu du joueur
     */
    private Bataille bataille;

    /**
     * Lecteur de flux en lecture pour récupérer les messages
     */
    private ObjectInputStream lecteurFlux;
    private boolean ecouter = true;


    /**
     * Constructeur de la classe
     * @param bataille Classe principale du jeu
     */
    public Reader(Bataille bataille) {
        this.bataille = bataille;
        ecouter = true;

        try {
            lecteurFlux = new ObjectInputStream(bataille.getSocket().getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Fonction pour récupérer les messages
     */
    @Override
    public void run() {
        while (ecouter) {
            if(bataille.getSocket() == null) {
                ecouter = false;
                Thread.interrupted();
            }

            try {
                Message message = (Message)lecteurFlux.readObject();

                switch (message.getType()) {
                    case READY: {
                        // Si le client est prêt alors il joue
                        if(bataille.estPret) {
                            bataille.jouer();
                        }
                    }
                    break;
                    case MOUVEMENT: {
                        MouvementMessage m = (MouvementMessage)message;

                        bataille.mouvement(m.l, m.c);
                    }
                    break;
                    case TOUCHE: {
                        ToucheMessage m = (ToucheMessage)message;

                        bataille.mouvementEnTexte(m.l, m.c, m.touche, m.couler);
                        // Si le client a gagné
                        if(m.victoire) {
                            bataille.victoire();

                            // On arrête d'écouter sur le socket
                            ecouter = false;
                            this.interrupt();

                            System.exit(0);
                        }
                    }
                    break;
                }
            } catch (IOException | ClassNotFoundException e) {
                GameUtils.cls();
                System.out.println("L'autre joueur vient de déconnecter...  Victoire !");

                ecouter = false;
                this.interrupt();

                System.exit(0);
            }
        }
    }
}
