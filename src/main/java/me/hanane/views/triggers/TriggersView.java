package me.hanane.views.triggers;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import me.hanane.security.AuthenticatedUser;
import me.hanane.trigger.Trigger;
import me.hanane.trigger.service.TriggersService;
import me.hanane.views.MainLayout;

import javax.annotation.security.PermitAll;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static me.hanane.utilities.Utilities.createNotification;

@PageTitle("Triggers")
@Route(value = "triggers", layout = MainLayout.class)
@PermitAll
public class TriggersView extends Div implements AfterNavigationObserver {

    private final Grid<Trigger> grid = new Grid<>();
    private final TriggersService triggersService;
    private final AuthenticatedUser authenticatedUser;
    private TriggerForm triggerForm;
    private Button submit;

    public TriggersView(TriggersService triggersService, AuthenticatedUser authenticatedUser) {
        this.triggersService = triggersService;
        this.authenticatedUser = authenticatedUser;
        addClassName("triggers-view");
        setSizeFull();

        addClassNames(LumoUtility.MaxWidth.SCREEN_LARGE,
                LumoUtility.Margin.Horizontal.AUTO,
                LumoUtility.Padding.Bottom.LARGE,
                LumoUtility.Padding.Horizontal.LARGE);

        setupForm();
        setupSubmitButton();

        add(
                triggerForm,
                submit,
                createGrid()
        );
    }

    private void setupForm() {
        this.triggerForm = new TriggerForm();
        triggerForm.setResponsiveSteps(new FormLayout.ResponsiveStep("500px", 3));
        triggerForm.setColspan(triggerForm.getMessage(), 3);
    }

    private void setupSubmitButton() {
        this.submit = new Button("Submit");
        submit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        submit.addClickShortcut(Key.ENTER);

        submit.addClickListener(listener -> {
            final Trigger result = triggerForm.getInputTrigger();

            if (result.getName() == null || result.getName().isBlank()) {
                final Notification error = createNotification("Please enter a trigger name!");
                error.setDuration(3 * 1000);
                error.addThemeVariants(NotificationVariant.LUMO_ERROR);
                error.open();
                return;
            }
            authenticatedUser.get()
                    .ifPresentOrElse(user -> System.out.println("USER ON " + user),
                    () -> System.out.println("USER NOT FOUND"));
            authenticatedUser.get().ifPresent(result::setUser);
            result.setDate(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").format(LocalDateTime.now()));
            triggersService.save(result);
            grid.setItems(triggersService.fetch());
            triggerForm.clear();
        });
    }

    private Grid<Trigger> createGrid() {
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(trigger -> TriggerView.build(triggersService, trigger));
        return grid;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        grid.setItems(triggersService.fetch());
    }

}
