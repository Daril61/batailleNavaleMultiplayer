package Utils.Message;

/**
 * Message qu'un joueur quand il attaque l'autre joueur
 *
 * @since 05/03/2023
 * @author Romain Veydarier
 */
public class MouvementMessage extends Message {

    /**
     * Numéro de ligne
     */
    public int l;
    /**
     * Numéro de colonne
     */
    public int c;

    /**
     * Constructeur de la classe
     *
     * @param l Numéro de ligne
     * @param c Numéro de colonne
     */
    public MouvementMessage(int l, int c) {
        this.l = l;
        this.c = c;
    }

    /**
     * Constructeur de la classe
     *
     * @param position Un tableau d'entier composé de 2 valeurs (0 => numéro de ligne | 1 => numéro de colonne)
     */
    public MouvementMessage(int[] position) {
        this.l = position[0];
        this.c = position[1];
    }

    @Override
    public MessageType getType() {
        return MessageType.MOUVEMENT;
    }
}
