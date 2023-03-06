package Utils;

import Utils.Message.Message;
import Utils.Message.MouvementMessage;
import Utils.Message.ToucheMessage;
import main.Bataille;

import java.io.IOException;
import java.io.ObjectInputStream;

public class Reader extends Thread {

    private Bataille bataille;
    private ObjectInputStream readStream;

    /**
     * Constructeur de la classe
     * @param bataille Classe principale du jeu
     */
    public Reader(Bataille bataille) {
        this.bataille = bataille;

        try {
            readStream = new ObjectInputStream(bataille.getSocket().getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (true) {
            if(bataille.getSocket() == null)
                Thread.interrupted();

            try {
                Message message = (Message)readStream.readObject();

                System.out.println("Reader: ");

                switch (message.getType()) {
                    case READY: {
                        // Si le client est prêt alors il joue
                        if(bataille.isReady) {
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
                            this.interrupt();
                        }
                    }
                    break;
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
