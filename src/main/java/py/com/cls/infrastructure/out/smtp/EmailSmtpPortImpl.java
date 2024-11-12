package py.com.cls.infrastructure.out.smtp;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.vavr.control.Try;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import py.com.cls.application.ports.out.EmailSmtpPort;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(value = Transactional.TxType.REQUIRED)
public class EmailSmtpPortImpl implements EmailSmtpPort {
    @Inject
    Mailer mailer;
    @Inject
    ObjectMapper objectMapper;
    @Override
    public void sendEmail(final String to, final String subject, final String body) {
        Try.run(() -> mailer.send(Mail.withText(to, subject, objectMapper.writeValueAsString(body))));
    }
}
