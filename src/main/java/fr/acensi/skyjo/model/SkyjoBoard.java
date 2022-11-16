package fr.acensi.skyjo.model;

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

        deal(playerCount);

        printPlayersFields();

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
     * DEBUG USE ONLY<br>
     * Affiche le board de chaque joueurs
     */
    public void printPlayersFields() {
        playersField.forEach(field -> {
            System.out.println(field.toString());
        });
    }
}
