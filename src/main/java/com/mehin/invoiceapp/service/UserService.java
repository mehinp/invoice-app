package com.mehin.invoiceapp.service;

import com.mehin.invoiceapp.domain.User;
import com.mehin.invoiceapp.dto.UserDTO;
import jakarta.validation.constraints.NotEmpty;

public interface UserService {
    UserDTO createUser(User user);

    UserDTO getUserByEmail(@NotEmpty String email);

    void sendVerificationCode(UserDTO user);

    void resetPassword(String email);

    UserDTO verifyPasswordKey (String key);

    void renewPassword(String key, String newPassword, String confirmPassword);

    UserDTO verifyAccountKey(String key);
}
