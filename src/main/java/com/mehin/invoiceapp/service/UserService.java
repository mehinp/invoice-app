package com.mehin.invoiceapp.service;

import com.mehin.invoiceapp.domain.User;
import com.mehin.invoiceapp.dto.UserDTO;
import com.mehin.invoiceapp.form.UpdateForm;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public interface UserService {
    UserDTO createUser(User user);

    UserDTO getUserByEmail(@NotEmpty String email);

    void sendVerificationCode(UserDTO user);

    void resetPassword(String email);

    UserDTO verifyPasswordKey (String key);

    void renewPassword(String key, String newPassword, String confirmPassword);

    UserDTO verifyAccountKey(String key);

    UserDTO updateUserDetails(@Valid UpdateForm user);

    UserDTO getUserById(Long userId);
}
