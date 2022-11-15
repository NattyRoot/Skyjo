package com.example.application.views;

import com.example.application.skyjo.model.SkyjoBoard;
import com.example.application.skyjo.model.SkyjoCard;
import com.example.application.skyjo.model.SkyjoPlayerField;
import com.example.application.views.components.SkyjoPlayerFieldComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Skyjo")
@Route(value = "skyjo")
public class SkyjoView extends HorizontalLayout {
    private final SkyjoBoard board;
    private List<SkyjoPlayerFieldComponent> playerFieldsComponents;
    private Button drawButton;
    private Button discardButton;
    private SkyjoCard selectedCard;

    public SkyjoView() {
        board = new SkyjoBoard(2);

        /** FIELD EVENTS **/
        board.getPlayersField().forEach(this::initBoard);
        playerFieldsComponents = board.getPlayersField().stream().map(SkyjoPlayerField::getFieldComponent).toList();
        add(playerFieldsComponents.toArray(new SkyjoPlayerFieldComponent[0]));
        /** DRAW EVENT **/
        initDeck();
        /** DISCARD EVENT**/
        initDiscardPile();
    }

    private void initDeck() {
        drawButton = new Button("DRAW");

        drawButton.addClickListener(event -> {
            if (selectedCard != null) {
                // Si on a déjà selectionné une carte, on ne peut pas la remettre dans la pioche
                System.out.println("Action impossible");
            } else {
                // On sélectionne la carte de la pioche et on la retourne
                selectedCard = board.getDeck().draw();
                setButtonStyle(drawButton, selectedCard.getColor(), selectedCard.toString());
            }
        });

        add(drawButton);
    }

    private void initDiscardPile() {
        discardButton = new Button();
        SkyjoCard topDiscardPile = board.getDiscardPile().getTopCard();
        setButtonStyle(discardButton, topDiscardPile.getColor(), topDiscardPile.toString());

        discardButton.addClickListener(event -> {
            if (selectedCard != null) {
                // Si un carte a été préalablement selectionné, on l'ajoute sur le dessus de la défausse
                board.getDiscardPile().discard(selectedCard);

                // On rafraichit l'affichage de la défausse et de la pioche
                setButtonStyle(discardButton, board.getDiscardPile().getTopCard().getColor(), board.getDiscardPile().getTopCard().toString());
                setButtonStyle(drawButton, "", "DRAW");

                // Il n'y a plus de carte selectionné
                selectedCard = null;
            } else {
                // On sélectionne la carte de la défausse
                selectedCard = board.getDiscardPile().draw();

                // On rafaichit
                if (board.getDiscardPile().getTopCard() == null) {
                    setButtonStyle(discardButton, "", "EMPTY");
                } else {
                    setButtonStyle(discardButton, board.getDiscardPile().getTopCard().getColor(), board.getDiscardPile().getTopCard().toString());
                }
            }
        });

        add(discardButton);
    }

    private void initBoard(SkyjoPlayerField playerField) {
        // On ajoute un évenement sur chaque carte du joueur
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < playerField.getField().length; col++) {
                int currentColumn = col;
                int currentRow = row;
                SkyjoCard card = playerField.getField()[col][row];
                Button button = card.getButton();
                button.addClickListener(event -> {
                    if (selectedCard != null) {
                        System.out.println("Changing value of [" + currentColumn + "][" + currentRow + "] to value " + selectedCard.getValue() + " for player " + playerField.getPlayerNum());

                        // Si une carte a été préalablement sélectionné, on remplace la carte du joueur
                        playerField.changeCard(currentColumn, currentRow, selectedCard);
                        // Et on ajoute cette carte remplacé sur le dessus de la défausse
                        board.getDiscardPile().discard(card);

                        if (playerField.checkForClearColumn(currentColumn)) {
                            SkyjoCard[] discardedCol = playerField.discardCol(currentColumn);
                            for (SkyjoCard discarded : discardedCol) {
                                board.getDiscardPile().discard(discarded);
                            }
                            // Rafraichissement de tout le field
                            reloadBoard();

                            board.printPlayersFields();
                        } else {
                            // Rafraichissement de la carte
                            setButtonStyle(button, selectedCard.getColor(), selectedCard.toString());
                            reloadBoard();
                        }

                        // Rafraichissement de la pioche et de la défausse
                        setButtonStyle(discardButton, board.getDiscardPile().getTopCard().getColor(), board.getDiscardPile().getTopCard().toString());
                        setButtonStyle(drawButton, "", "DRAW");

                        // Il n'y a plus de carte sélectionnée
                        selectedCard = null;
                    } else {
                        // Si aucune carte n'a été selectionné alors le joueur a pioché puis décidé de défausser la carte
                        // On doit donc réveler une de nos carte
                        card.setVisible(true);

                        if (playerField.checkForClearColumn(currentColumn)) {
                            SkyjoCard[] discardedCol = playerField.discardCol(currentColumn);
                            for (SkyjoCard discarded : discardedCol) {
                                board.getDiscardPile().discard(discarded);
                            }
                            // Rafraichissement de tout le field
                            reloadBoard();

                            board.printPlayersFields();
                        }
                    }
                });
            }
        }
    }

    private void setButtonStyle(Button button, String color, String text) {
        button.removeClassNames("color-purple", "color-blue", "color-green", "color-yellow", "color-red");
        button.setClassName("color-" + color);
        button.setText(text);
    }

    private void reloadBoard() {
        // Suppression des components
        remove(discardButton);
        remove(drawButton);
        remove(playerFieldsComponents.toArray(new SkyjoPlayerFieldComponent[0]));

        // Réinitialisation des components
        board.getPlayersField().forEach(this::initBoard);
        playerFieldsComponents = board.getPlayersField().stream().map(SkyjoPlayerField::getFieldComponent).toList();
        add(playerFieldsComponents.toArray(new SkyjoPlayerFieldComponent[0]));

        initDeck();
        initDiscardPile();
    }
}
