package me.hanane.views.weather;

import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.theme.lumo.LumoUtility.*;

import java.util.ArrayList;
import java.util.List;

public class WeatherViewCard extends ListItem {

    private List<TextField> textElements = new ArrayList<>();
    private String desc = "";

    public WeatherViewCard(String title) {
        addClassNames(Background.CONTRAST_5,
                Display.FLEX,
                FlexDirection.COLUMN,
                AlignItems.START,
                Padding.MEDIUM,
                BorderRadius.LARGE);

//        Div div = new Div();
//        div.addClassNames(Background.CONTRAST,
//                Display.FLEX,
//                AlignItems.CENTER,
//                JustifyContent.CENTER,
//                Margin.Bottom.MEDIUM,
//                Overflow.HIDDEN,
//                BorderRadius.MEDIUM,
//                Width.FULL);
//
//        div.setHeight("160px");

//        Image image = new Image();
//        image.setWidth("100%");
//        image.setSrc(url);
//        image.setAlt(text);

//        div.add(image);

        Span header = new Span();
        header.addClassNames(FontSize.XLARGE, FontWeight.SEMIBOLD);
        header.setText(title);

//        Span subtitle = new Span();
//        subtitle.addClassNames(FontSize.SMALL, TextColor.SECONDARY);
//        subtitle.setText("Card subtitle");

//        VerticalLayout textElementsLayout = new VerticalLayout();
//        textElementsLayout.addClassName(Margin.Vertical.MEDIUM);

        add(header);

//
//        Span badge = new Span();
//        badge.getElement().setAttribute("theme", "badge");
//        badge.setText("Label");
//        final TextField textField = new TextField();
//        textField.setReadOnly(true);
//        textField.setValue("THIS IS A TEST");

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

    public WeatherViewCard setTextElements(List<TextField> textElements) {
        this.textElements = textElements;
        return this;
    }
}
