package com.example.application.views.components;

import com.example.application.skyjo.model.SkyjoCard;
import com.example.application.skyjo.model.SkyjoPlayerField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.dom.Element;


@Tag("SkyjoPlayerFieldComponent")
@CssImport("./styles/skyjo.css")
public class SkyjoPlayerFieldComponent extends Component {
    public SkyjoPlayerFieldComponent(SkyjoPlayerField playerField) {
        Element label = new Label("Player " + (playerField.getPlayerNum() + 1)).getElement();
        Element table = new Element("table");

        for (int row = 0; row < 3; row++) {
            table.appendChild(new Element("tr"));
            for (int col = 0; col < playerField.getField().length; col++) {
                SkyjoCard currentCard = playerField.getField()[col][row];
                table.appendChild(new Element("td").appendChild(currentCard.getButton().getElement()));
            }
        }

        getElement().appendChild(label).appendChild(table);
    }
}
