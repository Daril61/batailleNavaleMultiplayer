package Utils.Message;

/*
 * Message de type
 */
public class ToucheMessage extends Message {

    /**
     * Numéro de ligne
     */
    public int l;
    /**
     * Numéro de colonne
     */
    public int c;
    /**
     * Entier qui prend la valeur de 0 si ça touche de l'eau ou un bateau déjà touché, sinon l'id du bateau
     */
    public int touche;
    /**
     * Boolean qui nous indique si le bateau touché est coulé
     */
    public boolean couler;

    /**
     * Boolean qui indique si on a gagné ou pas
     */
    public boolean victoire;

    /**
     * Constructeur de la classe
     *
     * @param l Numéro de ligne
     * @param c Numéro de colonne
     * @param touche Prend la valeur de 0 si ça touche de l'eau ou un bateau déjà touché sinon l'id du bateau
     * @param couler Vraie (true) si le bateau touché est coulé
     */
    public ToucheMessage(int l, int c, int touche, boolean couler, boolean victoire) {
        this.l = l;
        this.c = c;
        this.touche = touche;
        this.couler = couler;
        this.victoire = victoire;
    }

    @Override
    public MessageType getType() {
        return MessageType.TOUCHE;
    }
}
