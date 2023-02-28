package me.hanane.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility;
import me.hanane.components.appnav.AppNav;
import me.hanane.components.appnav.AppNavItem;
import me.hanane.data.entity.User;
import me.hanane.security.AuthenticatedUser;
import me.hanane.views.about.AboutView;
import me.hanane.views.dashboard.DashboardView;
import me.hanane.views.login.LoginView;
import me.hanane.views.register.RegisterView;
import me.hanane.views.triggers.TriggersView;

import java.util.Optional;

public class AuthLayout extends AppLayout {

    final Button button = new Button();
    public AuthLayout() {
        addHeaderContent();
    }
    private void addHeaderContent() {
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.setText("Register");
        button.addClickListener(listener -> {
            if (button.getText().equalsIgnoreCase("login")) {
                button.getUI().ifPresent(ui -> ui.navigate(LoginView.class));
                button.setText("Login");
            }
            else {
                button.getUI().ifPresent(ui -> ui.navigate(RegisterView.class));
                button.setText("Register");
            }
        });
        getCurrentPageTitle();

        addToNavbar(true, button);
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
    }

    private String getCurrentPageTitle() {
        System.out.println(getContent() == null ? "NULL CONTENT" : "NOT NULL");
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
