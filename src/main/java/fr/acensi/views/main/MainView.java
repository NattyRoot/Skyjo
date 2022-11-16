package fr.acensi.views.main;

import fr.acensi.views.SkyjoView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Main")
@Route(value = "")
public class MainView extends HorizontalLayout {

    private IntegerField playerCountInput;
    private Button start;

    public MainView() {
        playerCountInput = new IntegerField("# joueurs :");
        start = new Button("Start game");

        start.addClickListener(e -> {
            UI.getCurrent().navigate(SkyjoView.class, String.valueOf(playerCountInput.getValue()));
        });

        setMargin(true);
        setVerticalComponentAlignment(Alignment.END, start);

        add(playerCountInput, start);
    }

}
