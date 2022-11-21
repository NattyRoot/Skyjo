package fr.acensi.skyjo.business;

import com.vaadin.flow.component.button.Button;
import fr.acensi.skyjo.model.SkyjoBoard;
import fr.acensi.skyjo.model.SkyjoCard;
import fr.acensi.skyjo.model.SkyjoPlayerField;
import fr.acensi.skyjo.ui.views.SkyjoView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SkyjoLogic {
    private static SkyjoBoard board;
    private static Button drawButton;
    private static Button discardButton;
    private static SkyjoCard selectedCard;
    private static Integer skyjoCol;
    private static Integer skyjoRow;
    private static List<String> possibleNames = new ArrayList<>(List.of("Félix", "Alexandre", "Romain", "Thibault", "Maël", "Mathieu", "Yann"));

    public static SkyjoBoard getBoard() {
        return board;
    }

    public static Button getDrawButton() {
        return drawButton;
    }

    public static Button getDiscardButton() {
        return discardButton;
    }

    /**
     * DEBUG USE ONLY<br>
     * Affiche le board de chaque joueurs
     */
    public static void printPlayersFields() {
        board.printPlayersFields();
    }

    /**
     * Créé le board à partir du nombre de joueurs
     *
     * @param playerCount le nombre de joueurs
     */
    public static void initBoard(int playerCount) {
        //Reset des noms
        possibleNames = new ArrayList<>(List.of("Félix", "Alexandre", "Romain", "Thibault", "Maël", "Mathieu", "Yann"));

        // Création du board à partir du nombre de joueurs
        board = new SkyjoBoard(playerCount);

        // Distribution des cartes à chaque joueurs
        deal(playerCount);

        // Ajout de la première carte de la pioche dans la défausse
        getBoard().getDiscardPile().discard(getBoard().getDeck().draw());
    }

    /**
     * Initialise le field de tous les joueurs
     */
    public static void initFields(SkyjoView view, boolean hasVariante) {
        board.getPlayersField().forEach(playerField -> SkyjoLogic.initPlayerField(playerField, view, hasVariante));
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
    public static void initPlayerField(SkyjoPlayerField playerField, SkyjoView view, boolean hasVariante) {
        // On ajoute un évenement sur chaque carte du joueur
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < playerField.getField().length; col++) {
                int currentColumn = col;
                int currentRow = row;
                SkyjoCard card = playerField.getField()[col][row];
                Button button = new Button();

                setButtonStyle(button, card, "");

                button.addClickListener(event -> {
                    if (!hasVariante || !event.isShiftKey()) {
                        if (selectedCard != null) {
                            // Si une carte a été préalablement sélectionné, on remplace la carte du joueur
                            playerField.changeCard(currentColumn, currentRow, selectedCard, true);
                            // Et on ajoute cette carte remplacé sur le dessus de la défausse
                            board.getDiscardPile().discard(card);

                            if (playerField.checkForClearColumn(currentColumn)) {
                                clearColumn(playerField, currentColumn);
                            } else {
                                // Rafraichissement de la carte
                                setButtonStyle(button, selectedCard, "");
                            }

                            // Rafraichissement de la pioche et de la défausse
                            setButtonStyle(discardButton, board.getDiscardPile().getTopCard(), "boardButtons");
                            setButtonStyle(drawButton, "", "!", "boardButtons");

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
                        // Rafraichissement de toute la vue
                        view.refreshView(playerField.getPlayerNum());
                    } else {
                        if (skyjoCol == null ||skyjoRow == null) {
                            skyjoCol = currentColumn;
                            skyjoRow = currentRow;
                        } else {
                            swapCards(playerField, currentColumn, currentRow);

                            skyjoCol = null;
                            skyjoRow = null;

                            view.refreshView(playerField.getPlayerNum());
                        }
                    }
                });


                card.setButton(button);
            }
        }
    }

    /**
     * Initialise le deck du jeu avec un bouton et un évement de click
     */
    public static void initDeck() {
        drawButton = new Button("!");

        drawButton.addClickListener(event -> {
            if (selectedCard != null) {
                // Si on a déjà selectionné une carte, on ne peut pas la remettre dans la pioche
                System.out.println("Action impossible");
            } else {
                // Si la pioche est vide alors on transforme la défausse en pioche
                if (SkyjoLogic.getBoard().getDeck().getCards().isEmpty()) {
                    recycleDiscardPile();
                }
                // On sélectionne la carte de la pioche et on la retourne
                selectedCard = board.getDeck().draw();
                selectedCard.setVisible(true);
                setButtonStyle(drawButton, selectedCard, "boardButtons");
            }
        });

        setButtonStyle(drawButton, "", "!", "boardButtons");
    }

    /**
     * Initialise la pile de défausse du jeu en créant un bouton avec un évenement de click. La carte du dessus de la défausse est toujours visible
     */
    public static void initDiscardPile() {
        discardButton = new Button();

        setButtonStyle(discardButton, board.getDiscardPile().getTopCard(), "boardButtons");

        discardButton.addClickListener(event -> {
            if (selectedCard != null) {
                // Si un carte a été préalablement selectionné, on l'ajoute sur le dessus de la défausse
                board.getDiscardPile().discard(selectedCard);

                // On rafraichit l'affichage de la défausse et de la pioche
                setButtonStyle(discardButton, board.getDiscardPile().getTopCard(), "boardButtons");
                setButtonStyle(drawButton, "", "!", "boardButtons");

                // Il n'y a plus de carte selectionné
                selectedCard = null;
            } else {
                // On sélectionne la carte de la défausse
                selectedCard = board.getDiscardPile().draw();

                // On rafaichit
                if (board.getDiscardPile().getTopCard() == null) {
                    setButtonStyle(discardButton, "", "EMPTY", "boardButtons");
                } else {
                    setButtonStyle(discardButton, board.getDiscardPile().getTopCard(), "boardButtons");
                }
            }
        });

        setButtonStyle(discardButton, board.getDiscardPile().getTopCard(), "boardButtons");
    }

    /**
     * Distribue les cartes de chaques joueurs
     *
     * @param playerCount nombre de joueur dans la partie
     */
    public static void deal(int playerCount) {
        List<SkyjoPlayerField> fields = new ArrayList<>();

        // Rempli les deck ligne par ligne en alternant les joueurs
        for (int playerNum = 0; playerNum < playerCount; playerNum++) {
            SkyjoCard[][] cards = new SkyjoCard[4][3];
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 4; col++) {
                    cards[col][row] = getBoard().getDeck().draw();
                }
            }
            fields.add(new SkyjoPlayerField(playerNum, generateRandomName(playerNum), cards));
        }

        getBoard().setPlayersField(fields);
    }

    /**
     * Lorsqu'une colonne contient 3 carte identique, on la supprime du field
     *
     * @param playerField le player field à modifier
     * @param col l'index de la colonne à supprimer
     */
    public static void clearColumn(SkyjoPlayerField playerField, int col) {
        SkyjoCard[] discardedCol = playerField.discardCol(col);
        for (SkyjoCard discarded : discardedCol) {
            board.getDiscardPile().discard(discarded);
        }
    }

    /**
     * Remplace la carte situé au coordonnées [col, row] par celle situé aux coordonnées préalablement stocké [skyjoCol, skyjoRow]
     *
     * @param playerField le playerField à modifier
     * @param col la colonne à swap
     * @param row la ligne à swap
     */
    public static void swapCards(SkyjoPlayerField playerField, int col, int row) {
        SkyjoCard[][] cards = playerField.getField();
        SkyjoCard swapCard = cards[skyjoCol][skyjoRow];

        playerField.changeCard(skyjoCol, skyjoRow, cards[col][row], false);
        playerField.changeCard(col, row, swapCard, false);
    }

    /**
     * Remet les cartes de la défausse dans la pioche à l'exception de la première puis mélange le deck
     */
    public static void recycleDiscardPile() {
        // Garde la dernière carte de la défausse
        SkyjoCard topDiscard = getBoard().getDiscardPile().draw();
        // Met dans le deck toutes les cartes de la défausse
        getBoard().getDeck().setCards(new ArrayList<>(getBoard().getDiscardPile().getCards()));
        // Mélange le deck
        getBoard().getDeck().shuffle();
        // Vide la défausse
        getBoard().getDiscardPile().empty();
        // Remet la dernière carte dans la défausse
        getBoard().getDiscardPile().discard(topDiscard);
    }

    /**
     * Modifie le style du bouton si la carte associé est visible
     *
     * @param button Le bouton à modifier
     * @param card La carte associé
     * @param otherClasses les autres classes CSS à ajouter
     */
    private static void setButtonStyle(Button button, SkyjoCard card, String otherClasses) {
        if (card.isVisible()) {
            setButtonStyle(button, card.getColor(), card.toString(), otherClasses);
        } else {
            setButtonStyle(button, "", "?", otherClasses);
        }
    }

    /**
     * Modifie le bouton afin d'ajouter la couleur et le texte de la carte
     *
     * @param button le bouton à modifier
     * @param color la couleur à appliquer
     * @param text le text à afficher
     */
    private static void setButtonStyle(Button button, String color, String text, String otherClasses) {
        button.removeClassNames("card", "boardButtons", "color-purple", "color-blue", "color-green", "color-yellow", "color-red");
        button.setClassName(otherClasses + " card color-" + color);
        button.setText(text);
    }

    private static String generateRandomName(int playerNum) {
        // Si il n'y a plus de noms dispo
        if (SkyjoLogic.possibleNames.isEmpty()) {
            return "Player " + playerNum;
        }

        int rng = new Random().nextInt(SkyjoLogic.possibleNames.size());
        return possibleNames.remove(rng);
    }
}
