package me.hanane.views.triggers;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.theme.lumo.LumoUtility;
import me.hanane.trigger.Trigger;

import java.util.stream.Stream;

import static me.hanane.utilities.Utilities.*;

public class TriggerForm extends FormLayout {

    private final int charLimit = 4096;

    private final TextField name = createInputField(),
            description = createInputField();
    private final TextArea message = new TextArea();

    private final Checkbox runOnce = createCheckBox();

    private final Binder<Trigger> binder = new BeanValidationBinder<>(Trigger.class);

    private final NumberField temperature = createNumberField(),
            temp_feels_like = createNumberField(),
            minimumTemperature = createNumberField(),
            maximumTemperature = createNumberField(),
            pressure = createNumberField(),
            humidity = createNumberField(),
            rainLastHour = createNumberField(),
            cloudiness = createNumberField(),
            visibility = createNumberField(),
            windSpeed = createNumberField(),
            windDegree = createNumberField(),
            windGust = createNumberField();

    public TriggerForm() {
        addClassNames(LumoUtility.AlignItems.CENTER,
                LumoUtility.JustifyContent.EVENLY,
                LumoUtility.Margin.Bottom.XLARGE,
                LumoUtility.Margin.Top.XLARGE);

        setupInputFields();
        setupBinder();

        add(name,
                description,
                temperature,
                maximumTemperature,
                minimumTemperature,
                temp_feels_like,
                pressure,
                humidity,
                windSpeed,
                windDegree,
                windGust,
                cloudiness,
                rainLastHour,
                visibility,
                runOnce,
                message);
    }

    private void setupBinder() {
        binder.bindInstanceFields(this);
    }

    public Binder<Trigger> getBinder() {
        return binder;
    }

    public TextField getName() {
        return name;
    }

    public TextField getDescription() {
        return description;
    }

    public TextArea getMessage() {
        return message;
    }

    public Checkbox getRunOnce() {
        return runOnce;
    }

    public NumberField getTemperature() {
        return temperature;
    }

    public NumberField getTemp_feels_like() {
        return temp_feels_like;
    }

    public NumberField getMinimumTemperature() {
        return minimumTemperature;
    }

    public NumberField getMaximumTemperature() {
        return maximumTemperature;
    }

    public NumberField getPressure() {
        return pressure;
    }

    public NumberField getHumidity() {
        return humidity;
    }

    public NumberField getRainLastHour() {
        return rainLastHour;
    }

    public NumberField getCloudiness() {
        return cloudiness;
    }

    public NumberField getVisibility() {
        return visibility;
    }

    public NumberField getWindSpeed() {
        return windSpeed;
    }

    public NumberField getWindDegree() {
        return windDegree;
    }

    public NumberField getWindGust() {
        return windGust;
    }

    private void setupInputFields() {
        name.setLabel("Trigger name:");
        name.setRequired(true);
        name.setPlaceholder("Enter the trigger name");

        description.setLabel("Description:");
        description.setPlaceholder("Enter the trigger's description");

        temperature.setLabel("Temperature:");
        temperature.setPlaceholder("Trigger's temperature");
        temperature.setSuffixComponent(new Div(new Text("°C")));

        maximumTemperature.setLabel("Maximum Temperature:");
        maximumTemperature.setPlaceholder("Trigger's maximum temperature");
        maximumTemperature.setSuffixComponent(new Div(new Text("°C")));

        minimumTemperature.setLabel("Minimum Temperature:");
        minimumTemperature.setPlaceholder("Trigger's minimum temperature");
        minimumTemperature.setSuffixComponent(new Div(new Text("°C")));

        temp_feels_like.setLabel("Temperature Feels like:");
        temp_feels_like.setPlaceholder("Trigger's temperature feels like");
        temp_feels_like.setSuffixComponent(new Div(new Text("°C")));

        pressure.setLabel("Pressure:");
        pressure.setPlaceholder("Trigger's pressure");
        pressure.setSuffixComponent(new Div(new Text("Pa")));

        humidity.setLabel("Humidity:");
        humidity.setPlaceholder("Trigger's humidity");
        humidity.setSuffixComponent(new Div(new Text("g m³")));

        windSpeed.setLabel("Wind Speed:");
        windSpeed.setPlaceholder("Trigger's wind speed");
        windSpeed.setSuffixComponent(new Div(new Text("m/s")));

        windDegree.setLabel("Wind Degree:");
        windDegree.setPlaceholder("Trigger's wind degree");
        windDegree.setSuffixComponent(new Div(new Text("°")));
        windGust.setMax(360);
        windGust.setMin(-360);

        windGust.setLabel("Wind Gust:");
        windGust.setPlaceholder("Trigger's wind gust");
        windGust.setSuffixComponent(new Div(new Text("m/s")));

        cloudiness.setLabel("Cloudiness:");
        cloudiness.setPlaceholder("Trigger's cloudiness");
        cloudiness.setSuffixComponent(new Div(new Text("%")));
        cloudiness.setMax(100.0);

        rainLastHour.setLabel("Rain last hour:");
        rainLastHour.setPlaceholder("Trigger's rain last hour");
        rainLastHour.setSuffixComponent(new Div(new Text("mm")));

        visibility.setLabel("Visibility:");
        visibility.setPlaceholder("Trigger's visibility");
        visibility.setSuffixComponent(new Div(new Text("km")));
        visibility.setMax(10_000);

        runOnce.setLabel("Run once:");

        message.setLabel("Message:");
        message.setPlaceholder("Enter the trigger's message");

        message.setMaxLength(charLimit);
        message.setValueChangeMode(ValueChangeMode.EAGER);
        message.addValueChangeListener(e -> e.getSource()
                .setHelperText(e.getValue().length() + "/" + charLimit));
    }

    public void clear() {
        Stream.of(name,
                description,
                temperature,
                maximumTemperature,
                minimumTemperature,
                temp_feels_like,
                pressure,
                humidity,
                windSpeed,
                windDegree,
                windGust,
                cloudiness,
                rainLastHour,
                visibility,
                runOnce,
                message).forEach(HasValue::clear);
    }

    public Trigger getInputTrigger() {
        final Trigger trigger = new Trigger();
        if (name.getValue() != null && !name.getValue().isBlank()) {
            trigger.setName(name.getValue());
        }
        if (description.getValue() != null && !description.getValue().isBlank()) {
            trigger.setDescription(description.getValue());
        }
        if (message.getValue() != null && !message.getValue().isBlank()) {
            trigger.setMessage(message.getValue());
        }
        if (temperature.getValue() != null) {
            trigger.setTemperature(temperature.getValue());
        }
        if (maximumTemperature.getValue() != null) {
            trigger.setMaximumTemperature(maximumTemperature.getValue());
        }
        if (minimumTemperature.getValue() != null) {
            trigger.setMinimumTemperature(minimumTemperature.getValue());
        }
        if (temp_feels_like.getValue() != null) {
            trigger.setTemp_feels_like(temp_feels_like.getValue());
        }

        if (humidity.getValue() != null) {
            trigger.setHumidity(humidity.getValue().intValue());
        }
        if (pressure.getValue() != null) {
            trigger.setPressure(pressure.getValue().intValue());
        }
        if (cloudiness.getValue() != null) {
            trigger.setCloudiness(cloudiness.getValue().intValue());
        }
        if (visibility.getValue() != null) {
            trigger.setVisibility(visibility.getValue().intValue());
        }
        if (windGust.getValue() != null) {
            trigger.setWindGust(windGust.getValue().intValue());
        }
        if (rainLastHour.getValue() != null) {
            trigger.setRainLastHour(rainLastHour.getValue());
        }
        if (windSpeed.getValue() != null) {
            trigger.setWindSpeed(windSpeed.getValue());
        }
        trigger.setRunOnce(runOnce.getValue());

        return trigger;
    }
}
