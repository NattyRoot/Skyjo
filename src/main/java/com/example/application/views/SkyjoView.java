package com.example.application.views;

import com.example.application.skyjo.model.SkyjoBoard;
import com.example.application.skyjo.model.SkyjoCard;
import com.example.application.skyjo.model.SkyjoPlayerField;
import com.example.application.views.components.SkyjoPlayerFieldComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.*;

import java.util.List;
import java.util.Map;

@PageTitle("Skyjo")
@Route(value = "skyjo/:playerCount?")
public class SkyjoView extends HorizontalLayout implements HasUrlParameter<String> {
    private SkyjoBoard board;
    private List<SkyjoPlayerFieldComponent> playerFieldsComponents;
    private Button drawButton;
    private Button discardButton;
    private SkyjoCard selectedCard;

    public SkyjoView() {
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String playerCount) {
        board = new SkyjoBoard(Integer.parseInt(playerCount));

        /** FIELD EVENTS **/
        initBoard();
        /** DRAW EVENT **/
        initDeck();
        /** DISCARD EVENT**/
        initDiscardPile();
    }

    /**
     * Initialise le board des joueurs
     */
    private void initBoard() {
        board.getPlayersField().forEach(this::initPlayerField);
        playerFieldsComponents = board.getPlayersField().stream().map(SkyjoPlayerField::getFieldComponent).toList();
        add(playerFieldsComponents.toArray(new SkyjoPlayerFieldComponent[0]));
    }

    /**
     * Initialise le deck du jeu avec un bouton et un évement de click
     */
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

    /**
     * Initialise la pile de défausse du jeu en créant un bouton avec un évenement de click. La carte du dessus de la défausse est toujours visible
     */
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

    /**
     * Initialise un SkyjoPlayerField en ajoutant un évenement de click sur chaque boutons du joueur (1 bouton = 1 carte).
     * Par défaut, les carte sont faces cachés (représenté par une couleur neutre et le texte '?').
     * <br><br>
     * Si une carte de la pioche a été préalablement sélectionné (stocké dans la variable 'selectedCard') : <br>
     *      On défausse la carte du joueur et on met la carte de la pioche sur son emplacement<br><br>
     * Si une carte de la défausse a été préalablement sélectionné (stocké dans la variable 'selectedCard') : <br>
     *      On défausse la carte du joueur et on met la carte de la défausse sur son emplacement<br><br>
     * Si aucune carte n'a été préalablement sélectionné (selectedCard == null) : <br>
     *      On révèle la carte du joueur<br>
     *
     * @param playerField le SkyjoPlayerField à initialiser
     */
    private void initPlayerField(SkyjoPlayerField playerField) {
        // On ajoute un évenement sur chaque carte du joueur
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < playerField.getField().length; col++) {
                int currentColumn = col;
                int currentRow = row;
                SkyjoCard card = playerField.getField()[col][row];
                Button button = new Button();

                if (card.isVisible()) {
                    button.setClassName("color-" + card.getColor());
                    button.setText(card.toString());
                } else {
                    button.setText("?");
                }

                button.addClickListener(event -> {
                    if (selectedCard != null) {
                        System.out.println("Changing value of [" + currentColumn + "][" + currentRow + "] to value " + selectedCard.getValue() + " for player " + playerField.getPlayerNum());

                        // Si une carte a été préalablement sélectionné, on remplace la carte du joueur
                        playerField.changeCard(currentColumn, currentRow, selectedCard);
                        // Et on ajoute cette carte remplacé sur le dessus de la défausse
                        board.getDiscardPile().discard(card);

                        if (playerField.checkForClearColumn(currentColumn)) {
                            clearColumn(playerField, currentColumn);
                        } else {
                            // Rafraichissement de la carte
                            setButtonStyle(button, selectedCard.getColor(), selectedCard.toString());
                            // Rafraichissement de toute la vue
                            reloadView();
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
                           clearColumn(playerField, currentColumn);
                        }
                    }
                });
                card.setButton(button);
            }
        }
    }

    /**
     * Lorsqu'une colonne contient 3 carte identique, on la supprime du field
     *
     * @param playerField le player field à modifier
     * @param col l'index de la colonne à supprimer
     */
    private void clearColumn(SkyjoPlayerField playerField, int col) {
        SkyjoCard[] discardedCol = playerField.discardCol(col);
        for (SkyjoCard discarded : discardedCol) {
            board.getDiscardPile().discard(discarded);
        }
        // Rafraichissement de toute la vue
        reloadView();
    }

    /**
     * Modifie le bouton afin d'ajouter la couleur et le texte de la carte
     *
     * @param button le bouton à modifier
     * @param color la couleur à appliquer
     * @param text le text à afficher
     */
    private void setButtonStyle(Button button, String color, String text) {
        button.removeClassNames("color-purple", "color-blue", "color-green", "color-yellow", "color-red");
        button.setClassName("color-" + color);
        button.setText(text);
    }

    /**
     * Supprime puis ajoute les composant de la vue afin d'afficher les modifications
     */
    private void reloadView() {
        // Suppression des components
        remove(discardButton);
        remove(drawButton);
        remove(playerFieldsComponents.toArray(new SkyjoPlayerFieldComponent[0]));

        // Réinitialisation des components
        initBoard();
        initDeck();
        initDiscardPile();

        board.printPlayersFields();
    }
}
