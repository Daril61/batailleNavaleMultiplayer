package Utils.Message;

/**
 * Enumération des messages possibles dans le jeu
 *
 * MessageType.READY     => Message envoyé quand le joueur a fini de mettre en place ses bateaux
 * MessageType.MOUVEMENT => Message envoyé quand le joueur tir à une position
 * MessageType.TOUCHE    => Message envoyé quand un joueur a touché un bateau
 * MessageType.VICTOIRE  => Message envoyé quand le joueur adverse gagne
 */
public enum MessageType {
    READY,
    MOUVEMENT,
    TOUCHE,
    VICTOIRE
}
