package me.hanane.scheduling;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import me.hanane.data.service.UserService;
import me.hanane.email.EmailSender;
import me.hanane.location.GeolocationService;
import me.hanane.weather.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Optional;
import java.util.concurrent.Executor;

import static me.hanane.utilities.Utilities.createNotification;

@Configuration
@EnableScheduling
@EnableAsync
public class WeatherUpdate {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherUpdate.class);
    private final UserService userService;
    private final GeolocationService geolocationService;
    private final WeatherService weatherService;
    private final EmailSender emailSender;

    public WeatherUpdate(UserService userService,
                         GeolocationService geolocationService,
                         WeatherService weatherService,
                         EmailSender emailSender) {
        this.userService = userService;
        this.geolocationService = geolocationService;
        this.weatherService = weatherService;
        this.emailSender = emailSender;
    }

    @Scheduled(initialDelay = 1 * 60_000, fixedRate = 1 * 60_000)
    @Async
    public void update() {
        userService.findAll().forEach(user -> geolocationService.get(user.getCity())
                .thenCompose(weatherService::weather)
                .whenComplete((weather, error) -> {
                    if (error != null) {
                        LOGGER.error("Could not update weather data!");
                        LOGGER.error(error.getMessage());
                        return;
                    }
                    user.getTriggers()
                            .stream()
                            .filter(weather::testTrigger)
                            .forEach(trigger -> {
                                emailSender.send("weathertriggers@outlook.com",
                                        user.getEmail(),
                                        trigger.getMessage(),
                                        "Trigger: " + trigger.getName() + " (" + trigger.getDescription() + ") has been triggered");
                                System.out.println(trigger.getMessage());
                            });
                }));
    }

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("Weather-Geolocation-");
        executor.initialize();
        return executor;
    }
}
