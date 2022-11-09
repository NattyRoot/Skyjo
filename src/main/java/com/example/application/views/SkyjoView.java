package com.example.application.views;

import com.example.application.skyjo.model.SkyjoBoard;
import com.example.application.skyjo.model.SkyjoCard;
import com.example.application.views.components.SkyjoPlayerBoardComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Skyjo")
@Route(value = "skyjo")
public class SkyjoView extends HorizontalLayout {
    private SkyjoBoard board;
    private List<SkyjoPlayerBoardComponent> skyjoPlayerBoardComponent;
    private Button deckButton;
    private Button discardPileButton;
    private SkyjoCard selectedCard;

    public SkyjoView() {
        board = new SkyjoBoard(1);
        skyjoPlayerBoardComponent = new ArrayList<>();
        deckButton = new Button("DECK");
        discardPileButton = new Button(board.getDiscardPile().get(board.getDiscardPile().size() - 1).toString());

        deckButton.addClickListener((a) -> {
            selectedCard = board.getDeck().get(0);
            deckButton.setText(selectedCard.toString());
            board.getDeck().remove(0);
            deckButton.setEnabled(false);
        });

        discardPileButton.addClickListener((a) -> {
            if (selectedCard == null) {
                selectedCard = board.getDiscardPile().get(board.getDiscardPile().size() - 1);
            } else {

            }
        });

        board.getPlayerField().forEach((playerNum, playerDeck) -> {
            skyjoPlayerBoardComponent.add(new SkyjoPlayerBoardComponent(playerDeck));
        });

        skyjoPlayerBoardComponent.forEach((playerField) -> {
            playerField.getButtons().forEach((button) -> {
                button.add
            });
        });

        add(skyjoPlayerBoardComponent.toArray(new SkyjoPlayerBoardComponent[0]));

        add(deckButton, discardPileButton);
    }
}
