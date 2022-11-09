package com.example.application.views;

import com.example.application.skyjo.model.SkyjoBoard;
import com.example.application.skyjo.model.SkyjoCard;
import com.example.application.views.components.SkyjoActionBoardElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Skyjo")
@Route(value = "skyjo")
public class SkyjoView extends HorizontalLayout {
    private SkyjoBoard board;
    private List<SkyjoActionBoardElement> skyjoActionBoardElement;

    public SkyjoView() {
        board = new SkyjoBoard(1);
        skyjoActionBoardElement = new ArrayList<>();

        board.getPlayerField().forEach((playerNum, playerDeck) -> {
            skyjoActionBoardElement.add(new SkyjoActionBoardElement(playerDeck));
        });

        add(skyjoActionBoardElement.toArray(new SkyjoActionBoardElement[0]));
    }
}
