package nitpicksy.paymentgateway.service;

public interface EmailNotificationService {
    void sendEmail(String to, String subject, String text);
}

