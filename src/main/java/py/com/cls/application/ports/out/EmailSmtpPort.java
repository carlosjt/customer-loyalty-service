package py.com.cls.application.ports.out;

public interface EmailSmtpPort {
    void sendEmail(final String to, final String subject, final String body);
}
