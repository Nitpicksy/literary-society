package nitpicksy.literarysociety2.service;

public interface EmailNotificationService {
    void sendEmail(String to, String subject, String text);
}
