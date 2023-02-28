package me.hanane.views.dashboard;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.ListStyleType;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.MaxWidth;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;

import javax.annotation.security.PermitAll;

import me.hanane.location.Geolocation;
import me.hanane.location.GeolocationService;
import me.hanane.security.AuthenticatedUser;
import me.hanane.trigger.service.TriggersService;
import me.hanane.views.MainLayout;
import me.hanane.views.weather.WeatherViewCard;
import me.hanane.weather.info.MainInfo;
import me.hanane.weather.info.WeatherInfo;
import me.hanane.weather.WeatherService;
import me.hanane.weather.info.WindInfo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.hanane.utilities.Utilities.*;

@PageTitle("Weather")
@Route(value = "dashboard", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PermitAll
@PreserveOnRefresh
public class DashboardView extends Main implements HasComponents, HasStyle, AfterNavigationObserver {

    private OrderedList weatherComponents;
    private final GeolocationService geolocationService;
    private final WeatherService weatherService;

    private final AuthenticatedUser authenticatedUser;

    private final TriggersService triggersService;

    public DashboardView(GeolocationService geolocationService,
                         WeatherService weatherService,
                         AuthenticatedUser authenticatedUser,
                         TriggersService triggersService) {
        this.geolocationService = geolocationService;
        this.weatherService = weatherService;
        this.authenticatedUser = authenticatedUser;
        this.triggersService = triggersService;
        constructUI();
    }

    private void constructUI() {
        addClassNames("weather-view");
        addClassNames(MaxWidth.SCREEN_LARGE,
                Margin.Horizontal.AUTO,
                Padding.Bottom.LARGE,
                Margin.Vertical.MEDIUM,
                Padding.Horizontal.LARGE);

        weatherComponents = new OrderedList();
        weatherComponents.addClassNames(Gap.MEDIUM,
                Display.GRID,
                ListStyleType.NONE,
                Margin.NONE,
                Padding.NONE);

        if (!weatherService.getWeatherComponents().isEmpty()) {
            weatherComponents.add(weatherService.getWeatherComponents());
        }

        add(weatherComponents);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        if (weatherComponents.getChildren().findAny().isEmpty()) {
            authenticatedUser.get().ifPresent(user -> {
                System.out.println("USER FOUND");
                if (user.getCity() == null || user.getCity().isBlank()) {
                    System.out.println("CITY NULL");
                    final Notification notification = createNotification("Enter Country name for weather information!");
                    notification.setPosition(Notification.Position.MIDDLE);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notification.setDuration(5 * 1000);
                    notification.open();
                    return;
                }
                weatherService.setWeatherComponents(getWeatherComponents(user.getCity(),
                        user.getState(),
                        user.getCountry()));
                weatherComponents.add(weatherService.getWeatherComponents());
            });
        }

    }

    private List<Component> getWeatherComponents(String city, String state, String country) {
        final List<Component> components = new ArrayList<>();

        geolocationService.get(city, state, country)
                .thenCompose(geolocation -> {
                    components.add(getLocationInfoComponent(geolocation));
                    return weatherService.weather(geolocation);
                })
                .whenComplete((weatherInfo, error) -> {
                    System.out.println("WEATHER FOUND");

                    if (error != null) {
                        error.printStackTrace();
                        final Notification notification = createNotification("Could not find city!");
                        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                        notification.setDuration(3 * 1000);
                        notification.open();
                        return;
                    }
                    components.addAll(Arrays.asList(
                            getMainInfoComponent(weatherInfo.mainInfo()),
                            getTemperatureComponent(weatherInfo.mainInfo()),
                            getWeatherInfoComponent(weatherInfo.weatherInfo()),
                            getWindInfoComponent(weatherInfo.windInfo()),
                            getTriggersCount()
                    ));
                    getUI().ifPresent(ui -> {
                        ui.access(() -> {
                            weatherComponents.add(components);
                        });
                    });
                    triggersService.fetch().forEach(trigger -> {
                        System.out.println(trigger);
                        if (weatherInfo.testTrigger(trigger)) {
                            if (trigger.isRunOnce() && trigger.isExecuted())
                                return;
                            final Notification notification = createNotification(trigger.getMessage());
                            notification.setPosition(Notification.Position.MIDDLE);
                            notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
                            notification.setDuration(5 * 5000);
                            notification.open();
                        }
                    });
                });

        return components;
    }

    private Component getTriggersCount() {
        final WeatherViewCard card = new WeatherViewCard("Current Trigger Count");

        card.getTextElements().add(
                createTextElement("Total:", "" + triggersService.count())
        );
        return card.build();
    }

    private Component getLocationInfoComponent(Geolocation location) {
        final WeatherViewCard card = new WeatherViewCard("Current Location");

        if (location == null) {
            card.setDesc("Could not load location info!");
        } else {
            card.getTextElements().addAll(Arrays.asList(
                    createTextElement("City:", (location.city() == null ? "Not Found" : location.city())),
                    createTextElement("State:", (location.state() == null ? "Not Found" : location.state())),
                    createTextElement("Country:", (location.country() == null ? "Not Found" : location.country())),
                    createTextElement("Longitude:", location.longitude() + ""),
                    createTextElement("Latitude:", location.latitude() + "")
            ));
        }
        return card.build();
    }

    private Component getWeatherInfoComponent(WeatherInfo weatherInfo) {
        final WeatherViewCard card = new WeatherViewCard("Current Weather");

        if (weatherInfo == null) {
            card.setDesc("Could not load weather info!");
        } else {
            card.getTextElements().add(createTextElement(weatherInfo.name(), weatherInfo.description()));
            if (weatherInfo.cloudiness() > 0) {
                card.getTextElements().add(createTextElement("Cloudiness:", weatherInfo.cloudiness() + "%"));
            }
            if (weatherInfo.rainLastHour() > 0) {
                card.getTextElements().add(createTextElement("Rain last (1h):", weatherInfo.rainLastHour() + "mm"));
            }
        }

        return card.build();
    }

    private Component getMainInfoComponent(MainInfo mainInfo) {
        final WeatherViewCard card = new WeatherViewCard("Main Info");

        if (mainInfo == null) {
            card.setDesc("Could not load main info!");
        } else {
            card.getTextElements().addAll(Arrays.asList(
                    createTextElement("Humidity:", mainInfo.humidity() + "g m³"),
                    createTextElement("Pressure:", mainInfo.pressure() + "Pa"),
                    createTextElement("Ground Level:", mainInfo.grnd_level() + "m"),
                    createTextElement("Sea Level:", mainInfo.sea_level() + "m")
            ));
        }
        return card.build();
    }

    private Component getTemperatureComponent(MainInfo mainInfo) {
        final WeatherViewCard card = new WeatherViewCard("Temperature Info");

        if (mainInfo == null) {
            card.setDesc("Could not load temperature info!");
        } else {
            card.getTextElements().addAll(Arrays.asList(
                    createTextElement("Current Temperature:", mainInfo.temp() + "°C"),
                    createTextElement("Maximum Temperature:", mainInfo.temp_max() + "°C"),
                    createTextElement("Minimum Temperature:", mainInfo.temp_min() + "°C"),
                    createTextElement("Temperature Feels Like:", mainInfo.feels_like() + "°C")

            ));
        }
        return card.build();
    }

    private Component getWindInfoComponent(WindInfo windInfo) {
        final WeatherViewCard card = new WeatherViewCard("Wind Info");

        if (windInfo == null) {
            card.setDesc("Could not load wind info!");
        } else {
            card.getTextElements().addAll(Arrays.asList(
                    createTextElement("Speed:", windInfo.speed() + "m/s"),
                    createTextElement("Degree:", windInfo.deg() + "°"),
                    createTextElement("Gust:", windInfo.gust() + "°m/s")
            ));
        }
        return card.build();
    }

    /*

        final FormLayout formLayout = new FormLayout();
        formLayout.addClassNames(AlignItems.CENTER,
                JustifyContent.EVENLY,
                Margin.Bottom.XLARGE,
                Margin.Top.XLARGE);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 4));

        final TextField cityField = createSearchField(),
                stateField = createSearchField(),
                countryField = createSearchField();

        cityField.setLabel("City:");
        cityField.setRequired(true);
        cityField.setPlaceholder("Entry the city name");

        stateField.setLabel("State:");
        stateField.setPlaceholder("Entry the state name");

        countryField.setLabel("Country:");
        countryField.setPlaceholder("Entry the country name");

        final Button primaryButton = new Button("submit");
        primaryButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        primaryButton.addClickShortcut(Key.ENTER);

        primaryButton.addClickListener(listener -> {
            if (weatherComponents.getChildren().findAny().isPresent()) {
                weatherComponents.removeAll();
            }

            if (cityField.getValue().isBlank()) {
                final Notification error = createNotification("Please enter a city!");
                error.setDuration(3 * 1000);
                error.addThemeVariants(NotificationVariant.LUMO_ERROR);
                error.open();
                return;
            }
            weatherService.setWeatherComponents(getWeatherComponents(cityField.getValue(), stateField.getValue(), countryField.getValue()));
            weatherComponents.add(weatherService.getWeatherComponents());
        });

        formLayout.add(cityField, stateField, countryField, primaryButton);

        weatherComponents = new OrderedList();
        weatherComponents.addClassNames(Gap.MEDIUM,
                Display.GRID,
                ListStyleType.NONE,
                Margin.NONE,
                Padding.NONE);

        if (!weatherService.getWeatherComponents().isEmpty()) {
            weatherComponents.add(weatherService.getWeatherComponents());
        }
    *
    * */
}
