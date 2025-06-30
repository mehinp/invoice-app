package com.mehin.invoiceapp.service;

import com.mehin.invoiceapp.domain.User;
import com.mehin.invoiceapp.dto.UserDTO;
import com.mehin.invoiceapp.form.UpdateForm;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

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

    void updatePassword(Long id, String currentPassword, String newPassword, String confirmNewPassword);

    void updateUserRole(Long id, String roleName);

    void updateAccountSettings(Long userId, @NotNull(message= "Enabled cannot be null or empty") Boolean enabled, @NotNull(message= "Not Locked cannot be null or empty") Boolean notLocked);

    void updateImage(UserDTO user, MultipartFile image);
}
