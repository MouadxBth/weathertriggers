package me.hanane.registration;

import me.hanane.data.entity.User;
import me.hanane.data.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(UserService userService,
                               PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean register(RegistrationRequest request) {
        System.out.println(request);
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        final User user = new User(request);

        System.out.println("USER: " + user);

        userService.find(user.getUsername()).ifPresent(u -> {
            System.out.println("USER EXIST ");
            System.out.println(u.getEmail());
        });

        if (userService.find(user.getUsername()).isPresent()) {
            return false;
        }
        userService.save(user);
        return true;
    }

}
