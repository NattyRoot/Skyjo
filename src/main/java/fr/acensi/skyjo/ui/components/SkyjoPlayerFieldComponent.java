package fr.acensi.skyjo.ui.components;

import fr.acensi.skyjo.model.SkyjoCard;
import fr.acensi.skyjo.model.SkyjoPlayerField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.dom.Element;


@Tag("SkyjoPlayerFieldComponent")
@CssImport("./styles/skyjo.css")
public class SkyjoPlayerFieldComponent extends Component {
    public SkyjoPlayerFieldComponent(SkyjoPlayerField playerField) {
        Label label = new Label("Player " + playerField.getPlayerName());

        if (playerField.isHisTurn()){
            label.setClassName("color-red");
        }

        Element table = new Element("table");
        Element score = new Label("Score = " + playerField.calculateScore()).getElement();

        for (int row = 0; row < 3; row++) {
            table.appendChild(new Element("tr"));
            for (int col = 0; col < playerField.getField().length; col++) {
                SkyjoCard currentCard = playerField.getField()[col][row];
                table.appendChild(new Element("td").appendChild(currentCard.getButton().getElement()));
            }
        }

        getElement().setAttribute("class", "playerField").appendChild(label.getElement()).appendChild(table).appendChild(score);
    }
}
