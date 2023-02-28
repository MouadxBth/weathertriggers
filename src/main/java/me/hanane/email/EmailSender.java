package me.hanane.email;

public interface EmailSender {

    void send(String from, String to, String message, String subject);

}
