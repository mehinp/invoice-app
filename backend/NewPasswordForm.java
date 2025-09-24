package com.mehin.invoiceapp.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewPasswordForm {
    @NotNull(message = "ID cannot be null or empty")
    private Long userId;
    @NotEmpty(message = "New password cannot be empty.")
    private String newPassword;
    @NotEmpty(message = "Confirm password cannot be empty.")
    private String confirmPassword;
}
