package Utils.Message;

import java.io.Serializable;

/**
 * Classe qui contient la liste des messages possible que l'on peut envoyer/reçevoir
 *
 * Format d'un message "MOUVEMENT nbLigne,nbColonne"
 *
 */
public abstract class Message implements Serializable {
    public abstract MessageType getType();

}
