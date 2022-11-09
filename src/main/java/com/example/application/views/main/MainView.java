package com.example.application.views.main;

import com.example.application.skyjo.logic.SkyjoLogic;
import com.example.application.skyjo.model.SkyjoBoard;
import com.example.application.views.SkyjoView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Main")
@Route(value = "")
public class MainView extends HorizontalLayout {

    private Button start;

    public MainView() {

        start = new Button("Start game");
        start.addClickListener(e -> {
            UI.getCurrent().navigate(SkyjoView.class);
        });


        setMargin(true);
        setVerticalComponentAlignment(Alignment.END, start);

        add(start);


    }

}
