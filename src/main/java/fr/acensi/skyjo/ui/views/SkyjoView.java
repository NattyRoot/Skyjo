package fr.acensi.skyjo.ui.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import fr.acensi.skyjo.business.SkyjoLogic;
import fr.acensi.skyjo.ui.components.SkyjoBoardComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@PageTitle("Skyjo")
@Route(value = "skyjo/:playerCount?")
public class SkyjoView extends VerticalLayout implements HasUrlParameter<String> {
    private static SkyjoBoardComponent boardComponent;
    private boolean hasVariante;
    private int playerCount;

    /**
     * Méthode hérité de l'interface HasUrlParameters.
     * On ne connait pas les paramêtres d'URL avant d'arriver dans cette méthode (donc après le contructeur), on doit donc initialiser les composants ici
     *
     * @param beforeEvent          evenement BeforeEvent
     * @param playerCountParameter le parametre d'URL représentant le nombre de joueur (ex: http://localhost:8080/skyjo/5, playerCount = 5)
     */
    @Override
    public void setParameter(BeforeEvent beforeEvent, String playerCountParameter) {
        // Récupération du parametre "variante" dans les Query parameters
        hasVariante = getQueryParameter(
            beforeEvent,
            "variante",
            new ArrayList<>(Collections.singleton("false"))
        ).equals("true");

        // Récupération du parametre "playerCount" dans l'URL
        try {
            playerCount = Integer.parseInt(playerCountParameter);
        } catch (NumberFormatException e) {
            playerCount = 2;
        }

        // Initialisation du board
        SkyjoLogic.initBoard(playerCount);

        // Chargement des composants dans la vue
        loadView();
    }

    /**
     * Supprime puis ajoute les composant de la vue afin d'afficher les modifications
     */
    public void refreshView(int playerNum) {
        // Passage au joueur suivant
        SkyjoLogic.getBoard().getPlayersField().forEach(playerField -> playerField.setHisTurn(playerNum + 1, playerCount));

        // Suppression du component
        remove(boardComponent);

        loadView();
    }

    /**
     * Initialise les composants et les ajoutent à la vue
     */
    public void loadView() {
        // FIELDS
        SkyjoLogic.initFields(this, hasVariante);
        // DRAW
        SkyjoLogic.initDeck();
        // DISCARD
        SkyjoLogic.initDiscardPile();
        // BOARD
        boardComponent = new SkyjoBoardComponent(SkyjoLogic.getBoard().getPlayersField(), SkyjoLogic.getDrawButton(), SkyjoLogic.getDiscardButton());

        // Ajout du component
        add(boardComponent);
    }

    /**
     * Récupère un paramètre de requête identifié par la clé "key"
     *
     * @param event        l'évenement
     * @param key          la clé du paramètre
     * @param defaultValue la valeur par défaut si rien n'est retrouvé
     * @return la valeur du paramètre de requête recherché
     */
    private String getQueryParameter(BeforeEvent event, String key, List<String> defaultValue) {
        return event.getLocation().getQueryParameters().getParameters().getOrDefault(key, defaultValue).get(0);
    }
}
