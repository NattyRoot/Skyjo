package fr.acensi.skyjo.ui.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import fr.acensi.skyjo.model.SkyjoCard;
import fr.acensi.skyjo.model.SkyjoPlayerField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.dom.Element;

import java.util.List;

@Tag("SkyjoPlayerFieldComponent")
@CssImport("./styles/skyjo.css")
public class SkyjoBoardComponent extends Component {
    public SkyjoBoardComponent(List<SkyjoPlayerField> playerFields, Button drawButton, Button discardButton) {
        // Création du layout du board
        FlexLayout boardLayout = new FlexLayout();
        boardLayout.setFlexWrap(FlexLayout.FlexWrap.WRAP);

        for (SkyjoPlayerField playerField : playerFields) {
            // Création d'un layout pour chaque joueurs
            VerticalLayout playerLayout = new VerticalLayout();
            // Permet de retirer le style "width: 100%"
            playerLayout.setSizeUndefined();

            // Création du label avec le nom du joueur
            Label label = new Label(playerField.getPlayerName());

            // On ajoute la couleur rouge au nom du joueur lorsque c'est son tour
            if (playerField.isHisTurn()) {
                label.setClassName("color-red");
            }

            // Création de la table de boutons
            Element table = new Element("table");
            for (int row = 0; row < 3; row++) {
                table.appendChild(new Element("tr"));
                for (int col = 0; col < playerField.getField().length; col++) {
                    SkyjoCard currentCard = playerField.getField()[col][row];
                    table.appendChild(new Element("td").appendChild(currentCard.getButton().getElement()));
                }
            }

            // Création du label avec le score du joueur
            Element score = new Label("Score = " + playerField.calculateScore()).getElement();

            // Ajout du label, table et score sur le layout du joueur
            playerLayout.getElement().appendChild(label.getElement()).appendChild(table).appendChild(score);

            // Ajout du layout du joueur sur le ayout du board
            boardLayout.add(playerLayout);
        }

        // Création du layout pour la pioche et la défausse
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.add(drawButton, discardButton);

        // Ajout de la pioche et la défausse dans le layout du board
        boardLayout.add(buttonsLayout);

        // Ajout du layout sur le component
        getElement().appendChild(boardLayout.getElement());
    }
}
