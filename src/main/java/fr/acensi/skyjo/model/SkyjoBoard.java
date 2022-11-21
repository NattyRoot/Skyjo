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

    public void setPlayersField(List<SkyjoPlayerField> fields) {
        this.playersField = fields;
    }

    public SkyjoBoard(int playerCount) {
        this.playerCount = playerCount;
        this.deck = new SkyjoDeck();
        this.discardPile = new SkyjoDiscardPile();
        this.playersField = new ArrayList<>();
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
