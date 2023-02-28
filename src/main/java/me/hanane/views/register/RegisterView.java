package me.hanane.views.register;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import me.hanane.registration.RegistrationRequest;
import me.hanane.registration.RegistrationService;
import me.hanane.views.AuthLayout;
import me.hanane.views.login.LoginView;

import static me.hanane.utilities.Utilities.createNotification;

@PageTitle("Register")
@Route(value = "register")
@AnonymousAllowed
@Uses(Icon.class)
public class RegisterView extends Div {
    private final TextField firstName = new TextField("First name"),
            lastName = new TextField("Last name"),
            username = new TextField("Username"),
            city = new TextField("City"),
            state = new TextField("State"),
            country = new TextField("Country");
    private final EmailField email = new EmailField("Email address");
    private final PasswordField passwordField = new PasswordField("Password"),
            passwordConfirmField = new PasswordField("Confirm password");
    private final Button cancel = new Button("Cancel"),
            save = new Button("Save"),
            login = new Button("Login");

    public RegisterView(RegistrationService registrationService) {
        addClassName("register-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());

        clearForm();

        cancel.addClickListener(e -> clearForm());

        save.addClickListener(e -> {
            if (!passwordField.getValue().equals(passwordConfirmField.getValue())) {
                final Notification notification = createNotification("Password confirmation doesnt match");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setDuration(3 * 1000);
                notification.open();
                return;
            }
            final RegistrationRequest request = new RegistrationRequest(
                    firstName.getValue(),
                    lastName.getValue(),
                    username.getValue(),
                    passwordField.getValue(),
                    email.getValue(),
                    city.getValue(),
                    state.getValue(),
                    country.getValue()
            );
            if (!registrationService.register(request)) {
                final Notification notification = createNotification("User already exists!");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setDuration(3 * 1000);
                notification.open();
                return;
            }
            clearForm();
            save.getUI().ifPresent(ui -> ui.navigate(LoginView.class));
            final Notification notification = createNotification("Registered successfully!");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setDuration(3 * 1000);
            notification.open();
        });
    }

    private void clearForm() {
        firstName.clear();
        lastName.clear();
        username.clear();
        email.clear();
        passwordField.clear();
        passwordConfirmField.clear();
        city.clear();
        state.clear();
        country.clear();
    }

    private Component createTitle() {
        return new H3("Register");
    }

    private Component createFormLayout() {
        final FormLayout formLayout = new FormLayout();
        username.setRequired(true);
        passwordField.setRequired(true);
        passwordConfirmField.setRequired(true);
        city.setRequired(true);
        email.setErrorMessage("Please enter a valid email address");

        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2));
        formLayout.setColspan(email, 2);

        formLayout.add(firstName, lastName,
                email,
                passwordField, passwordConfirmField,
                username, city,
                state, country);
        return formLayout;
    }

    private Component createButtonLayout() {
        final HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        login.addClickListener(listener -> {
            login.getUI().ifPresent(ui -> ui.navigate(LoginView.class));
        });
        buttonLayout.add(save);
        buttonLayout.add(cancel);
        buttonLayout.add(login);

        return buttonLayout;
    }

}
