package com.mehin.invoiceapp.service.implementation;

import com.mehin.invoiceapp.domain.Role;
import com.mehin.invoiceapp.domain.User;
import com.mehin.invoiceapp.dto.UserDTO;
import com.mehin.invoiceapp.dtomapper.UserDTOMapper;
import com.mehin.invoiceapp.repository.RoleRepository;
import com.mehin.invoiceapp.repository.UserRepository;
import com.mehin.invoiceapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository<User> userRepository;
    private final RoleRepository<Role> roleRepository;

    @Override
    public UserDTO createUser(User user) {
        return mapToUserDTO(userRepository.create(user));
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return mapToUserDTO(userRepository.getUserByEmail(email));
    }

    @Override
    public void sendVerificationCode(UserDTO user) {
        userRepository.sendVerificationCode(user);
    }

    @Override
    public void resetPassword(String email) {
        userRepository.resetPassword(email);
    }

    @Override
    public UserDTO verifyPasswordKey(String key) {
        return mapToUserDTO(userRepository.verifyPasswordKey(key));
    }

    @Override
    public void renewPassword(String key, String newPassword, String confirmPassword) {
        userRepository.renewPassword(key, newPassword, confirmPassword);
    }

    @Override
    public UserDTO verifyAccountKey(String key) {
        return mapToUserDTO(userRepository.verifyAccountKey(key));
    }


    private UserDTO mapToUserDTO(User user) {
        return UserDTOMapper.fromUser(user, roleRepository.getRoleByUserId(user.getId()));
    }


}
