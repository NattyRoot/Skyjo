package com.example.application.views.components;

import com.example.application.skyjo.model.SkyjoCard;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.dom.Element;


@Tag("SkyjoActionBoardElement")
public class SkyjoActionBoardElement extends Component {
    Element table = new Element("table");

    public SkyjoActionBoardElement(SkyjoCard[][] playerDeck) {
        for (int row = 0; row < 3; row++) {
            table.appendChild(new Element("tr"));
            for (int col = 0; col < 4; col++) {
                table.appendChild(new Element("td"));
                SkyjoCard currentCard = playerDeck[col][row];
                Button currentButton = new Button("?");
                currentButton.addClickListener((a) -> currentButton.setText(currentCard.toString()));
                table.appendChild(currentButton.getElement());
            }
        }
        getElement().appendChild(table);
    }
}
