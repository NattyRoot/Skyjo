package com.example.application.skyjo.model;

import com.vaadin.flow.component.button.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SkyjoDeck {
    private List<SkyjoCard> cards;

    public SkyjoDeck() {
        createDeck();
        shuffle();
    }

    public SkyjoCard draw() {
        SkyjoCard topCard = cards.get(0);
        cards.remove(0);
        return topCard;
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    private void createDeck() {
        cards = new ArrayList<>();

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

            String color;
            switch (cardValue) {
                case -2:
                case -1:
                    color = "purple";
                    break;
                case 0:
                    color = "blue";
                    break;
                case 1:
                case 2:
                case 3:
                case 4:
                    color = "green";
                    break;
                case 5:
                case 6:
                case 7:
                case 8:
                    color = "yellow";
                    break;
                default:
                    color = "red";
            }

            for (int cardQuantity = 0; cardQuantity < quantity; cardQuantity++) {
                Button button = new Button("?");
                SkyjoCard card = new SkyjoCard(cardValue, color, button);
                cards.add(card);
            }
        }
    }
}
