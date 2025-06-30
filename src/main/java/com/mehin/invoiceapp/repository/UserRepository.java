package com.mehin.invoiceapp.repository;

import com.mehin.invoiceapp.domain.User;
import com.mehin.invoiceapp.dto.UserDTO;
import com.mehin.invoiceapp.form.UpdateForm;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

public interface UserRepository <T extends User> {
    T create (T data);
    Collection<T> list(int page, int pageSize);
    T get(Long id);
    T update(T  data);
    Boolean delete(Long id);

    T getUserByEmail(String email);

    void sendVerificationCode(UserDTO user);

    void resetPassword(String email);

    User verifyPasswordKey(String key);

    void renewPassword(String key, String newPassword, String confirmPassword);

    T verifyAccountKey(String key);

    T updateUserDetails(UpdateForm user);

    void updatePassword(Long id, String currentPassword, String newPassword, String confirmNewPassword);

    void updateAccountSettings(Long userId, Boolean enabled, Boolean notLocked);

    void updateImage(UserDTO user, MultipartFile image);
}
