package fr.acensi.views;

import com.vaadin.flow.component.orderedlayout.FlexLayout;
import fr.acensi.skyjo.business.SkyjoLogic;
import fr.acensi.views.components.SkyjoPlayerFieldComponent;
import com.vaadin.flow.router.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@PageTitle("Skyjo")
@Route(value = "skyjo/:playerCount?")
public class SkyjoView extends FlexLayout implements HasUrlParameter<String> {
    private boolean hasVariante;

    public SkyjoView() {
    }

    /**
     * Méthode hérité de l'interface HasUrlParameters.
     * On ne connait pas les paramêtres d'URL avant d'arriver dans cette méthode (donc après le contructeur), on doit donc initialiser les composants ici
     *
     * @param beforeEvent evenement BeforeEvent
     * @param playerCountParameter le parametre d'URL représentant le nombre de joueur (ex: http://localhost:8080/skyjo/5, playerCount = 5)
     */
    @Override
    public void setParameter(BeforeEvent beforeEvent, String playerCountParameter) {
        Location location = beforeEvent.getLocation();
        QueryParameters queryParameters = location
                .getQueryParameters();

        Map<String, List<String>> parametersMap = queryParameters.getParameters();
        List<String> hasVarianteParameters = parametersMap.getOrDefault("variante", new ArrayList<>(Collections.singleton("false")));

        hasVariante = hasVarianteParameters.get(0).equals("true");
        int playerCount;

        try {
            playerCount = Integer.parseInt(playerCountParameter);
        } catch (NumberFormatException e) {
            playerCount = 2;
        }

        SkyjoLogic.createBoard(playerCount);

        setFlexWrap(FlexWrap.WRAP);

        /* FIELD EVENTS */
        SkyjoLogic.initBoard(this, hasVariante);
        /* DRAW EVENT */
        SkyjoLogic.initDeck();
        /* DISCARD EVENT */
        SkyjoLogic.initDiscardPile();

        // Addin components
        add(SkyjoLogic.getPlayerFieldsComponents().toArray(new SkyjoPlayerFieldComponent[0]));
        add(SkyjoLogic.getDrawButton());
        add(SkyjoLogic.getDiscardButton());
    }

    /**
     * Supprime puis ajoute les composant de la vue afin d'afficher les modifications
     */
    public void reloadView() {
        // Suppression des components
        remove(SkyjoLogic.getDiscardButton());
        remove(SkyjoLogic.getDrawButton());
        remove(SkyjoLogic.getPlayerFieldsComponents().toArray(new SkyjoPlayerFieldComponent[0]));

        // BOARD
        SkyjoLogic.initBoard(this, hasVariante);
        // DRAW
        SkyjoLogic.initDeck();
        // DISCARD
        SkyjoLogic.initDiscardPile();

        // Adding components
        add(SkyjoLogic.getPlayerFieldsComponents().toArray(new SkyjoPlayerFieldComponent[0]));
        add(SkyjoLogic.getDrawButton());
        add(SkyjoLogic.getDiscardButton());

        SkyjoLogic.printPlayersFields();
    }
}
