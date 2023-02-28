package me.hanane.views.triggers;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.ValidationException;
import me.hanane.trigger.Trigger;
import me.hanane.trigger.service.TriggersRepository;
import me.hanane.trigger.service.TriggersService;

import static me.hanane.utilities.Utilities.createNotification;

public class TriggerView {

    public static Dialog createDialog(TriggersService triggersService, Trigger trigger) {
        final Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Edit " + trigger.getName());

        final TriggerForm triggerForm = new TriggerForm();
        triggerForm.getBinder().readBean(trigger);

        triggerForm.setResponsiveSteps(
                // Use one column by default
                new FormLayout.ResponsiveStep("0", 1),
                // Use two columns, if layout's width exceeds 500px
                new FormLayout.ResponsiveStep("500px", 2));
        triggerForm.setColspan(triggerForm.getMessage(), 2);
        dialog.add(triggerForm);

        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        saveButton.addClickListener(listener -> {
            try {
                triggerForm.getBinder().writeBean(trigger);
                triggersService.save(trigger);
                final Notification notification = createNotification("Modified successfully!");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                notification.setDuration(3 * 1000);
                notification.open();
                dialog.close();
                UI.getCurrent().navigate(TriggersView.class);
            } catch (ValidationException e) {
                final Notification notification = createNotification("No modification was applied!");
                notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
                notification.setDuration(3 * 1000);
                notification.open();
                throw new RuntimeException(e);
            }
        });

        Button cancelButton = new Button("Cancel", e -> dialog.close());

        Button deleteButton = new Button("Delete");
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(listener -> {
            try {
                triggerForm.getBinder().writeBean(trigger);
                triggersService.delete(trigger);
                final Notification notification = createNotification("Deleted successfully!");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                notification.setDuration(3 * 1000);
                notification.open();
                dialog.close();
                UI.getCurrent().navigate(TriggersView.class);
            } catch (ValidationException e) {
                final Notification notification = createNotification("No modification was applied!");
                notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
                notification.setDuration(3 * 1000);
                notification.open();
                throw new RuntimeException(e);
            }
        });

        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(deleteButton);
        dialog.getFooter().add(saveButton);
        return dialog;
    }

    public static Component build(TriggersService triggersService, Trigger trigger) {
        final VerticalLayout card = new VerticalLayout();
        card.addClassName("card");
        card.getThemeList().add("spacing-s");

        card.addClickListener(listener -> createDialog(triggersService, trigger).open());

        final HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.setSpacing(false);
        header.getThemeList().add("spacing-s");

        final Span name = new Span(trigger.getName().toUpperCase());
        name.addClassName("name");

        final Span description = new Span(trigger.getDescription() == null ? "" : trigger.getDescription().toLowerCase());
        name.addClassName("description");

        final Span date = new Span(trigger.getDate());
        date.addClassName("date");
        // header
        header.add(name, description, date);

        final Paragraph message = new Paragraph(trigger.getMessage());
        message.getStyle().set("white-space", "normal"); // Allow text to wrap
        message.setWidthFull();

        card.add(header, message);
        return card;
    }

}
