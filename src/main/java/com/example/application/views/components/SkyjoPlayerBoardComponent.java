package com.example.application.views.components;

import com.example.application.skyjo.model.SkyjoCard;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.dom.Element;

import java.util.List;

@Tag("SkyjoActionBoardElement")
public class SkyjoPlayerBoardComponent extends Component {

    private List<Button> buttons;

    public SkyjoPlayerBoardComponent(SkyjoCard[][] playerDeck) {
        Element table = new Element("table");

        for (int row = 0; row < 3; row++) {
            table.appendChild(new Element("tr"));
            for (int col = 0; col < 4; col++) {
                table.appendChild(new Element("td"));
                SkyjoCard currentCard = playerDeck[col][row];
                Button currentButton = new Button("?");
                currentButton.getElement().setAttribute("row", row + "");
                currentButton.getElement().setAttribute("col", col + "");
                currentButton.setEnabled(false);
                table.appendChild(currentButton.getElement());
                buttons.add(currentButton);
            }
        }
        table.appendChild(new Element("br"));

        getElement().appendChild(table);
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public void enableButtons() {
        buttons.forEach(button -> button.setEnabled(true));
    }

    public void changeValueOfButton(String value, int row, int col) {
        buttons.forEach(button -> {
            if ((row + "").equals(button.getElement().getAttribute("row")) && (col + "").equals(button.getElement().getAttribute("col"))) {
                button.setText(value);
            }
        });
    }
}
