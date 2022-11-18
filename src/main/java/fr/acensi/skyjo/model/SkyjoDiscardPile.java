package fr.acensi.skyjo.model;

import java.util.*;

public class SkyjoDiscardPile {
    private Queue<SkyjoCard> cards;

    public SkyjoDiscardPile() {
        // Création de L'array du cul
        cards = Collections.asLifoQueue(new ArrayDeque<>());
    }

    public Queue<SkyjoCard> getCards() {
        return cards;
    }

    /**
     * Récupère et supprime la première carte de la défausse
     *
     * @return la première carte de la défausse
     */
    public SkyjoCard draw() {
        return cards.remove();
    }

    /**
     * Rajoute une carte sur le dessus de la défausse
     *
     * @param card La carte a rajouter dans la défausse
     */
    public void discard(SkyjoCard card) {
        cards.add(card);
    }

    /**
     * Récupère la carte du dessus de la défausse et la rend visible si non-null
     *
     * @return
     */
    public SkyjoCard getTopCard() {
        SkyjoCard topCard = cards.peek();
        if (topCard != null) {
            topCard.setVisible(true);
        }
        return topCard;
    }

    public void empty() {
        cards = Collections.asLifoQueue(new ArrayDeque<>());
    }
}
