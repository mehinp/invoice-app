package com.mehin.invoiceapp.service;

import com.mehin.invoiceapp.enumeration.VerificationType;


public interface EmailService {
    void sendVerificationEmail(String firstName, String email, String verificationUrl, VerificationType verificationType);
}
