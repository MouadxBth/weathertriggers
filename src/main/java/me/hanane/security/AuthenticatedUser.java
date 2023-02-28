package me.hanane.security;

import com.vaadin.flow.spring.security.AuthenticationContext;

import java.util.ArrayList;
import java.util.Optional;
import me.hanane.data.entity.User;
import me.hanane.data.service.UserRepository;
import me.hanane.weather.WeatherService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUser {

    private final UserRepository userRepository;
    private final WeatherService weatherService;
    private final AuthenticationContext authenticationContext;

    public AuthenticatedUser(AuthenticationContext authenticationContext,
                             WeatherService weatherService,
                             UserRepository userRepository) {
        this.userRepository = userRepository;
        this.weatherService = weatherService;
        this.authenticationContext = authenticationContext;
    }

    public Optional<User> get() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class)
                .flatMap(userDetails -> userRepository.findByUsername(userDetails.getUsername()));
    }

    public void logout() {
        authenticationContext.logout();
        weatherService.setWeatherComponents(new ArrayList<>());
    }

}
