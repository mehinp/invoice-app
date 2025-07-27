package com.mehin.invoiceapp.service.implementation;

import com.mehin.invoiceapp.enumeration.VerificationType;
import com.mehin.invoiceapp.exception.ApiException;
import com.mehin.invoiceapp.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private final JavaMailSender javaMailSender;

    @Override
    public void sendVerificationEmail(String firstName, String email, String verificationUrl, VerificationType verificationType) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("geomehin@gmail.com");
            message.setTo(email);
            message.setText(getEmailMessage(firstName, verificationUrl, verificationType));
            message.setSubject(String.format("InvoiceApp - %s Verification Email", StringUtils.capitalize(verificationType.getType())));
            javaMailSender.send(message);
            log.info("Email sent to {}", firstName);

        } catch (Exception exception) {
            log.error(exception.getMessage());

        }
    }

    private String getEmailMessage(String firstName, String verificationUrl, VerificationType verificationType) {
        switch(verificationType) {
            case PASSWORD -> {
                return "Hello " + firstName + ",\n\nPlease click the link below to reset your password. " +
                        "\n\n" + verificationUrl + "\n\nInvoiceApp Support Team;";}
            case ACCOUNT -> {
                return "Hello " + firstName + ",\n\nThank you for creating an account. Please click the link below to verify your account." +
                        "\n\n" + verificationUrl + "\n\nInvoiceApp Support Team";}
            default -> throw new ApiException("Unable to send email. Email type unknown.");
        }
    }
}
