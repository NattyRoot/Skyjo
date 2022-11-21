package fr.acensi.skyjo.model;

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

    public List<SkyjoCard> getCards() {
        return cards;
    }

    public void setCards(List<SkyjoCard> cards) {
        this.cards = cards;
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
            int quantity = switch (cardValue) {
                case -2 -> 5;
                case 0 -> 15;
                default -> 10;
            };

            String color = switch (cardValue) {
                case -2, -1 -> "purple";
                case 0 -> "blue";
                case 1, 2, 3, 4 -> "green";
                case 5, 6, 7, 8 -> "yellow";
                default -> "red";
            };

            for (int cardQuantity = 0; cardQuantity < quantity; cardQuantity++) {
                Button button = new Button("?");
                button.setClassName("card");
                SkyjoCard card = new SkyjoCard(cardValue, color, button);
                cards.add(card);
            }
        }
    }
}
