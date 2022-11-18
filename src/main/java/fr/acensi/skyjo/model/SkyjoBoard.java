package fr.acensi.skyjo.model;

import fr.acensi.skyjo.business.SkyjoLogic;

import java.util.ArrayList;
import java.util.List;

public class SkyjoBoard {
    int playerCount;
    private SkyjoDeck deck;
    private SkyjoDiscardPile discardPile;
    private List<SkyjoPlayerField> playersField;

    public SkyjoDeck getDeck() {
        return deck;
    }

    public SkyjoDiscardPile getDiscardPile() {
        return discardPile;
    }

    public List<SkyjoPlayerField> getPlayersField() {
        return playersField;
    }

    public SkyjoBoard(int playerCount) {
        this.playerCount = playerCount;
        this.deck = new SkyjoDeck();
        this.discardPile = new SkyjoDiscardPile();
        this.playersField = new ArrayList<>();

        // Distribution des cartes à chaque joueurs
        deal(playerCount);
        // Ajout de la première carte de la pioche dans la défausse
        discardPile.discard(deck.draw());
    }

    /**
     * Distribue les cartes de chaques joueurs
     *
     * @param playerCount nombre de joueur dans la partie
     */
    public void deal(int playerCount) {
        // Rempli les deck ligne par ligne en alternant les joueurs
        for (int playerNum = 0; playerNum < playerCount; playerNum++) {
            SkyjoCard[][] cards = new SkyjoCard[4][3];
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 4; col++) {
                    cards[col][row] = deck.draw();
                }
            }
            playersField.add(new SkyjoPlayerField(playerNum, cards));
        }
    }

    /**
     * Remet les cartes de la défausse dans la pioche à l'exception de la première puis mélange le deck
     */
    public void turnDiscardPileIntoDeck() {
        // Garde la dernière carte de la défausse
        SkyjoCard topDiscard = SkyjoLogic.getBoard().getDiscardPile().draw();
        // Met dans le deck toutes les cartes de la défausse
        SkyjoLogic.getBoard().getDeck().setCards(new ArrayList<>(SkyjoLogic.getBoard().getDiscardPile().getCards()));
        // Mélange le deck
        SkyjoLogic.getBoard().getDeck().shuffle();
        // Vide la défausse
        SkyjoLogic.getBoard().getDiscardPile().empty();
        // Remet la dernière carte dans la défausse
        SkyjoLogic.getBoard().getDiscardPile().discard(topDiscard);
    }

    /**
     * DEBUG USE ONLY<br>
     * Affiche le board de chaque joueurs
     */
    public void printPlayersFields() {
        playersField.forEach(field -> {
            System.out.println(field.toString());
        });
    }
}
