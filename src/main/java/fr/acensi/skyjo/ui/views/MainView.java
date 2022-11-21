package fr.acensi.skyjo.ui.views;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.router.QueryParameters;
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
    private Checkbox hasVariantCheckbox;
    private Button start;

    public MainView() {
        playerCountInput = new IntegerField("# joueurs :");
        hasVariantCheckbox = new Checkbox("Variante");
        start = new Button("Start game");

        start.addClickListener(e -> {
            UI.getCurrent().navigate(SkyjoView.class, String.valueOf(playerCountInput.getValue()), QueryParameters.of("variante", hasVariantCheckbox.getValue().toString()));
        });

        setMargin(true);
        setVerticalComponentAlignment(Alignment.END, start, playerCountInput, hasVariantCheckbox);

        add(playerCountInput, hasVariantCheckbox, start);
    }

}
