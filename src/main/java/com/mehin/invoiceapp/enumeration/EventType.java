package com.mehin.invoiceapp.enumeration;

import lombok.Getter;

@Getter
public enum EventType {
    LOGIN_ATTEMPT("You tried to log in."),
    LOGIN_ATTEMPT_FAILURE("You failed to log in."),
    LOGIN_ATTEMPT_SUCCESS("You logged in successfully."),
    PROFILE_UPDATE("You updated your profile."),
    PROFILE_PICTURE_UPDATE("You updated your profile picture."),
    ROLE_UPDATE("You updated your role."),
    ACCOUNT_SETTINGS_UPDATE("You updated your account settings."),
    PASSWORD_UPDATE("You updated your password."),
    MFA_UPDATE("You updated your MFA settings.");

    private final String description;

    EventType(String description) {
        this.description = description;
    }

}
