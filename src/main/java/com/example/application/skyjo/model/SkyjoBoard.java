package com.example.application.skyjo.model;

import com.example.application.skyjo.logic.SkyjoLogic;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SkyjoBoard {

    int playerCount;
    private List<SkyjoCard> deck;
    private List<SkyjoCard> discardPile;
    private Map<Integer, SkyjoCard[][]> playerField;

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public List<SkyjoCard> getDeck() {
        return deck;
    }

    public void setDeck(List<SkyjoCard> deck) {
        this.deck = deck;
    }

    public List<SkyjoCard> getDiscardPile() {
        return discardPile;
    }

    public void setDiscardPile(List<SkyjoCard> discardPile) {
        this.discardPile = discardPile;
    }

    public Map<Integer, SkyjoCard[][]> getPlayerField() {
        return playerField;
    }

    public void setPlayerField(Map<Integer, SkyjoCard[][]> playerField) {
        this.playerField = playerField;
    }

    public SkyjoBoard(int playerCount) {
        this.playerCount = playerCount;
        List<SkyjoCard> createdDeck = SkyjoLogic.createDeck();
        discardPile = new ArrayList<>();

        System.out.println("Created a deck of " + createdDeck.size() + " cards.");

        deck = SkyjoLogic.shuffleDeck(createdDeck);
        System.out.println("Shuffled the deck");

        deal(playerCount);

        firstDiscard();
    }

    public void deal(int playerCount) {
        Map<Integer, SkyjoCard[][]> subdecks = new LinkedHashMap<>(playerCount);
        // Init un deck par joueur
        for (int playerNum = 0; playerNum < playerCount; playerNum++) {
            subdecks.put(playerNum, new SkyjoCard[4][3]);
        }

        // Rempli les deck ligne par ligne en alternant les joueurs
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 4; col++) {
                for (int playerNum = 0; playerNum < playerCount; playerNum++) {
                    subdecks.get(playerNum)[col][row] = deck.get(0);
                    deck.remove(0);
                }
            }
        }

        playerField = subdecks;

        playerField.forEach((playerNum, playerDeck) -> {
            System.out.println("Player " + playerNum);
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 4; col++) {
                    System.out.print(" " + playerDeck[col][row] + " ");
                }
                System.out.println();
            }
            System.out.println("----------------------------------------------------------");
        });
    }

    private void firstDiscard() {
        discardPile.add(deck.get(0));
        deck.remove(0);
    }

    public void fromDeckToDiscardPile() {
        discardPile.add(deck.get(0));
        deck.remove(0);
    }

    public void fromPlayerFieldToDiscard(int playerNum, int row, int col, SkyjoCard newCard) {
        discardPile.add(playerField.get(playerNum)[col][row]);
        playerField.get(playerNum)[col][row] = newCard;
    }
}
