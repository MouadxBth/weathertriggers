package me.hanane.views.weather;

import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.theme.lumo.LumoUtility.*;

import java.util.ArrayList;
import java.util.List;

public class WeatherViewCard extends ListItem {

    private final List<TextField> textElements = new ArrayList<>();
    private String desc = "";

    public WeatherViewCard(String title) {
        addClassNames(Background.CONTRAST_5,
                Display.FLEX,
                FlexDirection.COLUMN,
                AlignItems.START,
                Padding.MEDIUM,
                BorderRadius.LARGE);

        Span header = new Span();
        header.addClassNames(FontSize.XLARGE, FontWeight.SEMIBOLD);
        header.setText(title);

        add(header);

    }

    public WeatherViewCard build() {
        if (getTextElements().isEmpty())
            add(new Paragraph(desc));
        else
            getTextElements().forEach(this::add);
        return this;
    }

    public List<TextField> getTextElements() {
        return textElements;
    }

    public WeatherViewCard setDesc(String desc) {
        this.desc = desc;
        return this;
    }

}
