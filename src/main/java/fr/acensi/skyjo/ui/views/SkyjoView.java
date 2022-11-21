package fr.acensi.skyjo.ui.views;

import com.vaadin.flow.component.orderedlayout.FlexLayout;
import fr.acensi.skyjo.business.SkyjoLogic;
import fr.acensi.skyjo.ui.components.SkyjoPlayerFieldComponent;
import com.vaadin.flow.router.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@PageTitle("Skyjo")
@Route(value = "skyjo/:playerCount?")
public class SkyjoView extends FlexLayout implements HasUrlParameter<String> {
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
        setFlexWrap(FlexWrap.WRAP);

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

        // Suppression des components
        remove(SkyjoLogic.getDiscardButton());
        remove(SkyjoLogic.getDrawButton());
        remove(SkyjoLogic.getPlayerFieldsComponents().toArray(new SkyjoPlayerFieldComponent[0]));

        loadView();
    }

    /**
     * Initialise les composants et les ajoutent à la vue
     */
    public void loadView() {
        // BOARD
        SkyjoLogic.initFields(this, hasVariante);
        // DRAW
        SkyjoLogic.initDeck();
        // DISCARD
        SkyjoLogic.initDiscardPile();

        // Adding components
        add(SkyjoLogic.getPlayerFieldsComponents().toArray(new SkyjoPlayerFieldComponent[0]));
        add(SkyjoLogic.getDrawButton());
        add(SkyjoLogic.getDiscardButton());
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