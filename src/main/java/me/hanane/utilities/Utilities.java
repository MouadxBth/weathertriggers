package me.hanane.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

public interface Utilities {

    Gson GSON = new GsonBuilder().create();

    static String weatherApiUrl() {
        return "https://api.openweathermap.org/data/2.5/weather";
    }

    static String weatherApiKey() {
        return "c5a4ade7893c3232ebe559f3fef75d5a";
    }

    static String geolocationApiUrl() {
        return "https://api.api-ninjas.com/v1/geocoding";
    }

    static String geolocationApiKey() {
        return "VKqZ36uuEro2rbogAXosdg==rBuQub7JnhK2FCHT";
    }

    static int defaultValue() {
        return -112233;
    }

    static Span createSpan(String text) {
        final Span span = new Span();
        span.setTitle(text);

        return span;
    }

    static TextField createInputField() {
        final TextField textField = new TextField();
        textField.getElement().setAttribute("aria-label", "search");
        textField.setClearButtonVisible(true);
        textField.addClassNames("--lumo-size-xl");
        return textField;
    }

    static NumberField createNumberField() {
        final NumberField numberField = new NumberField();
        numberField.setClearButtonVisible(true);
        numberField.addClassNames("--lumo-size-xl");
        return numberField;
    }

    static Checkbox createCheckBox() {
        final Checkbox checkbox = new Checkbox();
        checkbox.addClassNames("--lumo-size-xl");
        return checkbox;
    }

    static Notification createNotification(String msg) {
        Notification notification = new Notification();
        notification.setPosition(Notification.Position.TOP_END);

        Div text = new Div(new Text(msg));

        Button closeButton = new Button(new Icon("lumo", "cross"));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.getElement().setAttribute("aria-label", "Close");
        closeButton.addClickListener(event -> notification.close());

        HorizontalLayout layout = new HorizontalLayout(text, closeButton);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        notification.add(layout);
        return notification;
    }

    static TextField createTextElement(String prefix, String value) {
        final TextField textField = new TextField();
        textField.setReadOnly(true);
        if (prefix != null)
            textField.setLabel(prefix);
//            textField.setPrefixComponent(new Div(new Text(prefix)));
        textField.setValue(value);
        return textField;
    }

}
