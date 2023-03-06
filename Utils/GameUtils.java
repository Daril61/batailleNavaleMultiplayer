package Utils;

/**
 * Classe qui contient les variables utiles au jeu
 *
 * @since 25/02/2023
 * @author Romain Veydarier
 */
public class GameUtils {

    /**
     * Liste des tailles des bateaux dans l'ordre par rapport à la variable bateauxNom
     */
    public static final int[] bateauxTaille = new int[]{
            5,
            4,
            3,
            3,
            2
    };
    /**
     * Liste des noms des bateaux
     */
    public static final String[] bateauxNom = new String[] {
            "Porte-avions",
            "Croiseur",
            "Contre-torpilleurs",
            "Sous-marin",
            "Torpilleur"
    };
    /**
     * Liste des colonnes, c'est-à-dire les lettres de la grille
     */
    public static final char[] colonne = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};


}
