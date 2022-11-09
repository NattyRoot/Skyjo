package com.example.application.skyjo.logic;

import com.example.application.skyjo.model.SkyjoCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SkyjoLogic {
    public static List<SkyjoCard> createDeck() {
        List<SkyjoCard> deck = new ArrayList<>();

        for(int cardValue = -2; cardValue <= 12; cardValue++) {
            int quantity;

            switch(cardValue) {
                case -2:
                    quantity = 5;
                    break;
                case 0:
                    quantity = 15;
                    break;
                default:
                    quantity = 10;
            }

            for (int cardQuantity = 0; cardQuantity < quantity; cardQuantity++) {
                SkyjoCard card = new SkyjoCard(cardValue, false);
                deck.add(card);
            }
        }

        return deck;
    }

    public static List<SkyjoCard> shuffleDeck(List<SkyjoCard> deck) {
        Collections.shuffle(deck);
        return deck;
    }
}
